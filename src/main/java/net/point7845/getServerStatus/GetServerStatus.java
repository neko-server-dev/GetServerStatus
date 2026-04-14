package net.point7845.getServerStatus;

import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;

public final class GetServerStatus extends JavaPlugin {

    @Override
    public void onEnable() {
        DiscordSRV.api.subscribe(new GetStatusManager());
    }

    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(GetStatusManager.class);
    }
}
