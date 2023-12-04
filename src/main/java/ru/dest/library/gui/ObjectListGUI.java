package ru.dest.library.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.object.BukkitItem;
import ru.dest.library.object.Pair;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * This class represents a basic object-list gui
 * @param <ItemObject> Object Class which realizing interface IHoldItemStack
 */
public class ObjectListGUI<ItemObject extends BukkitItem> extends GUI{

    protected List<ItemObject> items;
    protected List<Integer> emptySlots;

    protected BiConsumer<InventoryClickEvent, ItemObject> onItemClick;

    protected int page, pages;

    public ObjectListGUI(GUIConfig template, Player opener, List<Pair<String,String>> titleFormat, @NotNull List<ItemObject> items) {
        super(template, opener, titleFormat);
        this.items = items;
        this.emptySlots = getEmptySlots();
        this.page = 0;
        this.pages = items.size() % emptySlots.size() == 0 ? items.size() / emptySlots.size() : items.size() / emptySlots.size()+1;

        this.refillContent();

        this.setHandler(template.getSystemHandler("prev"), event -> {
            if(page < pages) {
                page++;
                refillContent();
            }
        });

        this.setHandler(template.getSystemHandler("prev"), event -> {
            if(page > 0) {
                page--;
                refillContent();
            }
        });
    }

    public ObjectListGUI(GUIConfig template, Player opener, @NotNull List<ItemObject> items) {
        this(template, opener, null, items);
    }

    private void refillContent(){
        this.clearContent();
        for(int i = 0; i < emptySlots.size(); i++){
            int item = i + (page == 0 ? 0 : page*emptySlots.size());

            if(item < items.size()){
                inventory.setItem(emptySlots.get(i), items.get(item).getItem());
            }
        }
    }

    private void clearContent(){
        for(int i : emptySlots){
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
    }


    @Override
    public final void handle(@NotNull InventoryClickEvent event) {
        if(onItemClick == null) return;
        int slot = event.getSlot();

        if(inventory.getItem(slot) == null || inventory.getItem(slot).getType() == Material.AIR) return;
        if(this.onItemClick == null) return;
        if(!emptySlots.contains(slot)) return;

        int itemPos = page == 0 ? slot-emptySlots.get(0) :  this.page * this.emptySlots.size() + slot;

        if(items.size() < itemPos) {
            System.out.println("Internal error item size " + items.size() +" < itemPos " + itemPos +". Page: " + page + "; Slot: " + (slot-emptySlots.get(0)));
            return;
        }

        this.onItemClick.accept(event, items.get(itemPos));
    }

    @Override
    public final void handle(@NotNull InventoryDragEvent event) {
        event.setCancelled(true);
    }

    /**
     * Set's a click on item handler
     * @param handler handler
     */
    protected final void setOnItemClickHandler(BiConsumer<InventoryClickEvent, ItemObject> handler){
        this.onItemClick = handler;
    }

}
