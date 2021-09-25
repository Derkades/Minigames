package derkades.minigames.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.reflection.ReflectionUtil;

public class ChatPoll extends Module {

	private static final long TOKEN_EXPIRE_TIME = 20*1000;
	private static final String COMMAND_NAME = "votepoll";

	private final Map<String, Long> tokens = new HashMap<>();
	private final Map<String, PollCallback> callbacks = new HashMap<>();

	public ChatPoll() {
		ReflectionUtil.registerCommand(Minigames.getInstance().getName(), new ChatPollCallbackCommand());
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

		public void send(final MPlayer player) {
			final String token = UUID.randomUUID().toString().replace("-", "");

			ChatPoll.this.tokens.put(token, System.currentTimeMillis());

			ChatPoll.this.callbacks.put(token, this.callback);

			player.sendChat(Component.text("-----------------------------------------", NamedTextColor.DARK_GRAY));
			player.sendPlainChat(this.question);

			final Component answerMessage = Component.empty();

			for (final PollAnswer answer : this.answers) {
				answerMessage.append(
						Component.text(String.format(" [%s] ", answer.displayName), answer.answerColor)
						.hoverEvent(HoverEvent.showText(Component.text(answer.hoverMessage, NamedTextColor.GRAY)))
						.clickEvent(ClickEvent.runCommand(String.format("/%s %s %s", COMMAND_NAME, token, answer.id)))
						);
			}

			player.sendChat(answerMessage);
			player.sendChat(Component.text("-----------------------------------------", NamedTextColor.DARK_GRAY));

		}
	}

	public static class PollAnswer {

		private final int id;
		private final String displayName;
		private final TextColor answerColor;
		private final String hoverMessage;

		public PollAnswer (final int id, final String displayName, final TextColor answerColor, final String hoverMessage){
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

	private class ChatPollCallbackCommand extends Command implements PluginIdentifiableCommand {

		protected ChatPollCallbackCommand() {
			super(COMMAND_NAME);
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

		@Override
		public @NotNull Plugin getPlugin() {
			return Minigames.getInstance();
		}

	}


}
