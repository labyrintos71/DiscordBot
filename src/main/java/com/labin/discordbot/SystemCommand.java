package com.labin.discordbot;

import java.util.Random;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class SystemCommand extends ListenerAdapter {
	private JDA botJDA;
	public SystemCommand(JDA system) {
		botJDA=system;
	}
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();

        if(user.isBot()) return;
        if(msg.getContentRaw().charAt(0) == '!'){
            String[] command = msg.getContentRaw().substring(1).split(" ");
            
            
            if(command[0].equals("퇴근")||command[0].equals("퇴근해")) {
            	int temp=new Random().nextInt(3);
            	if(true) {
	                tc.sendMessage("앗싸 퇴근이당!").queue();
	                botJDA.shutdown();
	                botJDA=null;
            	}else if(temp==2){
	                tc.sendMessage("싫어").queue();
            			
            	}else {
	                tc.sendMessage("퇴 까").queue();
            	}
            }
            
            if(command[0].equals("리스너제거")) {
                tc.sendMessage("제거중,,,").queue();
            	for(int i=0;i<botJDA.getRegisteredListeners().size();i++) {
            		if(!botJDA.getRegisteredListeners().get(i).getClass().getSimpleName().equals("SystemCommandListener")) {
            			botJDA.removeEventListener(botJDA.getRegisteredListeners().get(i));
            			i=-1;
            		}
            	}
                tc.sendMessage("제거완료!").queue();
            }
            
            if(command[0].equals("리스너목록")) {
            	for(int i=0;i<botJDA.getRegisteredListeners().size();i++) {
            		tc.sendMessage(botJDA.getRegisteredListeners().get(i).getClass().getSimpleName()).queue();
            	}
            }

            if(command[0].equals("길드목록")) {
            	for(int i=0;i<botJDA.getGuilds().size();i++)
            		tc.sendMessage(botJDA.getGuilds().get(i).getName()).queue();
            }
            if(command[0].equals("유저목록")) {
            	for(int i=0;i<botJDA.getUsers().size();i++)
            		tc.sendMessage(botJDA.getUsers().get(i).getName()).queue();
            }
            if(command[0].equals("보이스채널목록")) {
            	for(int i=0;i<botJDA.getVoiceChannels().size();i++) {
            		tc.sendMessage(botJDA.getVoiceChannels().get(i).getId()+" /// "+botJDA.getVoiceChannels().get(i).getName()).queue();
            	}
            }
            
            if(command[0].equals("보이스채널유저목록")) {
            	for(int i=0;i<botJDA.getVoiceChannels().size();i++) {
            		if(botJDA.getVoiceChannels().get(i).getId().equals(command[1])){
            			for(int j=0;j<botJDA.getVoiceChannels().get(i).getMembers().size();j++)
            				tc.sendMessage(botJDA.getVoiceChannels().get(i).getMembers().get(j).getAsMention()).queue();
            		}
            	}
            }
        }
    }
}
