package xyz.derkades.minigames.games;

@Deprecated
public class Elytra {/*extends Game {

	public static final int GAME_DURATION = 30;
	public static final int PRE_START_TIME = 3;

	Elytra() {
		super("Elytra", new String[]{
				"Fly to the end of the cave without touching",
				"the ground or lava."
		}, 2, null);
	}

	private final List<UUID> dead = new ArrayList<>();
	private final List<UUID> finished = new ArrayList<>();

	private boolean listener = false;

	@Override
	void begin(final GameMap genericMap) {
		Utils.delayedTeleport(new Location(Var.WORLD, 163.5, 76.5, 339.5, 120, 25), Bukkit.getOnlinePlayers());

		this.listener = false;

		for (final Player player : Bukkit.getOnlinePlayers()){
			player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
			Utils.giveInfiniteEffect(player, PotionEffectType.SLOW, 5);
			Utils.giveInfiniteEffect(player, PotionEffectType.INVISIBILITY, 2);
		}

		new GameTimer(this, GAME_DURATION, PRE_START_TIME) {

			@Override
			public void onStart() {
				// game starting, remove slowness
				Utils.clearPotionEffects();
				Elytra.this.listener = true;
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (Bukkit.getOnlinePlayers().size() == (Elytra.this.dead.size() + Elytra.this.finished.size()) && secondsLeft > 2) {
					return 2;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				Elytra.super.endGame(Utils.getPlayerListFromUUIDList(Elytra.this.finished));
			}

		};
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
		if (!this.listener) {
			return;
		}

		final Player player = event.getPlayer();
		final Material type = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

		if(!player.isFlying() && (type == Material.STONE || type == Material.COBBLESTONE || type == Material.LAVA)) {
			// die
			Utils.clearInventory(player);
			player.teleport(new Location(Var.WORLD, 151.5, 76, 343.5));
			this.sendMessage(player.getName() + " has died");

			if (!this.dead.contains(player.getUniqueId()))
				this.dead.add(player.getUniqueId());
		}

		if (type == Material.LIME_WOOL) {
			// win
			Utils.clearInventory(player);
			player.teleport(new Location(Var.WORLD, 151.5, 76, 343.5));

			if (!this.finished.contains(player.getUniqueId()))
				this.finished.add(player.getUniqueId());

			this.sendMessage(player.getName() + " has finished");
		}
	}*/

}
