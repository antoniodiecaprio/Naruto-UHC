package fr.lyneris.narutouhc.roles.sankyodai;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.BoundingBox;
import fr.lyneris.narutouhc.packet.Cuboid;
import fr.lyneris.narutouhc.packet.RayTrace;
import fr.lyneris.narutouhc.particle.WorldUtils;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Gaara extends NarutoRole {

    public static boolean narutoHit = false;
    public int fixCooldown = 0;
    public int shukakuCooldown = 0;
    public boolean usingShukaku = false;
    public List<Block> blocks = new ArrayList<>();
    public boolean usingArmure = false;

    @Override
    public void resetCooldowns() {
        fixCooldown = 0;
        shukakuCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(fixCooldown > 0) fixCooldown--;
        if(shukakuCooldown > 0) shukakuCooldown--;
    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        if(Role.isRole(damager, NarutoRoles.NARUTO)) {
            Player gaara = Role.findPlayer(NarutoRoles.GAARA);
            if(gaara == null) return;

            narutoHit = true;
            Tasks.runAsyncLater(() -> narutoHit = false, 30*20);
        }
    }

    @Override
    public String getRoleName() {
        return "Gaara";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.GAARA;
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemStack(Material.SAND, 128));
        Role.knowsRole(player, NarutoRoles.TEMARI);
        Role.knowsRole(player, NarutoRoles.KANKURO);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Manipulation du sable")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Shukaku")).toItemStack());
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if(event.getInventory().getName().equalsIgnoreCase("Manipulation du sable")) {
            if(event.getSlot() == 3) {
                Inventory inv = Bukkit.createInventory(null, 9, "Attaque");

                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Tsunami de Sable").setLore(
                        "§7Il permet de créer une vague de sable qui",
                        "§7déferle face à Gaara, si elle vient à",
                        "§7touché un joueur, le joueur sera propulsé",
                        "§7violemment en arrière, et reçoit 2 cœurs",
                        "§7de dégâts. Ce pouvoir nécessite 30 blocs",
                        "§7de sables."
                ).toItemStack());
                inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Sarcophage de Sable").setLore(
                        "§7Il permet de créer une structure de sable",
                        "§7ayant une base de 4x4 avec une hauteur de",
                        "§75 blocs, le milieu de la structure est",
                        "§7occupée par le joueur ciblé, pour cibler un",
                        "§7joueur, il suffit de viser celui-ci et",
                        "§7d’effectuer un clique gauche avec l’item.",
                        "§7Ce pouvoir nécessite 45 blocs de sables."
                ).toItemStack());
                inv.setItem(3, new ItemBuilder(Material.NETHER_STAR).setName("§6Lance").setLore(
                        "§7Il permet de donner à Gaara une épée en",
                        "§7diamant Tranchant 4, cependant celleci",
                        "§7possède seulement 25 points de durabilités.",
                        "§7Ce pouvoir nécessite 64 blocs de sables."
                ).toItemStack());

                player.openInventory(inv);
            }
        }

        if(event.getSlot() == 5) {
            Inventory inv = Bukkit.createInventory(null, 9, "Défense");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Bouclier de Sable").setLore(
                    "§7Il permet d’entourer Gaara de blocs de",
                    "§7pierres de sables qui sont 5 fois plus",
                    "§7difficiles à détruire qu’un bloc normal,",
                    "§7cependant à l’intérieur gaara reçoit",
                    "§7l’effet célérité 2, ce qui lui permet",
                    "§7de casser les blocs qui l’entoure plus",
                    "§7facilement. Ce pouvoir nécessite 64 blocs",
                    "§7de sables. "
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Armure de Sable").setLore(
                    "§7Il permet de donner à Gaara l’effet Résistance",
                    "§71. Ce pouvoir nécessite 32 blocs de sables,",
                    "§7cependant chaque coup qu’il reçoit lui coûte",
                    "§75 blocs de sables, s’il n’a plus ou pas",
                    "§7suffisamment de blocs de sables, son pouvoir",
                    "§7ne fait plus effet."
            ).toItemStack());

            player.openInventory(inv);
        }


        if(event.getInventory().getName().equalsIgnoreCase("Attaque")) {
            if(event.getSlot() == 1) {
                if(Role.getItemAmount(player, Material.SAND) < 30) {
                    player.sendMessage(prefix("&cIl vous faut un total de 30 sables pour utiliser ce pouvoir."));
                    return;
                }

                player.sendMessage(prefix("&fVous avez utilisé votre &aTsunami de Sable&f."));
                useTsunami(player);
                Role.removeItem(player, Material.SAND, 30);
                player.closeInventory();
            }

            if(event.getSlot() == 2) {
                if(Role.getItemAmount(player, Material.SAND) < 45) {
                    player.sendMessage(prefix("&cIl vous faut un total de 45 sables pour utiliser ce pouvoir."));
                    return;
                }

                player.sendMessage(prefix("&fVous avez utilisé votre &aSarcophage de Sable&f."));
                useSarcophage(player);
                Role.removeItem(player, Material.SAND, 45);
                player.closeInventory();
            }

            if(event.getSlot() == 3) {
                if(Role.getItemAmount(player, Material.SAND) < 64) {
                    player.sendMessage(prefix("&cIl vous faut un total de 64 sables pour utiliser ce pouvoir."));
                    return;
                }

                player.sendMessage(prefix("&fVous avez utilisé votre &aLance&f."));
                useLance(player);
                Role.removeItem(player, Material.SAND, 64);
                player.closeInventory();
            }
        }


        if(event.getInventory().getName().equalsIgnoreCase("Défense")) {
            if (event.getSlot() == 1) {
                if (Role.getItemAmount(player, Material.SAND) < 64) {
                    player.sendMessage(prefix("&cIl vous faut un total de 64 sables pour utiliser ce pouvoir."));
                    return;
                }

                player.sendMessage(prefix("&fVous avez utilisé votre &aBouclier de Sable&f."));
                useBouclier(player);
                Role.removeItem(player, Material.SAND, 45);
                player.closeInventory();
            }

            if (event.getSlot() == 2) {
                if (Role.getItemAmount(player, Material.SAND) < 32) {
                    player.sendMessage(prefix("&cIl vous faut un total de 32 sables pour utiliser ce pouvoir."));
                    return;
                }

                player.sendMessage(prefix("&fVous avez utilisé votre &aArmure de Sable&f."));
                useArmure(player);
                Role.removeItem(player, Material.SAND, 32);
                player.closeInventory();
            }


        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(Item.interactItem(event, "Manipulation du sable"))  {
            Inventory inv = Bukkit.createInventory(null, 9, "Manipulation du sable");

            inv.setItem(3, new ItemBuilder(Material.IRON_SWORD).setName("§6Attaque").setLore("", "§f§l» §eCliquez-ici pour accéder au menu").toItemStack());
            inv.setItem(5, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setName("§6Défense").setLore("", "§f§l» §eCliquez-ici pour accéder au menu").toItemStack());

            player.openInventory(inv);
        }

        if(Item.interactItem(event, "Shukaku")) {
            if(shukakuCooldown > 0) {
                player.sendMessage(Messages.cooldown(shukakuCooldown));
                return;
            }

            player.sendMessage(prefix("Vous avez utilisé votre &aShukaka&f."));
            usingShukaku = true;

            Tasks.runAsyncLater(() -> usingShukaku = false, 5*20*60);
            shukakuCooldown = 20*60;
        }
    }

    @Override
    public void onAllPlayerItemInteract(PlayerInteractEvent event, Player player) {

        if(Role.isRole(player, NarutoRoles.GAARA)) {
            if(event.getClickedBlock() == null) {
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                return;
            }
            if(!this.blocks.contains(event.getClickedBlock())) {
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 5*20, 1, false, false));
        } else {
            if(event.getClickedBlock() == null) {
                player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                return;
            }
            if(!this.blocks.contains(event.getClickedBlock())) {
                player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5*20, 1, false, false));
        }
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if(usingArmure) {
            if(Role.getItemAmount(player, Material.SAND) < 5) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                usingArmure = false;
            } else {
                Role.removeItem(player, Material.SAND, 5);
            }
        }
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if(args[0].equalsIgnoreCase("fix")) {

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns fix <player>"));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if(this.fixCooldown > 0) {
                player.sendMessage(Messages.cooldown(this.fixCooldown));
                return;
            }

            if(target.getLocation().distance(player.getLocation()) > 30) {
                player.sendMessage(prefix("&cVous devez être à moins de 30 blocks de ce joueur."));
                return;
            }

            UUID targetUUID = target.getUniqueId();
            Location oldLocation = target.getLocation();

            target.teleport(manager.getJumpLocation());
            player.sendMessage(prefix("&fVous avez scellé &c" + target.getName() + " &fdans votre monde."));
            target.sendMessage(prefix("&cVous avez été scellé par Gaara. Si vous voulez sortir de cette zone avant 5 minutes"));

            fixCooldown = 15*60;

            new BukkitRunnable() {
                int timer = 5*60;
                public void teleport(Player player, Location location) {
                    player.teleport(location);
                    player.sendMessage(prefix("&fVous avez été &atéléporté &fdans le monde &anormal&f."));
                }
                @Override
                public void run() {

                    Player target = Bukkit.getPlayer(targetUUID);

                    if(timer <= 0) {
                        teleport(target, oldLocation);
                        cancel();
                    }

                    if(target.getLocation().distance(manager.getEndJumpLocation()) <= 2) {
                        timer = 1;
                    }

                    if(target.getLocation().getY() <= 7) {
                        target.teleport(manager.getJumpLocation());
                    }

                    timer--;
                }
            }.runTaskTimer(narutoUHC, 0, 20);
        }
    }

    public void useTsunami(Player player) {
        Location initialLocation = player.getLocation().clone();
        initialLocation.setPitch(0.0f);

        Vector direction = initialLocation.getDirection();

        List<List<Location>> shape = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            List<Location> line = new ArrayList<>();

            Vector front = direction.clone().multiply(i);

            line.add(initialLocation.clone().add(front));
            for (int j = 0; j <= 2; j++) {
                Vector right = this.getRightHeadDirection(player).multiply(j), left = this.getLeftHeadDirection(player).multiply(j);


                line.add(initialLocation.clone().add(front.clone().add(right)));
                line.add(initialLocation.clone().add(front.clone().add(left)));
            }
            shape.add(line);
        }


        new Wave(initialLocation.toVector(), shape);
    }

    private Vector getRightHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();
    }

    private Vector getLeftHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
    }

    public class Wave extends BukkitRunnable {

        private final Vector origin;
        private final List<List<Location>> shape;
        private int index;

        public Wave(Vector origin, List<List<Location>> shape) {
            this.origin = origin;
            this.shape = shape;
            this.start(2);
        }

        /**
         * Starts The Timer
         *
         * @param delay
         */
        private void start(int delay) {
            super.runTaskTimer(narutoUHC, 0, delay);
        }

        /**
         * Stops The Timer
         */
        protected void stop() {
            cancel();
        }

        @Override
        public void run() {
            if(index >= shape.size()){
                cancel();
                return;
            }
            for (Location loc : shape.get(index)) {
                FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, Material.SAND, (byte) 0);
                fb.setDropItem(false);
                fb.setHurtEntities(true);
                fb.setVelocity(new Vector(0, .3, 0));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if(WorldUtils.getDistanceBetweenTwoLocations(players.getLocation(), loc) <= 1){
                        Vector fromPlayerToTarget = players.getLocation().toVector().clone().subtract(origin);
                        fromPlayerToTarget.multiply(4); //6
                        fromPlayerToTarget.setY(1); // 2
                        players.setVelocity(fromPlayerToTarget);
                        players.damage(2D*2D);
                    }
                }
            }
            index++;
        }
    }

    public void useSarcophage(Player player) {
        Player target = this.getTargetPlayer(player, 10);
        if(target != null){
            Location min = target.getLocation().clone().subtract(2, 1, 2), max = target.getLocation().clone().add(2, 5, 2);

            Cuboid sarcophage = new Cuboid(min, max);
            sarcophage.getBlocks().forEach(block -> block.setType(Material.SAND));
        }
    }

    public Player getTargetPlayer(Player player, double distanceMax) {

        RayTrace rayTrace = new RayTrace(player.getEyeLocation().toVector(), player.getEyeLocation().getDirection());
        List<Vector> positions = rayTrace.traverse(distanceMax, 0.01D);
        for (int i = 0; i < positions.size(); i++) {
            Location position = positions.get(i).toLocation(player.getWorld());
            Collection<Entity> entities = player.getWorld().getNearbyEntities(position, 1.0D, 1.0D, 1.0D);
            for (Entity entity : entities) {
                if (entity instanceof Player && entity != player && rayTrace.intersects(new BoundingBox(entity), distanceMax, 0.01D))
                    return (Player) entity;
            }
        }
        return null;
    }

    public void useLance(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.DIAMOND_SWORD);
        itemBuilder.addEnchant(Enchantment.DAMAGE_ALL, 4);
        itemBuilder.setDurability((short) (Material.DIAMOND_SWORD.getMaxDurability() - 25));
        player.getInventory().addItem(itemBuilder.toItemStack());
    }

    public void useBouclier(Player player) {
        for(Block block : getBlocks(player.getLocation(), 5, false, true)) {
            block.setType(Material.SANDSTONE);
            this.blocks.add(block);
        }
    }

    public List<Block> getBlocks(Location center, int radius, boolean hollow, boolean sphere) {
        List<Location> locs = circle(center, radius, radius, hollow, sphere, 0);
        List<Block> blocks = new ArrayList<>();

        for (Location loc : locs) {
            blocks.add(loc.getBlock());
        }

        return blocks;
    }

    public List<Location> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY) {
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = (sphere ? cy - radius : cy); y < (sphere ? cy
                        + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z)
                            + (sphere ? (cy - y) * (cy - y) : 0);

                    if (dist < radius * radius
                            && !(hollow && dist < (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY,
                                z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }

    public void useArmure(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        usingArmure = true;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    @Override
    public Camp getCamp() {
        return Camp.SANKYODAI;
    }
}
