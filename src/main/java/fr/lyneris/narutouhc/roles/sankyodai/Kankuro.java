package fr.lyneris.narutouhc.roles.sankyodai;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.events.NarutoListener;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Kankuro extends NarutoRole {

    public boolean sasoriDied = false;
    public boolean usedKarasu = false;
    public boolean usedKuroari = false;
    public boolean usedSanshouo = false;
    public boolean sasori = false;
    public List<UUID> marionnettes = new ArrayList<>();

    @Override
    public String getRoleName() {
        return "Kankurô";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §eKankurô\n" +
                "§7▎ Objectif: §rSon but est de gagner avec §eSankyôdai\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "• Il dispose d’un item nommé \"§rMarionnettes§7\" lorsqu’il l’utilise, il obtient la liste des joueurs qu’il a tué. Lorsqu’il clique sur l’un d’entre eux, un autre menu s’offre à lui avec 3 options listées ci-dessous, qu’il pourra choisir qu’une seule fois pour chacun dans la partie, une fois une option choisit, le joueur ciblé sera ressuscité, ce joueur ne pourra plus utiliser un seul de ces pouvoirs, cependant il disposera des pouvoirs que lui à donner §eKankurô§7 avec l’aide de l’option choisit au départ, voici les pouvoirs que procure les options:\n" +
                "§e §f\n" +
                "§7Karasu: Le joueur choisit disposera d’un item nommé \"§rShuryûdan§7\", celui-ci lui permet de créer une zone de 10 blocs qui empoisonnera pendant 10 secondes tous les joueurs présents dans la zone, cette zone est limité avec des particules, ce pouvoir dure 1 minute et possède un délai de 10 minutes. Ce joueur disposera aussi de la particularité, avec 20% de chance, d’infliger poison pendant 5 secondes à lorsqu’il inflige un coup d’épée ou une flèche à l’arc.\n" +
                "§e §f\n" +
                "§7Kuroari: Le joueur choisit disposera de la particularité, avec 20% de chance, d’infligé lenteur et faiblesse pendant 5 secondes lorsqu’il inflige un coup d’épée ou une flèche à l’arc, il disposera aussi de l’effet §bVitesse 1§7.\n" +
                "§e §f\n" +
                "§7Sanshôuo: Le joueur choisit disposera de l’effet §9Résistance 1§7.\n" +
                "§e §f\n" +
                "§7Si §cSasori§7 vient à mourir, il obtiendra une seconde utilisation pour §rKuroari§7 et de la marionnette §rSasori§7, donc il obtiendra une nouvelle option dans son item §rMarionnettes§7, voici donc ce que procure la marionnette:\n" +
                "§e §f\n" +
                "§7Sasori: Le joueur choisit disposera de §c5 cœurs§7 supplémentaires.\n" +
                "§e §f\n" +
                "§7Si §eKankurô§7 vient à mourir alors les marionnettes mourront 3 minutes après sa mort, s’il est ressuscité dans un autre camp ou rejoint un autre camp d’une quelconque manière, les marionnettes rejoindront son camp sans avoir à mourir, ce pouvoir ne possède aucun délai.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’effet §bVitesse 1§7 permanent.\n" +
                "§e §f\n" +
                "§7• Il connaît les identités de §eGaara§7 et §eTemari§7, si §eGaara§7 vient à rejoindre un autre camp, Il en sera averti et le rejoindra automatiquement.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §9Suiton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.KANKURO;
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if(Role.isRole(player, NarutoRoles.SASORI)) {
            Player kankuro = getPlayer();
            kankuro.sendMessage(prefix("&cSasori &fest mort. Vous avez donc débloqué une nouvelle option pour votre &aMarrionnettes&f."));
            kankuro.sendMessage(prefix("&fVous avez également reçu une deuxième utilisation pour votre &aKuroari&f."));
            usedKuroari = false;
            sasoriDied = true;
        }
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, NarutoRoles.GAARA);
        Role.knowsRole(player, NarutoRoles.TEMARI);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marionnettes")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event, "Marionnettes")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            Inventory inv = Bukkit.createInventory(null, 9, "Marionnettes");
            int i = 0;
            for (String s : manager.getDeath().keySet()) {
                if (manager.getDeath().get(s).equals(player.getUniqueId())) {
                    if (Bukkit.getPlayer(s) != null) {
                        inv.setItem(i, new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setName("§6" + s).toItemStack());
                        i++;
                    }
                }
            }
            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equalsIgnoreCase("Marionnettes")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            Inventory inv = Bukkit.createInventory(null, 9, "Marionnettes " + target.getName());
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());

            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Karasu " + (usedKarasu ? "§cUtilisé" : "")).setLore(
                    "§7Le joueur choisit disposera d’un item",
                    "§7nommé 'Shuryûdan', celui-ci lui permet",
                    "§7de créer une zone de 10 blocs qui",
                    "§7empoisonnera pendant 10 secondes tous",
                    "§7les joueurs présents dans la zone, cette",
                    "§7zone est limité avec des particules, ce",
                    "§7pouvoir dure 1 minute et possède un délai",
                    "§7de 10 minutes. Ce joueur disposera aussi",
                    "§7de la particularité, avec 20 % de chance,",
                    "§7d’infliger poison pendant 5 secondes à",
                    "§7chaque lorsqu’il inflige un coup d’épée",
                    "§7ou une flèche à l’arc."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Kuroari " + (usedKuroari ? "§cUtilisé" : "")).setLore(
                    "§7Le joueur choisit disposera de la",
                    "§7particularité avec 20 % de chance",
                    "§7d’infligé lenteur et faiblesse pendant",
                    "§75 secondes, il disposera aussi de",
                    "§7l’effet Vitesse 1."
            ).toItemStack());
            inv.setItem(3, new ItemBuilder(Material.NETHER_STAR).setName("§6Sanshôuo " + (usedSanshouo ? "§cUtilisé" : "")).setLore(
                    "§7Le joueur choisit disposera de l’effet",
                    "§7Résistance 1."
            ).toItemStack());
            if(sasoriDied) {
                inv.setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§6Sasori " + (sasori ? "§cUtilisé" : "")).setLore(
                        "§7Le joueur choisit disposera de 5 cœurs.",
                        "§7supplémentaires."
                ).toItemStack());
            }

            player.openInventory(inv);
        }

        if (event.getInventory().getName().startsWith("Marionnettes ")) {
            event.setCancelled(true);
            if(event.getSlot() == 1) {
                if(usedKarasu) {
                    player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir."));
                    return;
                }
                Player target = Bukkit.getPlayer(event.getInventory().getName().replace("Marionnettes ", ""));
                if (target == null) {
                    player.sendMessage(prefix("&cCe joueur n'est pas connecté&f."));
                    return;
                }
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                target.sendMessage(prefix("&aKankurô &fa décidé de vous ressuscité&f. Vous obtenez également l'item &aShuryûdan&f."));
                giveStuff(target);
                UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
                manager.getDeath().remove(target.getName());
                this.marionnettes.add(target.getUniqueId());
            }

            if(event.getSlot() == 2) {
                if(usedKuroari) {
                    player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir."));
                    return;
                }
                Player target = Bukkit.getPlayer(event.getInventory().getName().replace("Marionnettes ", ""));
                if (target == null) {
                    player.sendMessage(prefix("&cCe joueur n'est pas connecté&f."));
                    return;
                }
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                target.sendMessage(prefix("&aKankurô &fa décidé de vous ressuscité&f. &fVous disposez de §bSpeed 1 &fainsi qu'une probabilité de &a20% &fd'infliger faiblesse et lenteur."));
                giveStuff(target);
                NarutoListener.kuroari.add(target.getUniqueId());
                target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
                manager.getDeath().remove(target.getName());
                this.marionnettes.add(target.getUniqueId());
            }

            if(event.getSlot() == 3) {
                if(usedSanshouo) {
                    player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir."));
                    return;
                }
                Player target = Bukkit.getPlayer(event.getInventory().getName().replace("Marionnettes ", ""));
                if (target == null) {
                    player.sendMessage(prefix("&cCe joueur n'est pas connecté&f."));
                    return;
                }
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                target.sendMessage(prefix("&aKankurô &fa décidé de vous ressuscité&f. &fVous disposez de §7Résistance 1&f."));
                giveStuff(target);
                target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
                manager.getDeath().remove(target.getName());
                this.marionnettes.add(target.getUniqueId());
            }

            if(event.getSlot() == 4) {
                if(sasori) {
                    player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir."));
                    return;
                }
                Player target = Bukkit.getPlayer(event.getInventory().getName().replace("Marionnettes ", ""));
                if (target == null) {
                    player.sendMessage(prefix("&cCe joueur n'est pas connecté&f."));
                    return;
                }
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                target.sendMessage(prefix("&aKankurô &fa décidé de vous ressuscité&f. &fVous disposez de §c5 coeurs&f en plus."));
                giveStuff(target);
                target.setMaxHealth(target.getMaxHealth() + 10);
                UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
                manager.getDeath().remove(target.getName());
                this.marionnettes.add(target.getUniqueId());
            }
        }

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
        for (UUID uuid : marionnettes) {
            if (Bukkit.getPlayer(uuid) != null) {
                Player target = Bukkit.getPlayer(uuid);
                target.sendMessage(prefix("&cKankuro &fest mort. De ce fait vous allez mourrir dans 3 minutes."));
            }
        }
        Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> marionnettes.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).forEach(uuid -> Bukkit.getPlayer(uuid).setHealth(0)), 3*20*60);
    }



    @Override
    public void onAllPlayerCampChange(Camp camp, Player player) {
        Player kankuro = getPlayer();
        if (Role.isRole(player, NarutoRoles.GAARA)) {
            kankuro.sendMessage(prefix("&aGaara &fa changé de camp &8(" + camp.getFormat() + "&8)&f. Vous avez donc rejoint ce camp."));
            roleManager.setCamp(kankuro, camp);
        }

        if(Role.isRole(player, NarutoRoles.KANKURO)) {
            for (UUID uuid : marionnettes) {
                if (Bukkit.getPlayer(uuid) != null) {
                    Player target = Bukkit.getPlayer(uuid);
                    roleManager.setCamp(target.getUniqueId(), camp);
                    target.sendMessage(prefix("&cKankuro &fa changé de camp. De ce fait vous rejoignez le camp " + camp.getFormat()));
                }
            }
        }
    }

    public void giveStuff(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.LOG, 64));
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().setHelmet(new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());

        if (roleManager.getRole(player) != null) {
            roleManager.getRole(player).onDistribute(player);
        }

        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Shuryûdan")).toItemStack());
    }

    @Override
    public Camp getCamp() {
        return Camp.SANKYODAI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }
}
