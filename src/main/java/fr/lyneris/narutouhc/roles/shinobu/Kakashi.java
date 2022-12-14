package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Kakashi extends NarutoRole {

    public UUID kakashiTarget = null;
    public int avancement = 0;
    public HashMap<String, List<PotionEffectType>> copies = new HashMap<>();
    public int pakkunCooldown = 0;
    public Wolf wolf = null;
    public HashMap<UUID, Location> oldLocation = new HashMap<>();
    private int arimasuCooldown = 0;
    private int sonohokaCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.KAKASHI;
    }

    @Override
    public void resetCooldowns() {
        pakkunCooldown = 0;
        arimasuCooldown = 0;
        sonohokaCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (pakkunCooldown > 0) {
            pakkunCooldown--;
        }

        if (arimasuCooldown > 0) {
            arimasuCooldown--;
        }

        if (sonohokaCooldown > 0) {
            sonohokaCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Kakashi";
    }

    @Override
    public String getDescription() {
        return "??7??m--------------------------------------\n" +
                "??e ??f\n" +
                "??7??? R??le: ??rKakashi\n" +
                "??7??? Objectif: ??rSon but est de gagner avec les ??aShinobi\n" +
                "??e ??f\n" +
                "??7??l??? Items :\n" +
                "??e ??f\n" +
                "??7??? Il dispose d???un item nomm?? ?????rSharingan??7??? il permet lorsque ??aKakashi??7 clique sur celui-ci, d???ouvrir un menu lui indiquant deux choix, le premier se nomme ?????rcopie??7???, celui-ci lui permet de voir tous les joueurs dans un rayon de 20 blocs, lorsqu???il clique sur l???un des joueurs, une progression commence, ce pouvoir permet de copier le pouvoir ou les pouvoirs du joueur cibl?? (les effets permanents), ensuite le deuxi??me menu se nomme ?????rTechniques??7???, ce menu montre une liste de tout les pouvoirs qu???il a copi??, cependant il faut r??ussir la progression de son pouvoir de copie et celle-ci poss??de certaines conditions, en effet il faut ??tre proche du joueur cibl?? pour que celle-ci avance, la progression se fait de la mani??re suivante : \n" +
                "??e ??f\n" +
                "??720 blocs = 2pts/s\n" +
                "??710 blocs = 5pts/s\n" +
                "??75 blocs = 10pts/s \n" +
                "??e ??f\n" +
                "??7Le tout sur un total de 2500 points, cependant il ne peut pas choisir un autre joueur, s???il en a d??j?? choisit un auparavant, il doit attendre que la progression se termine ou que le joueur cibl?? finisse par mourir, pour choisir un autre joueur avec son pouvoir, il ne poss??de aucun d??lai et aucune limite d???utilisation.\n" +
                "??e ??f\n" +
                "??7??? Il dispose d???un autre item nomm?? ?????rKamui??7???, celui-ci lui affiche un menu dans lequel il a deux choix, le premier est nomm?? ?????rArimasu??7???, celui-ci lui permet de le t??l??porter dans le monde li?? ?? Kamui et ceci pendant 10 minutes, le deuxi??me nomm?? ?????rSonohoka??7???, lui permet de t??l??porter un joueur dans un rayon de 20 blocs autour de lui, dans le monde li?? ?? Kamui et cela pendant 5 minutes, ???Arimasu??? poss??de un d??lai de 15 minutes, ???Sonohoka??? poss??de un d??lai de 30 minutes. Dans le monde de Kamui on ne peut pas poser de blocs, sauf le seau de lave et d'eau qui s'enl??ve automatiquement au bout d'une minute, puis on ne peut pas prendre de d??g??ts de chutes, il peut utiliser la commande /ns yameru pour t??l??porter lui et le joueur (s???il en a t??l??port?? un) dans le monde normal, suite ?? sa commande ou si le temps est ??coul??, ils seront t??l??port??s ?? l???endroit exact o?? ils se situaient avant que ??aKakashi??7 utilise son pouvoir. \n" +
                "??e ??f\n" +
                "??7??? Il poss??de un dernier item nomm?? ?????rPakkun??7???, celui-ci fera appara??tre un chien inoffensif (c???est ?? dire qu???il n???attaquera personne), apr??s cela un menu s'affiche avec la t??te de tous les joueurs, lorsqu'il le en s??lectionne un, le chien Pakkun traquera le joueur cibl??, il poss??de un d??lai de 30 minutes. \n" +
                "??e ??f\n" +
                "??7??? Il dispose de la nature de Chakra : ??7Raiton\n" +
                "??e ??f\n" +
                "??7??m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Sharingan")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kamui")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Pakkun")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Sharingan")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Selection Kakashi");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("??6Copie").setLore(
                    "??7Celui-ci lui permet de voir tous les",
                    "??7joueurs dans un rayon de 20 blocs, lorsqu???il",
                    "??7clique sur l???un des joueurs, une progression",
                    "??7commence, ce pouvoir permet de copier le",
                    "??7pouvoir ou les pouvoirs du joueur cibl??",
                    "??7Celle-ci ??7poss??de certaines conditions, en",
                    "??7effet il faut ??tre proche du joueur cibl??",
                    "??7pour que celle-ci avance, la progression",
                    "??7se fait de la mani??re suivante:",
                    "??f??l?? 20 blocs = 2pts/s",
                    "??f??l?? 10 blocs = 5pts/s",
                    "??f??l?? 5 blocs = 10pts/s"
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("??6Techniques").setLore(
                    "??7Ce menu montre une liste de tout les pouvoirs",
                    "??7qu???il a copi??, cependant il faut r??ussir la",
                    "??7progression de son pouvoir."
            ).toItemStack());

            player.openInventory(inv);
        }

        if (Item.interactItem(event.getItem(), "Kamui")) {

            Inventory inv = Bukkit.createInventory(null, 9, "Kamui");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());

            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("??6Arimasu").toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("??6Sonohoka").toItemStack());

            player.openInventory(inv);

        }

        if (Item.interactItem(event.getItem(), "Pakkun")) {

            if (pakkunCooldown > 0) {
                player.sendMessage(Messages.cooldown(pakkunCooldown));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
            wolf.setAngry(false);
            wolf.setSitting(false);
            wolf.setTamed(true);
            wolf.setOwner(player);


            int i = 9;
            int nearbyPlayer = 0;
            for (Player ignored : Loc.getNearbyPlayers(player, 20)) {
                nearbyPlayer++;
            }
            if (nearbyPlayer > 8 && nearbyPlayer <= 17) {
                i = 18;
            } else if (nearbyPlayer > 17) {
                i = 27;
            }
            Inventory inv = Bukkit.createInventory(null, i, "Pakkun");
            int j = 1;
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("??6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                j++;
            }
            player.openInventory(inv);

            pakkunCooldown = 30 * 60;
        }


    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if (player.getUniqueId().equals(kakashiTarget)) {
            kakashiTarget = null;
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        if (event.getInventory().getName().equals("Selection Kakashi")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, 20)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if (nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Copie");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("??6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

            if (event.getSlot() == 2) {
                Inventory inv;
                if (copies.size() < 8) {
                    inv = Bukkit.createInventory(null, 9, "Techniques");
                } else {
                    inv = Bukkit.createInventory(null, 18, "Techniques");
                }

                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (String s : copies.keySet()) {
                    List<String> lore = new ArrayList<>();
                    if (copies.get(s).size() == 0) lore.add("??cAucun effet");
                    copies.get(s).forEach(str -> lore.add("??f??l?? " + str.getName().toLowerCase().replace("_", " ")));
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setLore(lore).setName("??6" + s).setSkullOwner(s).toItemStack());
                    j++;
                }

                player.openInventory(inv);
            }
        }

        if (event.getInventory().getName().equals("Techniques")) {
            event.setCancelled(true);
        }

        if (event.getInventory().getName().equals("Copie")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("??6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("??cCe joueur n'est pas connect??"));
                return;
            }

            if (kakashiTarget != null) {
                player.sendMessage(CC.prefix("??cVous utilisez d??j?? ce pouvoir sur quelqu'un"));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            kakashiTarget = target.getUniqueId();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (kakashiTarget == null) return;

                    Player kakashi = Role.findPlayer(NarutoRoles.KAKASHI);
                    Player newTarget = Bukkit.getPlayer(kakashiTarget);
                    if (avancement >= 2500) {

                        if (newTarget != null && kakashi != null) {
                            player.sendMessage(CC.prefix("??aVous avez termin?? la progression sur " + newTarget.getName()));
                            List<PotionEffectType> map = new ArrayList<>();
                            for (PotionEffect activePotionEffect : newTarget.getActivePotionEffects()) {
                                if (activePotionEffect.getDuration() > 30 * 20 * 60) {
                                    kakashi.addPotionEffect(activePotionEffect);
                                    kakashi.sendMessage(CC.prefix("??fVous venez de recevoir l'effet ??a" + activePotionEffect.getType().getName()));
                                    map.add(activePotionEffect.getType());
                                }
                            }
                            copies.put(newTarget.getName(), map);
                            cancel();
                        }
                    } else {
                        if (newTarget != null && kakashi != null) {
                            int distance = (int) kakashi.getLocation().distance(newTarget.getLocation());
                            if (distance <= 5) {
                                avancement += 10;
                                Title.sendActionBar(kakashi, "??7??? ??fAvancement ??a" + newTarget.getName() + " ??f??l?? ??a" + avancement + "??8/??a2500");
                            } else if (distance <= 10) {
                                avancement += 5;
                                Title.sendActionBar(kakashi, "??7??? ??fAvancement ??a" + newTarget.getName() + " ??f??l?? ??a" + avancement + "??8/??a2500");
                            } else if (distance <= 20) {
                                avancement += 2;
                                Title.sendActionBar(kakashi, "??7??? ??fAvancement ??a" + newTarget.getName() + " ??f??l?? ??a" + avancement + "??8/??a2500");
                            }

                        }
                    }

                }
            }.runTaskTimer(narutoUHC, 0, 20);

            player.closeInventory();

        }

        if (event.getInventory().getName().equals("Pakkun")) {

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("??6", ""));
            if (target == null) {
                player.sendMessage(CC.prefix("??cCe joueur n'est pas connect??"));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.sendMessage(CC.prefix("??cVotre chien traquera d??sormais " + target.getName()));

            wolf.setAngry(true);
            wolf.setTarget(target);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (wolf != null && Bukkit.getPlayer(target.getName()) != null) {
                        wolf.setAngry(true);
                        wolf.setTarget(Bukkit.getPlayer(target.getName()));
                    }
                    if (wolf == null) {
                        cancel();
                    }
                }
            }.runTaskTimer(narutoUHC, 0, 20);

        }

        if (event.getInventory().getName().equals("Kamui")) {

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

            if (event.getSlot() == 1) {
                final Location oldLocation;
                oldLocation = player.getLocation();

                if (arimasuCooldown > 0) {
                    player.sendMessage(Messages.cooldown(sonohokaCooldown));
                    return;
                }
                if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);
                player.teleport(manager.getKamuiSpawn());
                this.oldLocation.put(player.getUniqueId(), oldLocation);
                player.sendMessage(CC.prefix("??fVous serez ret??l??port?? ?? votre ancienne position dans ??a10 minutes??f."));

                Tasks.runLater(() -> {
                    player.teleport(oldLocation);
                    player.sendMessage(CC.prefix("??fVous avez ??t?? t??l??port?? ?? votre ancienne position."));
                }, 10 * 20 * 60);

                arimasuCooldown = 15 * 60;

            }

            if (event.getSlot() == 2) {

                int i = 9;
                int nearbyPlayer = 0;
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 7 && nearbyPlayer <= 16) {
                    i = 18;
                } else if (nearbyPlayer > 16) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Arimasu");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("??6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

        }

        if (event.getInventory().getName().equals("Arimasu")) {
            event.setCancelled(true);
            final Location oldLocation;
            oldLocation = player.getLocation();

            if (sonohokaCooldown > 0) {
                player.sendMessage(Messages.cooldown(arimasuCooldown));
                return;
            }

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("??6", ""));
            if (target == null) {
                player.sendMessage(CC.prefix("??cCe joueur n'est pas connect??"));
                return;
            }

            player.closeInventory();
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.sendMessage(CC.prefix("??fVous avez t??l??port?? ??a" + player.getName() + " ??fdans le monde li?? de Kamui."));
            target.teleport(manager.getKamuiSpawn());
            this.oldLocation.put(target.getUniqueId(), oldLocation);
            target.sendMessage(CC.prefix("??cKakashi ??fvous a t??l??port?? au monde li?? de Kamui."));
            target.sendMessage(CC.prefix("??fVous serez ret??l??port?? ?? votre ancienne position dans ??a5 minutes??f."));

            Tasks.runLater(() -> {
                target.teleport(oldLocation);
                target.sendMessage(CC.prefix("??fVous avez ??t?? t??l??port?? ?? votre ancienne position."));
            }, 5 * 20 * 60);

            sonohokaCooldown = 30 * 60;
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {

        if (!args[0].equalsIgnoreCase("yameru")) return;
        if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
            player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
            return;
        }
        NarutoUHC.usePower(player);
        for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
            if (Bukkit.getPlayer(uuid) != null) {
                Player target = Bukkit.getPlayer(uuid);
                if (target.getWorld().getName().equals("kamui") && oldLocation.get(uuid) != null) {
                    target.teleport(oldLocation.get(uuid));
                    target.sendMessage(CC.prefix("??aKakashi ??fa d??cid?? de vous t??l??porter sur le monde normal."));
                    player.sendMessage(CC.prefix("??fVous avez t??l??port?? ??a" + target.getName() + " ??fdans le monde normal."));

                }
            }
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.RAITON;
    }
}
