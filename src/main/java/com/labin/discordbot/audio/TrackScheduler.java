package com.labin.discordbot.audio;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.labin.discordbot.data.YouTubeVideoData;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.AudioManager;


public class TrackScheduler extends AudioEventAdapter {

	  private final AudioPlayer player;
	  private final TextChannel textChannel;
	  private final Queue<AudioTrack> queue= new LinkedList<>();
	  private final ArrayList<YouTubeVideoData> queueData= new ArrayList<>();
	  private final AudioManager audioManager;
	  private YouTubeVideoData data;
	  private AudioCloser closer;
	  private boolean repeat=false;
	  private AudioTrack lastTrack;
	  private EmbedBuilder eb;

	  /**
	   * @param player The audio player this scheduler uses
	   */
	  public TrackScheduler(AudioPlayer player,AudioManager audiomanager,TextChannel textchannel) {
	    this.player = player;
	    audioManager=audiomanager;
	    textChannel=textchannel;
	  }

	  /**
	   * Add the next track to queue or play right away if nothing is in the queue.
	   * @param track The track to play or add to queue.
	   */
	  public void queue(AudioTrack track) {
	    // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
	    // something is playing, it returns false and does nothing. In that case the player was already playing so this
	    // track goes to the queue instead.
	    if (!player.startTrack(track, true)) {
	       queue.offer(track);
	    }
	  }

	  /**
	   * Start the next track, stopping the current one if it is playing.
	   */
	  public void nextTrack() {
	    // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
	    // giving null to startTrack, which is a valid argument and will simply stop the player.
	    player.startTrack(queue.poll(), false);
	  }

	  @Override
	  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
	    // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
		/*if(endReason==AudioTrackEndReason.STOPPED&&queue.size()==0) {
		    closer=new AudioCloser(audioManager);
	    	closer.start();
		}*/
	    if (endReason==AudioTrackEndReason.STOPPED||endReason==AudioTrackEndReason.FINISHED&&queue.size()==0&&!repeat) {
		    closer=new AudioCloser(audioManager);
	    	closer.start();
	    }
	    if (endReason.mayStartNext) {
	    	if(repeat) player.startTrack(lastTrack.makeClone(), false);
	    	else nextTrack();
	    }
	  }
	  
	  @Override
		public void onTrackStart(AudioPlayer player, AudioTrack track) {
			// TODO Auto-generated method stub
			super.onTrackStart(player, track);
			lastTrack=track;
			for(int i=0;i<queueData.size();i++) 
				if(track.getInfo().uri.equals("https://www.youtube.com/watch?v="+queueData.get(i).getID()))
					data=queueData.get(i);
			
			eb = new EmbedBuilder();
			eb.setTitle("Now Playing", null);
			eb.setDescription(data.getTitle());
			eb.setColor(new Color(82, 199,184));
			eb.setThumbnail(data.getThumbnail());
			eb.addField("uploader", data.getUploader(), true);
			eb.addField("length", "`"+getTimeforMilli(track.getInfo().length)+"`", true);
			
			
			textChannel.sendMessage(eb.build()).queue();
		}
	  
		private String getTimeforMilli(long ms) {		 
			return shiftZero(ms / 1000 / 3600) + ":" + shiftZero(ms / 1000 % 3600 / 60) + ":" + shiftZero(ms / 1000 % 3600 % 60);
		}
		  
		private String shiftZero(long time) {
			if(time<10) return "0"+time;
			else return ""+time;
		}
		
		public Queue<AudioTrack> getQueue() {
			  return new LinkedList<AudioTrack>(queue);
		}
		
		public void initQueue() {
			queue.clear();
		}
		
		public boolean getRepeat() {
			return repeat;
		}
		
		public void setRepeat(boolean bool) {
			repeat=bool;
		}
		
		public void shuffle()
	    {
	        Collections.shuffle((List<?>) queue,new Random());
	    }
		
		public void addData(YouTubeVideoData data) {
			queueData.add(data);
		}
		
		public AudioTrack getPlayingTrack() {
			return lastTrack;
		}
}
