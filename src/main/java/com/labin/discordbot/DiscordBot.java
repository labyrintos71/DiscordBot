package com.labin.discordbot;

import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;

public class DiscordBot {
	 private JDA jda;
	 public JDABuilder jb;
	public void start() {
        try {
            jda =new JDABuilder(AccountType.BOT).setAutoReconnect(true).setToken("NDMwMjMwNjg1MDE0NTU2Njcz.DaNLZw._4YaaOM8sAuWgvKgy1qzsyUY2Ow").buildBlocking();
            jda.addEventListener(new BadMessage());
            jda.addEventListener(new FriendCommand());
            jda.addEventListener(new MillitaryCommand());
            jda.addEventListener(new ReinforceCommand());
            jda.addEventListener(new MusicCommand(jda));
            jda.addEventListener(new SystemCommand(jda));
            //jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB,Game.playing("봇실행중"));
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
       // Event event=new Event(jda);
        List<TextChannel> tc = jda.getTextChannels();
        tc.get(0).sendMessage("ㅎㅇ 출근함").queue();
      
       
	}

}
