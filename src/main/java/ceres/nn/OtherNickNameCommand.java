package ceres.nn;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static ceres.nn.NickName.convertToMiniMessageFormat;

public class OtherNickNameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("nickname.command.other")) {
            NickName.sendMessage(sender, "command-other.have-no-permission");
            return false;
        }
        if (args.length == 0) {
            NickName.sendMessage(sender, "command-other.missing-args");
        }else  if (args.length == 1) {
            NickName.sendMessage(sender, "command-other.missing-nick-name");
        }else {
            StringBuilder message = new StringBuilder();
            Player target = Bukkit.getPlayer(args[0]);

            for (int i = 1; i < args.length; i++){
                message.append(args[i]).append(" ");
            }
            String nickName = message.substring(0, message.length()-1);
            if (target != null) {
                target.displayName(MiniMessage.miniMessage().deserialize(convertToMiniMessageFormat(nickName)));
            }
            NickName.instance.getConfig().set("player-data." + args[0], nickName);
            Bukkit.getAsyncScheduler().runNow(NickName.instance, scheduledTask -> {
                NickName.instance.saveConfig();
                NickName.sendMessage(sender, "command-other.success");
            });


            return true;
        }
        return false;
    }
}
