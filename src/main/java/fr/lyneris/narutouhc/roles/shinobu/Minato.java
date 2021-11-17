package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Minato extends NarutoRole {

    public List<Arrow> arrows = new ArrayList<>();
    private boolean usedKurama = false;
    public int kuramaCooldown = 0;
    public ArrayList<Location> balises = new ArrayList<>();
    public int baliseCooldown = 0;
    private int arrowCooldown = 0;

    @Override
    public void resetCooldowns() {
        kuramaCooldown = 0;
        baliseCooldown = 0;
        arrowCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(kuramaCooldown > 0) {
            kuramaCooldown--;
        }

        if(baliseCooldown > 0) {
            baliseCooldown--;
        }

        if(arrowCooldown > 0) {
            arrowCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Minato";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.NARUTO);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.DURABILITY, 5).setName(Item.specialItem("Shuriken Jutsu")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kurama")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Shuriken Jutsu")).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event.getItem(), "Kurama")) {
            if(kuramaCooldown > 0) {
                player.sendMessage(Messages.cooldown(kuramaCooldown));
                return;
            }

            usedKurama = true;
            kuramaCooldown = 20*60;
            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aKurama§f."));

            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 0, false, false));
            Tasks.runLater(() -> usedKurama = false, 5*20*60);
        }

        if(Item.interactItem(event.getItem(), "Shuriken Jutsu")) {

            if(player.isSneaking()) {
                TextComponent text = new TextComponent("§7▎ §aCliquez-ici si vous souhaitez poser une balise ou utilisez la commande /ns balise");
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ns balise"));
                player.spigot().sendMessage(text);
            } else {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                    nearbyPlayer++;
                }
                if(nearbyPlayer > 7 && nearbyPlayer <= 16) {
                    i = 18;
                } else if(nearbyPlayer > 16) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Shuriken Jutsu");
                inv.setItem(1, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + player.getName()).setSkullOwner(player.getName()).toItemStack());
                int j = 2;
                for (Player entity : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        if(event.getInventory().getName().equals("Shuriken Jutsu")) {
            event.setCancelled(true);
            if(!event.getCurrentItem().hasItemMeta()) return;
            if(!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
            if(target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }
            Inventory inv = Bukkit.createInventory(null, 9, "Balises » " + target.getName());
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            int i = 1;
            for (Location location : balises) {
                inv.setItem(i, new ItemBuilder(Material.BEACON).setName("§6Balise " + i).setAmount(i).setLore(
                        "§f(§c" + location.getBlockX() + "§f, §c" + location.getBlockY() + "§f, §c" + location.getBlockZ() + "§f)"
                ).toItemStack());
                i++;
            }
            player.openInventory(inv);
        }

        if(event.getInventory().getName().startsWith("Balises")) {
            event.setCancelled(true);
            Player target = Bukkit.getPlayer(event.getInventory().getName().replace("Balises » ", ""));
            if(!event.getCurrentItem().hasItemMeta()) return;
            int i = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6Balise ", ""));
            Location balise = balises.get(i-1);

            if(baliseCooldown > 0) {
                player.sendMessage(Messages.cooldown(baliseCooldown));
                return;
            }

            if(target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            baliseCooldown = 5*60;
            target.teleport(balise);
            player.sendMessage(CC.prefix("§fVous avez téléporté §a" + target.getName() + " §fsur une de vos balises"));
            target.sendMessage(CC.prefix("§cMinato §fvous a téléporté sur une de ses balises."));
        }

    }

    @Override
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event, Player shooter) {
        if(!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();

        if(!Item.specialItem(shooter.getItemInHand(), "Shuriken Jutsu")) return;

        if(arrowCooldown > 0) {
            shooter.sendMessage(Messages.cooldown(arrowCooldown));
            return;
        }

        arrowCooldown = 15;

        this.arrows.add(arrow);
    }

    @Override
    public void onProjectileHitEvent(ProjectileHitEvent event, Player shooter) {
        if(!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();

        if(arrows.contains(arrow)) {
            shooter.teleport(arrow.getLocation());
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        NarutoRole targetRole = roleManager.getRole(event.getEntity());
        NarutoRole role = roleManager.getRole(killer);

        if(targetRole == null || role == null) return;

        //TODO CHECK ACTUAL CAMP
        if(targetRole.getCamp() != role.getCamp()) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2*20*60, 2, false, false));
            killer.removePotionEffect(PotionEffectType.SPEED);

            killer.sendMessage(CC.prefix("§fVous avez tué un joueur du camp opposé, vous obtenez donc un effet de §bSpeed 3 §fpendant 2 minutes."));
            Tasks.runLater(() -> {
                killer.removePotionEffect(PotionEffectType.SPEED);
                killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            }, 2*20*60);
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if(args[0].equalsIgnoreCase("smell")) {
            if(args.length != 2) {
                player.sendMessage(Messages.syntax("/ns smell <player>"));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if(!usedKurama) {
                player.sendMessage(CC.prefix("§cVous  n'êtes pas en train d'utiliser l'item Kurama"));
                return;
            }

            boolean var1 = false;

            NarutoRole targetRole = NarutoUHC.getNaruto().getRoleManager().getRole(target);
            if(targetRole == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'a pas de rôle."));
                return;
            }

            //TODO CHECK CAMP
            if(targetRole.getCamp() != Camp.SHINOBI) {
                var1 = true;
            }

            if(targetRole.getRoleName().equalsIgnoreCase("SAI")) {
                var1 = Role.findPlayer(NarutoRoles.SASUKE) != null && Role.findPlayer(NarutoRoles.SASUKE).getLocation().distance(target.getLocation()) <= 30;
            } else if(targetRole.getRoleName().equalsIgnoreCase("ITACHI") || targetRole.getRoleName().equalsIgnoreCase("KARIN") || targetRole.getRoleName().equalsIgnoreCase("OBITO")) {
                var1 = false;
            }

            usedKurama = false;

            if(var1) {
                player.sendMessage(CC.prefix("§c" + target.getName() + " §fpossède d'intentions meurtrières."));
            } else {
                player.sendMessage(CC.prefix("§a" + target.getName() + " §fne possède pas d'intentions meurtrières."));
            }
        }

        if(args[0].equalsIgnoreCase("balise")) {
            if(balises.size() >= 5) {
                player.sendMessage(CC.prefix("§cVous avez déjà posé 5 balises."));
            } else {
                this.balises.add(player.getLocation());
                player.sendMessage(CC.prefix("§fVous venez de poser une §aBalise§f."));
            }

        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }
}
