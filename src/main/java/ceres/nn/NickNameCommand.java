package ceres.nn;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static ceres.nn.NickName.convertToMiniMessageFormat;

public class NickNameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("nickname.command.self")) {
            NickName.sendMessage(sender, "command-self.have-no-permission");
            return false;
        }
        if (sender instanceof Player player){
            if (args.length == 0) {
                NickName.sendMessage(sender, "command-self.missing-args");
            }else {
                StringBuilder message = new StringBuilder();
                for (String arg : args) {
                    message.append(arg).append(" ");
                }
                String nickName = message.substring(0, message.length()-1);
                if (NickName.banNickName.contains(nickName.toLowerCase())) {
                    NickName.sendMessage(sender, "command-self.banned-nick");
                }else {
                    player.displayName(MiniMessage.miniMessage().deserialize(convertToMiniMessageFormat(nickName)));
                    NickName.instance.getConfig().set("player-data." + player.getName(), nickName);
                    Bukkit.getAsyncScheduler().runNow(NickName.instance, scheduledTask -> {
                        NickName.instance.saveConfig();
                        NickName.sendMessage(sender, "command-self.successful");
                    });
                    return true;
                }
            }
        }else {
            NickName.sendMessage(sender, "command-self.is-console");
        }
        return false;
    }
}
