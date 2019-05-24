package xyz.derkades.minigames;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class BugCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command arg1, final String arg2, final String[] args) {
		Player player = (Player) sender;
		try {
			final String user = Minigames.getInstance().getConfig().getString("github-user");
			final String pass = Minigames.getInstance().getConfig().getString("github-password");
			final GitHub github = GitHub.connectUsingPassword(user, pass);
			final GHRepository repo = github.getRepository("Derkades/Minigames");
			final String description = String.join(" ", args);
			final GHIssue issue = repo.createIssue("[" + player.getName() + "] " + description).body(description + "").create();
			issue.addLabels(repo.getLabel("bot"));
			player.sendMessage("your feedback has been noted.");
		} catch (final IOException e) {
			player.sendMessage("error. oh no.");
			e.printStackTrace();
		}
		
		return true;
	}

}
