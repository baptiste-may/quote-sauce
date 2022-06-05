package fr.djredstone.quoteSauce;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {

    public static final String prefix = ":";
    public static JDA jda;

    public static void main(String[] args) throws LoginException {

        JDABuilder builder = JDABuilder.createDefault(System.getenv().get("TOKEN"));

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setActivity(Activity.watching("être développé"));

        jda = builder.build();

        new Setup(jda);
    }

}
