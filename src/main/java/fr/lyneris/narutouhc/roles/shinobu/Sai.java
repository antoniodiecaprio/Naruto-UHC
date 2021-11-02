package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
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

    @Override
    public void resetCooldowns() {
        montureCooldown = 0;
        tigresCooldown = 0;
    }

    @Override
    public void startRunnableTask() {
        if(montureCooldown > 0) {
            montureCooldown--;
        }

        if(tigresCooldown > 0) {
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
        if(Item.interactItem(event.getItem(), "Toile aux Monstres Fantomatiques")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Toile aux Monstres Fantomatiques");
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.SADDLE).setName("§6Monture").setLore(
                    "§7Lorsqu'il l'utilise, il est immobilisé pendant",
                    "§75 secondes suite auxquelles un cheval apparaît",
                    "§7sa position, seulement lui peut le chevaucher.",
                    "§7Ce pouvoir possède un délai de 10 minutes.",
                    "",
                    "§f§l» §eCooldown: §f" + montureCooldown + " secondes",
                    "§f§l» §eCliquez-ici pour utiliser"
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.COBBLESTONE).setName("§6Tigres").setLore(
                    "§7Lorsqu'il l'utilise, il est immobilisé pendant",
                    "§73 secondes et après cela 5 poissons d'argent",
                    "§7apparaissent, ils attaqueront le joueur le plus",
                    "§7proche, ils ont pour effet §bVitesse 2§f. Ce",
                    "§7pouvoir a un délai de 5 minutes.",
                    "",
                    "§f§l» §eCooldown: §f" + tigresCooldown + " secondes",
                    "§f§l» §eCliquez-ici pour utiliser"
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
                    "§f§l» §eCliquez-ici pour utiliser"
            ).toItemStack());
            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {

        if(saiTarget != null) {
            Player var1 = Bukkit.getPlayer(saiTarget);
            if(var1 != null && minuteHasPassed) {
                var1.setGameMode(GameMode.SURVIVAL);
                var1.teleport(event.getEntity().getLocation());
                var1.sendMessage("§7▎ §cSaï §fest mort. Vous avez donc été téléporté à sa position.");
                saiTarget = null;
            }
        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        float oldWalkSpeed;
        oldWalkSpeed = player.getWalkSpeed();

        if(event.getInventory().getName().equals("Toile aux Monstres Fantomatiques")) {
            event.setCancelled(true);

            switch (event.getSlot()) {
                case 1:
                    if(montureCooldown > 0) {
                        player.sendMessage(Messages.cooldown(montureCooldown));
                        break;
                    }
                    player.setWalkSpeed(0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 200, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5*20, 200, false, false));
                    
                    Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> {
                        player.setWalkSpeed(oldWalkSpeed);
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

                    }, 5*20);
                    montureCooldown = 10*60;
                    break;
                case 2:
                    if(tigresCooldown > 0) {
                        player.sendMessage(Messages.cooldown(tigresCooldown));
                        break;
                    }
                    Player target = null;
                    for (Entity nearbyEntity : player.getNearbyEntities(20, 20, 20)) {
                        if(nearbyEntity instanceof Player) {
                            target = (Player) nearbyEntity;
                            break;
                        }
                    }
                    if(target == null) {
                        player.sendMessage("§7▎ §cIl n'y aucun joueur à proximité de vous.");
                        player.closeInventory();
                        return;
                    }
                    Player finalTarget = target;
                    player.setWalkSpeed(0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 200, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5*20, 200, false, false));
                    
                    Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> {
                        player.setWalkSpeed(oldWalkSpeed);
                        for(int i = 0; i <= 5; i++) {
                            Silverfish fish = (Silverfish) player.getWorld().spawnEntity(player.getLocation(), EntityType.SILVERFISH);
                            fish.setTarget(finalTarget);
                            fish.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                            fish.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
                        }
                    }, 5*20);
                    tigresCooldown = 5*60;
                    break;
                case 3:
                    if(usedFuinjutsu) {
                        player.sendMessage("§7▎ §cVous avez déjà utilisé ce pouvoir.");
                        return;
                    }
                    int i = 9;
                    int nearbyPlayer = 0;
                    for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
                        if (entity instanceof Player) {
                            nearbyPlayer++;
                        }
                    }
                    if(nearbyPlayer > 9 && nearbyPlayer <= 18) {
                        i = 18;
                    } else if(nearbyPlayer > 18) {
                        i = 27;
                    }
                    nearbyPlayer = 0;
                    Inventory inv = Bukkit.createInventory(null, i, "Choisir un joueur");
                    for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
                        if (entity instanceof Player) {
                            inv.setItem(nearbyPlayer, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                            nearbyPlayer++;
                        }
                    }
                    player.openInventory(inv);
                    break;
            }

        }

        if(event.getInventory().getName().equals("Choisir un joueur")) {
            event.setCancelled(true);
            if(!event.getCurrentItem().hasItemMeta()) return;
            if(!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
            if(target == null) {
                player.sendMessage("§7▎ §cCe joueur n'est pas connecté");
                return;
            }
            player.setWalkSpeed(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60*20, 200, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60*20, 200, false, false));
            Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> {
                player.setWalkSpeed(oldWalkSpeed);
                if(UHC.getUhc().getGameManager().getPlayers().contains(player.getUniqueId())) {
                    if(saiTarget != null) {
                        Bukkit.getPlayer(saiTarget).setGameMode(GameMode.SPECTATOR);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(saiTarget == null) {
                                    cancel();
                                } else {
                                    player.setPassenger(target);
                                }
                            }
                        }.runTaskTimer(NarutoUHC.getNaruto(), 0, 20);
                    }
                } else {
                    saiTarget = null;
                }
                minuteHasPassed = true;
             }, 60*20);
            target.sendMessage("§7▎ §cSaï §fest en train de vous sceller. vous avez 1 minute pour l'éliminer.");
            target.sendMessage("§7▎ §fVous avez scellé §c" + target.getName() + "§f.");
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
        if(!event.getMount().getName().startsWith("...")) return;
        if(!entity.getName().equals(event.getMount().getName().replace("...", ""))) event.setCancelled(true);
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        if(Role.isRole(event.getEntity(), "Sasuke")) {
            killer.setMaxHealth(killer.getMaxHealth() + 4);
            Role.knowsRole(killer, "Sakura");
        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }
}
