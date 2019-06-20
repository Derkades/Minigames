package xyz.derkades.minigames.games;

public class Speedrun {/*extends Game {

	Speedrun() {
		super("Speedrun", new String[] {
				"Jump to the finish with super speed"
		}, 1, SpeedrunMap.MAPS);
	}

	private List<UUID> finished;

	private boolean NO_ONE_FINISHED = true;

	private SpeedrunMap map;

	@Override
	void begin(final GameMap genericMap){
		this.finished = new ArrayList<>();
		this.NO_ONE_FINISHED = true;
		this.map = (SpeedrunMap) genericMap;

		for (final Player player : Bukkit.getOnlinePlayers()){
			player.teleport(this.map.getStartLocation());
			Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 30);
		}

		this.timer();
	}

	private void timer(){
		new BukkitRunnable(){
			@Override
			public void run(){
				Speedrun.this.sendMessage("5 seconds left!");
				new BukkitRunnable(){
					@Override
					public void run(){
						Speedrun.this.endGame(Utils.getPlayerListFromUUIDList(Speedrun.this.finished));
					}
				}.runTaskLater(Minigames.getInstance(), 5*20);
			}
		}.runTaskLater(Minigames.getInstance(), 40*20);
	}

	private void playerDie(final Player player){
		player.teleport(this.map.getStartLocation());
	}

	private void playerWin(final Player player){
		Utils.clearPotionEffects(player);
		if (this.NO_ONE_FINISHED){
			this.NO_ONE_FINISHED = false;
			super.sendMessage(player.getName() + " finished first and got an extra point!");
			Points.addPoints(player, 1);
			this.finished.add(player.getUniqueId());
		} else {
			super.sendMessage(player.getName() + " has finished!");
			this.finished.add(player.getUniqueId());
		}
		player.teleport(this.map.getSpectatorLocation());
		player.setAllowFlight(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final Player player = event.getPlayer();

		if (this.finished.contains(player.getUniqueId())){
			return;
		}

		if (player.isSneaking()){
			player.sendMessage(ChatColor.RED + "You cannot sneak!");
			this.playerDie(player);
			return;
		}

		if (player.isSprinting()){
			player.sendMessage(ChatColor.RED + "You cannot sprint!");
			this.playerDie(player);
			return;
		}

		final Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
		final Material type = block.getType();
		if (type == this.map.getFloorBlock()){
			this.playerDie(player);
		} else if(type == this.map.getEndBlock()){
			this.playerWin(player);
		}
	}*/

}
