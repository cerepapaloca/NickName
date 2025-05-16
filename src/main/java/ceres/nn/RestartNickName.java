package ceres.nn;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RestartNickName implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("nickname.command.reset")) {
            NickName.sendMessage(sender, "command-reset.have-no-permission");
            return false;
        }
        if (sender instanceof Player player) {
            player.displayName(player.name());
            NickName.instance.getConfig().set("player-data." + player.getName(), player.getName());
            Bukkit.getAsyncScheduler().runNow(NickName.instance, scheduledTask -> {
                NickName.instance.saveConfig();
                NickName.sendMessage(sender, "command-reset.success");
            });
            return true;
        }else {
            NickName.sendMessage(sender, "command-self.is-console");
        }
        return false;
    }
}
