package ru.dest.library.object.gui;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.helpers.ActionsExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickHandler {

    private final List<String> clickCommands;
    private final Map<ClickType, List<String>> commandsByClickType;

    public ClickHandler(@NotNull ConfigurationSection item){
        this.clickCommands = item.isSet("click_commands") && item.isList("click_commands") ? item.getStringList("click_commands") : new ArrayList<>();
        this.commandsByClickType = new HashMap<>();

        for(ClickType type : ClickType.values()){
            if(item.isSet(type.name().toLowerCase()+"_commands") && item.isList(type.name().toLowerCase()+"_commands")){
                commandsByClickType.put(type, item.getStringList(type.name().toLowerCase()+"_commands"));
            }else commandsByClickType.put(type, new ArrayList<>());
        }
    }

    public void onClick(Player player, ClickType type){
        clickCommands.forEach(s-> ActionsExecutor.exec(s, player));
        commandsByClickType.get(type).forEach(s->ActionsExecutor.exec(s,player));
    }
}
