package ru.dest.library.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dest.library.object.Pair;
import ru.dest.library.utils.ColorUtils;
import ru.dest.library.utils.StringUtils;
import ru.dest.library.utils.Utils;

import java.util.*;
import java.util.function.Consumer;

import static ru.dest.library.utils.ColorUtils.parse;

/**
 * Represents a Simple GUI which can be showed for player
 *
 * @since 1.0
 * @author DestKoder
 */
public class GUI implements InventoryHolder {

    protected final Inventory inventory;
    protected final Player opener;

    private final GUIConfig config;

    private final Map<Integer, Consumer<InventoryClickEvent>> handlers;
    private List<Pair<String,String>> format;
    private Consumer<InventoryCloseEvent> closeHandler;

    /**
     * Create gui by config & opener
     * @param config {@link GUIConfig} config
     * @param opener {@link Player} player for whom opens gui
     */
    public GUI(@NotNull GUIConfig config,@NotNull Player opener) {
        this(config, opener, null);
    }

    /**
     * Create gui by config & opener with title formatting
     * @param config {@link GUIConfig} config
     * @param opener {@link Player} player for whom opens gui
     * @param titleFormat title format
     */
    public GUI(@NotNull GUIConfig config,@NotNull Player opener,@Nullable List<Pair<String,String>> titleFormat){
        this.config = config;
        this.opener = opener;
        this.format = titleFormat == null ? new ArrayList<>() : titleFormat;
        this.handlers = new HashMap<>();
        this.inventory = Bukkit.createInventory(this, config.getSize(), Utils.applyPlaceholders(titleFormat == null ? config.getTitle() : config.getTitle(titleFormat), opener));
        this.fill();
    }

    /**
     * Create gui with title, size & opener
     * @param title Title of GUI
     * @param size Size of gui
     * @param opener {@link Player} player for whom opens gui
     */
    public GUI(String title, int size, Player opener){
        this.config = null;
        this.opener = opener;
        this.handlers = new HashMap<>();
        this.inventory = Bukkit.createInventory(this, size, Utils.applyPlaceholders(ColorUtils.parse(title), opener));
        this.format = new ArrayList<>();
    }

    /**
     * Create gui with specified type
     * @param title Title of GUI
     * @param type {@link InventoryType} of GUI
     * @param opener {@link Player} player for whom opens gui
     */
    public GUI(String title, InventoryType type, Player opener){
        this.config = null;
        this.opener = opener;
        this.handlers = new HashMap<>();
        this.inventory = Bukkit.createInventory(this, type, ColorUtils.parse(Utils.applyPlaceholders(title, opener)));
        this.format = new ArrayList<>();
    }

    protected void setItem(int slot, @NotNull ItemStack itemStack){
        ItemStack item = itemStack.clone();

        ItemMeta meta = item.getItemMeta();

        assert meta != null;

        if(meta.hasDisplayName()) meta.setDisplayName(parse(Utils.applyPlaceholders(StringUtils.format(meta.getDisplayName(), format), opener)));

        if(meta.hasLore()){
            List<String> newLore = new ArrayList<>();
            meta.getLore().forEach(s -> newLore.add(parse(Utils.applyPlaceholders(StringUtils.format(s, format), opener))));
            meta.setLore(newLore);
        }

        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    private void fill(){
        if(config != null) config.applyItems((l, i) -> l.forEach(slot -> setItem(slot, i.getItem())));
    }

    public final void h(@NotNull InventoryClickEvent e){
        e.setCancelled(true);
        if(config != null){
            config.tryHandle(e);
        }
        if(handlers.containsKey(e.getSlot())){
            handlers.get(e.getSlot()).accept(e);
        }
        handle(e);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GUI gui = (GUI) object;
        return Objects.equals(opener, gui.opener) && Objects.equals(config, gui.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opener, config);
    }

    public final void h(InventoryCloseEvent e){if(closeHandler != null) closeHandler.accept(e);}

    public void handle(InventoryDragEvent event){}

    @Override
    public final Inventory getInventory() {
        return inventory;
    }

    /**
     * Get slots without items
     * @return List of slots numbers
     */
    protected List<Integer> getEmptySlots(){
        List<Integer> toReturn = new ArrayList<>();

        for(int i =0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) toReturn.add(i);
        }
        return toReturn;
    }

    /**
     * Set custom handler to a slot
     * @param slot slot to set handler on
     * @param handler handler
     */
    protected final void setHandler(int slot, Consumer<InventoryClickEvent> handler){
        this.handlers.put(slot, handler);
    }

    /**
     * Set a close handler called on gui close;
     * @param handler handler
     */
    protected final void setCloseHandler(Consumer<InventoryCloseEvent> handler){
        this.closeHandler = handler;
    }

    /**
     * Handling an InventoryDragEvent
     * @param e event
     */
    protected void handle(InventoryClickEvent e){}

    /**
     * Display gui to player
     */
    public void show(){
        this.opener.openInventory(inventory);
    }
}
