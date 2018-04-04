package com.labin.discordbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BadMessage extends ListenerAdapter {
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();
        if(user.isBot()) return;
        if(msg.getContentRaw().startsWith("!")) return;
        if(msg.getContentRaw().replaceAll(" ", "").indexOf("왜")!=-1){
            tc.sendMessage("왜는 일본이야, " + user.getAsMention()).queue();
        }
        if(msg.getContentRaw().replaceAll(" ", "").indexOf("웨")!=-1){
            tc.sendMessage("웨는 하스야," + user.getAsMention()).queue();
        }
        if(msg.getContentRaw().replaceAll(" ", "").indexOf("외")!=-1){
            tc.sendMessage("외는 바깥이야," + user.getAsMention()).queue();
        }
        if(msg.getContentRaw().replaceAll(" ", "").indexOf("섹스")!=-1){
            tc.sendMessage(user.getAsMention()+", 울집에서 라면먹고갈가?" ).queue();
        }
        if(msg.getContentRaw().replaceAll(" ", "").indexOf("시발")!=-1||msg.getContentRaw().replaceAll(" ", "").indexOf("씨발")!=-1||msg.getContentRaw().replaceAll(" ", "").indexOf("병신")!=-1
        		||msg.getContentRaw().indexOf("좆")!=-1){
            tc.sendMessage("욕하지마 씨발련아, " + user.getAsMention()).queue();
        }
        if(msg.getContentRaw().replaceAll(" ", "").indexOf("존나")!=-1||msg.getContentRaw().replaceAll(" ", "").indexOf("새기")!=-1||msg.getContentRaw().replaceAll(" ", "").indexOf("새끼")!=-1
        		||msg.getContentRaw().replaceAll(" ", "").indexOf("씨벌")!=-1||msg.getContentRaw().replaceAll(" ", "").indexOf("씨붤")!=-1||msg.getContentRaw().replaceAll(" ", "").indexOf("좃")!=-1){
            tc.sendMessage(msg.getContentRaw()+"는 욕이 잖아 ㅡㅡ " + user.getAsMention()).queue();
        }
	}

}
