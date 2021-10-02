package derkades.minigames.games.buildcopy;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.Leaderboard;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import xyz.derkades.derkutils.ListUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BuildCopy extends Game<BuildCopyMap> {

	private static final Material[] MATERIALS = {
			Material.BIRCH_PLANKS,
			Material.END_STONE_BRICKS,
			Material.END_STONE,
			Material.SANDSTONE,
			Material.SAND,
	};

	private static final Set<Material> MATERIALS_SET = Set.of(MATERIALS);

	private static final String[] MATERIALS_VANILLA = {
			"birch_planks",
			"end_stone_bricks",
			"end_stone",
			"sandstone",
			"sand",
	};

	private static final ItemStack PICKAXE = new PaperItemBuilder(Material.IRON_PICKAXE)
			.canDestroyMinecraft(MATERIALS_VANILLA)
			.unbreakable()
			.create();

	public BuildCopy() {
		super(
				"buildcopy",
				"Build Copy",
				new String[] {
						"Copy a pattern of annoyingly similar looking blocks."
				},
				Material.SANDSTONE,
				new BuildCopyMap[] {
						new Prototype(),
				}
		);
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public int getDuration() {
		return 70;
	}

	private Map<UUID, Integer> positions = null;
	private Map<UUID, Material[]> currentPatterns = null;
	private Leaderboard leaderboard = null;

	private Material[] generatePattern() {
		final Material[] pattern = new Material[9];
		for (int i = 0; i < 9; i++) {
			pattern[i] = ListUtils.choice(MATERIALS);
		}
		return pattern;
	}

	@Override
	public void onPreStart() {
		this.positions = new HashMap<>();
		this.currentPatterns = new HashMap<>();
		this.leaderboard = new Leaderboard();

		for (int i = 0; i < this.map.getSupportedPlayerCount(); i++) {
			this.map.clearCopy(i);
		}

		int position = 0;
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getSpawnLocation(position));
			final Material[] pattern = generatePattern();
			this.map.buildOriginal(position, pattern);
			this.currentPatterns.put(player.getUniqueId(), pattern);
			this.positions.put(player.getUniqueId(), position);
			position++;
		}
	}

	private void giveItems(final MPlayer player) {
		player.clearInventory();
		final ItemStack[] items = new ItemStack[MATERIALS.length + 1];
		items[0] = PICKAXE;
		for (int i = 0; i < MATERIALS.length; i++) {
			items[i + 1] = new PaperItemBuilder(MATERIALS[i])
					.amount(64)
					.canPlaceOnMinecraft("gray_stained_glass")
					.create();
		}
		player.giveItem(items);
	}

	@Override
	public void onStart() {
		this.leaderboard.show();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			giveItems(player);
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		this.leaderboard.update(secondsLeft);
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false;
	}

	@Override
	public void onEnd() {
		endGame(this.leaderboard.getWinnersPrintHide());
		this.positions = null;
		this.currentPatterns = null;
		this.leaderboard = null;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlace(final BlockPlaceEvent event) {
		final MPlayer player = new MPlayer(event.getPlayer());
		if (!this.positions.containsKey(player.getUniqueId())) {
			Logger.warning("Position unknown for %s", player.getOriginalName());
			return;
		}

		final int position = this.positions.get(player.getUniqueId());
		if (this.map.checkCopy(position, this.currentPatterns.get(player.getUniqueId()))) {
			this.map.clearCopy(position);

			this.sendMessage(
					player.getDisplayName()
							.append(Component.text(" finished pattern ", NamedTextColor.GRAY))
							.append(Component.text(this.leaderboard.incrementAndGetScore(player), NamedTextColor.YELLOW))
							.append(Component.text('!', NamedTextColor.GRAY))
			);

			giveItems(player);

			final Material[] pattern = generatePattern();
			this.map.buildOriginal(position, pattern);
			this.currentPatterns.put(player.getUniqueId(), pattern);

			final Firework firework = this.map.getSpawnLocation(position).getWorld().spawn(this.map.getSpawnLocation(position), Firework.class);
			final FireworkMeta meta = firework.getFireworkMeta();
			meta.setPower(30);
			meta.addEffect(FireworkEffect.builder().withFlicker().withTrail().withColor(Color.RED, Color.ORANGE, Color.AQUA, Color.YELLOW).build());
			firework.setFireworkMeta(meta);
			firework.detonate();
		} else {
//			Logger.debug("Incorrect pattern for %s", player.getName());
		}
	}

	@EventHandler
	public void onPlace(final PlayerInteractEvent event) {
		Block clicked = event.getClickedBlock();
		if (event.getAction() != Action.LEFT_CLICK_BLOCK &&
				event.getPlayer().getInventory().getItemInMainHand().getType() == PICKAXE.getType() &&
				clicked != null &&
				MATERIALS_SET.contains(clicked.getType())) {
			clicked.setType(Material.AIR);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (this.positions.containsKey(player.getUniqueId())) {
			player.teleport(this.map.getSpawnLocation(this.positions.get(player.getUniqueId())));
		} else {
			player.spectator();
			player.teleport(this.map.getSpawnLocation(0));
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
