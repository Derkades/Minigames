package derkades.minigames.modules;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.reflection.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatPoll extends Module {

	private static final long TOKEN_EXPIRE_TIME = 20*1000;
	private static final String COMMAND_NAME = "votepoll";

	private final Map<UUID, Long> pollSendTimes = new HashMap<>();
	private final Map<UUID, PollCallback> callbacks = new HashMap<>();

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
			final UUID token = UUID.randomUUID();

			Logger.debug("new poll with token %s", token);

			ChatPoll.this.pollSendTimes.put(token, System.currentTimeMillis());
			ChatPoll.this.callbacks.put(token, this.callback);

			player.sendChat(Component.text("-----------------------------------------", NamedTextColor.DARK_GRAY));
			player.sendPlainChat(this.question);

			Component answerMessage = Component.empty();

			for (final PollAnswer answer : this.answers) {
				answerMessage = answerMessage.append(
						Component.text(String.format(" [%s] ", answer.displayName), answer.answerColor)
						.hoverEvent(HoverEvent.showText(Component.text(answer.hoverMessage, NamedTextColor.GRAY)))
						.clickEvent(ClickEvent.runCommand(String.format("/%s %s %s", COMMAND_NAME, token, answer.id)))
						);
			}

			player.sendChat(answerMessage);
			player.sendChat(Component.text("-----------------------------------------", NamedTextColor.DARK_GRAY));
		}
	}

	public record PollAnswer(int id, String displayName, TextColor answerColor,
							 String hoverMessage) {

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
		public boolean execute(final @NotNull CommandSender sender, final @NotNull String commandLabel, final String[] args) {
			if (args.length != 2) {
				return true;
			}

			UUID providedToken;
			try {
				providedToken = UUID.fromString(args[0]);
			} catch (Exception e) {
				sender.sendMessage("invalid token");
				e.printStackTrace();
				return true;
			}

			Logger.debug("received vote with token %s", providedToken);

			if (!ChatPoll.this.pollSendTimes.containsKey(providedToken)) {
				sender.sendMessage(ChatColor.RED + "You have already voted on this poll.");
				return true;
			}

			if (System.currentTimeMillis() - ChatPoll.this.pollSendTimes.get(providedToken) > TOKEN_EXPIRE_TIME) {
				sender.sendMessage(ChatColor.RED + "This poll has expired.");
				return true;
			}

			ChatPoll.this.pollSendTimes.remove(providedToken);

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
