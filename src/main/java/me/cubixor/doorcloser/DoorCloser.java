package me.cubixor.doorcloser;

import org.bukkit.plugin.java.JavaPlugin;

public final class DoorCloser extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("closedoors").setExecutor(new Command());
    }

    @Override
    public void onDisable() {
    }
}
