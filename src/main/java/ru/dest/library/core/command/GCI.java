package ru.dest.library.core.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.Library;
import ru.dest.library.command.BukkitCommand;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.annotation.Permission;
import ru.dest.library.command.annotation.RequireArgs;
import ru.dest.library.command.annotation.exp.Arguments;
import ru.dest.library.command.annotation.exp.MultiPermission;
import ru.dest.library.items.CustomItem;
import ru.dest.library.items.ItemRegistry;
import ru.dest.library.object.RegistryKey;
import ru.dest.library.utils.ChatUtils;
import ru.dest.library.utils.Patterns;

@Permission(p = "bukkit.givecustomitem")
@RequireArgs(args = "{player} {string}")
public class GCI extends BukkitCommand<Library> {

    public GCI(Library plugin) {
        super(plugin, "givecustomitem", "Give Custom item", "/gci <player> <item> [amount]", "gci", "giveci", "gcustomitem");
    }

    @Override
    public void perform(@NotNull ExecutionData execution) throws Exception {
        Player actioned = execution.getPlayer(0);

        String itemId = execution.argument(1);

        if(!itemId.contains(":")) {
            ChatUtils.send(execution.executor(), "&cInvalid itemId: must be a <plugin>:<item>");
            return;
        }

        RegistryKey id = RegistryKey.fromString(itemId);

        if(!ItemRegistry.get().hasRegisteredItem(id)){
            ChatUtils.send(execution.executor(), "&cNo such item");
            return;
        }

        CustomItem item = ItemRegistry.get().getItem(id);
        int amount = 1;

        if(execution.arguments().length > 2){
            if(execution.argument(2).matches(Patterns.INTEGER)){
                amount = Integer.parseInt(execution.argument(2));
            }
        }

        item.give(actioned, amount);
    }
}
