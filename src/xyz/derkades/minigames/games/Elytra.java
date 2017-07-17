package xyz.derkades.minigames.games;

@Deprecated
public class Elytra /*extends Game*/ {
/*
	@Deprecated
	private Map<String, Boolean> isDead = new HashMap<>();
	private Map<String, Boolean> hasFinished = new HashMap<>();
	
	@Override
	String[] getDescription() {
		return new String[]{
				"Fly to the end of the cave without",
				"touching the ground. Good luck!"
		};
	}

	@Override
	public String getName() {
		return "Elytra";
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public GamePoints getPoints() {
		return new GamePoints(2, 5);
	}

	@Override
	public void resetHashMaps(Player player) {
		isDead.put(player.getName(), false);
		hasFinished.put(player.getName(), false);
	}

	@Override
	void begin() {
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 163.5, 76.5, 339.5, 120, 25));
			player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
			Utils.giveInfiniteEffect(player, PotionEffectType.SLOW, 2);
			Utils.giveInfiniteEffect(player, PotionEffectType.INVISIBILITY, 2);
		}
		
		new BukkitRunnable(){
			public void run(){
				for (Player player : Bukkit.getOnlinePlayers())
					Utils.clearPotionEffects(player);
			}
		}.runTaskLater(Main.getInstance(), 20);
		
		timer();
	}
	
	private void timer(){
		Scheduler.runTaskLater(13*20, new Runnable(){
			public void run(){
				sendMessage("5 seconds left!");
				Scheduler.runTaskLater(5*20, new Runnable(){
					public void run(){
						endGame(Arrays.asList());
					}
				});
			}
		});
	}
	
	private void endGame(List<Player> winners){
		super.startNextGame(Utils.getWinnersFromFinishedHashMap(hasFinished));
	}
	
	private void playerDie(Player player){
		Utils.clearInventory(player);
		player.teleport(new Location(Var.WORLD, 151.5, 76, 343.5));
	}
	
	private void playerWin(Player player){
		Utils.clearInventory(player);
		player.teleport(new Location(Var.WORLD, 151.5, 76, 343.5));
		hasFinished.put(player.getName(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		if (!super.isRunning()) return;
		
		Player player = event.getPlayer();
		Material type = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if((type == Material.STONE || 
				type == Material.COBBLESTONE ||
				type == Material.LAVA ||
				type == Material.STATIONARY_LAVA) &&
				!player.isFlying())
			playerDie(player);
			
		if (type == Material.WOOL)
			playerWin(player);
	}*/

}
