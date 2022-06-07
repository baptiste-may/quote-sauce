package fr.djredstone.quoteSauce;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import org.jetbrains.annotations.NotNull;

public class Main implements EventListener {

    public static final String prefix = ",";
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {

        JDABuilder builder = JDABuilder.createDefault(System.getenv().get("TOKEN"));

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setActivity(Activity.playing("être développé"));

        builder.addEventListeners(new Main());
        jda = builder.build();

        jda.awaitReady();

        new Setup(jda);
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            System.out.println("Quote Sauce Bot is online!");
    }

}
