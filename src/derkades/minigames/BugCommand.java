package derkades.minigames;

import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.net.URL;

public class BugCommand implements CommandExecutor {

	@NotNull
	private static final Component SUBMITTING = Component.text("Submitting feedback...", NamedTextColor.GRAY);
	@NotNull
	private static final Component UNAVAILABLE = Component.text("Sorry, this command is temporarily unavailable.", NamedTextColor.GRAY);

	@Override
	public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command arg1, final @NotNull String label, final String[] args) {
		final Player player = (Player) sender;

		if (args.length < 3) {
			player.sendMessage("Please provide a longer description");
			return true;
		}

		sender.sendMessage(SUBMITTING);

		Scheduler.async(() -> {
			try {
//				String issueLabel;
//				if (label.equalsIgnoreCase("bug")) {
//					issueLabel = "bug";
//				} else if (label.equalsIgnoreCase("idea")) {
//					issueLabel = "enhancement";
//				} else {
//					issueLabel = null;
//				}

				if (!Minigames.getInstance().getConfig().isString("github-token")) {
					sender.sendMessage(UNAVAILABLE);
					return;
				}

				final GitHub github = GitHub.connectUsingOAuth(Minigames.getInstance().getConfig().getString("github-token"));
				final GHRepository repo = github.getRepository("Derkades/Minigames");
				final String description = String.join(" ", args);
				final GHIssue issue = repo.createIssue(description).body(description + "\n\n" + "This issue was submitted in-game by '" + player.getName() + "'.").create();
//				if (issueLabel == null) {
//					issue.addLabels(repo.getLabel("bot"));
//				} else {
//					issue.addLabels(repo.getLabel("bot"), repo.getLabel(issueLabel));
//				}

				URL url = issue.getUrl();

				player.sendMessage(Component.text()
						.append(Component.text("Thanks for letting us know. You can view your issue ")
								.color(NamedTextColor.GRAY))
						.append(Component.text("here").color(NamedTextColor.YELLOW)
								.hoverEvent(HoverEvent.showText(
										Component.text("Click to visit " + url.toExternalForm()).color(NamedTextColor.GRAY)))
								.clickEvent(ClickEvent.openUrl(url))
						.append(Component.text(".").color(NamedTextColor.GRAY))));
			} catch (final Exception e) {
				Logger.warning("%s encountered an issue while trying to create an issue", sender.getName());
				player.sendMessage("An error occured while trying to create an issue.");
				e.printStackTrace();
			}
		});

		return true;
	}

}
