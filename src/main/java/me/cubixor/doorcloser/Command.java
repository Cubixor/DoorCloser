package me.cubixor.doorcloser;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("closedoors")) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You have to be player to do it!");
        }

        if (!sender.hasPermission("closedoors")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Wrong usage! Use /closedoor <radius>");
            return true;
        }

        try {
            Integer.parseInt(args[0]);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Radius must be a number!");
            return true;
        }

        // All door types list
        List<Material> doorTypes = new ArrayList<>();
        doorTypes.add(Material.DARK_OAK_DOOR);
        doorTypes.add(Material.ACACIA_DOOR);
        doorTypes.add(Material.SPRUCE_DOOR);
        doorTypes.add(Material.OAK_DOOR);
        doorTypes.add(Material.BIRCH_DOOR);
        doorTypes.add(Material.JUNGLE_DOOR);
        doorTypes.add(Material.IRON_DOOR);

        Player player = (Player) sender;
        int playerRadius = Integer.parseInt(args[0]);
        Block middle = player.getLocation().getBlock();
        int closedDoorsAmount = 0;

        // Searching for doors
        for (int x = playerRadius; x >= -playerRadius; x--) {
            for (int y = playerRadius; y >= -playerRadius; y--) {
                for (int z = playerRadius; z >= -playerRadius; z--) {
                    if (doorTypes.contains(middle.getRelative(x, y, z).getType())) {

                        Block doorBlock = middle.getRelative(x, y, z);
                        BlockData data = doorBlock.getBlockData();

                        // Closing doors
                        if (data instanceof Openable) {
                            if (((Openable) data).isOpen()) {
                                ((Openable) data).setOpen(false);
                                doorBlock.setBlockData(data);
                                if (doorBlock.getType().equals(Material.IRON_DOOR)) {
                                    doorBlock.getWorld().playSound(doorBlock.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1F, 1F);
                                } else {
                                    doorBlock.getWorld().playSound(doorBlock.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, SoundCategory.BLOCKS, 1F, 1F);
                                }
                                closedDoorsAmount++;
                            }
                        }
                    }
                }
            }
        }

        if (closedDoorsAmount == 0) {
            sender.sendMessage(ChatColor.RED + "There was no doors to close in specified radius!");
        } else {
            sender.sendMessage(ChatColor.GREEN + "You have successfully closed " + ChatColor.YELLOW + closedDoorsAmount + ChatColor.GREEN + " doors in specified radius!");
        }
        return true;
    }
}
