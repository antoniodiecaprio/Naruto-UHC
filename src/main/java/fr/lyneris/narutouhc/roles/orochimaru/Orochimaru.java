package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Orochimaru extends NarutoRole {

    int newHealth = 20;
    int timer = 0;
    private Chakra chakra = Chakra.KATON;
    boolean killedPlayer = false;
    private boolean revive = false;
    public boolean usingManda = false;

    public NarutoRoles getRole() {
        return NarutoRoles.OROCHIMARU;
    }

    @Override
    public void runnableTask() {
        if (timer > 0) {
            timer--;
        }

        if (timer == 0) {
            timer = -1;
            Player player = Role.findPlayer(NarutoRoles.OROCHIMARU);
            player.setMaxHealth(newHealth);
        }

    }

    @Override
    public String getRoleName() {
        return "Orochimaru";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §5Orochimaru\n" +
                "§7▎ Objectif: §rSon but est de gagner avec le camp d'§5Orochimaru\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item nommé “§rEdo Tensei§7”, celui-ci lui permet de voir un menu de la liste de tous les joueurs morts dans un rayon de 20 blocs (emplacement de leur cadavre), lorsqu’il clique sur l’un d’entre eux, le joueur ressuscite d’entre les morts. Il rejoint le camp d’§5Orochimaru§7.\n" +
                "§e §f\n" +
                "§7• Il possède une épée en diamant Tranchant 4 nommé “§rKusanagi§7”.\n" +
                "§e §f\n" +
                "§7• Il dispose aussi d’un item nommé “§rManda§7”, pour utiliser celui-ci, il doit avoir tué un joueur, un joueur de plus pour la deuxième utilisation et ainsi de suite, §rManda§7 donnera à §5Orochimaru§7 un pouvoir aléatoire parmi ceux cités ci-dessous, le pouvoir reçu aura une durée de 5 minutes et cet item ne possède aucun délai, il ne peut faire deux utilisation en même temps, il doit attendre que le premier pouvoir s’arrête, voici donc les pouvoirs que lui octroie Manda :\n" +
                "§7- Il octroie §9Résistance 2§7 à §5Orochimaru§7.\n" +
                "§7- Il octroie §bVitesse 2§7 à §5Orochimaru§7.\n" +
                "§7- Il octroie §cForce 1§7 à §5Orochimaru§7.\n" +
                "§7- Il ressuscite instantanément §5Orochimaru§7 lorsqu’il meurt\n" +
                "§7- Il ne donne rien à §5Orochimaru§7.\n" +
                "§7- Il enlève §c1 cœur§7 non permanent toutes les 30 secondes à §5Orochimaru§7.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns marquemaudite <Joueur>§7, celui-ci lui permet de donner 5% de §cForce§7 et de §9Résistance§7 supplémentaires au joueur ciblé, si le joueur possède déjà la marque maudite alors il reçoit 10% de §cForce§7 et de §9Résistance§7 supplémentaires.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• À partir de 40 minutes de jeu il perd §c1 cœur§7 de manière permanente toutes les 10 minutes, pour résoudre ce problème, à chaque fois qu’il effectue un meurtre sa vie revient à la normal pendant les dix minutes qui suivent son meurtre, ce cycle revient et il se doit de refaire un meurtre pour dévier le problème.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’effet §9Résistance 1§7, cependant lorsqu’il se trouve à moins de §c4 cœurs§7 sur sa barre de vie, il reçoit l’effet §9Résistance 2§7.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il mange une pomme d’or, il régénère §c3 cœurs§7 à la place des §c2 cœurs§7 de base.\n" +
                "§e §f\n" +
                "§7• Il dispose de la liste du camp §5Orochimaru§7 et une autre liste du camp §6Taka§7.\n" +
                "§e §f\n" +
                "§7• Il dispose d’une nature de Chakra aléatoire.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        List<Chakra> chakras = new ArrayList<>(Arrays.asList(Chakra.values()));
        chakras.remove(Chakra.AUCUN);
        Collections.shuffle(chakras);

        this.chakra = chakras.get(0);

        List<String> orochimaru = new ArrayList<>();
        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(e -> Bukkit.getPlayer(e) != null)
                .filter(uuid -> narutoUHC.getRoleManager().getCamp(uuid) == Camp.OROCHIMARU)
                .map(e -> Bukkit.getPlayer(e).getName())
                .forEach(orochimaru::add);

        List<String> taka = new ArrayList<>();
        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(e -> Bukkit.getPlayer(e) != null)
                .filter(uuid -> narutoUHC.getRoleManager().getCamp(uuid) == Camp.TAKA)
                .map(e -> Bukkit.getPlayer(e).getName())
                .forEach(taka::add);


        player.sendMessage(CC.prefix("§cListe des Orichimaru:"));
        orochimaru.forEach(s -> player.sendMessage(" §8- §c" + s));
        player.sendMessage(" ");

        player.sendMessage(CC.prefix("§cListe des Taka:"));
        taka.forEach(s -> player.sendMessage(" §8- §c" + s));
        player.sendMessage(" ");

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).setName(Item.specialItem("Kusanagi")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Edo Tensei")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Manda")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event, "Manda")) {
            if (!killedPlayer) {
                player.sendMessage(prefix("&cPour utiliser ce pouvoir, vous devez avoir tué quelqu'un."));
                return;
            }

            if(usingManda) {
                player.sendMessage(prefix("&cVous êtes déjà sous l'effet de Manda."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            int random = (int) (Math.random() * 6);
            if (random == 0) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5*20*60, 1, false, false));
                Tasks.runLater(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false)), 5*20*60+1);
                player.sendMessage(prefix("&fVous avez obtenu &7Résistance&f pendant 5 minutes."));
            } else if (random == 1) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20*60, 1, false, false));
                player.sendMessage(prefix("&fVous avez obtenu &bSpeed&f pendant 5 minutes."));
            } else if (random == 2) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 0, false, false));
                player.sendMessage(prefix("&fVous avez obtenu &cForce&f pendant 5 minutes."));
            } else if( (random == 3)) {
                this.revive = true;
                Tasks.runAsyncLater(() -> this.revive = false, 5*20*60);
                player.sendMessage(prefix("&fSi vous mourrez dans les &c5 &fprochaines minutes. Vous serrez ressuscité."));
            } else if( (random == 4)) {
                player.sendMessage(prefix("&fVous perdez &c1 coeur &ftoutes les 30 secondes pendant 5 minutes."));
                new BukkitRunnable() {
                    int timer = 10;
                    @Override
                    public void run() {
                        if(timer == 0) {
                            cancel();
                            return;
                        }
                        timer--;
                        if(player.getHealth() < 1) {
                            player.setHealth(0);
                            return;
                        }
                        player.setHealth(player.getHealth() - 1);

                    }
                }.runTaskTimer(narutoUHC, 0, 30*20);
            } else {
                player.sendMessage(prefix("&fVous n'avez pas eu de chance et avez &crien reçu&f."));
            }
            usingManda = true;
            Tasks.runLater(() -> usingManda = false, 5*20*60);
        }

        if (Item.interactItem(event.getItem(), "Edo Tensei")) {
            Inventory inv = Bukkit.createInventory(null, 18, "Edo Tensei");
            int j = 1;
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            for (UUID uuid : NarutoUHC.getNaruto().getManager().getDeathLocation().keySet()) {
                if (Bukkit.getPlayer(uuid) != null) {
                    if (NarutoUHC.getNaruto().getManager().getDeathLocation().get(uuid).distance(player.getLocation()) <= 20) {
                        if (!UHC.getUHC().getGameManager().getPlayers().contains(uuid)) {
                            Player target = Bukkit.getPlayer(uuid);
                            inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + target.getName()).setSkullOwner(target.getName()).toItemStack());
                            j++;
                        }
                    }
                }
            }
            player.openInventory(inv);
        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Edo Tensei")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

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

            giveStuff(target);
            UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
            roleManager.setCamp(target.getUniqueId(), Camp.OROCHIMARU);

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

    }

    @Override
    public void onMinute(int minute, Player player) {


        if (minute == 40) {
            Tasks.runTimer(() -> {
                newHealth -= 2;
                player.setMaxHealth(player.getMaxHealth() - 2);
            }, 0, 20 * 60 * 10);
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        timer = 10 * 60;
        killer.setMaxHealth(20);
        this.killedPlayer = true;
    }

    @Override
    public void onPlayerHealthRegain(EntityRegainHealthEvent event, Player player) {
        if (player.getHealth() > 8) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {

        if(event.getDamage() > player.getHealth() && revive) {
            event.setCancelled(true);
            World world = Bukkit.getWorld("uhc_world");
            int x = (int) (Math.random() * (world.getWorldBorder().getSize() / 2));
            int z = (int) (Math.random() * (world.getWorldBorder().getSize() / 2));
            int y = world.getHighestBlockYAt(x, z) + 1;
            player.teleport(new Location(world, x, y, z));
            player.sendMessage(prefix("&aVous avez été ressuscité."));
        }

        if (player.getHealth() <= 8) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        }
    }

    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            Tasks.runLater(() -> {
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 1, false, false));
            }, 1L);
        }
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("marquemaudite")) {

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns marquemaudite <player>"));
                return;
            }


            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            HashMap<UUID, Integer> strength = NarutoUHC.getNaruto().getManager().getStrength();
            HashMap<UUID, Integer> resistance = NarutoUHC.getNaruto().getManager().getStrength();
            NarutoUHC.getNaruto().getManager().getStrength().put(target.getUniqueId(), strength.getOrDefault(target.getUniqueId(), 0) + 5);
            NarutoUHC.getNaruto().getManager().getResistance().put(target.getUniqueId(), resistance.getOrDefault(target.getUniqueId(), 0) + 5);

            player.sendMessage(CC.prefix("§fVous avez donné §c5% §fde §cForce §fet de §7Résistence §fen plus à §a" + target.getName()));
            target.sendMessage(CC.prefix("§aOrochimaru §fvous a donné §c5% §fde §cForce §fet de §7Résistence §fen plus."));

        }
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

    @Override
    public Chakra getChakra() {
        return chakra;
    }
}
