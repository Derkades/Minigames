package xyz.derkades.minigames.worlds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldCreateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!sender.hasPermission("minigames.world.create")){
			return true;
		}

		if (args.length != 2) {
			return true;
		}

		if (args[0].equals("u")) {
			GameWorld.valueOf(args[1].toUpperCase()).unload();
			sender.sendMessage("unloaded");
			return true;
		}

		/*if (args.length == 2 && args[1].equals("d") && sender.hasPermission("minigames.world.delete")) {
			if (args[0].equalsIgnoreCase("minigames")) {
				sender.sendMessage("NOOOOOOOOOOOOOOOOOOO!!!!!!!!!!!!!!");
				return true;
			}

			final World world = Bukkit.getWorld(args[0]);
			Bukkit.getServer().unloadWorld(world, true);
			try {
				FileUtils.deleteDirectory(world.getWorldFolder());
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return true;
		}

		sender.sendMessage("creating world");



		sender.sendMessage("done");*/
		return true;
	}

}
