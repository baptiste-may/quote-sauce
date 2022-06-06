package fr.djredstone.quoteSauce;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import fr.djredstone.quoteSauce.commands.Ping;
import org.apache.commons.io.FilenameUtils;

public class Setup {

    public Setup(JDA jda) {

        commands(jda);
        activity();
        try {
            themeList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void commands(JDA jda) {
        jda.addEventListener(new Ping());
    }

    private static int activity = 0;
    private static final Activity[] activities = new Activity[] {
            Activity.playing("être développé"),
            Activity.watching("si quelqu'un utilise ,help"),
            Activity.playing("vous faire deviner des citations")
    };

    private static void activity() {

        Runnable changeActivity = () -> {
            Main.jda.getPresence().setActivity(activities[activity]);
            activity ++;
            if (activity == activities.length) activity = 0;
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(changeActivity, 0, 15, TimeUnit.SECONDS);
    }

    private static void themeList() throws IOException {
        StringBuilder builder = new StringBuilder("Available themes : ");
        String path = Main.relativePath + "\\themes";
        boolean first = true;
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            if (!first) builder.append(" - ");
            else first = false;
            builder.append(FilenameUtils.removeExtension(file.getName()));
        }
        System.out.println(builder);
    }

}
