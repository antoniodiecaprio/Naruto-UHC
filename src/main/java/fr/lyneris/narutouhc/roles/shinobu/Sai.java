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
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Sai extends NarutoRole {

    private int montureCooldown = 0;
    private int tigresCooldown = 0;
    private boolean usedFuinjutsu = false;
    private boolean minuteHasPassed = false;
    private UUID saiTarget = null;

    public NarutoRoles getRole() {
        return NarutoRoles.SAI;
    }

    @Override
    public void resetCooldowns() {
        montureCooldown = 0;
        tigresCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (montureCooldown > 0) {
            montureCooldown--;
        }

        if (tigresCooldown > 0) {
            tigresCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Saï";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>(Arrays.asList("frérot", "t'es sai grosse merde"));
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Toile aux Monstres Fantomatiques")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Toile aux Monstres Fantomatiques")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Toile aux Monstres Fantomatiques");
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.SADDLE).setName("§6Monture").setLore(
                    "§7Lorsqu'il l'utilise, il est immobilisé pendant",
                    "§75 secondes suite auxquelles un cheval apparaît",
                    "§7sa position, seulement lui peut le chevaucher.",
                    "§7Ce pouvoir possède un délai de 10 minutes.",
                    "",
                    "§8» §7Cooldown: §f" + montureCooldown + " secondes",
                    "§8» §7Cliquez-ici pour utiliser"
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.COBBLESTONE).setName("§6Tigres").setLore(
                    "§7Lorsqu'il l'utilise, il est immobilisé pendant",
                    "§73 secondes et après cela 5 poissons d'argent",
                    "§7apparaissent, ils attaqueront le joueur le plus",
                    "§7proche, ils ont pour effet §bVitesse 2§f. Ce",
                    "§7pouvoir a un délai de 5 minutes.",
                    "",
                    "§8» §7Cooldown: §f" + tigresCooldown + " secondes",
                    "§8» §7Cliquez-ici pour utiliser"
            ).toItemStack());
            inv.setItem(3, new ItemBuilder(Material.PAPER).setName("§6Fûinjutsu").setLore(
                    "§7Lorsqu'il l'utilise. il lui est affiché tous",
                    "§7les joueurs présents dans un rayon de 20 blocs",
                    "§7autour de lui. Lorsqu'il clique sur l'un des",
                    "§7joueurs. Sai est immobilisé 1 minute. Le joueur",
                    "§7est prévenu qu'il a une minute pour tuer Sai,",
                    "§7afin d'éviter de se faire sceller par son Fûinjutsu.",
                    "§7A la fin de cette minute. le joueur ciblé se",
                    "§7retrouve en spectateur, il ne peux plus parler",
                    "§7et ne peux pas se séparer de Sai. Si ce demier",
                    "§7 vient à mourir, le joueur ciblé sera libéré",
                    "§7 et sera de retour à la positionde la mort de Sai",
                    "§7Cependant s'il ne meurt pas et que le camp Shinobi",
                    "§7vient à gagner la partie. le joueur ciblé est",
                    "§7donc considéré comme mort, puisqu'il a été scellé",
                    "§7 durant toute la partie. Son pouvoir est utilisable",
                    "§7une seule fois dans la partie.",
                    "",
                    "§8» §7Cliquez-ici pour utiliser"
            ).toItemStack());
            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {

        if (saiTarget != null) {
            Player var1 = Bukkit.getPlayer(saiTarget);
            if (var1 != null && minuteHasPassed) {
                var1.setGameMode(GameMode.SURVIVAL);
                var1.teleport(event.getEntity().getLocation());
                var1.sendMessage(CC.prefix("§cSaï §fest mort. Vous avez donc été téléporté à sa position."));
                saiTarget = null;
            }
        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        if (event.getInventory().getName().equals("Toile aux Monstres Fantomatiques")) {
            event.setCancelled(true);

            switch (event.getSlot()) {
                case 1:
                    if (montureCooldown > 0) {
                        player.sendMessage(Messages.cooldown(montureCooldown));
                        break;
                    }

                    if(Kisame.isBlocked(player)) {
                        player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                        return;
                    }
                    NarutoUHC.usePower(player);

                    manager.setStuned(player, true, 5);
                    Tasks.runLater(() -> {
                        Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
                        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                        horse.setTamed(true);
                        horse.setOwner(player);
                        horse.setBreed(true);
                        horse.setDomestication(horse.getMaxDomestication());
                        horse.setVariant(Horse.Variant.HORSE);
                        horse.setColor(Horse.Color.BLACK);
                        horse.setStyle(Horse.Style.NONE);
                        horse.setCustomNameVisible(false);
                        horse.setCustomName("..." + player.getName());

                    }, 5 * 20);
                    montureCooldown = 10 * 60;
                    break;
                case 2:
                    if (tigresCooldown > 0) {
                        player.sendMessage(Messages.cooldown(tigresCooldown));
                        break;
                    }

                    if(Kisame.isBlocked(player)) {
                        player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                        return;
                    }
                    NarutoUHC.usePower(player);
                    Player target = null;
                    for (Player nearbyEntity : Loc.getNearbyPlayers(player, 20)) {
                        target = nearbyEntity;
                        break;
                    }
                    if (target == null) {
                        player.sendMessage(CC.prefix("§cIl n'y aucun joueur à proximité de vous."));
                        player.closeInventory();
                        return;
                    }
                    Player finalTarget = target;
                    manager.setStuned(player, true, 5);

                    Player finalTarget2 = target;
                    Tasks.runLater(() -> {
                        for (int i = 0; i <= 5; i++) {
                            Silverfish fish = (Silverfish) player.getWorld().spawnEntity(player.getLocation(), EntityType.SILVERFISH);
                            Tasks.runTimer(() -> {
                                if (fish != null && Bukkit.getPlayer(finalTarget2.getUniqueId()) != null) {
                                    fish.setTarget(finalTarget);
                                }
                            }, 0, 20);
                            fish.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                            fish.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
                        }
                    }, 5 * 20);
                    tigresCooldown = 5 * 60;
                    break;
                case 3:
                    if (usedFuinjutsu) {
                        player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                        return;
                    }

                    int i = 9;
                    int nearbyPlayer = 0;
                    for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                        if (entity instanceof Player) {
                            nearbyPlayer++;
                        }
                    }
                    if (nearbyPlayer > 9 && nearbyPlayer <= 18) {
                        i = 18;
                    } else if (nearbyPlayer > 18) {
                        i = 27;
                    }
                    nearbyPlayer = 0;
                    Inventory inv = Bukkit.createInventory(null, i, "Choisir un joueur");
                    for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                        if (entity instanceof Player) {
                            inv.setItem(nearbyPlayer, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                            nearbyPlayer++;
                        }
                    }
                    player.openInventory(inv);
                    break;
            }

        }

        if (event.getInventory().getName().equals("Choisir un joueur")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);
            manager.setStuned(player, true, 60);
            Tasks.runLater(() -> {
                if (UHC.getUHC().getGameManager().getPlayers().contains(player.getUniqueId())) {
                    if (saiTarget != null) {
                        Bukkit.getPlayer(saiTarget).setGameMode(GameMode.SPECTATOR);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (saiTarget == null) cancel();
                                else player.setPassenger(target);
                            }
                        }.runTaskTimer(NarutoUHC.getNaruto(), 0, 20);
                    }
                } else {
                    saiTarget = null;
                }
                minuteHasPassed = true;
            }, 60 * 20);
            target.sendMessage(CC.prefix("§cSaï §fest en train de vous sceller. vous avez 1 minute pour l'éliminer."));
            player.sendMessage(CC.prefix("§fVous avez scellé §c" + target.getName() + "§f."));
            usedFuinjutsu = true;
            saiTarget = target.getUniqueId();
            player.closeInventory();
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public void onAllPlayerEntityMountEvent(EntityMountEvent event, Player entity) {
        if (!event.getMount().getName().startsWith("...")) return;
        if (!entity.getName().equals(event.getMount().getName().replace("...", ""))) event.setCancelled(true);
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        if (Role.isRole(event.getEntity(), NarutoRoles.SASUKE)) {
            killer.setMaxHealth(killer.getMaxHealth() + 4);
            Role.knowsRole(killer, NarutoRoles.SAKURA);
        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }
}
