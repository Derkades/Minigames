package xyz.derkades.minigames;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import xyz.derkades.minigames.utils.Scheduler;

public class BugCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command arg1, final String label, final String[] args) {
		final Player player = (Player) sender;

		if (args.length < 3) {
			player.sendMessage("Please provide a longer description");
			return true;
		}

		Scheduler.async(() -> {
			try {
				String issueLabel;
				if (label.equalsIgnoreCase("bug")) {
					issueLabel = "bug";
				} else if (label.equalsIgnoreCase("ide")) {
					issueLabel = "enhancement";
				} else {
					issueLabel = null;
				}

				final String user = Minigames.getInstance().getConfig().getString("github-user");
				final String pass = Minigames.getInstance().getConfig().getString("github-password");
				final GitHub github = GitHub.connectUsingPassword(user, pass);
				final GHRepository repo = github.getRepository("Derkades/Minigames");
				final String description = String.join(" ", args);
				final GHIssue issue = repo.createIssue("[" + player.getName() + "] " + description).body(description + "").create();
				if (issueLabel == null) {
					issue.addLabels(repo.getLabel("bot"));
				} else {
					issue.addLabels(repo.getLabel("bot"), repo.getLabel(issueLabel));
				}
				player.sendMessage("your feedback has been noted.");
			} catch (final IOException e) {
				player.sendMessage("error. oh no.");
				e.printStackTrace();
			}
		});

		return true;
	}

}
