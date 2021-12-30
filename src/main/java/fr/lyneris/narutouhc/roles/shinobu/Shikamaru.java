package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.particle.WorldUtils;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Shikamaru extends NarutoRole {

    private final Map<UUID, PacketDisplay> inEye = new HashMap<>();
    public int intonUses = 0;
    public boolean usedEtreinte = false;
    public int eyeUses = 0;
    public boolean usedPower = false;
    public boolean hasNerf = false;
    public boolean usedSearch = false;

    public NarutoRoles getRole() {
        return NarutoRoles.SHIKAMARU;
    }

    @Override
    public String getRoleName() {
        return "Shikamaru";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aShikamaru\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rInton§7“, lorsqu’il clique sur celui-ci, il est dirigé dans un menu, dans lequel plusieurs choix s’offrent à lui :\n" +
                "§e §f\n" +
                "§7- Manipulation : (obsidienne) Lorsqu’il l’utilise, tous les joueurs se trouvant à 15 blocs autour de lui sont affichés dans ce menu, il dispose aussi d’une option qui lui permet de le faire sur tous les joueurs qui se situent dans ce même rayon, lorsqu’il clique sur la tête d’un des joueurs, il est immobilisé ainsi que le joueur ciblé et cela pendant 10 secondes, pendant ce laps de temps ils recevront tous les deux l’effet §9Résistance 2§7, lorsque lui et la personne ciblé sont immobilisés, ils pourront recevoir des coups venant d’autres joueurs, ce pouvoir ne peut être utilisé que 3 fois dans la partie et ne possède aucun délai.\n" +
                "§e §f\n" +
                "§7- Étreinte Mortelle : (épée en fer) Lorsqu’il l’utilise, tous les joueurs se trouvant à 15 blocs autour de lui sont affichés dans ce menu, lorsqu’il clique sur la tête d’un des joueur, lui et le joueur ciblé reçoivent les mêmes effets que la Manipulation cependant le joueur ciblé reçoit l’effet §aPoison 2§7 pendant 10 secondes, ce pouvoir ne peut être utilisé qu’une fois dans la partie.\n" +
                "§e §f\n" +
                "§7- Œil : (œil de l’end) Lorsqu’il l’utilise, tous les joueurs se trouvant à 15 blocs autour de lui, sont affichés dans ce menu, lorsqu’il clique sur la tête d’un des joueur, à partir de ce moment il pourra voir la vie du joueur ciblé au dessus de sa tête jusqu’à la fin de la partie, ce pouvoir peut être utilisé 5 fois dans la partie.\n" +
                "§e §f\n" +
                "§7- Recherche : (cœur d’araignée) Lorsqu’il l’utilise, tous les joueurs se trouvant à 15 blocs autour de lui, sont affichés dans ce menu, lorsqu’il clique sur la tête d’un des joueur, il pourra voir si le joueur ciblé a commis des meurtres et (si c’est le cas) l’identité du ou des joueurs morts, ce pouvoir ne peut être utilisé qu’une fois dans la partie.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités\n" +
                "      \n" +
                "§7• Lorsqu’il fait nuit, le rayon pour utiliser son pouvoir s’étend à un rayon de 30 blocs.\n" +
                "§e §f\n" +
                "§7• S’il utilise au moins deux techniques (hormis Œil) dans un intervalle de moins de 5 minutes, il obtiendra les effets §lFaiblesse 1§7 et §8Lenteur 1§7 pendant 1 minute à chaque fois qu’il utilisera une technique et ce, pendant toute la partie.\n" +
                "§e §f\n" +
                "§7• A 70 minutes de jeu, il obtient la particularité de voir la vie de tous les joueurs dans la partie.\n" +
                "      \n" +
                "§7• Il connaît l’identité de §aChôji§7.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.CHOJI);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Inton")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Inton")) {

            Inventory inv = Bukkit.createInventory(null, 9, "Inton");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.OBSIDIAN).setName("§6Manipulation").setLore(
                    "§7Lorsqu’il l’utilise, tous les joueurs se",
                    "§7trouvant à 15 blocs autour de lui sont",
                    "§7affichés dans ce menu, il dispose aussi d’une",
                    "§7option qui lui permet de le faire sur tous les",
                    "§7joueurs qui se situent dans ce même rayon,",
                    "§7lorsqu’il clique sur la tête d’un des joueurs,",
                    "§7il est immobilisé ainsi que le joueur ciblé et",
                    "§7cela pendant 10 secondes, pendant ce laps de",
                    "§7temps ils recevront tous les deux l’effet",
                    "§7Résistance 2, lorsque lui et la personne ciblé",
                    "§7sont immobilisés, ils pourront recevoir des",
                    "§7coups venant d’autres joueurs, ce pouvoir ne",
                    "§7peut être utilisé que 3 fois dans la partie",
                    "§7et ne possède aucun délai.",
                    " ",
                    "§8» §7Cliquez-ici pour y accéder"
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.IRON_SWORD).setName("§6Étreinte Mortelle").setLore(
                    "§7Lorsqu’il l’utilise, tous les joueurs se",
                    "§7trouvant à 15 blocs autour de lui sont",
                    "§7affichés dans ce menu, lorsqu’il clique sur",
                    "§7la tête d’un des joueurs, lui et le joueur",
                    "§7ciblé reçoivent les mêmes effets que la",
                    "§7Manipulation cependant le joueur ciblé reçoit",
                    "§7l’effet Poison 2 pendant 10 secondes, ce",
                    "§7pouvoir ne peut être utilisé qu’une fois",
                    "§7dans la partie.",
                    " ",
                    "§8» §7Cliquez-ici pour y accéder"
            ).toItemStack());
            inv.setItem(3, new ItemBuilder(Material.EYE_OF_ENDER).setName("§6Oeil").setLore(
                    "§7Lorsqu’il l’utilise, tous les joueurs se",
                    "§7trouvant à 15 blocs autour de lui, sont",
                    "§7affichés dans ce menu, lorsqu’il clique sur",
                    "§7la tête d’un des joueur, à partir de ce",
                    "§7moment il pourra voir la vie du joueur ciblé",
                    "§7au dessus de sa tête jusqu’à la fin de la",
                    "§7partie, ce pouvoir peut être utilisé 5 fois",
                    "§7dans la partie.",
                    " ",
                    "§8» §7Cliquez-ici pour y accéder"
            ).toItemStack());
            inv.setItem(4, new ItemBuilder(Material.SPIDER_EYE).setName("§6Recherche").setLore(
                    "§7Lorsqu’il l’utilise, tous les joueurs se",
                    "§7trouvant à 15 blocs autour de lui, sont",
                    "§7affichés dans ce menu, lorsqu’il clique",
                    "§7sur la tête d’un des joueurs, il pourra",
                    "§7voir si le joueur ciblé a commis des meurtres",
                    "§7et (si c’est le cas) l’identité du ou des",
                    "§7joueurs morts, ce pouvoir ne peut être",
                    "§7utilisé qu’une fois dans la partie.",
                    " ",
                    "§8» §7Cliquez-ici pour y accéder"
            ).toItemStack());

            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        int radius = (manager.isDay() ? 15 : 30);

        if (event.getInventory().getName().equals("Inton")) {
            event.setCancelled(true);
            if (intonUses >= 3) {
                player.closeInventory();
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 3 fois."));
                return;
            }
            if (event.getSlot() == 1) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, radius)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if (nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Manipulation");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.BOOK).setName("§6Tous les joueurs").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, radius)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }
            if (event.getSlot() == 2) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, radius)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if (nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Étreinte Mortelle");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, radius)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

            if (event.getSlot() == 3) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, radius)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if (nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Oeil");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, radius)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

            if (event.getSlot() == 4) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, radius)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if (nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Recherche");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, radius)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

        }

        if (event.getInventory().getName().equals("Recherche")) {
            event.setCancelled(true);
            if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                if (!event.getCurrentItem().hasItemMeta()) return;
                if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
                player.closeInventory();

                if (usedSearch) {
                    player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                }

                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

                Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

                if (!manager.getDeath().containsValue(target.getUniqueId())) {
                    player.sendMessage(CC.prefix("§cCe joueur n'a pas effectué de meurtre."));
                    return;
                }

                manager.getDeath().keySet().forEach(s -> {
                    if (manager.getDeath().get(s).equals(target.getUniqueId())) {
                        player.sendMessage(CC.prefix("§c" + target.getName() + " §fa tué §a" + s));
                    }
                });



                usedSearch = true;

            }
        }

        if (event.getInventory().getName().equals("Manipulation")) {
            event.setCancelled(true);
            if (event.getSlot() == 0) {
                player.closeInventory();
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                manager.setStuned(player, true, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 1, false, false));
                Loc.getNearbyPlayers(player, radius).forEach(target -> {
                    manager.setStuned(target, true, 10);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 1, false, false));
                    player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir de §aManipulation sur §a" + target.getName()));
                    target.sendMessage(CC.prefix("§aShikamaru §fa utilisé son pouvoir §aManipulation §fsur vous. De ce fait, vous êtes immobilisé pendant §c10 secondes§f."));
                });

                if (usedPower && !hasNerf) {
                    hasNerf = true;
                    player.sendMessage(CC.prefix("§cVous avez utilisé deux pouvoirs dans une intervalle de 5 minutes. De ce fait, à chaque fois que vous utiliserais un pouvoir (hormis votre Oeil) vous recevrez Lenteur et Faiblesse pendant 1 minute."));
                }

                if (hasNerf) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * 20, 0, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 * 20, 0, false, false));
                }

                usedPower = true;
                Tasks.runLater(() -> usedPower = false, 5 * 20 * 60);

                intonUses++;
            } else if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                if (!event.getCurrentItem().hasItemMeta()) return;
                if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
                player.closeInventory();
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                intonUses++;

                Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

                manager.setStuned(target, true, 10);
                target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 1, false, false));

                player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir de §aManipulation sur §a" + target.getName()));
                target.sendMessage(CC.prefix("§aShikamaru §fa utilisé son pouvoir §aManipulation §fsur vous. De ce fait, vous êtes immobilisé pendant §c10 secondes§f."));

                manager.setStuned(player, true, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 1, false, false));

                if (usedPower && !hasNerf) {
                    hasNerf = true;
                    player.sendMessage(CC.prefix("§cVous avez utilisé deux pouvoirs dans une intervalle de 5 minutes. De ce fait, à chaque fois que vous utiliserais un pouvoir (hormis votre Oeil) vous recevrez Lenteur et Faiblesse pendant 1 minute."));
                }

                if (hasNerf) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * 20, 0, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 * 20, 0, false, false));
                }

                usedPower = true;
                Tasks.runLater(() -> usedPower = false, 5 * 20 * 60);

            }

        }

        if (event.getInventory().getName().equals("Étreinte Mortelle")) {
            event.setCancelled(true);
            if (!(event.getCurrentItem().getType() == Material.SKULL_ITEM)) return;
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            player.closeInventory();

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            if (usedEtreinte) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 1, false, false));

            manager.setStuned(target, true, 10);
            manager.setStuned(player, true, 10);

            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir de §aManipulation sur §a" + target.getName()));
            target.sendMessage(CC.prefix("§aShikamaru §fa utilisé son pouvoir §aManipulation §fsur vous. De ce fait, vous êtes immobilisé pendant §c10 secondes§f. Vous recevez également §2Poison 2 §fpendant 10 secondes."));

            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 1, false, false));

            usedEtreinte = true;

            if (usedPower && !hasNerf) {
                hasNerf = true;
                player.sendMessage(CC.prefix("§cVous avez utilisé deux pouvoirs dans une intervalle de 5 minutes. De ce fait, à chaque fois que vous utiliserais un pouvoir (hormis votre Oeil) vous recevrez Lenteur et Faiblesse pendant 1 minute."));
            }

            if (hasNerf) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * 20, 0, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 * 20, 0, false, false));
            }

            usedPower = true;
            Tasks.runLater(() -> usedPower = false, 5 * 20 * 60);

        }

        if (event.getInventory().getName().equals("Oeil")) {
            event.setCancelled(true);
            if (!(event.getCurrentItem().getType() == Material.SKULL_ITEM)) return;
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            player.closeInventory();

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            if (eyeUses >= 5) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 5 fois."));
                return;
            }

            eyeUses++;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (inEye.containsKey(target.getUniqueId())) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir sur ce joueur."));
                return;
            }

            player.sendMessage(CC.prefix("§fVous voyez désormais la vie de §a" + target.getName()));

            setupOeil(player, target);
        }

    }

    @Override
    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {
        if (inEye.containsKey(player.getUniqueId())) {
            PacketDisplay packetDisplay = inEye.get(player.getUniqueId());

            Player shikamaru = Role.findPlayer(NarutoRoles.SHIKAMARU);
            if (shikamaru != null) {
                if (!packetDisplay.isCustomNameVisible()) {
                    packetDisplay.setCustomNameVisible(true, shikamaru);
                }
                if (!packetDisplay.getText().equals(WorldUtils.getBeautyHealth(player) + " ❤")) {
                    packetDisplay.rename(WorldUtils.getBeautyHealth(player) + " ❤", shikamaru);
                }
                packetDisplay.teleport(player.getLocation(), shikamaru);
            }
        }
    }

    public void setupOeil(Player shikamaru, Player target) {
        if (target != null) {
            PacketDisplay display = new PacketDisplay(target.getLocation(), WorldUtils.getBeautyHealth(target) + " ❤");

            display.display(shikamaru);

            inEye.put(target.getUniqueId(), display);
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }
}
