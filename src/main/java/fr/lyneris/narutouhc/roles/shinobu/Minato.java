package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
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
    public int kuramaCooldown = 0;
    public ArrayList<Location> balises = new ArrayList<>();
    public int baliseCooldown = 0;
    private boolean usedKurama = false;
    private int arrowCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.MINATO;
    }

    @Override
    public void resetCooldowns() {
        kuramaCooldown = 0;
        baliseCooldown = 0;
        arrowCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (kuramaCooldown > 0) {
            kuramaCooldown--;
        }

        if (baliseCooldown > 0) {
            baliseCooldown--;
        }

        if (arrowCooldown > 0) {
            arrowCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Minato";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §rMinato\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d'un arc spécial nommé \"§rShurikenJutsu§7\" celui-ci lui permet de se téléporter à l'emplacement où la flèche atterrit. Il est le seul à pouvoir l'utiliser.\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rShurikenJutsu§7\", le même nom que son arc, lorsqu’il utilise son item en étant accroupis (en sneak), un message apparaît dans le chat lui demandant s’il souhaite poser une balise (shuriken dans l’univers), pour accepter il lui suffit de cliquer sur le message. Une fois cela fait, il doit cliquer normalement sur son item et une liste de tous les joueurs autour de lui dans un rayon de 20 blocs s’affiche. S’il clique sur l’un des joueur, un autre menu s’affiche avec toutes ses balises numérotés de 1 à 5 puisqu’il dispose d’un maximum de 5 balises, lorsqu’il clique sur l’une d’entre elles, le joueur ciblé sera téléporté sur la balise sélectionnée. Ce pouvoir possède un délai de 15 minutes.\n" +
                "§e §f\n" +
                "§7• Il dispose d'un item nommé \"§rKurama§7\", lorsqu’il clique sur celui-ci, l'item permet de recevoir §cForce 1§7 et §bVitesse 2§7 pendant 5 minutes, après avoir cliqué sur cet item, il pourra effectuer la commande ci-dessous.  \n" +
                "§e §f\n" +
                "§r➜ /ns smell <Joueur>§7, cette commande lui permet de savoir si la personne choisie possède des intentions meurtrières. Tout camps autres que celui des §aShinobi§7 possèdent des intentions meurtrières. Cependant §cItachi§7, §6Karin§7 et §dTobi§7 ne possèdent aucunes intentions meurtrières et lorsque §aSaï§7 se situe dans un rayon de 30 blocs proche de §6Sasuke§7, il possède des intentions meurtrières.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l'effet §bVitesse 2§7 permanent.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il tue un joueur ne faisant pas partie de son camp, il reçoit l’effet §bVitesse 3§7 pendant 2 minutes.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’identité de §aNaruto§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §cKaton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
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
        if (Item.interactItem(event.getItem(), "Kurama")) {
            if (kuramaCooldown > 0) {
                player.sendMessage(Messages.cooldown(kuramaCooldown));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            usedKurama = true;
            kuramaCooldown = 20 * 60;
            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aKurama§f."));

            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
            Tasks.runLater(() -> usedKurama = false, 5 * 20 * 60);
        }

        if (Item.interactItem(event.getItem(), "Shuriken Jutsu")) {

            if (player.isSneaking()) {
                if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                TextComponent text = new TextComponent("§7▎ §aCliquez-ici si vous souhaitez poser une balise ou utilisez la commande /ns balise");
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ns balise"));
                player.spigot().sendMessage(text);
            } else {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, 20)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 7 && nearbyPlayer <= 16) {
                    i = 18;
                } else if (nearbyPlayer > 16) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Shuriken Jutsu");
                inv.setItem(1, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + player.getName()).setSkullOwner(player.getName()).toItemStack());
                int j = 2;
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        if (event.getInventory().getName().equals("Shuriken Jutsu")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
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

        if (event.getInventory().getName().startsWith("Balises")) {
            event.setCancelled(true);
            Player target = Bukkit.getPlayer(event.getInventory().getName().replace("Balises » ", ""));
            if (!event.getCurrentItem().hasItemMeta()) return;


            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            int i = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6Balise ", ""));
            Location balise = balises.get(i - 1);

            if (baliseCooldown > 0) {
                player.sendMessage(Messages.cooldown(baliseCooldown));
                return;
            }

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            baliseCooldown = 15 * 60;
            target.teleport(balise);
            player.sendMessage(CC.prefix("§fVous avez téléporté §a" + target.getName() + " §fsur une de vos balises"));
            target.sendMessage(CC.prefix("§cMinato §fvous a téléporté sur une de ses balises."));
        }

    }

    @Override
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event, Player shooter) {
        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();

        if (!Item.specialItem(shooter.getItemInHand(), "Shuriken Jutsu")) return;

        if (arrowCooldown > 0) {
            shooter.sendMessage(Messages.cooldown(arrowCooldown));
            return;
        }

        arrowCooldown = 15;

        this.arrows.add(arrow);
    }

    @Override
    public void onProjectileHitEvent(ProjectileHitEvent event, Player shooter) {
        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();

        if (arrows.contains(arrow)) {
            shooter.teleport(arrow.getLocation());
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        NarutoRole targetRole = roleManager.getRole(event.getEntity());
        NarutoRole role = roleManager.getRole(killer);

        if (targetRole == null || role == null) return;

        if (roleManager.getCamp(event.getEntity().getUniqueId()) != roleManager.getCamp(killer)) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20 * 60, 2, false, false));
            killer.removePotionEffect(PotionEffectType.SPEED);

            killer.sendMessage(CC.prefix("§fVous avez tué un joueur du camp opposé, vous obtenez donc un effet de §bSpeed 3 §fpendant 2 minutes."));
            Tasks.runLater(() -> {
                killer.removePotionEffect(PotionEffectType.SPEED);
                killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            }, 2 * 20 * 60);
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("smell")) {
            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns smell <player>"));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (!usedKurama) {
                player.sendMessage(CC.prefix("§cVous  n'êtes pas en train d'utiliser l'item Kurama"));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            boolean var1 = false;

            NarutoRole targetRole = NarutoUHC.getNaruto().getRoleManager().getRole(target);
            if (targetRole == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'a pas de rôle."));
                return;
            }

            if (narutoUHC.getRoleManager().getCamp(target) != Camp.SHINOBI) {
                var1 = true;
            }

            if (targetRole.getRole().equals(NarutoRoles.SAI)) {
                var1 = Role.findPlayer(NarutoRoles.SASUKE) != null && Role.findPlayer(NarutoRoles.SASUKE).getLocation().distance(target.getLocation()) <= 30;
            } else if (targetRole.getRole().equals(NarutoRoles.ITACHI) || targetRole.getRole().equals(NarutoRoles.KARIN) || targetRole.getRole().equals(NarutoRoles.OBITO)) {
                var1 = false;
            }

            usedKurama = false;

            if (var1) {
                player.sendMessage(CC.prefix("§c" + target.getName() + " §fpossède d'intentions meurtrières."));
            } else {
                player.sendMessage(CC.prefix("§a" + target.getName() + " §fne possède pas d'intentions meurtrières."));
            }
        }

        if (args[0].equalsIgnoreCase("balise")) {
            if (balises.size() >= 5) {
                player.sendMessage(CC.prefix("§cVous avez déjà posé 5 balises."));
            } else {
                if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
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
