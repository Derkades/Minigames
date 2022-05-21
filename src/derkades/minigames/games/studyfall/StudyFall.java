package derkades.minigames.games.studyfall;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.sidebar.Sidebar;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StudyFall extends Game<StudyFallMap> {

	private static final int NO_RECORD = 1000;

	public StudyFall() {
		super(
				"studyfall",
				"Study Fall",
				new String[] {},
				Material.DRIED_KELP_BLOCK,
				new StudyFallMap[] {
						new Prototype()
				},
				2,
				30,
				EnumSet.of(GameLabel.BLOCKS, GameLabel.SINGLEPLAYER)
		);
	}

	private Sidebar sidebar;
	private int record;
	private Set<BlockCoordinates> placedBlocks;
	private Set<UUID> skipCheck;
	private Set<UUID> recordHolders;

	@Override
	protected void onPreStart() {
		this.sidebar = new Sidebar(Component.text("Scores"));
		this.record = NO_RECORD;
		this.placedBlocks = new HashSet<>();
		this.skipCheck = new HashSet<>();
		this.recordHolders = new HashSet<>();
		map.clearGlass();

		for (MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(map.getPreSpawnLocation(), p -> player.setGameMode(GameMode.SPECTATOR));
		}
	}

	@Override
	protected void onStart() {
		ItemStack placeableBlock = new ItemBuilder(Material.GLASS)
				.canPlaceOnMinecraft("dried_kelp_block", "red_terracotta")
				.create();

		for (MPlayer player : Minigames.getOnlinePlayers()) {
			this.sidebar.showTo(player.bukkit());
			player.queueTeleport(map.getSpawnLocation(), p -> {
				player.setGameMode(GameMode.ADVENTURE);
				player.giveItem(placeableBlock);
			});
		}
	}

	@Override
	public int gameTimer(int secondsLeft) {
		for (MPlayer player : Minigames.getOnlinePlayers()) {
			if (skipCheck.contains(player.getUniqueId())) {
				continue;
			}

			Block block = player.getBlockOn();
			switch(block.getType()) {
				case GLASS -> {
					skipCheck.add(player.getUniqueId());
					BlockCoordinates coords = BlockCoordinates.fromBlock(block);
					if (placedBlocks.contains(coords)) {
						sendMessage(player.getDisplayName().append(Component.text(" landed on someone else's block.", NamedTextColor.GRAY)));
						this.sidebar.addEntry(player.getDisplayName().append(Component.text(" DEAD", NamedTextColor.RED)));
						player.dieTo(map.getPreSpawnLocation());
					} else {
						this.placedBlocks.add(coords);
						int score = player.getLocation().getBlockY();
						if (score < this.record) {
							if (this.record == NO_RECORD) {
								sendMessage(player.getDisplayName().append(Component.text(" set a new record of " + score + "!", NamedTextColor.GRAY)));
							} else {
								sendMessage(player.getDisplayName().append(Component.text(" set a new score of " + score + ", beating the previous record of " + this.record + "!", NamedTextColor.GRAY)));
								this.recordHolders.clear();
							}
							this.record = score;
							this.recordHolders.add(player.getUniqueId());
						} else if (score == this.record) {
							sendMessage(player.getDisplayName().append(Component.text(" also scored " + score + ".", NamedTextColor.GRAY)));
							this.recordHolders.add(player.getUniqueId());
						}
						this.sidebar.addEntry(player.getDisplayName().append(Component.text(" " + score, NamedTextColor.GRAY)));
					}
				}
				case MAGMA_BLOCK -> {
					skipCheck.add(player.getUniqueId());
					this.sidebar.addEntry(player.getDisplayName().append(Component.text(" DEAD", NamedTextColor.RED)));
					player.dieTo(map.getPreSpawnLocation());
				}
			}
		}
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return Utils.allPlayersIn(skipCheck);
	}

	@Override
	protected void onEnd() {
		super.endGame(this.recordHolders);

		for (Player player : Bukkit.getOnlinePlayers()) {
			this.sidebar.hideFrom(player);
		}
		this.sidebar = null;
		this.placedBlocks = null;
		this.skipCheck = null;
		this.recordHolders = null;
	}

	@Override
	public void onPlayerJoin(MPlayer player) {
		this.skipCheck.add(player.getUniqueId());
		player.spectator();
		player.queueTeleport(map.getPreSpawnLocation());
	}

	@Override
	public void onPlayerQuit(MPlayer player) {

	}

	private record BlockCoordinates(int x, int y, int z) {

		static BlockCoordinates fromBlock(Block block) {
			return new BlockCoordinates(block.getX(), block.getY(), block.getZ());
		}

	}

}
