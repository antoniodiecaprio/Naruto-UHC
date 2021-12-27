package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ino extends NarutoRole {

    public static UUID transfer = null;
    public static List<UUID> nearPlayers = new ArrayList<>();
    public List<String> chosen = new ArrayList<>();
    public int chatCooldown = 0;
    public boolean usedTransfer = false;

    public NarutoRoles getRole() {
        return NarutoRoles.INO;
    }

    @Override
    public void resetCooldowns() {
        chatCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (chatCooldown > 0) {
            chatCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Ino";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    public void openInoMenu(Player player) {
        List<Player> players = new ArrayList<>();
        UHC.getUHC().getGameManager().getPlayers().stream().filter(e -> Bukkit.getPlayer(e) != null).map(Bukkit::getPlayer).forEach(players::add);

        int inventorySize;

        if (players.size() < 9) {
            inventorySize = 9;
        } else if (players.size() < 18) {
            inventorySize = 18;
        } else if (players.size() < 27) {
            inventorySize = 27;
        } else if (players.size() < 36) {
            inventorySize = 36;
        } else if (players.size() < 45) {
            inventorySize = 45;
        } else {
            inventorySize = 54;
        }

        Inventory inv = Bukkit.createInventory(null, inventorySize, "Ino");
        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(" ").toItemStack());
        int j = 1;
        for (Player player1 : players) {
            String lore;
            if (chosen.contains(player1.getName())) {
                lore = "§aCe joueur est selectionné";
            } else {
                lore = "§cCe joueur n'est pas selectionné";
            }
            inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM).setSkullOwner(player1.getName()).setDurability(SkullType.PLAYER.ordinal()).setName("§6" + player1.getName()).setLore(
                    lore,
                    " ",
                    "§8» §7Cliquez-ici pour changer"
            ).toItemStack());
            j++;
        }
        player.openInventory(inv);
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.SHIKAMARU);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Ino")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Ino")) {
            openInoMenu(player);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Ino")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getSlot() != 0 && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
                if (target == null) {
                    player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté."));
                    return;
                }
                if (chosen.contains(target.getName())) {
                    chosen.remove(target.getName());
                } else {
                    chosen.add(target.getName());
                }
                openInoMenu(player);
            }
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        if (transfer != null && transfer.equals(player.getUniqueId())) {
            player.sendMessage(CC.prefix("§fVous avez §c30 secondes §fpour utiliser votre dernière volontée."));
            TextComponent text = new TextComponent("§7▎ §aCliquez-ici pour commencer à la rédiger.");
            text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ns lastword "));
            player.spigot().sendMessage(text);
            Tasks.runLater(() -> transfer = null, 30 * 20);
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {

        if (args[0].equals("transfer")) {

            if (usedTransfer) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns transfer <player>"));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            transfer = target.getUniqueId();
            target.sendMessage(CC.prefix("§aIno §fa utilisé son pouvoir de transfert sur vous. De ce fait lors de votre mort, vous pourrez envoyer un message de dernière volontée. Attention, si vous mourrez dans §c20 minutes§f ou plus, vous ne pourrez pas utiliser cette dernière volontée."));
            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir sur §a" + target.getName()));
            usedTransfer = true;
            Tasks.runLater(() -> transfer = null, 20 * 20 * 60);
        }

        if (args[0].equalsIgnoreCase("chat")) {

            if (args.length < 2) {
                player.sendMessage(Messages.syntax("/ns chat <message>"));
                return;
            }

            if (chatCooldown > 0) {
                player.sendMessage(Messages.cooldown(chatCooldown));
                return;
            }

            if (chosen.size() == 0) {
                player.sendMessage(CC.prefix("§cIl n'y a personne dans votre liste."));
                return;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            chosen.stream().filter(s -> Bukkit.getPlayer(s) != null).map(Bukkit::getPlayer).forEach(target -> target.sendMessage(CC.prefix("§6Ino: §f" + message)));
            player.sendMessage(CC.prefix("§6Ino: §f" + message));

            chatCooldown = 10 * 60;

        }
    }
}
