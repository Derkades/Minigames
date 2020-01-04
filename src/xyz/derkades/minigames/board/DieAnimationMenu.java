package xyz.derkades.minigames.board;

import java.util.Arrays;

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
import xyz.derkades.minigames.constants.BoardConfig;

public class DieAnimationMenu extends IconMenu {

	static final int TOTAL_OPEN_TICKS = BoardConfig.DIE_MENU_DURATION_SECONDS + BoardConfig.DIE_MENU_FINAL_STATIC_SECONDS * 20;

	private static final int MAX_ITERATIONS = BoardConfig.DIE_MENU_DURATION_SECONDS * 20 / BoardConfig.DIE_MENU_INTERVAL_TICKS;
	private static final int FINAL_ITERATIONS = BoardConfig.DIE_MENU_FINAL_STATIC_SECONDS * 20 / BoardConfig.DIE_MENU_INTERVAL_TICKS;

	private static final Minigames plugin = Minigames.getInstance();

	private final AnimationTimer timer;

	private final int min;
	private final int max;
	private final int endResult;

	public DieAnimationMenu(final BoardPlayer player, final int min, final int max, final int endResult) {
		super(plugin, "Die", 6, player.bukkit());
		this.min = min;
		this.max = max;
		this.endResult = endResult;

		this.timer = new AnimationTimer();
		this.timer.runTaskTimer(plugin, 0, BoardConfig.DIE_MENU_INTERVAL_TICKS);
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
		Material material;
		int[] slots;

		// Please note: These arrays must be ordered for the binary search below to work
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

		for (int slot = 0; slot < this.getSize(); slot++) {
			if (Arrays.binarySearch(slots, slot) >= 0) {
				if (!this.hasItem(slot)) {
					this.addItem(slot, item);
				}
			} else {
				if (this.hasItem(slot)) {
					this.addItem(slot, null);
				}
			}
		}

//		for (final int i : slots) {
//			this.items.put(i, item);
//		}

//		refreshItems();
	}

	private class AnimationTimer extends BukkitRunnable {

		int previous = -1;
		int i = -1;

		@Override
		public void run() {
			this.i++;

			if (this.i >= MAX_ITERATIONS + FINAL_ITERATIONS) {
				close();
				return;
			}

			if (this.i == MAX_ITERATIONS) {
				displayNumber(DieAnimationMenu.this.endResult);
				return;
			}

			if (this.i > MAX_ITERATIONS) {
				return; // static final result
			}

			int random;
			do {
				random = Random.getRandomInteger(DieAnimationMenu.this.min, DieAnimationMenu.this.max);
			} while(random == this.previous);

			this.previous = random;
			displayNumber(random);
		}
	}

}
