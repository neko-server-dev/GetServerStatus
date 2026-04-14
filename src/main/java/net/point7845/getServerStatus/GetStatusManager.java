package net.point7845.getServerStatus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;

import java.lang.Runtime;
import java.util.stream.Collectors;

public class GetStatusManager {

    @Subscribe
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {
        if (event.getChannel().getId().equals(DiscordSRV.getPlugin().getMainTextChannel().getId())) {
            String message = event.getMessage().getContentRaw();
            if (message.equals("getstatus")) {
                EmbedBuilder embed = new EmbedBuilder();
                Spark spark = Bukkit.getServicesManager().load(Spark.class);
                DoubleStatistic<StatisticWindow.TicksPerSecond> tpsStat = spark.tps();
                GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> msptStat = spark.mspt();
                DoubleStatistic<StatisticWindow.CpuUsage> cpuUsage = spark.cpuSystem();

                int onlinePlayers = Bukkit.getOnlinePlayers().size();
                embed.addField("Current player count", String.format("%d players", onlinePlayers), false);
                String playerNames;
                if (onlinePlayers == 0) {
                    playerNames = "No players are online";
                } else {
                    playerNames = Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .collect(Collectors.joining(", "));
                }
                embed.addField("Online players:\n", playerNames, false);

                double tps = tpsStat.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
                embed.addField("TPS", String.format("%.2f", tps), false);

                if (tps >= 18) {
                    embed.setColor(0x00FF00);
                } else if (tps >= 15) {
                    embed.setColor(0xFFFF00);
                } else if (tps >= 13) {
                    embed.setColor(0xFFA500);
                } else {
                    embed.setColor(0xFF0000);
                }

                double mspt = msptStat.poll(StatisticWindow.MillisPerTick.SECONDS_10).mean();
                embed.addField("MSPT", String.format("%.2f", mspt), false);

                Runtime runtime = Runtime.getRuntime();
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                long totalMemory = runtime.totalMemory();
                embed.addField("Memory usage", String.format("%d GB / %d GB", usedMemory / (1024 * 1024 * 1024), totalMemory / (1024 * 1024 * 1024)), false);

                double cpu = cpuUsage.poll(StatisticWindow.CpuUsage.SECONDS_10);
                embed.addField("CPU usage", String.format("%.2f%%", cpu), false);

                MessageEmbed builtEmbed = embed.build();
                event.getChannel().sendMessageEmbeds(builtEmbed).queue();
            }
        }
    }
}
