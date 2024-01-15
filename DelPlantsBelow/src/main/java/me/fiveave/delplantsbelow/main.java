package me.fiveave.delplantsbelow;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class main extends JavaPlugin {
    static main plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Objects.requireNonNull(this.getCommand("delplantsbelow")).setExecutor(new cmds());
        Objects.requireNonNull(this.getCommand("delplantsbelow")).setTabCompleter(new cmds());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    static String header = ChatColor.GRAY + "[" + ChatColor.RED + "Del" + ChatColor.GREEN + "Plants" + ChatColor.GOLD + "Below" + ChatColor.GRAY + "] ";
}
