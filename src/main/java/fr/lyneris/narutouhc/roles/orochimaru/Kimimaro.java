package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.ForetOsGenerator;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.Utils;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Kimimaro extends NarutoRole {

    private int osUses = 0;
    private boolean usedForest = false;
    private int marqueCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.KIMIMARO;
    }

    @Override
    public void resetCooldowns() {
        marqueCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (marqueCooldown > 0) {
            marqueCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Kimimaro";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §5Kimimaro\n" +
                "§7▎ Objectif: §rSon but est de gagner avec le camp d'§5Orochimaru\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rShikotsumyaku§7”, lorsqu’il l’utilise un menu s’affiche avec plusieurs options :\n" +
                "§e §f\n" +
                "§7Epée en os : lorsqu’il utilise son pouvoir, il obtient un os aussi fort qu’une épée en diamant Tranchant 4, cependant son os disparaît 1 minute après l’avoir utilisé. Il peut utiliser ce pouvoir 5 fois dans la partie.\n" +
                "§e §f\n" +
                "§7Forêt d’os : Lorsqu’il l’utilise, une forêt géante à base de quartz fait son apparition sur un rayon de 50 blocs tout autour de sa position, il peut le faire une seule fois dans la partie, celle-ci disparaît au bout de 3 minutes.\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rMarque Maudite§7”, celui-ci lui permet de recevoir les effets §9Résistance 1§7, §dRégénération 1§7 et §c3 cœurs§7 supplémentaires régénérés, cependant il perd son effet de §bVitesse 1§7 et reçoit l’effet §8Lenteur 1§7, son pouvoir dure 2 minutes, suite à ses deux minutes il reçoit l’effet §lFaiblesse 1§7, perd son effet de §cForce 1§7, garde son effet de §8Lenteur 1§7, tout cela pendant 5 minutes et il possède un délai de 20 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose des effets §cForce 1§7 et §bVitesse 1§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’identité de §5Sakon§7, §5Ukon§7, §5Kidômaru§7, §5Tayuya§7 et §5Jirôbô§7.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.SAKON);
        Role.knowsRole(player, NarutoRoles.UKON);
        Role.knowsRole(player, NarutoRoles.KIDOMARU);
        Role.knowsRole(player, NarutoRoles.TAYUYA);
        Role.knowsRole(player, NarutoRoles.JIROBO);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Shikotsumyaku")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marque Maudite")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Shikotsumyaku")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Shikotsumyaku");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Epee en os").setLore(
                    "§7Lorsqu’il utilise son pouvoir, il obtient",
                    "§7un os aussi fort qu’une épée en diamant",
                    "§7Tranchant 5, cependant son os disparaît",
                    "§71 minute après l’avoir utilisé. Il peut",
                    "§7utiliser ce pouvoir 5 fois dans la partie."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Forêt d’os").setLore(
                    "§7Lorsqu’il l’utilise, une forêt géante à",
                    "§7base de quartz fait son apparition sur",
                    "§7un rayon de 50 blocs tout autour de sa",
                    "§7position, il peut le faire une seule fois",
                    "§7dans la partie, celle-ci disparaît au",
                    "§7bout de 3 minutes."
            ).toItemStack());

            player.openInventory(inv);
        }

        if (Item.interactItem(event.getItem(), "Marque Maudite")) {

            if (marqueCooldown > 0) {
                player.sendMessage(Messages.cooldown(marqueCooldown));
                return;
            }

            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 2, 0, false, false));

            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 60 * 7, 0, false, false));

            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60 * 2, 0, false, false));

            player.setMaxHealth(player.getMaxHealth() + (3D * 2D));
            player.setHealth(player.getHealth() + (3D * 2D));

            Tasks.runLater(() -> {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));


                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 60 * 5, 0, false, false));

                player.setMaxHealth(player.getMaxHealth() - (3D * 2D));
            }, 20 * 60 * 2);

            marqueCooldown = 20 * 60;

        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Shikotsumyaku")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {
                if (osUses >= 5) {
                    player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 5 fois."));
                    return;
                }

                osUses++;
                player.closeInventory();

                player.sendMessage(CC.prefix("§fVous avez reçu votre §aépée§f."));
                player.getInventory().addItem(new EpeeOsItem(new ItemBuilder(Material.BONE).setName(Item.specialItem("Epée en os")).toItemStack()).getItem());
                Tasks.runLater(() -> {
                    player.getInventory().remove(Material.BONE);
                    player.sendMessage(CC.prefix("§cVotre épée à été détruite."));
                }, 60 * 20);
            }

            if (event.getSlot() == 2) {

                if (usedForest) {
                    player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                    return;
                }

                List<Block> blocks = ForetOsGenerator.spawnForetOs(player.getLocation(), 50);
                new ForetOsTask(player.getUniqueId(), blocks).runTaskTimer(narutoUHC, 0, 20);
                usedForest = true;
                player.sendMessage(CC.prefix("§aVous avez fait apparaître votre forêt d'os."));

            }
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

    public static class EpeeOsItem {

        private final ItemStack item;

        public EpeeOsItem(ItemStack item) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

            NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();

            NBTTagList modifiers = new NBTTagList();
            NBTTagCompound damage = new NBTTagCompound();

            damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
            damage.set("Name", new NBTTagString("generic.attackDamage"));
            damage.set("Amount", new NBTTagDouble(12)); //13.25
            damage.set("Operation", new NBTTagInt(0));
            damage.set("UUIDLeast", new NBTTagInt(894654));
            damage.set("UUIDMost", new NBTTagInt(2872));

            modifiers.add(damage);
            compound.set("AttributeModifiers", modifiers);
            nmsStack.setTag(compound);

            this.item = CraftItemStack.asBukkitCopy(nmsStack);
        }

        public ItemStack getItem() {
            return this.item;
        }
    }

    public static class ForetOsTask extends BukkitRunnable {

        private final UUID playerID;
        private final List<Block> blocks;

        private int timer = 3 * 60;

        public ForetOsTask(UUID playerID, List<Block> blocks) {
            this.playerID = playerID;
            this.blocks = blocks;
        }

        @Override
        public void run() {
            Player player = Bukkit.getPlayer(playerID);
            if (this.timer == 0) {
                blocks.forEach(block -> block.setType(Material.AIR));
                if (player != null) {
                    player.sendMessage(CC.prefix("Votre forêt s'est détruite !"));
                }
                cancel();
            }
            if (player != null) {
                Title.sendActionBar(player, "§7▎ §7Forêt en Os §f§l» " + Utils.getFormattedTime(this.timer));
            }
            this.timer--;
        }
    }


}
