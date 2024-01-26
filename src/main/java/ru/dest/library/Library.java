package ru.dest.library;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.bukkit.BukkitPlugin;
import ru.dest.library.bukkit.BukkitServices;
import ru.dest.library.core.command.GCI;
import ru.dest.library.gui.GUI;
import ru.dest.library.items.CustomItem;
import ru.dest.library.items.ItemRegistry;
import ru.dest.library.items.i.*;
import ru.dest.library.core.listener.EventListener;
import ru.dest.library.core.listener.ItemListener;
import ru.dest.library.locale.Lang;
import ru.dest.library.nms.NMS;
import ru.dest.library.nms.TagUtils;
import ru.dest.library.scoreboard.ScoreboardService;
import ru.dest.library.scoreboard.TabScoreboard;
import ru.dest.library.service.economy.PlayerPointsEconomy;
import ru.dest.library.service.economy.VaultEconomy;
import ru.dest.library.session.SessionManager;
import ru.dest.library.task.TaskManager;

import java.io.File;
import java.util.*;

public final class Library extends BukkitPlugin<Library> implements Listener {

    @Getter
    private static Library instance;

    private TaskManager tM;
    @Getter
    private TagUtils nbtUtils;
    @Getter
    private ScoreboardService<?> scoreboardService;

    @Getter
    private NamespacedKey itemId;

    @Getter
    private Lang lang;

    private final Map<Long, GUI> toShow = new HashMap<>();

    @Override
    public void load() {
        File lang = new File(getDataFolder(), "lang.yml");
        if(!lang.exists()) saveResource("lang.yml", true);
        this.lang = Lang.loadYaml(lang);
    }

    @Override
    public void enable() {
        pluginManager.registerEvents(this, this);
        tM = TaskManager.get();

        try {
            boolean hasMethod = false;
            try {
                ItemMeta.class.getMethod("getPersistentDataContainer");
                hasMethod = true;
            }catch (NoSuchMethodException ignored) {}

            NMS nms;

            if(hasMethod) {
                nms = NMS.v1_16_R3;
                logger.info("Detected server version 1.14+. Tag api activated");
            } else {
                String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                nms = NMS.valueOf(version);
                logger.info("Detected server version " + version + ". Tag api activated");
            }
            this.nbtUtils = nms.createTagUtils();
        }catch (IllegalArgumentException e){
            logger.warning("Couldn't detect server version or server version isn't supported. Tag api deactivated.");
        }

        if(getServer().getPluginManager().isPluginEnabled("TAB")){
            try {
                this.scoreboardService = new TabScoreboard();
                logger.info("Found TAB plugin by NEZNAMY. Scoreboard support enabled");
            }catch (Exception e){
                logger.warning("Found TAB plugin by NEZNAMY, but it doesn't have api, maybe old version? For Scoreboard support please update TAB plugin");
            }
        }else {
            logger.warning("For Scoreboard support please install TAB plugin");
        }

        itemId = new NamespacedKey(this, "itemId");

        getTaskManager().callRepeating(this, 10, 0, false, new BukkitRunnable() {
            @Override
            public void run() {
                if(toShow.isEmpty()) return;
                for(long l : toShow.keySet()){
                    toShow.get(l).show();;
                    toShow.remove(l);
                }
            }
        });
        instance = this;

        registry.register(
                new EventListener(this),
                new ItemListener(this)
        );

        registry.register(
                new GCI(this)
        );

        this.initializeServices();
    }

    @Override
    public void disable() {
        tM.cancelAll();
    }

    private void initializeServices(){
        VaultEconomy vaultEconomy = VaultEconomy.init(getServer());

        if(vaultEconomy != null){
            logger.info("Initialized Vault Economy service");
            BukkitServices.register(VaultEconomy.class, vaultEconomy);
        }
        PlayerPointsEconomy playerPointsEconomy = PlayerPointsEconomy.init(getServer());

        if(playerPointsEconomy != null){
            logger.info("Initialized PlayerPoints Economy service");
            BukkitServices.register(PlayerPointsEconomy.class, playerPointsEconomy);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void h(@NotNull AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String message = event.getMessage();

        if(SessionManager.get().tryFinish(player, message))event.setCancelled(true);
    }

    @EventHandler
    public void h(@NotNull final InventoryClickEvent e){
        if( e.getInventory().getHolder() == null)return;
        if(!(e.getInventory().getHolder() instanceof GUI))return;

        ((GUI)e.getInventory().getHolder()).h(e);
    }

    @EventHandler
    public void h(@NotNull final InventoryCloseEvent e){
        if(e.getInventory().getHolder() == null)return;
        if(!(e.getInventory().getHolder() instanceof GUI))return;
        ((GUI)e.getInventory().getHolder()).h(e);
    }

    @EventHandler
    public void h(@NotNull final InventoryDragEvent e){
        if( e.getInventory().getHolder() == null)return;
        if(!(e.getInventory().getHolder() instanceof GUI))return;
        ((GUI)e.getInventory().getHolder()).handle(e);
    }

    @EventHandler
    public void h(@NotNull BlockBreakEvent event){
        ItemStack item = Objects.requireNonNull(event.getPlayer().getEquipment()).getItemInMainHand();

        if(item.getType().name().contains("AIR")) return;

        CustomItem i = ItemRegistry.get().getItem(item);
        if(i == null) return;

        if(i instanceof ITool){
            ( (ITool) i ).onBlockBreak(event);
        }
    }

    @EventHandler
    public void h(@NotNull PlayerInteractEvent event){
        if(!event.hasItem()) return;

        ItemStack item = event.getItem();

        assert item != null;
        CustomItem cItem = ItemRegistry.get().getItem(item);
        if(cItem == null) return;
        event.setCancelled(true);

        if(cItem instanceof IInteractItem) {
            ((IInteractItem)cItem).onInteract(event);
        }
    }

    @EventHandler
    public void h(@NotNull PlayerItemConsumeEvent event){
        ItemStack item = event.getItem();

        CustomItem cItem = ItemRegistry.get().getItem(item);
        if(cItem == null) return;
        event.setCancelled(true);

        if(cItem instanceof IConsumeableItem) {
            ((IConsumeableItem)cItem).onConsume(event);
        }
    }

    @EventHandler
    public void h(@NotNull EntityShootBowEvent event){
        ItemStack item = event.getBow();

        assert item != null;
        CustomItem cItem = ItemRegistry.get().getItem(item);
        if(cItem == null) return;
        event.setCancelled(true);

        if(cItem instanceof IBow) {
            ((IBow)cItem).onShoot(event);
        }
    }

    @EventHandler
    public void h(@NotNull EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof LivingEntity))return;

        LivingEntity damager = (LivingEntity) event.getDamager();

        ItemStack hand = Objects.requireNonNull(damager.getEquipment()).getItemInMainHand();

        CustomItem cItem = ItemRegistry.get().getItem(hand);
        if(cItem == null) return;
        event.setCancelled(true);

        if(cItem instanceof ISword) {
            ((ISword)cItem).onDamage(event);
        }
    }

    public void g(GUI g){
        this.toShow.put(System.currentTimeMillis(), g);
    }
}
