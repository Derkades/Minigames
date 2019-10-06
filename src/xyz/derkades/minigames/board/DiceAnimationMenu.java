package xyz.derkades.minigames.board;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
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

	private static final int MAX_ITERATIONS = ANIMATION_DURATION_SECONDS * 20 / ANIMATION_INTERVAL_TICKS;
	private static final int FINAL_ITERATIONS = FINAL_STATIC_SECONDS * 20 / ANIMATION_INTERVAL_TICKS;

	private static final Minigames plugin = Minigames.getInstance();

	private AnimationTimer timer;

	private final int min;
	private final int max;
	private final int endResult;

	public DiceAnimationMenu(final Player player, final int min, final int max, final int endResult) {
		super(plugin, "Die", 6*9, player);
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

		//ItemStack item = new ItemBuilder(Material.GRAY_DYE).amount(number).create();

		Material material;
		final List<Integer> slots = new ArrayList<>();

		if (number == 1) {
			material = Material.RED_CONCRETE;
			slots.add(4);
			slots.add(12);
			slots.add(13);
			slots.add(22);
			slots.add(31);
			slots.add(40);
			slots.add(49);
			slots.add(48);
			slots.add(50);
		} else if (number == 2) {
			material = Material.ORANGE_CONCRETE;
			slots.add(11);
			slots.add(3);
			slots.add(4);
			slots.add(5);
			slots.add(15);
			slots.add(23);
			slots.add(31);
			slots.add(39);
			slots.add(47);
			slots.add(48);
			slots.add(49);
			slots.add(50);
			slots.add(51);
		} else if (number == 3) {
			material = Material.YELLOW_CONCRETE;
			slots.add(3);
			slots.add(4);
			slots.add(5);
			slots.add(15);
			slots.add(21);
			slots.add(22);
			slots.add(23);
			slots.add(33);
			slots.add(42);
			slots.add(48);
			slots.add(49);
			slots.add(50);
		} else if (number == 4) {
			material = Material.LIME_CONCRETE;
			slots.add(5);
			slots.add(13);
			slots.add(14);
			slots.add(21);
			slots.add(23);
			slots.add(29);
			slots.add(30);
			slots.add(31);
			slots.add(32);
			slots.add(33);
			slots.add(41);
			slots.add(50);
		} else if (number == 5) {
			material = Material.GREEN_CONCRETE;
			slots.add(2);
			slots.add(3);
			slots.add(4);
			slots.add(5);
			slots.add(6);
			slots.add(11);
			slots.add(20);
			slots.add(21);
			slots.add(22);
			slots.add(23);
			slots.add(33);
			slots.add(42);
			slots.add(47);
			slots.add(48);
			slots.add(49);
			slots.add(50);
		} else if (number == 6) {
			material = Material.LIGHT_BLUE_CONCRETE;
			slots.add(3);
			slots.add(4);
			slots.add(5);
			slots.add(11);
			slots.add(20);
			slots.add(21);
			slots.add(22);
			slots.add(23);
			slots.add(29);
			slots.add(33);
			slots.add(38);
			slots.add(42);
			slots.add(48);
			slots.add(49);
			slots.add(50);
		} else if (number == 7) {
			material = Material.BLUE_CONCRETE;
			slots.add(2);
			slots.add(3);
			slots.add(4);
			slots.add(5);
			slots.add(6);
			slots.add(15);
			slots.add(23);
			slots.add(32);
			slots.add(40);
			slots.add(49);
		} else if (number == 8) {
			material = Material.PINK_CONCRETE;
			slots.add(3);
			slots.add(4);
			slots.add(5);
			slots.add(11);
			slots.add(15);
			slots.add(21);
			slots.add(22);
			slots.add(23);
			slots.add(29);
			slots.add(33);
			slots.add(38);
			slots.add(42);
			slots.add(48);
			slots.add(49);
			slots.add(50);
		} else if (number == 9) {
			material = Material.MAGENTA_CONCRETE;
			slots.add(3);
			slots.add(4);
			slots.add(5);
			slots.add(11);
			slots.add(15);
			slots.add(20);
			slots.add(24);
			slots.add(30);
			slots.add(31);
			slots.add(32);
			slots.add(33);
			slots.add(42);
			slots.add(48);
			slots.add(49);
			slots.add(50);
		} else if (number == 10) {
			material = Material.PURPLE_CONCRETE;
			slots.add(2);
			slots.add(10);
			slots.add(11);
			slots.add(20);
			slots.add(29);
			slots.add(38);
			slots.add(46);
			slots.add(47);
			slots.add(48);
			slots.add(6);
			slots.add(14);
			slots.add(16);
			slots.add(23);
			slots.add(25);
			slots.add(32);
			slots.add(34);
			slots.add(41);
			slots.add(43);
			slots.add(51);
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

			if (this.i > MAX_ITERATIONS) {
				return; // static final result
			}

			int random;
			do {
				random = Random.getRandomInteger(DiceAnimationMenu.this.min, DiceAnimationMenu.this.max);
			} while(random == this.previous);

			this.previous = random;
			DiceAnimationMenu.this.displayNumber(random);
		}
	}

}
