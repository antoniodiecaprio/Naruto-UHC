package fr.lyneris.narutouhc.roles.taka;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Reach;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Sasuke extends NarutoRole {

    boolean killedPlayer = false;
    boolean revive = false;

    private boolean usingSusano = false;
    private int susanoCooldown = 0;
    private int rinneganCooldown = 0;
    private int bowCooldown = 0;
    private int amaterasuCooldown = 0;
    private boolean usedIzanagi = false;
    public boolean itachiDied = false;
    private int swordCooldown = 0;
    private int susanoTime() {
        return (itachiDied ? 10 : 5);
    }
    boolean usingManda = false;

    @Override
    public void resetCooldowns() {
        susanoCooldown = 0;
        bowCooldown = 0;
        rinneganCooldown = 0;
        amaterasuCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (susanoCooldown > 0) susanoCooldown--;
        if (bowCooldown > 0) bowCooldown--;
        if (rinneganCooldown > 0) rinneganCooldown--;
        if (amaterasuCooldown > 0) amaterasuCooldown--;
    }

    public NarutoRoles getRole() {
        return NarutoRoles.SASUKE;
    }

    @Override
    public String getRoleName() {
        return "Sasuke";
    }

    @Override
    public String getDescription() {
        return "??7??m--------------------------------------\n" +
                "??e ??f\n" +
                "??7??? R??le: ??6Sasuke\n" +
                "??7??? Objectif: ??rSon but est de gagner avec le camp de ??6Taka\n" +
                "??e ??f\n" +
                "??7??l??? Items :\n" +
                "??e ??f\n" +
                "??7??? Il dispose d???un item nomm?? \"??rSusano??7\" qui lui permet d'enflammer un ennemi d??s qu'il le frappe, il gagne ??galement l???effet ??9R??sistance 1??7, il re??oit aussi un arc Puissance 7, il peut tirer avec son arc une fois toutes les 20 secondes, ce pouvoir dure 5 minutes et poss??de un d??lai d'utilisation de 20 minutes.\n" +
                "??e ??f\n" +
                "??7??? Il dispose de l???item ?????rRinnegan??7???, celui-ci lui permet ?? son utilisation il re??oit l???effet ??bVitesse 2??7 pendant 2 minutes et ce pouvoir lui permet d?????changer de place avec un joueur en se t??l??portant ?? sa position et en faisant pareil pour le joueur, pour cela il doit simplement regarder le joueur. Le pouvoir poss??de un d??lai 5 minutes.\n" +
                "??e ??f\n" +
                "??7??? Il dispose aussi de l???item ?????rAmaterasu??7???, celui-ci lui permet d???enflammer un joueur en cliquant sur l???item en regardant le joueur qu???il souhaite cibl??, ce pouvoir dure 1 minute, la particularit?? de ces flammes sont qu???elles ne peuvent s?????teindre et ce m??me si le joueur reste dans de l???eau, cependant ??6Sasuke??7 perd ??c1 c??ur??7 permanent lors de l???utilisation de l???item et ce m??me pouvoir poss??de un d??lai d???utilisation de 15 minutes.\n" +
                "??e ??f\n" +
                "??7??l??? Commandes :\n" +
                "??e ??f\n" +
                "??r??? /ns Izanagi??7, celle-ci lui permet de recevoir 5 pommes d???or et d?????tre enti??rement r??g??n??r??, cependant il perd ??c1 c??ur??7 permanent, et il ne pourra plus utiliser le ??rSusano??7, il peut l???utiliser qu???une seule fois dans la partie.\n" +
                "??e ??f\n" +
                "??7??l??? Particularit??s :\n" +
                "??e ??f\n" +
                "??7??? Son camp se retrouvera souvent alli?? avec un autre camp, c???est pour cela qu???au d??but de la partie, son camp fait partie du camp d?????5Orochimaru??7. Si ??5Orochimaru??7 vient ?? mourir alors son camp sera sans alli??, suite ?? ??a, si ??cItachi??7 vient ?? mourir, son camp s???alliera avec l?????cAkatsuki??7 et donc il conna??tra  l???identit?? de ??cNagato??7. Si ??dTobi??7 et ??dMadara??7 rassemble les 9 biju pour former J??bi, il s???alliera au camp ??aShinobi??7.\n" +
                "??e ??f\n" +
                "??7??? S???il tue ??cItachi??7, il re??oit ??c3 c??urs??7 suppl??mentaires et son ??rSusano??7 fusionne avec celui de ??cItachi??7, il  recevra donc l?????p??e d?????cItachi??7 et le pouvoir dure 10 minutes ?? la place des 5 minutes de base.\n" +
                "??e ??f\n" +
                "??7??? Il dispose de l???effet ??bVitesse 1??7, ??6R??sistance au feu 1??7 et ??c5 c??urs??7 suppl??mentaires.\n" +
                "??e ??f\n" +
                "??7??? Il conna??t l???identit?? d?????5Orochimaru??7, ?? la mort de celui-ci, il obtient les identit??s de ??6Karin??7, ??6Suigetsu??7 et ??6Jug????7, il re??oit aussi l???item ??rManda??7 (voir r??le ??5Orochimaru??7).\n" +
                "??e ??f\n" +
                "??7??? Il dispose de la nature de Chakra : ??cKaton\n" +
                "??e ??f\n" +
                "??7??m--------------------------------------";
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (usingSusano) {
            if (event.getEntity().getFireTicks() <= 0) {
                event.getEntity().setFireTicks(100);
            }
        }

        if (Item.specialItem(player.getItemInHand(), "Epee")) {
            if (usingSusano) {
                if (swordCooldown > 0) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.cooldown(swordCooldown));
                    return;
                }

                swordCooldown = 20;

            } else {
                event.setCancelled(true);
                player.getInventory().removeItem(player.getItemInHand());
            }
        }

    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.OROCHIMARU);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 10);
        player.getInventory().addItem(Item.getInteractItem("Susano"));
        player.getInventory().addItem(Item.getInteractItem("Rinnegan"));
        player.getInventory().addItem(Item.getInteractItem("Amaterasu"));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Manda")).toItemStack());
    }


    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {

        if (event.getDamage() > player.getHealth() && revive) {
            event.setCancelled(true);
            World world = Bukkit.getWorld("uhc_world");
            int x = (int) (Math.random() * (world.getWorldBorder().getSize() / 2));
            int z = (int) (Math.random() * (world.getWorldBorder().getSize() / 2));
            int y = world.getHighestBlockYAt(x, z) + 1;
            player.teleport(new Location(world, x, y, z));
            player.sendMessage(prefix("&aVous avez ??t?? ressuscit??."));
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event, "Manda")) {
            if (!killedPlayer) {
                player.sendMessage(prefix("&cPour utiliser ce pouvoir, vous devez avoir tu?? quelqu'un."));
                return;
            }
            if(usingManda) {
                player.sendMessage(prefix("&cVous ??tes d??j?? sous l'effet de Manda."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            int random = (int) (Math.random() * 5);
            if (random == 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5*20*60, 1, false, false));
                player.sendMessage(prefix("&fVous avez obtenu &7R??sistance&f pendant 5 minutes."));
            } else if (random == 1) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20*60, 1, false, false));
                player.sendMessage(prefix("&fVous avez obtenu &bSpeed&f pendant 5 minutes."));
            } else if (random == 2) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 0, false, false));
                player.sendMessage(prefix("&fVous avez obtenu &cForce&f pendant 5 minutes."));
            } else if( (random == 3)) {
                this.revive = true;
                Tasks.runAsyncLater(() -> this.revive = false, 5*20*60);
                player.sendMessage(prefix("&fSi vous mourrez dans les &c5 &fprochaines minutes. Vous serrez ressuscit??."));
            } else {
                player.sendMessage(prefix("&fVous n'avez pas eu de chance et avez &crien re??u&f."));
            }
            usingManda = true;
            Tasks.runLater(() -> usingManda = false, 5*20*60);
        }

        if (Item.interactItem(event, "Amaterasu")) {
            if (this.amaterasuCooldown > 0) {
                Messages.getCooldown(amaterasuCooldown).queue(player);
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            for (Player players : Loc.getNearbyPlayers(player, 50)) {
                if (Reach.getLookingAt(player, players) && Role.isAlive(players)) {
                    UUID uuid = players.getUniqueId();
                    players.sendMessage(prefix("&cSasuke &fa utilis?? son pouvoir sur vous. De ce fait vous serez enflamm?? pendant &c1&f minute."));
                    player.sendMessage(prefix("&fVous avez utilis?? votre &aAmaterasu &fsur &c" + players.getName()));
                    player.setMaxHealth(player.getMaxHealth() - 2);
                    new BukkitRunnable() {
                        int timer = 60;

                        @Override
                        public void run() {
                            Player players = Bukkit.getPlayer(uuid);
                            if (timer <= 0) {
                                players.setFireTicks(0);
                                cancel();
                            }
                            timer--;
                            players.setFireTicks(40);
                        }
                    }.runTaskTimer(narutoUHC, 0, 20);
                    this.amaterasuCooldown = 15 * 60;
                    break;
                }
            }
        }

        if (Item.interactItem(event, "Rinnegan")) {
            if (this.rinneganCooldown > 0) {
                player.sendMessage(Messages.cooldown(rinneganCooldown));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            for (Player players : Loc.getNearbyPlayers(player, 50)) {
                if (Reach.getLookingAt(player, players) && Role.isAlive(players)) {
                    Location loc1 = player.getLocation();
                    Location loc2 = players.getLocation();
                    player.teleport(loc2);
                    players.teleport(loc1);
                    players.sendMessage(prefix("??cSasuke &fvient d'??changer sa position avec vous."));
                    player.sendMessage(prefix("&fVous avez utilis?? votre &aRinnegan"));
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2 * 60, 1, false, false));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                                player.removePotionEffect(PotionEffectType.SPEED);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                        }
                    }.runTaskLater(narutoUHC, 20 * 2 * 60);

                    this.rinneganCooldown = 5 * 60;
                    break;
                }
            }
        }

        if (Item.interactItem(event, "Susano")) {
            if (this.susanoCooldown > 0) {
                player.sendMessage(Messages.cooldown(susanoCooldown));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.sendMessage(prefix("&fVous avez utilis?? votre &aSusano&f."));
            player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 7).setName(Item.specialItem("Arc")).toItemStack());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, susanoTime() * 20 * 60, 0, false, false));
            if(itachiDied) {
                player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 7).setName(Item.specialItem("Epee")).toItemStack());
            }

            usingSusano = true;
            Tasks.runLater(() -> {
                usingSusano = false;
                ItemStack is = Arrays.stream(getPlayer().getInventory().getContents()).filter(itemStack -> Item.specialItem(itemStack, "Arc")).findFirst().orElse(null);
                if(this.itachiDied) {
                    ItemStack is2 = Arrays.stream(getPlayer().getInventory().getContents()).filter(itemStack -> Item.specialItem(itemStack, "Epee")).findFirst().orElse(null);
                    player.getInventory().removeItem(is2);
                }
                player.getInventory().removeItem(is);
            }, (long) susanoTime() * 20 * 60);
            susanoCooldown = 20 * 60;
        }
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("izanagi")) {

            if (usedIzanagi) {
                player.sendMessage(CC.prefix("??cVous avez d??j?? utilis?? ce pouvoir."));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.sendMessage(CC.prefix("??fVous avez utilis?? votre pouvoir ??aIzanagi??f."));

            player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 5).toItemStack());
            player.setHealth(player.getMaxHealth());
            player.setMaxHealth(player.getMaxHealth() - 2);
            usedIzanagi = true;
        }

    }

    @Override
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event, Player shooter) {
        if (Item.specialItem(shooter.getItemInHand(), "Arc")) {
            if (!usingSusano) {
                shooter.getInventory().removeItem(shooter.getItemInHand());
                return;
            }
            if (bowCooldown > 0) {
                shooter.sendMessage(Messages.cooldown(bowCooldown));
                return;
            }

            bowCooldown = 20;
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        if (Role.isRole(event.getEntity(), NarutoRoles.ITACHI)) {
            killer.setMaxHealth(killer.getMaxHealth() + 6);
            this.itachiDied = true;
            killer.sendMessage(prefix("Vous avez tu?? &cItachi&f. De ce fait vous obtenez &c3 coeurs&f permanent. Votre &aSusano &fa ??galement fusionn?? avec celui d'&cItachi&f. De ce fait vous recevez l'??p??e d'&cItachi&f et votre pouvoir dure 10 minutes ?? la place de 5 minutes."));
        }
        this.killedPlayer = true;
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if (Role.isRole(player, NarutoRoles.OROCHIMARU)) {
            Player sasuke = getPlayer();
            if (sasuke == null) return;
            roleManager.setCamp(sasuke, Camp.SOLO);
            sasuke.sendMessage(CC.prefix("&aOrochimaru &fest mort. Vous devez maintenant gagner tout &cseul&f."));
            Role.knowsRole(sasuke, NarutoRoles.KARIN);
            Role.knowsRole(sasuke, NarutoRoles.SUIGETSU);
            Role.knowsRole(sasuke, NarutoRoles.JUGO);
        }

        if (Role.isRole(player, NarutoRoles.ITACHI)) {
            Player sasuke = getPlayer();
            if (sasuke == null) return;
            roleManager.setCamp(sasuke, Camp.AKATSUKI);
            sasuke.sendMessage(CC.prefix("&aItachi &fest mort. Vous devez maintenant gagner tout l'&cAkatsuki&f."));
            Role.knowsRole(sasuke, NarutoRoles.NAGATO);
        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }
}
