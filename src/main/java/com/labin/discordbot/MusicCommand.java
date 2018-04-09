package com.labin.discordbot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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

import net.dv8tion.jda.core.EmbedBuilder;
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
	private EmbedBuilder eb;
	public MusicCommand(JDA jda) {
		botJDA=jda;
        
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        user = event.getAuthor();
        msg = event.getMessage();
        textChannel = event.getTextChannel();
        playerManager = new DefaultAudioPlayerManager(); 
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
		guild=textChannel.getGuild();
        audioManager = guild.getAudioManager();
        if (user.isBot()) return;
        if (msg.getContentRaw().startsWith("!play")) {
        	voiceChannel=findVoiceChannel(textChannel,user.getId());
        	if(voiceChannel==null) {
        		textChannel.sendMessage("음성챗에 먼저 들어가세여 ㅡㅡ" + user.getAsMention()).queue();
        		return;
        	}
        	
        	if(msg.getContentRaw().indexOf(" ")==-1) {
        		textChannel.sendMessage("사용 가능한 커맨드 목록"+'\n'+"play"+'\n'+"find"+'\n'+"stop"+'\n'+"skip"+'\n'+"pause"+'\n'+"repeat"+'\n'+"shuffle"+'\n'+"queue"+'\n').queue();
        		return;
        	}
        	
            command = msg.getContentRaw().substring(msg.getContentRaw().indexOf(" ")+1);
			data=parser.getVideo(command, 1);
			if(data.size()==0) {
        	textChannel.sendMessage("검색된 데이터가 없습니다!").queue();
				return;
			}
        	audioManager.openAudioConnection(voiceChannel);
            loadAndPlay(data.get(0));
        }
        if(msg.getContentRaw().startsWith("!find")) {
        	int count=0;
        	if(msg.getContentRaw().indexOf(" ")==-1) { 
        	  eb = new EmbedBuilder();
		      eb.setTitle("명령어 형식 : !find 검색결과개수 노래제목", null);
		      eb.setDescription("ex) !find 10 shape of you");
			  eb.setColor(new Color(0x33CC66));
			  textChannel.sendMessage(eb.build()).queue();
        		return;
        	}	
        	 command = msg.getContentRaw().substring(msg.getContentRaw().indexOf(" ")+1);
        	 try {
        		 count=Integer.parseInt(command.substring(0,command.indexOf(" ")));
        	 }catch(Exception ex) {
	           	  eb = new EmbedBuilder();
	   		      eb.setTitle("검색 개수를 입력해주세요", null);
	   			  eb.setColor(new Color(0x33CC66));
	   			  textChannel.sendMessage(eb.build()).queue();
	   			  return;
        	 }
 			 data=parser.getVideo(command.substring(command.indexOf(" ")+1), count);
 			 for(int i=0;i<data.size();i++) {
 				eb = new EmbedBuilder();
				eb.setTitle("#"+(i+1), null);
				eb.setDescription(data.get(i).getTitle());
				eb.setColor(new Color(0x33CC66));
				eb.setThumbnail(data.get(i).getThumbnail());
				eb.addField("uploader", data.get(i).getUploader(), true);
				textChannel.sendMessage(eb.build()).queue();
 			 }
        	 
        	 
        }
        
        command=msg.getContentRaw();
        
        if (command.equals("!stop"))
        	stopTrack();
        if (command.equals("!skip"))
        	skipTrack();
        if (command.equals("!pause"))
        	pauseTrack();        
        if (command.equals("!repeat"))
        	repeatTrack();     
        if (command.equals("!shuffle"))
        	shuffleTrack();       
        if (command.equals("!queue"))
        	printQueue();
	}

	  	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		    long guildId = Long.parseLong(guild.getId());
		    if(musicManagers.get(guildId)==null) {
		      musicManagers.put(guildId,  new GuildMusicManager(playerManager, audioManager,textChannel));
		    }
		    musicManager = musicManagers.get(guildId);
		    guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		    return musicManager;
		  }

		  private void loadAndPlay(YouTubeVideoData data) {
			  musicManager = getGuildAudioPlayer(textChannel.getGuild());
			  musicManager.scheduler.addData(data);
		      playerManager.loadItemOrdered(musicManager,"https://www.youtube.com/watch?v=" + data.getID(), resultHandler);
		  }
		  
		  private AudioLoadResultHandler resultHandler =new AudioLoadResultHandler() {
		      @Override
		      public void trackLoaded(AudioTrack track) {
		    	YouTubeVideoData temp=null;
		        musicManager.scheduler.queue(track);
		        if(musicManager.scheduler.getQueue().size()>0){
		        	for(int i=0;i<data.size();i++) 
						if(track.getInfo().uri.equals("https://www.youtube.com/watch?v="+data.get(i).getID()))
							temp=data.get(i);
		        	eb = new EmbedBuilder();
					eb.setTitle("Adding to Queue - ["+musicManager.scheduler.getQueue().size()+"]", null);
					eb.setDescription(temp.getTitle());
					eb.setColor(new Color(0x33CC66));
					eb.setThumbnail(temp.getThumbnail());
					eb.addField("uploader", temp.getUploader(), true);
					eb.addField("Duration", "`"+getTimeforMilli(track.getInfo().length)+"`", true);
					textChannel.sendMessage(eb.build()).queue();
		        }
			 
		      }

		      @Override
		      public void playlistLoaded(AudioPlaylist playlist) {
		      /*  AudioTrack firstTrack = playlist.getSelectedTrack();

		        if (firstTrack == null) {
		          firstTrack = playlist.getTracks().get(0);
		        }

		        textChannel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
		        musicManager.scheduler.queue(track);*/	
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
		  
		  //skip
		  private void skipTrack() {
			GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());
		    eb = new EmbedBuilder();
			eb.setTitle("현재 재생중인 노래를 스킵할게요.", null);
			eb.setDescription(musicManager.scheduler.getPlayingTrack().getInfo().title);
			eb.setColor(new Color(0x33CC66));
		    textChannel.sendMessage(eb.build()).queue();
		    musicManager.scheduler.nextTrack();
		  }
		  
		  //stop
		  private void stopTrack() {
			  GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());
			  eb = new EmbedBuilder();
		      eb.setTitle("현재 재생중인 노래를 끊을게요.", null);
			  eb.setDescription(musicManager.scheduler.getPlayingTrack().getInfo().title);
			  eb.setColor(new Color(0x33CC66));
			  textChannel.sendMessage(eb.build()).queue();
			  
			  musicManager.scheduler.nextTrack();
			  musicManager.player.stopTrack();
			  musicManager.scheduler.initQueue();
		  }
		  
		  //pause
		  private void pauseTrack() {
			  GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());

			  eb = new EmbedBuilder();
		      eb.setTitle("현재 재생중인 노래를 멈출게요.", null);
			  eb.setDescription(musicManager.scheduler.getPlayingTrack().getInfo().title);
			  eb.setColor(new Color(0x33CC66));
			  textChannel.sendMessage(eb.build()).queue();
			  
			  musicManager.player.setPaused(!musicManager.player.isPaused());
		  }
		  
		  //repeat
		  private void repeatTrack() {
			  GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());

			  eb = new EmbedBuilder();
			  if(musicManager.scheduler.getRepeat())
				  eb.setTitle("무한 반복 모드를 false 로 설정합니다.", null);
			  else
				  eb.setTitle("무한 반복 모드 를 true 로 설정합니다.", null);
			  eb.setColor(new Color(0x33CC66));
			  textChannel.sendMessage(eb.build()).queue();
			  
			  musicManager.scheduler.setRepeat(!musicManager.scheduler.getRepeat());
		  }
		  
		  //shuffle
		  private void shuffleTrack() {
			  GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());

			  eb = new EmbedBuilder();
		      eb.setTitle("노래 재생 목록을 석겠습니다!", null);
			  eb.setColor(new Color(0x33CC66));
			  textChannel.sendMessage(eb.build()).queue();
			  musicManager.scheduler.shuffle();
			  printQueue();
		  }
		  //queue
		  private void printQueue() {
			  GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());
			  Queue<AudioTrack> q = musicManager.scheduler.getQueue();
			  String des="";
			  int cnt=0;
			  eb = new EmbedBuilder();
			  if(q.isEmpty())
			      eb.setTitle("현재 재생 목록이 비어있습니다.", null);
			  else
				  eb.setTitle("현재 재생 목록 입니다.", null);
			  eb.setColor(new Color(0x33CC66));
			  while(!q.isEmpty()) {
				  cnt++;
				   //eb.addField("","["+cnt+"] : "+q.poll().getInfo().title, false);
				  des+="["+cnt+"] : "+q.poll().getInfo().title+'\n';
			  }
			  eb.setDescription(des);
			  textChannel.sendMessage(eb.build()).queue();
			  
		  }
		  //////////시스템 메소드
		  private VoiceChannel findVoiceChannel(TextChannel channel, String id) {
			  List<VoiceChannel> voiceChannels=channel.getGuild().getVoiceChannels();
	         	for(int i=0;i<voiceChannels.size();i++) {
	         			for(int j=0;j<voiceChannels.get(i).getMembers().size();j++) {
	         				if(voiceChannels.get(i).getMembers().get(j).getUser().getId().equals(id))
	         					return voiceChannels.get(i);
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