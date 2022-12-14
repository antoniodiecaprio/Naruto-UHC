package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Kidomaru extends NarutoRole {

    private boolean spiderAlive;
    private Player nearestPlayer = null;
    private Spider spider;
    private int kyodaigumoCooldown = 0;
    private int marqueCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.KIDOMARU;
    }

    @Override
    public void resetCooldowns() {
        kyodaigumoCooldown = 0;
        marqueCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (kyodaigumoCooldown > 0) {
            kyodaigumoCooldown--;
        }

        if (marqueCooldown > 0) {
            marqueCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Kidomaru";
    }

    @Override
    public String getDescription() {
        return "??7??m--------------------------------------\n" +
                "??e ??f\n" +
                "??7??? R??le: ??5Kid??maru\n" +
                "??7??? Objectif: ??rSon but est de gagner avec le camp d'??5Orochimaru\n" +
                "??e ??f\n" +
                "??7??l??? Items :\n" +
                "??e ??f\n" +
                "??7??? Il dispose d???un arc Puissance 4.\n" +
                "??e ??f\n" +
                "??7??? Il dispose d???un item nomm?? ?????rKyodaigumo??7???, celui-ci lui permet de faire appara??tre une araign??e g??ante statique au dessus de lui, celle-ci fera appara??tre en continue des araign??es venimeuses ce qui cr??e une pluie d???araign??es, elles attaqueront tous les joueurs hormis lui, son item poss??de un d??lai de 15 minutes et il dure 1 minute.\n" +
                "??e ??f\n" +
                "??7??? Il dispose d???un item nomm?? ?????rMarque Maudite??7???, celui-ci lui permet de recevoir l???effet ??cForce 1??7 et de perdre son effet de faiblesse, son pouvoir dure 5 minutes et poss??de un d??lai de 30 minutes, ?? la fin de l???utilisation il perd ??c2 c??urs??7 pendant 15 minutes.\n" +
                "??e ??f\n" +
                "??7??l??? Particularit??s :\n" +
                "??e ??f\n" +
                "??7??? Il dispose de 60% de chance qu???une de ses fl??ches infliges 15% de d??g??ts suppl??mentaires et 20% de chance qu???elles infliges l???effet ??0C??cit?? 1??7 pendant 3 secondes.\n" +
                "??e ??f\n" +
                "??7??? Il dispose de l???effet ??lFaiblesse 1??7.\n" +
                "??e ??f\n" +
                "??7??m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {

        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 4).setName(Item.specialItem("Arc")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kyodaigumo")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marque Maudite")).toItemStack());


    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if(event.getEntity() instanceof CaveSpider || event.getEntity() instanceof CaveSpider) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Kyodaigumo")) {

            if (kyodaigumoCooldown > 0) {
                player.sendMessage(Messages.cooldown(kyodaigumoCooldown));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            double nearestDistance = Integer.MAX_VALUE;
            for (Player players : Role.getAliveOnlinePlayers()) {
                if (player.getLocation().distance(players.getLocation()) < nearestDistance) {
                    nearestPlayer = players;
                    nearestDistance = player.getLocation().distance(players.getLocation());
                }
            }


            World world = player.getLocation().getWorld();
            spider = world.spawn(player.getLocation().add(0, 3, 0), Spider.class);

            EntityLiving nmsEntity = ((CraftLivingEntity) spider).getHandle();
            NBTTagCompound tag = nmsEntity.getNBTTag();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            spider.setCustomName("??eKyodaigumo");
            nmsEntity.c(tag);
            tag.setInt("NoAI", 1);
            tag.setInt("NoGravity", 0);
            tag.setBoolean("Invulnerable", true);
            nmsEntity.f(tag);

            spiderAlive = true;
            player.sendMessage(CC.prefix("??fVous avez utilis?? votre pouvoir ??aKyodaigumo??f."));

            new BukkitRunnable() {
                int tickSpawnSpider = 1;

                @Override
                public void run() {
                    if (spiderAlive) {
                        if (tickSpawnSpider <= 0) {
                            for (int x = -10; x <= 10; x += 5) {
                                for (int z = -10; z <= 10; z += 5) {
                                    if(nearestPlayer.getUniqueId() == player.getUniqueId()) return;
                                    CaveSpider caveSpider = world.spawn(new Location(world, spider.getLocation().getBlockX() + x, world.getHighestBlockYAt(spider.getLocation()) + 2, spider.getLocation().getBlockZ() + z), CaveSpider.class);
                                    caveSpider.setCustomName("??eEnfant de Kyodaigumo");
                                    caveSpider.setTarget(nearestPlayer);
                                }
                            }
                            tickSpawnSpider = 20 * 20;
                        }
                    } else {
                        spider.remove();
                        cancel();
                    }
                    tickSpawnSpider--;
                }
            }.runTaskTimer(narutoUHC, 0, 1);

            Tasks.runLater(() -> {
                spiderAlive = false;
                spider.remove();
            }, 20 * 60);

            kyodaigumoCooldown = 15 * 60;

        }

        if (Item.interactItem(event.getItem(), "Marque Maudite")) {

            if (marqueCooldown > 0) {
                player.sendMessage(Messages.cooldown(marqueCooldown));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));

            Tasks.runLater(() -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
                player.setMaxHealth(player.getMaxHealth() - 4);
                player.sendMessage(CC.prefix("??fVous perdez ??c2 coeurs ??fpendant 15 minutes."));
            }, 5 * 20 * 60);
            Tasks.runLater(() -> {
                player.setMaxHealth(player.getMaxHealth() + 4);
                player.sendMessage(CC.prefix("??fVous avez r??cup??r?? vos ??a2 coeurs??f."));
            }, 20 * 20 * 60);

            marqueCooldown = 30 * 60;

        }

    }

    @Override
    public void onArrowHitPlayerEvent(EntityDamageByEntityEvent event, Player player, Player shooter, Arrow arrow) {
        int random = (int) (Math.random() * 5);
        if (random >= 0 && random <= 4) {
            event.setDamage(event.getDamage() * 1.5);
            shooter.sendMessage(CC.prefix("??fVotre fl??che a inflig?? l'effet ??a15% ??fde d??g??ts en plus ?? ??a" + player.getName()));
        }
        if (random == 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, false, false));
            shooter.sendMessage(CC.prefix("??fVotre fl??che a inflig?? l'effet ??7Blindness 1 ??f?? ??a" + player.getName()));
            player.sendMessage(CC.prefix("??cKidomaru ??fvous a inflig?? ??7Blindness 1 ??favec son arc."));
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }
}
