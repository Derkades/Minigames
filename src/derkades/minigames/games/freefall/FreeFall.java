package derkades.minigames.games.freefall;

public class FreeFall {/*extends Game<FreeFallMap> {

	private static final FreeFallMap[] MAPS = {
			new Prototype(),
	};

	@Override
	public @NotNull String getIdentifier() {
		return "free_fall";
	}

	@Override
	public @NotNull String getName() {
		return "Free Fall";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"On every layer, choose the correct platform",
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.DIAMOND_BOOTS;
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public FreeFallMap[] getGameMaps() {
		return MAPS;
	}

	@Override
	public int getDuration() {
		return this.map.getLayers().length * 10 + 10;
	}

	private int layerHeight;
	private Set<UUID> winners;

	@Override
	public void onPreStart() {
		this.layerHeight = this.map.getLayers().length - 1;

		for (final Layer layer : this.map.getLayers()) {
			layer.placeBlocks();
		}

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(new Location(this.map.getWorld(), 0, 242, 0));
			player.setDisableDamage(false);
		}
	}

	@Override
	public void onStart() {

	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (this.layerHeight == 0) {
			return Math.min(secondsLeft, 2);
		}

		if (secondsLeft % 10 == 0) {
			final Layer layer = this.map.getLayers()[this.layerHeight];
			final Hole correctHole = layer.setRandomCorrectHole();
			layer.placeFluid();
			layer.removeBlocks();

			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				if (correctHole.isInHole(player)) {
					player.sendActionBar(Component.text("You chose the correct hole", NamedTextColor.GREEN));
				} else {
					player.sendActionBar(Component.text("You chose the wrong hole", NamedTextColor.RED));
				}
			}
			return secondsLeft;
		}

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false;
	}

	@Override
	public void onEnd() {
		this.endGame(this.winners);
		this.winners = null;
	}

	@EventHandler
	public void onDamage(MPlayerDamageEvent event){
		event.setCancelled(event.getDamagerPlayer() != null);
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();
		player.removeFire();

		if (event.getCause() == DamageCause.FALL){
			event.setCancelled(true);
			return;
		}

		if (event.willBeDead()) {
			event.setCancelled(true);
			this.winners.remove(player.getUniqueId());
			player.die();
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getSpawnLocation());
		player.spectator();
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.winners.remove(player.getUniqueId());
	}*/

}
