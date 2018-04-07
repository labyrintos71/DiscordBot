package com.labin.discordbot;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MillitaryCommand extends ListenerAdapter {
    private MillitaryCalculator mc=new MillitaryCalculator();

	public MillitaryCommand() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();
        if(user.isBot()) return;

        String[] args = msg.getContentRaw().substring(1).split(" ");
        if(args[0].equals("전역일")) {
        	if(args.length==1) {
        		tc.sendMessage("현재 사용가능한 닉네임은 "+mc.broadcastNickName()+" 입니다.").queue();
        	}
        	else {
        		mc.setName(args[1]);
        		// Create the EmbedBuilder instance
        		EmbedBuilder eb = new EmbedBuilder();
        		eb.setTitle(args[1]+"("+mc.getRealName()+") 님", null);
        		//에러처리하는거 만들기
        		eb.setDescription(mc.getStartDate()+"~"+mc.getEndDate());
        		eb.setColor(Color.red);
        		//eb.setColor(new Color(0xF40C0C));
        		//eb.setColor(new Color(255, 0, 54));
        		eb.addField("현재 복무일", mc.getStartDays()+"일", true);
        		eb.addField("남은 복무일", mc.getEndDays()+"일", true);
        		eb.addBlankField(true);
        		eb.addField("현재 복무율", mc.getStartPercent()+"% ", true);
        		eb.addField("남은 복무율", mc.getEndPercent()+"% ", true);
        		eb.addBlankField(true);
        		/*
        		    Add embed author:
        		    1. Arg: name as string
        		    2. Arg: url as string (can be null)
        		    3. Arg: icon url as string (can be null)
        		 */
        		//eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
        		/*
        		    Set footer:
        		    1. Arg: text as string
        		    2. icon url as string (can be null)
        		 */
        		//eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
        		//eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
        		tc.sendMessage(eb.build()).queue();
        	}
        }
      
	}

}
