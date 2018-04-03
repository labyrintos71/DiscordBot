package com.labin.discordbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReinforceCommand extends ListenerAdapter {

	public ReinforceCommand() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();
        if(user.isBot()) return;
        
        if(msg.getContentRaw().startsWith("!강화")){
        	if(msg.getContentRaw().indexOf(" ")==-1) {
        		tc.sendMessage("강화할 이름을 입력해주세요 " + user.getAsMention()).queue();
        	}
        
        }
      
	}
}