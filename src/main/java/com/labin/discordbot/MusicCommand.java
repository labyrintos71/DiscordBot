package com.labin.discordbot;

import java.util.HashMap;
import java.util.Map;

import com.labin.discordbot.audio.GuildMusicManager;
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
	private String name;
	private VoiceChannel voiceChannel;
	private TextChannel textChannel;
	private JDA botJDA;
	private AudioManager audioManager;
	private Guild guild;
	private User user;
	private AudioPlayerManager playerManager;
	private Map<Long, GuildMusicManager> musicManagers;
	private GuildMusicManager musicManager;
	public MusicCommand(JDA jda) {
		botJDA=jda;
		guild=botJDA.getGuilds().get(0);
        audioManager = guild.getAudioManager();
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        user = event.getAuthor();
        textChannel = event.getTextChannel();
        Message msg = event.getMessage();
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        
        if (user.isBot()) return;
        if (msg.getContentRaw().startsWith("!play")) {
        	if(msg.getContentRaw().indexOf(" ")==-1) {
        		textChannel.sendMessage("제목을 입력해주세요 " + user.getAsMention()).queue();
        		return;
        	}
        	voiceChannel=findVoiceChannel(user.getAsMention());
        	if(voiceChannel!=null) {
        		audioManager.openAudioConnection(voiceChannel);
        	}
        	else {
        		textChannel.sendMessage("음성챗에 먼저 들어가세여 ㅡㅡ" + user.getAsMention()).queue();
        		return;
        	}
            loadAndPlay("https://www.youtube.com/watch?v=kfE0y1IV8Bc");
        	
        }
        if (msg.getContentRaw().startsWith("!stop"))
        	audioManager.closeAudioConnection();
        if (msg.getContentRaw().startsWith("!test")) {
        	
        	YouTubeParser ytp=new YouTubeParser("heymama");
        	ytp.start();
        }
        
	}

	  	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		    long guildId = Long.parseLong(guild.getId());
		    if(musicManagers.size()==0) {
		      musicManager = new GuildMusicManager(playerManager);
		      musicManagers.put(guildId, musicManager);
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
		    	  textChannel.sendMessage("Adding to queue " + track.getInfo().title).queue();

		        play(voiceChannel.getGuild(), musicManager, track);
		      }

		      @Override
		      public void playlistLoaded(AudioPlaylist playlist) {
		        AudioTrack firstTrack = playlist.getSelectedTrack();

		        if (firstTrack == null) {
		          firstTrack = playlist.getTracks().get(0);
		        }

		        textChannel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

		        play(voiceChannel.getGuild(), musicManager, firstTrack);
		      }

		      @Override
		      public void noMatches() {
		    	//  textChannel.sendMessage("Nothing found by " + trackUrl).queue();
		      }

		      @Override
		      public void loadFailed(FriendlyException exception) {
		    	  textChannel.sendMessage("Could not play: " + exception.getMessage()).queue();
		      }
		    };
		    
		  //!play
		  private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
			//뮤직봇이 들어와줘야 할 자리
		    musicManager.scheduler.queue(track);
		  }
		  
		  // !skip
		  private void skipTrack(TextChannel channel) {
		    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		    musicManager.scheduler.nextTrack();

		    channel.sendMessage("Skipped to next track.").queue();
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
		  
}