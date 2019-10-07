package xyz.derkades.minigames.board;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.Random;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.MenuCloseEvent;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Minigames.ShutdownReason;

public class DiceAnimationMenu extends IconMenu {

	private static final int ANIMATION_INTERVAL_TICKS = 5;
	private static final int ANIMATION_DURATION_SECONDS = 3;
	private static final int FINAL_STATIC_SECONDS = 2;
	static final int TOTAL_OPEN_TICKS = ANIMATION_DURATION_SECONDS + FINAL_STATIC_SECONDS * 20;

	private static final int MAX_ITERATIONS = ANIMATION_DURATION_SECONDS * 20 / ANIMATION_INTERVAL_TICKS;
	private static final int FINAL_ITERATIONS = FINAL_STATIC_SECONDS * 20 / ANIMATION_INTERVAL_TICKS;

	private static final Minigames plugin = Minigames.getInstance();

	private AnimationTimer timer;

	private final int min;
	private final int max;
	private final int endResult;

	public DiceAnimationMenu(final BoardPlayer player, final int min, final int max, final int endResult) {
		super(plugin, "Die", 6*9, player.bukkit());
		this.min = min;
		this.max = max;
		this.endResult = endResult;
	}

	@Override
	public void open() {
		this.timer = new AnimationTimer();
		this.timer.runTaskTimer(plugin, 0, ANIMATION_INTERVAL_TICKS);
		super.open();
	}

	@Override
	public void onClose(final MenuCloseEvent event) {
		this.timer.cancel();
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		return false;
	}

	private void displayNumber(final int number) {
		this.items.clear();

		Material material;
		int[] slots;

		if (number == 1) {
			material = Material.RED_CONCRETE;
			slots = new int[] {4, 12, 13, 22, 31, 40, 49, 48, 50};
		} else if (number == 2) {
			material = Material.ORANGE_CONCRETE;
			slots = new int[] {11, 3, 4, 5, 15, 23, 31, 39, 47, 48, 49, 50, 51};
		} else if (number == 3) {
			material = Material.YELLOW_CONCRETE;
			slots = new int[] {3, 4, 5, 15, 21, 22, 23, 33, 42, 48, 49, 50};
		} else if (number == 4) {
			material = Material.LIME_CONCRETE;
			slots = new int[] {5, 13, 14, 21, 23, 29, 30, 31, 32, 33, 41, 50};
		} else if (number == 5) {
			material = Material.GREEN_CONCRETE;
			slots = new int[] {2, 3, 4, 5, 6, 11, 20, 21, 22, 23, 33, 42, 47, 48, 49, 50};
		} else if (number == 6) {
			material = Material.LIGHT_BLUE_CONCRETE;
			slots = new int[] {3, 4, 5, 11, 20, 21, 22, 23, 29, 33, 38, 42, 48, 49, 50};
		} else if (number == 7) {
			material = Material.BLUE_CONCRETE;
			slots = new int[] {2, 3, 4, 5, 6, 15, 23, 32, 40, 49};
		} else if (number == 8) {
			material = Material.PINK_CONCRETE;
			slots = new int[] {3, 4, 5, 11, 15, 21, 22, 23, 29, 33, 38, 42, 48, 49, 50};
		} else if (number == 9) {
			material = Material.MAGENTA_CONCRETE;
			slots = new int[] {3, 4, 5, 11, 15, 20, 24, 30, 31, 32, 33, 42, 48, 49, 50};
		} else if (number == 10) {
			material = Material.PURPLE_CONCRETE;
			slots = new int[] {2, 10, 11, 20, 29, 38, 46, 47, 48, 6, 14, 16, 23, 25, 32, 34, 41, 43, 51};
		} else {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Invalid random number");
			throw new RuntimeException("oh nee!!");
		}

		final ItemStack item = new ItemBuilder(material).name(" ").create();

		for (final int i : slots) {
			this.items.put(i, item);
		}

		this.refreshItems();
	}

	private class AnimationTimer extends BukkitRunnable {

		int previous = -1;
		int i = -1;

		@Override
		public void run() {
			this.i++;

			if (this.i >= MAX_ITERATIONS + FINAL_ITERATIONS) {
				DiceAnimationMenu.this.close();
				return;
			}

			if (this.i == MAX_ITERATIONS) {
				DiceAnimationMenu.this.displayNumber(DiceAnimationMenu.this.endResult);
				return;
			}

			if (this.i > MAX_ITERATIONS)
				return; // static final result

			int random;
			do {
				random = Random.getRandomInteger(DiceAnimationMenu.this.min, DiceAnimationMenu.this.max);
			} while(random == this.previous);

			this.previous = random;
			DiceAnimationMenu.this.displayNumber(random);
		}
	}

}
