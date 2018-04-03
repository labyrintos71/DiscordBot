package com.labin.discordbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

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
	private VoiceChannel channel;
	private JDA botJDA;
	private AudioManager audioManager;
	private Guild guild;
	public MusicCommand(JDA jda) {
		botJDA=jda;
		guild=botJDA.getGuilds().get(0);
        audioManager = guild.getAudioManager();
	}
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();
        if (user.isBot()) return;
       // if (!msg.getContentRaw().startsWith("!play")) return;
        if (msg.getContentRaw().startsWith("!play")) {
        	if(msg.getContentRaw().indexOf(" ")==-1) {
        		tc.sendMessage("제목을 입력해주세요 " + user.getAsMention()).queue();
        		return;
        	}
        	channel=findVoiceChannel(user.getAsMention());
        	if(channel!=null) {
        		audioManager.openAudioConnection(findVoiceChannel(user.getAsMention()));
        	}
        	else {
        		tc.sendMessage("음성챗에 먼저 들어가세여 ㅡㅡ" + user.getAsMention()).queue();
        		return;
        	}
        	//audioManager.setSendingHandler(handler);
        	
        }
        if (msg.getContentRaw().startsWith("!stop"))
        	audioManager.closeAudioConnection();
        AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);
        
	}
	
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
