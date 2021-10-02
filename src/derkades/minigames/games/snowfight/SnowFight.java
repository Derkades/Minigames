package derkades.minigames.games.snowfight;

@Deprecated
public class SnowFight {/*extends Game {

	private static final int MAX_DURATION = 70;

	SnowFight() {
		super("Snow Fight", new String[] {
				"Kill other players using snowballs. Get snowballs",
				"by breaking snow with your shovel.",
				"The player with the most kills wins.",
		}, 2, SnowFightMap.MAPS);
	}

	private List<UUID> dead;
	private SnowFightMap map;

	private Map<UUID, Integer> kills;

	private Sidebar sidebar;

	@Override
	void begin(final GameMap genericMap) {
		this.dead = new ArrayList<>();
		this.map = (SnowFightMap) genericMap;
		this.kills = new HashMap<>();

		this.sidebar = new Sidebar(ChatColor.DARK_AQUA + "" + ChatColor.DARK_AQUA + "Kills",
				Minigames.getInstance(), Integer.MAX_VALUE, new SidebarString[] {new SidebarString("Loading...")});

		Utils.setGameRule("doTileDrops", false);

		for (final Player player : Bukkit.getOnlinePlayers()){
			player.teleport(this.map.getSpawnLocation());

			this.kills.put(player.getUniqueId(), 0);

			Minigames.setCanTakeDamage(player, true);
		}

		final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.unbreakable()
				.name("Snow Shoveler")
				.lore("Break snow to get snowballs")
				.canDestroy("snow")
				.create();

		shovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);

		new GameTimer(this, MAX_DURATION, 2) {

			@Override
			public void onStart() {
				for (final Player player : Bukkit.getOnlinePlayers()) {
					player.getInventory().addItem(shovel);
					SnowFight.this.sidebar.showTo(player);
					Minigames.setCanTakeDamage(player, true);
				}
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				SnowFight.this.updateSidebar(secondsLeft);

				//End the game if everyone is a spectator except one player (or everyone is a spectator)
				if (SnowFight.this.dead.size() >= (Bukkit.getOnlinePlayers().size() - 1) && secondsLeft > 2) {
					return 2;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				for (final Player player : Bukkit.getOnlinePlayers()) {
					SnowFight.this.sidebar.hideFrom(player);
				}

				SnowFight.this.endGame(Utils.getWinnersFromPointsHashmap(SnowFight.this.kills));
			}

		};
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if (event.getEntity().getLastDamageCause().getCause() == DamageCause.PROJECTILE) {
			event.setDamage(4);
		} else {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event){
		//Block block = event.getBlock();

		event.setCancelled(true);

		//if (block.getType() != Material.SNOW){
		//	return;
		//}

		final Player player = event.getPlayer();
		final Inventory inv = player.getInventory();

		if (!inv.contains(new ItemStack(Material.SNOWBALL, 16))) {
			int amount = Random.getRandomInteger(1, 3);

			if (amount + inv.getItem(0).getAmount() > 16) {
				amount = 16;
			}

			inv.addItem(new ItemStack(Material.SNOWBALL, amount));
		}

		//Snow snow = (Snow) block.getBlockData();

		//new BukkitRunnable() {
		//	public void run() {
		//		snow.setLayers(Random.getRandomInteger(1, 4));
		//	}
		//}.runTaskLater(Minigames.getInstance(), 3 * 20);
	}

	@EventHandler
	public void onKill(final PlayerDeathEvent event){
		final Player killer = event.getEntity().getKiller();

		this.kills.put(killer.getUniqueId(), this.kills.get(killer.getUniqueId()) + 1);

		if (event.getEntityType() == EntityType.PLAYER){
			final Player player = event.getEntity().getPlayer();
			//event.setDeathMessage(DARK_GRAY + "[" + DARK_AQUA + getName() + DARK_GRAY + "] " + AQUA + killer.getName() + " has killed " + pn + "!");
			event.setDeathMessage("");
			this.sendMessage(killer.getName() + " has killed " + player.getName() + "!");

			Scheduler.delay(1, () -> {
				player.spigot().respawn();
				player.teleport(Var.LOBBY_LOCATION);

				//Utils.hideForEveryoneElse(player);
				//Utils.giveInvisibility(player);

				this.dead.add(player.getUniqueId());
				Utils.clearInventory(player);
				Minigames.setCanTakeDamage(player, false);
			});
		}
	}

	private void updateSidebar(final int secondsLeft) {
		this.kills = Utils.sortByValue(this.kills);

		final List<SidebarString> sidebarStrings = new ArrayList<>();

		for (final Player player : Bukkit.getOnlinePlayers()) {
			try {
				final int points = this.kills.get(player.getUniqueId());
				sidebarStrings.add(new SidebarString(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points));
			} catch (final NullPointerException e) { continue; }
		}

		this.sidebar.setEntries(sidebarStrings)
			.addEmpty()
			.addEntry(new SidebarString(ChatColor.GRAY + "Time left: " + secondsLeft + " seconds."))
			.update();
	}*/

}
