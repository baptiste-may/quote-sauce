package fr.djredstone.quoteSauce;

import net.dv8tion.jda.api.JDA;

import fr.djredstone.quoteSauce.commands.Ping;

public class Setup {

    public Setup(JDA jda) {

        commands(jda);

    }

    private static void commands(JDA jda) {
        jda.addEventListener(new Ping());
    }

}
