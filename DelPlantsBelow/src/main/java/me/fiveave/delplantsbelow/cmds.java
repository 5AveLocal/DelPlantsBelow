package me.fiveave.delplantsbelow;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.fiveave.delplantsbelow.main.header;
import static org.bukkit.Bukkit.getServer;

public class cmds implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        switch (args.length) {
            case 2:
            case 3:
                try {
                    int r = Integer.parseInt(args[0]);
                    if (r > 0) {
                        int count = 0;
                        for (int x = p.getLocation().getBlockX() - r; x <= p.getLocation().getBlockX() + r; x++) {
                            for (int y = p.getLocation().getBlockY() - 1; y >= 1; y--) {
                                for (int z = p.getLocation().getBlockZ() - r; z <= p.getLocation().getBlockZ() + r; z++) {
                                    Block blk = p.getWorld().getBlockAt(x, y, z);
                                    // List of blocks
                                    List<Material> blklist = new ArrayList<>(Arrays.asList(Material.GRASS, Material.TALL_GRASS,
                                            Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES, Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES,
                                            Material.FERN, Material.DEAD_BUSH, Material.COBWEB, Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP,
                                            Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_PAD, Material.LILY_OF_THE_VALLEY, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM,
                                            Material.MUSHROOM_STEM, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK, Material.SUGAR_CANE, Material.KELP, Material.KELP_PLANT, Material.SEAGRASS, Material.TALL_SEAGRASS, Material.BAMBOO, Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.LARGE_FERN, Material.VINE, Material.SUNFLOWER, Material.SNOW));
                                    if (args.length == 3) {
                                        String[] s = args[2].split(",");
                                        for (String str : s) {
                                            // Special blocks list
                                            if (str.equals("water") || str.equals("sand") || str.equals("gravel") || str.equals("clay") || str.equals("air") || str.equals("cave_air") || str.equals("ice")) {
                                                blklist.add(Material.valueOf(str.toUpperCase()));
                                            }
                                            // Log list
                                            if (str.equals("log")) {
                                                blklist.addAll(Arrays.asList(Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG));
                                            }
                                            // Mineshaft block list
                                            if (str.equals("mineshaft")) {
                                                blklist.addAll(Arrays.asList(Material.OAK_PLANKS, Material.OAK_FENCE, Material.RAIL, Material.SPAWNER));
                                            }
                                        }
                                    }
                                    // Get totally dark blocks only (check surrounding)
                                    int bright = 0;
                                    for (int x1 = x - 1; x1 <= x + 1; x1++) {
                                        for (int z1 = z - 1; z1 <= z + 1; z1++) {
                                            if (p.getWorld().getBlockAt(x1, blk.getLocation().getBlockY(), z1).getLightLevel() >= 2) {
                                                bright++;
                                            }
                                        }
                                    }
                                    Material oldmaterial = blk.getType();
                                    if (bright == 0 && blklist.contains(oldmaterial)) {
                                        Material newmaterial = Material.valueOf(args[1].toUpperCase());
                                        // Check CoreProtect enabled or not
                                        if (getServer().getPluginManager().getPlugin("CoreProtect") != null) {
                                            // Call CoreProtect API
                                            CoreProtectAPI api = getCoreProtect();
                                            // Ensure we have access to the API
                                            if (api != null) {
                                                api.logRemoval(p.getName(), blk.getLocation(), oldmaterial, blk.getBlockData());
                                                api.logPlacement(p.getName(), blk.getLocation(), newmaterial, blk.getBlockData());
                                            }
                                        }
                                        blk.setType(newmaterial);
                                        count++;
                                    }
                                }
                            }
                        }
                        p.sendMessage(header + ChatColor.GREEN + "Replacement finished." + ChatColor.GRAY + " (" + count + " block(s) have been replaced.)");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                break;
            default:
                p.sendMessage(header + ChatColor.RED + "Missing or too many arguments. Correct usage: /delplantsbelow <radius> <material> [water/log/sand/gravel/clay/air/cave_air/ice/mineshaft]");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ta = new ArrayList<>();
        List<String> result = new ArrayList<>();
        ta.add("");
        for (String a : ta) {
            if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                result.add(a);
            }
        }
        return result;
    }

    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
        // Check that CoreProtect is loaded
        if (!(plugin instanceof CoreProtect)) {
            return null;
        }
        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (!CoreProtect.isEnabled()) {
            return null;
        }
        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 9) {
            return null;
        }
        return CoreProtect;
    }
}
