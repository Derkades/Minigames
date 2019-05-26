package xyz.derkades.minigames;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class ChatPoll {

	private static final long TOKEN_EXPIRE_TIME = 10*1000;

	private static Map<String, Long> tokens;
	private static Map<String, PollCallback> callbacks;

	static void startup(final JavaPlugin plugin) {
		tokens = new HashMap<>();
		callbacks = new HashMap<>();
		plugin.getCommand("vote").setExecutor(new ChatPollCallbackCommand());
	}

	public static void sendPoll(final Player player, final Poll poll) {
		final String token = UUID.randomUUID().toString().replace("-", "");

		tokens.put(token, System.currentTimeMillis());

		callbacks.put(token, poll.callback);

		player.sendMessage(ChatColor.GRAY + "----------------------------------------");
		player.spigot().sendMessage(new ComponentBuilder(poll.question).create());

		final ComponentBuilder answerMessage = new ComponentBuilder("");

		for (final PollAnswer answer : poll.answers) {
			answerMessage.append(String.format(" [%s] ", answer.displayName))
				.color(answer.answerColor)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(answer.hoverMessage).color(ChatColor.GRAY).create()))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/vote %s %s", token, answer.id)));
		}

		player.spigot().sendMessage(answerMessage.create());
		player.sendMessage(ChatColor.GRAY + "----------------------------------------");

	}

	public static class Poll {

		private final String question;
		private final PollCallback callback;
		private final PollAnswer[] answers;

		public Poll (final String question, final PollCallback callback, final PollAnswer... answers){
			this.question = question;
			this.callback = callback;
			this.answers = answers;
		}

	}

	public static class PollAnswer {

		private final int id;
		private final String displayName;
		private final ChatColor answerColor;
		private final String hoverMessage;

		public PollAnswer (final int id, final String displayName, final ChatColor answerColor, final String hoverMessage){
			this.id = id;
			this.displayName = displayName;
			this.answerColor = answerColor;
			this.hoverMessage = hoverMessage;
		}

	}

	public abstract static class PollCallback {

		public abstract void callback(Player player, int option);

	}

	private static class ChatPollCallbackCommand implements CommandExecutor {

		@Override
		public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
			if (args.length != 2) {
				return true;
			}

			final String providedToken = args[0];

			if (!tokens.containsKey(providedToken)) {
				sender.sendMessage(ChatColor.RED + "You have already voted on this poll.");
				return true;
			}

			if (System.currentTimeMillis() - tokens.get(providedToken) > TOKEN_EXPIRE_TIME) {
				sender.sendMessage(ChatColor.RED + "This poll has expired.");
			}

			tokens.remove(providedToken);

			final Player player = (Player) sender;

			callbacks.get(providedToken).callback(player, Integer.parseInt(args[1]));

			return true;
		}

	}


}