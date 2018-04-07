package com.labin.discordbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.labin.discordbot.audio.GuildMusicManager;
import com.labin.discordbot.data.YouTubeVideoData;
import com.labin.discordbot.parser.YouTubeParser;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicCommand extends ListenerAdapter {
	private StringTokenizer token;
	private String command;
	private VoiceChannel voiceChannel;
	private TextChannel textChannel;
	private JDA botJDA;
	private AudioManager audioManager;
	private Guild guild;
	private User user;
	private AudioPlayerManager playerManager;
	private Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
	private Message msg;
	private YouTubeParser parser=new YouTubeParser();
	private GuildMusicManager musicManager;
	private ArrayList<YouTubeVideoData> data;
	public MusicCommand(JDA jda) {
		botJDA=jda;
		guild=botJDA.getGuilds().get(0);
        audioManager = guild.getAudioManager();
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        user = event.getAuthor();
        msg = event.getMessage();
        textChannel = event.getTextChannel();
        playerManager = new DefaultAudioPlayerManager(); 
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        
        if (user.isBot()) return;
        if (msg.getContentRaw().startsWith("!play")) {
        	voiceChannel=findVoiceChannel(user.getAsMention());
        	if(voiceChannel==null) {
        		textChannel.sendMessage("음성챗에 먼저 들어가세여 ㅡㅡ" + user.getAsMention()).queue();
        		return;
        	}
        	if(msg.getContentRaw().indexOf(" ")==-1) {
        		textChannel.sendMessage("노래 제목을 입력해주세요").queue();
        		return;
        	}
            String command = msg.getContentRaw().substring(msg.getContentRaw().indexOf(" "));
        	switch(command.length()) {
        		case 0:
            		textChannel.sendMessage("제목을 입력해주세요 " + user.getAsMention()).queue();
            		return;
        		case 1:
                    break;
        			
        	}
			data=parser.getVideo(command, 1);
			if(data.size()==0) {
        	textChannel.sendMessage("검색된 데이터가 없습니다!").queue();
				return;
			}
        	audioManager.openAudioConnection(voiceChannel);
            loadAndPlay("https://www.youtube.com/watch?v="+data.get(0).getID());
        }
        if (msg.getContentRaw().startsWith("!stop"))
        	stopTrack();
        if (msg.getContentRaw().startsWith("!skip"))
        	skipTrack();
        if (msg.getContentRaw().startsWith("!pause"))
        	pauseTrack();
        
	}

	  	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		    long guildId = Long.parseLong(guild.getId());
		    if(musicManagers.size()==0) {
		      musicManagers.put(guildId,  new GuildMusicManager(playerManager, audioManager,textChannel));
		    }
		    musicManager = musicManagers.get(guildId);
		    guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		    return musicManager;
		  }

		  private void loadAndPlay(String trackUrl) {
			  musicManager = getGuildAudioPlayer(textChannel.getGuild());
		      playerManager.loadItemOrdered(musicManager, trackUrl, resultHandler);
		  }
		  
		  private AudioLoadResultHandler resultHandler =new AudioLoadResultHandler() {
		      @Override
		      public void trackLoaded(AudioTrack track) {

		        musicManager.scheduler.queue(track);
		        if(musicManager.scheduler.getQueue().size()>0)
		    	textChannel.sendMessage("Adding to queue["+musicManager.scheduler.getQueue().size()+"] : " + track.getInfo().title+" : "+"`"+getTimeforMilli(track.getInfo().length)).queue();
			 
		      }

		      @Override
		      public void playlistLoaded(AudioPlaylist playlist) {
		        AudioTrack firstTrack = playlist.getSelectedTrack();

		        if (firstTrack == null) {
		          firstTrack = playlist.getTracks().get(0);
		        }

		        textChannel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
		       // musicManager.scheduler.queue(track);
		      }

		      @Override
		      public void noMatches() {
		    	  textChannel.sendMessage("Nothing found by " ).queue();
		      }

		      @Override
		      public void loadFailed(FriendlyException exception) {
		    	  textChannel.sendMessage("Could not play: " + exception.getMessage()).queue();
		      }
		    };
		  
		  // !skip
		  private void skipTrack() {
			GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());
		    musicManager.scheduler.nextTrack();
		    textChannel.sendMessage("Skipped to next track.").queue();
		  }
		  
		  //stop
		  private void stopTrack() {
			  GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());
			  musicManager.player.stopTrack();
			  musicManager.scheduler.initQueue();
		  }
		  
		  //pause
		  private void pauseTrack() {
			  GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());
			  musicManager.player.setPaused(!musicManager.player.isPaused());
		  }
		  
		  //////////시스템 메소드
		  private VoiceChannel findVoiceChannel(String emote) {
	         	for(int i=0;i<botJDA.getVoiceChannels().size();i++) {
	         			for(int j=0;j<botJDA.getVoiceChannels().get(i).getMembers().size();j++) {
	         				if(botJDA.getVoiceChannels().get(i).getMembers().get(j).getAsMention().equals(emote))
	         					return botJDA.getVoiceChannels().get(i);
	         			}
	         	}
	         return null;
		  }
		  
		  private String getTimeforMilli(long ms) {
			 
			  return shiftZero(ms / 1000 / 3600) + ":" + shiftZero(ms / 1000 % 3600 / 60) + ":" + shiftZero(ms / 1000 % 3600 % 60);
		  }
		  
		  private String shiftZero(long time) {
			  if(time<10) return "0"+time;
			  else return ""+time;
		  }
}