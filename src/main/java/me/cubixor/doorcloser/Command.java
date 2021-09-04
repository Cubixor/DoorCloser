package me.cubixor.doorcloser;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

public class Command implements CommandExecutor {


    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("closedoors")) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You have to be player to do it!");
            return true;
        }

        if (!sender.hasPermission("doorcloser.use")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Wrong use!" + ChatColor.GRAY + " Type: /closedoors" + ChatColor.YELLOW + " <radius>");
            return true;
        }

        int playerRadius;
        try {
            playerRadius = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Radius must be a number!");
            return true;
        }

        Player player = (Player) sender;
        Block middle = player.getLocation().getBlock();
        int closedDoorsAmount = 0;


        for (int x = playerRadius; x >= -playerRadius; x--) {
            for (int y = playerRadius; y >= -playerRadius; y--) {
                for (int z = playerRadius; z >= -playerRadius; z--) {
                    Block block = middle.getRelative(x, y, z);
                    if (block.getType().toString().contains("DOOR") && !block.getType().toString().contains("TRAP")) {

                        try {
                            BlockData data = block.getBlockData();
                            Openable door = (Openable) data;
                            if (door.isOpen()) {
                                door.setOpen(false);
                                block.setBlockData(data);

                                playSound(block.getLocation());
                                closedDoorsAmount++;
                            }

                        } catch (Throwable e) {
                            BlockState state = block.getState();
                            Door door = (Door) state.getData();
                            if (door.isTopHalf()) {
                                state = block.getRelative(BlockFace.DOWN).getState();
                                door = (Door) state.getData();
                            }
                            if (door.isOpen()) {
                                door.setOpen(false);
                                state.setData(door);
                                state.update();

                                playSound(block.getLocation());
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

    private void playSound(Location location) {
        Sound sound;
        try {
            sound = Sound.valueOf("DOOR_CLOSE");
        } catch (Throwable throwable) {
            sound = Sound.valueOf("BLOCK_WOODEN_DOOR_CLOSE");
        }

        location.getWorld().playSound(location, sound, 1, 1);

    }
}
