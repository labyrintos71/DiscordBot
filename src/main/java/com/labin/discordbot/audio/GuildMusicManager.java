package com.labin.discordbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class GuildMusicManager {
	 /**
	   * Audio player for the guild.
	   */
	  public final AudioPlayer player;
	  /**
	   * Track scheduler for the player.
	   */
	  public final TrackScheduler scheduler;

	  /**
	   * Creates a player and a track scheduler.
	   * @param manager Audio player manager to use for creating the player.
	   */
	  public GuildMusicManager(AudioPlayerManager manager,AudioManager audiomanager, TextChannel textChannel) {
		 
	    player = manager.createPlayer();
	    scheduler = new TrackScheduler(player,audiomanager,textChannel);
	    player.addListener(scheduler);
	  }
	  /**
	   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
	   */
	  public AudioPlayerSendHandler getSendHandler() {
	    return new AudioPlayerSendHandler(player);
	  }
}
