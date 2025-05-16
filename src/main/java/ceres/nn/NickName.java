package ceres.nn;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class NickName extends JavaPlugin implements Listener {

    public static NickName instance;
    public static final List<String> banNickName = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("nickname").setExecutor(new NickNameCommand());
        this.getCommand("othernickname").setExecutor(new OtherNickNameCommand());
        this.getCommand("resetnickname").setExecutor(new RestartNickName());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @Override
    public void reloadConfig() {
        super.reloadConfig();
        banNickName.clear();
        banNickName.addAll(getConfig().getStringList("ban-nickname").stream().map(String::toLowerCase).toList());
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.displayName(MiniMessage.miniMessage().deserialize(convertToMiniMessageFormat(
                Objects.requireNonNullElse(getConfig().getString("player-data." + player.getName()), player.getName())
        )));
    }

    @NotNull
    @Contract(pure = true)
    public static String convertToMiniMessageFormat(@NotNull String input) {
        input = input.replace('§', '&');
        input = input.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");

        input = input.replace("&l", "<bold>");
        input = input.replace("&o", "<italic>");
        input = input.replace("&n", "<underlined>");
        input = input.replace("&m", "<strikethrough>");
        input = input.replace("&k", "<obfuscated>");
        input = input.replace("&r", "<reset>");
        input = input.replace("&0", "<black>");
        input = input.replace("&1", "<dark_blue>");
        input = input.replace("&2", "<dark_green>");
        input = input.replace("&3", "<dark_aqua>");
        input = input.replace("&4", "<dark_red>");
        input = input.replace("&5", "<dark_purple>");
        input = input.replace("&6", "<gold>");
        input = input.replace("&7", "<gray>");
        input = input.replace("&8", "<dark_gray>");
        input = input.replace("&9", "<blue>");
        input = input.replace("&a", "<green>");
        input = input.replace("&b", "<aqua>");
        input = input.replace("&c", "<red>");
        input = input.replace("&d", "<light_purple>");
        input = input.replace("&e", "<yellow>");
        input = input.replace("&f", "<white>");

        return input;
    }

    public static void sendMessage(CommandSender sender, @NotNull String path) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize(instance.getConfig().getString("prefix") + " " + instance.getConfig().getString("messages." + path)));
    }


}
