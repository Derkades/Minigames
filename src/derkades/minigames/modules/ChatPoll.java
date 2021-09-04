package derkades.minigames.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import derkades.minigames.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import xyz.derkades.derkutils.bukkit.reflection.ReflectionUtil;

public class ChatPoll extends Module {

	private static final long TOKEN_EXPIRE_TIME = 20*1000;
	private static final String COMMAND_NAME = "dm90ZSEh";

	private final Map<String, Long> tokens = new HashMap<>();
	private final Map<String, PollCallback> callbacks = new HashMap<>();

	public ChatPoll() {
		ReflectionUtil.registerCommand(COMMAND_NAME, new ChatPollCallbackCommand(COMMAND_NAME));
	}

	public class Poll {

		private final String question;
		private final PollCallback callback;
		private final PollAnswer[] answers;

		public Poll(final String question, final PollCallback callback, final PollAnswer... answers){
			this.question = question;
			this.callback = callback;
			this.answers = answers;
		}

		public void send(final Player player) {
			final String token = UUID.randomUUID().toString().replace("-", "");

			ChatPoll.this.tokens.put(token, System.currentTimeMillis());

			ChatPoll.this.callbacks.put(token, this.callback);

			player.sendMessage(Utils.getChatPrefix(ChatColor.AQUA, 'P') + ChatColor.DARK_GRAY + "-----------------------------------------");
			player.spigot().sendMessage(new ComponentBuilder("").appendLegacy(Utils.getChatPrefix(ChatColor.AQUA, 'P')).append(this.question).create());

			final ComponentBuilder answerMessage = new ComponentBuilder("");

			answerMessage.appendLegacy(Utils.getChatPrefix(ChatColor.AQUA, 'P'));

			for (final PollAnswer answer : this.answers) {
				answerMessage.append(String.format(" [%s] ", answer.displayName))
					.color(answer.answerColor)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder(answer.hoverMessage).color(ChatColor.GRAY).create())))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/dm90ZSEh %s %s", token, answer.id)));
			}

			player.spigot().sendMessage(answerMessage.create());
			player.sendMessage(Utils.getChatPrefix(ChatColor.AQUA, 'P') + ChatColor.DARK_GRAY + "-----------------------------------------");

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

	@FunctionalInterface
	public interface PollCallback {

		void callback(Player player, int option);

	}

	private class ChatPollCallbackCommand extends Command {

		protected ChatPollCallbackCommand(final String name) {
			super(name);
		}

		@Override
		public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
			if (args.length != 2) {
				return true;
			}

			final String providedToken = args[0];

			if (!ChatPoll.this.tokens.containsKey(providedToken)) {
				sender.sendMessage(ChatColor.RED + "You have already voted on this poll.");
				return true;
			}

			if (System.currentTimeMillis() - ChatPoll.this.tokens.get(providedToken) > TOKEN_EXPIRE_TIME) {
				sender.sendMessage(ChatColor.RED + "This poll has expired.");
				return true;
			}

			ChatPoll.this.tokens.remove(providedToken);

			final Player player = (Player) sender;

			ChatPoll.this.callbacks.get(providedToken).callback(player, Integer.parseInt(args[1]));

			return true;
		}

	}


}
