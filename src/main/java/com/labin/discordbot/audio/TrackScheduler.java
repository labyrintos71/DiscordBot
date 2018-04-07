package com.labin.discordbot.audio;

import java.util.LinkedList;
import java.util.Queue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.AudioManager;


public class TrackScheduler extends AudioEventAdapter {

	  private final AudioPlayer player;
	  private final AudioManager audioManager;
	  private final TextChannel textChannel;
	  private final Queue<AudioTrack> queue= new LinkedList<>();

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
	   *
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
	    if (endReason==AudioTrackEndReason.STOPPED) audioManager.closeAudioConnection();
	    if (endReason==AudioTrackEndReason.FINISHED&&queue.size()==0)audioManager.closeAudioConnection();
	    if (endReason.mayStartNext) {
	      nextTrack();
	    }
	  }
	  
	  @Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		// TODO Auto-generated method stub
		super.onTrackStart(player, track);
		textChannel.sendMessage("Now Playing : "+track.getInfo().title).queue();
	}

	public Queue getQueue() {
		  return queue;
	  }
	  
	  public void initQueue() {
		  queue.clear();
	  }
}
