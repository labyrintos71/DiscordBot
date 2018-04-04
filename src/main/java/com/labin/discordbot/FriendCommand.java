package com.labin.discordbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class FriendCommand extends ListenerAdapter {
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();
        if(user.isBot()) return;

        if(msg.getContentRaw().charAt(0) == '!'){
            String[] args = msg.getContentRaw().substring(1).split(" ");
            //args 는 명령어
            //args 1 2 3 은 인수가 있을경우
            if(args[0].equals("ㅎㅇ")) tc.sendMessage("ㅎㅇ").queue();
            switch(args[0]) {
            case "최태열":
            	break;
            }
            if(args[0].indexOf("님태열")!=-1) tc.sendMessage("좆같은소리하지마;").queue();
            if(args[0].indexOf("동훈")!=-1) tc.sendMessage("씨발련아").queue();
            if(args[0].indexOf("퇴까")!=-1) tc.sendMessage("씨 발!").queue();
            if(args[0].indexOf("용진")!=-1) tc.sendMessage("현재 던파중이여서 연락을 할수 없으며,, 자세한 연락은 대신택배로 부탁드립니다").queue();
        }
    }
}
