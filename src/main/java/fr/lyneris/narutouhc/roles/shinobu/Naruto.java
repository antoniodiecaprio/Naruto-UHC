package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Cuboid;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Naruto extends NarutoRole {

    public boolean usedKurama = false;
    public int kuramaCooldown = 0;
    public int rasenganCooldown = 0;
    public int senjutsuCooldown = 0;
    public int paumeUse = 0;
    public boolean isUsingRasengan = false;
    int dasshuCooldown = 0;
    int isanCooldown = 0;
    int kisuoiriCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.NARUTO;
    }

    @Override
    public String getRoleName() {
        return "Naruto";
    }

    @Override
    public void resetCooldowns() {
        kuramaCooldown = 0;
        rasenganCooldown = 0;
        senjutsuCooldown = 0;
        dasshuCooldown = 0;
        isanCooldown = 0;
        kisuoiriCooldown = 0;
    }

    @Override
    public void onAllPlayerPowerUse(Player player) {
        if(Role.isRole(player, NarutoRoles.JIRAYA)) {
            getPlayer().sendMessage(prefix("&aJiraya &fvient d'utiliser un pouvoir. Voici ses coordonnées:"));
            Location loc = player.getLocation();
            getPlayer().sendMessage(prefix("&a" + loc.getBlockX() + "&f, &a" + loc.getBlockY() + "&f, &a" + loc.getBlockZ()));
        }
    }

    @Override
    public void runnableTask() {
        if (kuramaCooldown > 0) {
            kuramaCooldown--;
        }

        if (rasenganCooldown > 0) {
            rasenganCooldown--;
        }

        if (senjutsuCooldown > 0) {
            senjutsuCooldown--;
        }
        if (dasshuCooldown > 0) {
            dasshuCooldown--;
        }
        if (isanCooldown > 0) {
            isanCooldown--;
        }
        if (kisuoiriCooldown > 0) {
            kisuoiriCooldown--;
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if(event.getInventory().getName().equalsIgnoreCase("Gamabunta")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {
                if(dasshuCooldown > 0) {
                    player.sendMessage(Messages.cooldown(dasshuCooldown));
                    return;
                }

                player.sendMessage(prefix("Vous avez utilisé votre &aDasshu&f."));
                player.setVelocity(player.getLocation().getDirection().multiply(10));
                dasshuCooldown = 5*60;
            }

//            if (event.getSlot() == 2) {
//                if(isanCooldown > 0) {
//                    player.sendMessage(Messages.cooldown(isanCooldown));
//                    return;
//                }
//
//            if(Kisame.isBlocked(player)) {
//                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
//                return;
//            }
//            NarutoUHC.usePower(player);
//
//                List<Player> players = new ArrayList<>();
//                int i = 0;
//                for (Player player1 : Loc.getNearbyPlayers(player, 30)) {
//                    if (i < 5) {
//                        players.add(player1);
//                        i++;
//                    }
//                }
//                player.sendMessage(prefix("Vous avez utilisé votre &aIsan&f."));
//                //TODO TELEPORTER DANS LA MAP QUI EST PAS ARRIVEE
//
//                isanCooldown = 20*60;
//
//            }

            if(event.getSlot() == 3) {
                if(this.kisuoiriCooldown > 0) {
                    Messages.getCooldown(kisuoiriCooldown).queue(player);
                    return;
                }
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                    return;
                }
                NarutoUHC.usePower(player);
                List<Entity> burning = new ArrayList<>();
                new BukkitRunnable() {
                    int timer = 40;

                    @Override
                    public void run() {
                        if (timer == 0) {
                            burning.clear();
                            cancel();
                            return;
                        }
                        char c = Loc.getCharCardinalDirection(player);
                        Cuboid cuboid;
                        if (c == 'W') {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() + 10);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 0.5, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() + 1);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ());
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        } else if (c == 'E') {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() - 10);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 0.5, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() - 1);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ());
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        } else if (c == 'N') {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() - 10, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() + 0.5);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 1, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() - 0.5);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ() + (i + 0.1));
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ() + (i + 0.1));
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        } else {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 10, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() + 0.5);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() + 1, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() - 0.5);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ() + (i + 0.1));
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ() + (i + 0.1));
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        }
                        player.getNearbyEntities(20, 20, 20).forEach(entity -> {
                            for (Block block : cuboid.getBlocks()) {
                                if (block.getLocation().distance(entity.getLocation()) <= 3 && entity.getUniqueId() != player.getUniqueId() ) {
                                    entity.setFireTicks(20);
                                    burning.add(entity);
                                    break;
                                }
                            }
                        });
                        timer--;
                        burning.forEach(entity -> entity.setFireTicks(20));
                    }
                }.runTaskTimer(narutoUHC, 0, 5);

                kisuoiriCooldown = 10*60;
            }

        }
    }

    @Override
    public List<String> getDescription() {
        ArrayList<String> list = new ArrayList<>();
        list.add("§f§m-------------------------------");
        list.add("§7Vous êtes Naruto");
        list.add("");
        list.add("§7Son but est de gagner avec les §aShinobi§7.");
        list.add("");
        list.add("§7§lItems");
        list.add(" ");
        list.add("§7• Il dispose d'un item nommé '§8Kurama§7', lorsqu’il clique sur celui-ci, l'item permet de recevoir §cForce 1§7 et §bVitesse 2 §7pendant 5 minutes, après avoir cliqué sur cet item, il pourra effectuer la commande ci-dessous.");
        list.add(" ");
        list.add("§8➜ /ns smell <Joueur>§7, cette commande lui permet de savoir si la personne choisit possède des intentions meurtrières, tout camps autres que celui des §aShinobi§7 possèdent des intentions meurtrières et inversement ce n’est pas le cas, cependant §cItachi§7, §6Karin§7 et §dTobi§7 ne possèdent aucunes intentions meurtrières, lorsque §aSaï§7 se situe dans un rayon de 30 blocs proche de §6Sasuke§7, il possède des intentions meurtrières.");
        list.add(" ");
        list.add("§7• Il dispose d’un item nommé “§8Rasengan§7”, lorsqu’il l’utilise, et qu’après cela il frappe un joueur, celui-ci explosera de la valeur d’une dynamite (TNT) dans le jeu, son item possède un délai de 5 minutes.");
        list.add(" ");
        list.add("§7• Il dispose d'un item nommé '§8Senjutsu§7', lorsqu’il clique sur celui-ci, il obtient les effets §bVitesse 1§7 et §cForce 2§7 pendant 5 minutes, il possède un délai de 30 minutes.");
        list.add(" ");
        list.add("§7§lCommandes :");
        list.add(" ");
        list.add("§8➜ /ns paume <Joueur>§7, cette commande permet au joueur ciblé de recevoir les effets §dRégénération 10§7 et §eAbsorption 2 §7pendant 5 secondes, le joueur ciblé doit être à moins de dix blocs de lui, il peut le faire 2 fois dans la partie, il ne peut pas se choisir lui même. Lorsqu’il utilise son pouvoir sur §aGaï§7, pendant qu’il utilise son item §8Gaï de la nuit§7, il ne meurt pas à la fin de son utilisation.");
        list.add(" ");
        list.add("§7§lParticularités :");
        list.add(" ");
        list.add("§7• Il dispose de §c2 cœurs§7 supplémentaires.");
        list.add(" ");
        list.add("§7• Il connaît l'identité de §aJiraya§7, lorsque celui-ci utilise son pouvoir, il est écrit dans son chat que §aJiraya§7 vient d'utiliser son pouvoir ainsi que l'emplacement ou il a utilisé celui-ci.");
        list.add(" ");
        list.add("§7• Si §aJiraya§7 vient à mourir, il obtient §c3 cœurs§7 supplémentaires.");
        list.add(" ");
        list.add("§7• A 30 minutes et 40 minutes de jeu, ses coordonnées sont écrites par message textuels uniquement à l’§cAkatsuki§7.");
        list.add("");
        list.add("§7• Il dispose de la nature de Chakra : §aFûton");
        list.add("§f§m-------------------------------");
        return list;
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kurama")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Rasengan")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Senjutsu")).toItemStack());
        player.setMaxHealth(player.getMaxHealth() + 4);
        Role.knowsRole(player, NarutoRoles.JIRAYA);
    }

    @Override
    public void onMinute(int minute, Player player) {

        if (minute == 30 || minute == 40) {
            for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
                Player akatsuki = Bukkit.getPlayer(uuid);
                if (akatsuki != null) {
                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();
                    akatsuki.sendMessage(CC.CC_BAR);
                    akatsuki.sendMessage(CC.prefix("§fCoordonnées de §aNaruto§f:"));
                    akatsuki.sendMessage(" §f§l» §a" + x + "§8, §a" + y + "§8, " + z);
                    akatsuki.sendMessage(CC.CC_BAR);
                }
            }
        }

    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        if (Role.isRole(player, NarutoRoles.JIRAYA)) {
            Player naruto = Role.findPlayer(NarutoRoles.NARUTO);
            if (naruto == null) return;
            naruto.sendMessage(CC.prefix("§cJiraya §fest mort. Vous obtenez donc §63 coeurs §fpermanent ainsi que l'item &aGamabunta&f."));
            naruto.setMaxHealth(naruto.getMaxHealth() + 6);
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(Item.interactItem(event, "Gamabunta")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Gamabunta");
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Dasshu").setLore(
                    "§7Ce pouvoir permet à Jiraya de faire un",
                    "§7grand saut dans la direction qu’il regarde,",
                    "§7il saute d’environ 30 blocs, celui-ci",
                    "§7possède un délai de 5 minutes."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Isan").setLore(
                    "§7Ce pouvoir permet à Jiraya de téléporter",
                    "§75 joueurs proches de lui, dans un rayon de",
                    "§730 blocs, dans le suc gastrique de Gamabunta",
                    "§7dans cette zone les joueurs ne peuvent",
                    "§7utilisés leurs pouvoirs, seuls les effets",
                    "§7restent actifs et perdent 1 cœur non",
                    "§7permanent toutes les 30 secondes (hormis",
                    "§7Jiraya), ce pouvoir dure 3 minutes et",
                    "§7possède un délai de 20 minutes."
            ).toItemStack());
        }

        if (Item.interactItem(event.getItem(), "Kurama")) {
            if (kuramaCooldown > 0) {
                player.sendMessage(Messages.cooldown(kuramaCooldown));
                return;
            }

            usedKurama = true;
            kuramaCooldown = 20 * 60;
            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aKurama§f."));

            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 1, false, false));
            Tasks.runLater(() -> usedKurama = false, 5 * 20 * 60);
        }

        if (Item.interactItem(event.getItem(), "Senjutsu")) {
            if (senjutsuCooldown > 0) {
                player.sendMessage(Messages.cooldown(senjutsuCooldown));
                return;
            }

            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aSenjutsu§f."));

            senjutsuCooldown = 30 * 60;
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));
        }

        if (Item.interactItem(player.getItemInHand(), "Rasengan")) {
            if (rasenganCooldown > 0) {
                player.sendMessage(Messages.cooldown(rasenganCooldown));
                return;
            }
            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aRasengan§f."));

            rasenganCooldown = 5 * 60;
            isUsingRasengan = true;

        }

    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!isUsingRasengan) return;


        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 5);
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 5);

        Tasks.runLater(() -> player.getWorld().createExplosion(event.getEntity().getLocation(), 3.0f), 3);

        isUsingRasengan = false;

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
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
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

            if (targetRole.getRoleName().equals("SAI")) {
                var1 = Role.findPlayer(NarutoRoles.SASUKE) != null && Role.findPlayer(NarutoRoles.SASUKE).getLocation().distance(target.getLocation()) <= 30;
            } else if (targetRole.getRoleName().equals("ITACHI") || targetRole.getRoleName().equals("KARIN") || targetRole.getRoleName().equals("OBITO")) {
                var1 = false;
            }

            usedKurama = false;


            if (var1) {
                player.sendMessage(CC.prefix("§c" + target.getName() + " §fpossède d'intentions meurtrières."));
            } else {
                player.sendMessage(CC.prefix("§a" + target.getName() + " §fne possède pas d'intentions meurtrières."));
            }
        }

        if (args[0].equalsIgnoreCase("paume")) {
            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns paume <player>"));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);
            if (Role.isRole(target, NarutoRoles.GAI_MAITO)) {
                if (GaiMaito.isUsingGai) {
                    GaiMaito.paumeSaved = true;
                    target.sendMessage(prefix("&fVous n'allez pas mourrir à la fin de votre &aGaï de la nuit&f."));
                }
            }

            if (paumeUse >= 2) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 2 fois."));
                return;
            }

            if (target.getName().equals(player.getName())) {
                player.sendMessage(CC.prefix("§cVous ne pouvez pas utiliser le pouvoir sur vous-même."));
                return;
            }

            paumeUse++;

            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 9, false, false));
            target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5 * 20, 1, false, false));


            target.sendMessage(CC.prefix("§aNaruto §fa décidé d'utiliser son pouvoir sur vous. De ce fait vous obtenez §dRégénération 10 §fet §eAbsorption 2 §fpendant 5 secondes."));
            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvour sur §a" + target.getName() + "§f."));

        }

    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }
}
