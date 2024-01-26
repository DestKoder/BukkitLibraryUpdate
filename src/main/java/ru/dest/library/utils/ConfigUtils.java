package ru.dest.library.utils;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.Library;
import ru.dest.library.exception.InvalidMaterialException;
import ru.dest.library.exception.ItemLoadException;
import ru.dest.library.exception.MissingConfigurationException;
import ru.dest.library.nms.TagUtils;

public class ConfigUtils {

    private static TagUtils nbt;

    /**
     * Save location to {@link ConfigurationSection}
     * @param loc - {@link Location} to save;
     * @param section - {@link ConfigurationSection} in which location will be saved
     * @param saveYawAndPitch is needed to save yaw and pitch?
     */
    public static void saveLocation(@NotNull Location loc,@NotNull ConfigurationSection section, boolean saveYawAndPitch) {
        if(loc.getWorld() == null) return;

        section.set("world", loc.getWorld().getName());
        section.set("posX", loc.getBlockX());
        section.set("posY", loc.getBlockY());
        section.set("posZ", loc.getBlockZ());

        if(saveYawAndPitch){
            section.set("yaw", loc.getYaw());
            section.set("pitch", loc.getPitch());
        }
    }

    /**
     * Load location from config
     * @param section section to load Location from
     * @return {@link Location} loaded from section;
     * @throws MissingConfigurationException if one of required arguments is missing
     */
    @NotNull
    public static Location loadLocation(@NotNull ConfigurationSection section){

        if(!section.isSet("world") || !section.isSet("posX") || !section.isSet("posY") || !section.isSet("posZ")) throw new MissingConfigurationException("Invalid location config. One of values (world, x, y, z) is missing");

        World world = Bukkit.getWorld(section.getString("world"));

        if(world == null){
            throw new NullPointerException("Location world doesn't exists!");
        }

        Location location = new Location(world, section.getDouble("posX"), section.getDouble("posY"), section.getDouble("posZ"));

        if(section.isSet("yaw")) location.setYaw((float) section.getDouble("yaw"));
        if(section.isSet("pitch")) location.setPitch((float) section.getDouble("pitch"));

        return location;
    }

    /**
     * Load item from config section
     * @param section {@link ConfigurationSection} with item info
     * @return item which will loaded from section
     */

    @Contract("_ -> new")
    public static @NotNull ItemStack getItem(@NotNull ConfigurationSection section){
        if(nbt == null) nbt = Library.getInstance().getNbtUtils();
        if(!hasString("material", section)) throw new ItemLoadException("ID not specified");
        ItemStack item;
        try{
            item = ItemUtils.createByMaterial(section.getString("material"));
        }catch (InvalidMaterialException e){
            throw new ItemLoadException("Invalid material");
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;

        if(hasString("name", section)) meta.setDisplayName(ColorUtils.parse(section.getString("name")));
        if(hasList("lore", section)) meta.setLore(ColorUtils.parse(section.getStringList("lore")));

        item.setItemMeta(meta);

        if(hasSection("nbt", section) && nbt != null ){
            ConfigurationSection nbtSection = section.getConfigurationSection("nbt");
            for(String key : nbtSection.getKeys(false)){
                if(!key.contains(":")){
                    if(nbtSection.isInt(key)) item = nbt.setIntegerTag(item, key, nbtSection.getInt(key));
                    else item = nbt.setStringTag(item, key, nbtSection.getString(key));
                    continue;
                }
                String[] data = key.split(":");
                NamespacedKey k = new NamespacedKey(data[0], data[1]);

                if(nbtSection.isInt(key)) item = nbt.setIntegerTag(item, k, nbtSection.getInt(key));
                item = nbt.setStringTag(item, k, nbtSection.getString(key));

            }
        }

        if(hasSection("enchantments", section)){
            ItemUtils.applyEnchantments(item, section.getConfigurationSection("enchantments"));
        }

        if(hasInt("model", section) && nbt != null ) ItemUtils.setCustomModelData(item, section.getInt("model"));

        return item;
    }

    public static boolean hasString(String key, @NotNull ConfigurationSection section){
        return section.isSet(key) && section.isString(key);
    }

    public static boolean hasList(String key, @NotNull ConfigurationSection section){
        return section.isSet(key) && section.isList(key);
    }

    public static boolean hasInt(String key, @NotNull ConfigurationSection section){
        return section.isSet(key) && section.isInt(key);
    }

    public static boolean hasSection(String key, @NotNull ConfigurationSection section){
        return section.isSet(key) && section.isConfigurationSection(key);
    }
}
