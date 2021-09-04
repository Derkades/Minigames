package derkades.minigames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

public class BugCommand implements CommandExecutor {

	private static final String SUBMITTING = Utils.getChatPrefix(ChatColor.AQUA, 'P') + "Submitting feedback..";

	private static final String UNAVAILABLE = Utils.getChatPrefix(ChatColor.AQUA, 'P') + "Sorry, this command is temporarily unavailable.";

	private static final Content CLICK_TO_VISIT = new Text(
			new ComponentBuilder("Click to visit https://github.com/Derkades/Minigames/issues")
			.color(ChatColor.GRAY).create());

	private static final BaseComponent[] THANKS = new ComponentBuilder("")
			.appendLegacy(Utils.getChatPrefix(ChatColor.AQUA, 'P') + "Thanks for letting us know. You can view all open issues ")
			.append("here.")
			.color(ChatColor.YELLOW)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, CLICK_TO_VISIT))
			.event(new ClickEvent(ClickEvent.Action.OPEN_URL,
					"https://github.com/Derkades/Minigames/issues"))
			.create();

	@Override
	public boolean onCommand(final CommandSender sender, final Command arg1, final String label, final String[] args) {
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
				/*final GHIssue issue = */repo.createIssue(description).body(description + "\n\n" + "This issue was submitted in-game by '" + player.getName() + "'.").create();
//				if (issueLabel == null) {
//					issue.addLabels(repo.getLabel("bot"));
//				} else {
//					issue.addLabels(repo.getLabel("bot"), repo.getLabel(issueLabel));
//				}

				player.spigot().sendMessage(THANKS);
			} catch (final Exception e) {
				Logger.warning("%s encountered an issue while trying to create an issue", sender.getName());
				player.sendMessage("An error occured while trying to create an issue.");
				e.printStackTrace();
			}
		});

		return true;
	}

}
