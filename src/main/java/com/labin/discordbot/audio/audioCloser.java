package com.labin.discordbot.audio;

import net.dv8tion.jda.core.managers.AudioManager;

public class audioCloser extends Thread {

	private AudioManager audioManager;
	public audioCloser(AudioManager manager) {
		// TODO Auto-generated constructor stub
		audioManager=manager;
	}
	  public void run() {
		  audioManager.closeAudioConnection();
	  }
}
