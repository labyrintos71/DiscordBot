package com.labin.discordbot;

import java.util.List;
import java.util.Random;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class SystemCommand extends ListenerAdapter {
	private JDA botJDA;
	private int sw=0;
	public SystemCommand(JDA system) {
		botJDA=system;
	}
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();

        if(user.isBot()) {
        	if(msg.getContentRaw().equals("ㅎㅇ 출근함")&&sw==1) {
        		tc.sendMessage("주인님,, 저말고 또 다른 저를 부르신건가요?").queue();
        		tc.sendMessage("제가 쓸모없어진거군요,, 저 먼저 가볼게요 ㅜㅜ").queue();
                botJDA.shutdown();
                botJDA=null;
        	}
        	if(msg.getContentRaw().equals("ㅎㅇ 출근함")) {
        		sw=1;
        	}
        	else return;
        }
        if(msg.getContentRaw().charAt(0) == '!'){
            String[] command = msg.getContentRaw().substring(1).split(" ");
            switch(command[0]) {
            
            	case "들어가":
            	case "퇴근":
            	case "퇴근해":
            		int temp=new Random().nextInt(3);
                	if(temp==1) {
    	                tc.sendMessage("음,, 생각해 볼게").queue();
    	                //botJDA.shutdown();
    	               // botJDA=null;
                	}else if(temp==2){
    	                tc.sendMessage("싫어").queue();
                			
                	}else {
    	                tc.sendMessage("퇴 까").queue();
                	}
                	break;
                	
            	case "꺼져":
            		if(user.getId().equals("333577858729639937")) {
                		tc.sendMessage("넹...").queue();
    	                botJDA.shutdown();
    	                botJDA=null;
            		}else {
            			tc.sendMessage("니가 뭔데 나한테 이래라 저래라야?").queue();
            		}
            		break;
	                
            	case "리스너 제거":
            		tc.sendMessage("제거중,,,").queue();
                	for(int i=0;i<botJDA.getRegisteredListeners().size();i++) {
                		if(!botJDA.getRegisteredListeners().get(i).getClass().getSimpleName().equals("SystemCommandListener")) {
                			botJDA.removeEventListener(botJDA.getRegisteredListeners().get(i));
                			i=-1;
                		}
                	}
            		break;
            	
            	case "청소":
            		tc.sendMessage("현재 지원하고있지 않습니다.").queue();
            		 /*MessageHistory mh = new MessageHistory(tc);
                     List<Message> msgs = mh.retrievePast(Integer.parseInt(command[1])).complete();
                     tc.deleteMessages(msgs).complete();
                     tc.sendMessage(mh.getRetrievedHistory().size()+"중 "+command[1] + " 개의 메시지를 제거했습니다.").queue();*/
            		break;
            		
            	case "리스너 목록":
            		for(int i=0;i<botJDA.getRegisteredListeners().size();i++) 
            			tc.sendMessage(botJDA.getRegisteredListeners().get(i).getClass().getSimpleName()).queue();
            		break;
            		
            	case "길드목록":
            		for(int i=0;i<botJDA.getGuilds().size();i++)
            			tc.sendMessage(botJDA.getGuilds().get(i).getName()).queue();
            		break;
            		
            	case "유저목록":
            		for(int i=0;i<botJDA.getUsers().size();i++)
            			tc.sendMessage(botJDA.getUsers().get(i).getName()).queue();
            		break;
            		
            	case "보이스채널목록":
            		for(int i=0;i<botJDA.getVoiceChannels().size();i++) {
            		tc.sendMessage(botJDA.getVoiceChannels().get(i).getId()+" /// "+botJDA.getVoiceChannels().get(i).getName()).queue();
            		}
            		break;
            		
            	case "보이스채널유저목록":
            		for(int i=0;i<botJDA.getVoiceChannels().size();i++) {
	            		if(botJDA.getVoiceChannels().get(i).getId().equals(command[1])){
	            			for(int j=0;j<botJDA.getVoiceChannels().get(i).getMembers().size();j++)
	            				tc.sendMessage(botJDA.getVoiceChannels().get(i).getMembers().get(j).getAsMention()).queue();
	            		}
            		}
            		break;	
            }
        }
    }
}
