package ru.dest.library.object.gui;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dest.library.helpers.ActionsExecutor;
import ru.dest.library.utils.ConfigUtils;
import ru.dest.library.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.dest.library.utils.ColorUtils.parse;

public class GUIItem {

    @Getter
    private final ItemStack item;
    private final List<String> clickCommands;
    private final Map<ClickType, List<String>> commandsByClickType;

    public GUIItem(ConfigurationSection item){
        this.item = ConfigUtils.getItem(item);
        this.clickCommands = item.isSet("click_commands") && item.isList("click_commands") ? item.getStringList("click_commands") : new ArrayList<>();
        this.commandsByClickType = new HashMap<>();

        for(ClickType type : ClickType.values()){
            if(item.isSet(type.name().toLowerCase()+"_commands") && item.isList(type.name().toLowerCase()+"_commands")){
                commandsByClickType.put(type, item.getStringList(type.name().toLowerCase()+"_commands"));
            }else commandsByClickType.put(type, new ArrayList<>());
        }
    }

    public ItemStack finishItem(Player player) {
        ItemStack item = this.item.clone();
        ItemMeta meta = item.getItemMeta();

        if(meta.hasDisplayName()) meta.setDisplayName(parse(Utils.applyPlaceholders(meta.getDisplayName(), player)));
        if(meta.hasLore()){
            List<String> newLore = new ArrayList<>();
            meta.getLore().forEach(s -> newLore.add(parse(Utils.applyPlaceholders(s, player))));
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public void onClick(Player player, ClickType type){
        clickCommands.forEach(s-> ActionsExecutor.exec(s, player));
        commandsByClickType.get(type).forEach(s->ActionsExecutor.exec(s,player));
    }

}
