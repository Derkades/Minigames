package derkades.minigames.games;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;

import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public enum GameTeam {

	RED("Red", ChatColor.RED, "RED"),
	BLUE("Blue", ChatColor.BLUE, "BLUE"),
	GREEN("Green", ChatColor.DARK_GREEN, "GREEN"),
	ORANGE("Orange", ChatColor.GOLD, "ORANGE"),
	PURPLE("Purple", ChatColor.DARK_PURPLE, "PURPLE"),
	LIGHT_BLUE("Light Blue", ChatColor.BLUE, "LIGHT_BLUE"),
	YELLOW("Yellow", ChatColor.YELLOW, "YELLOW"),
	PINK("Pink", ChatColor.LIGHT_PURPLE, "PINK"),
	LIME("Lime", ChatColor.GREEN, "LIME"),
	WHITE("White", ChatColor.WHITE, "WHITE"),

	;

	private ChatColor color;
	private TextColor textColor;
	private Color bukkitColor;
	private String string;
	private Material glassBlock;
	private Material glassPane;
	private Material concrete;
	private Material terracotta;

	GameTeam(final String name, final ChatColor color, final String materialPrefix) {
		this.color = color;
		this.textColor = TextColor.color(color.getColor().getRGB());
		this.bukkitColor = Color.fromRGB(color.getColor().getRGB() & 0x00FFFFFF); // need to remove alpha bits or bukkit will complain
		this.string = color + "" + ChatColor.BOLD + this.name();
		this.glassBlock = Material.valueOf(materialPrefix + "_STAINED_GLASS");
		this.glassPane = Material.valueOf(materialPrefix + "_STAINED_GLASS_PANE");
		this.concrete = Material.valueOf(materialPrefix + "_CONCRETE");
		this.terracotta = Material.valueOf(materialPrefix + "_TERRACOTTA");
	}

	@Deprecated
	public ChatColor getColor() {
		return this.color;
	}

	public ChatColor getChatColor() {
		return this.color;
	}

	public TextColor getTextColor() {
		return this.textColor;
	}

	public Color getBukkitColor() {
		return this.bukkitColor;
	}

	public String getDisplayName() {
		return this.string;
	}

	@Override
	public String toString() {
		return this.string;
	}

	public Material getGlassBlock() {
		return this.glassBlock;
	}

	public Material getGlassPane() {
		return this.glassPane;
	}

	public Material getConcrete() {
		return this.concrete;
	}

	public Material getTerracotta() {
		return this.terracotta;
	}

	public void equipArmor(final MPlayer player) {
		player.setArmor(
				new ItemBuilder(Material.LEATHER_HELMET).leatherArmorColor(this.getBukkitColor()).create(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).leatherArmorColor(this.getBukkitColor()).create(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).leatherArmorColor(this.getBukkitColor()).create(),
				new ItemBuilder(Material.LEATHER_BOOTS).leatherArmorColor(this.getBukkitColor()).create()
				);
	}

	private static final Map<Material, GameTeam> BY_MATERIAL = new HashMap<>();

	static {
		for (final GameTeam team : GameTeam.values()) {
			BY_MATERIAL.put(team.getGlassBlock(), team);
			BY_MATERIAL.put(team.getGlassPane(), team);
			BY_MATERIAL.put(team.getConcrete(), team);
			BY_MATERIAL.put(team.getTerracotta(), team);
		}
	}

	public static GameTeam fromMaterial(final Material material) {
		return BY_MATERIAL.get(material);
	}

	public static List<GameTeam> getTeams(final int amount) {
//		final GameTeam[] array = GameTeam.values();
//		Validate.isTrue(amount <= array.length, "Requested too many teams");
//		final List<GameTeam> teams = new ArrayList<>(array.length);
//		for (final GameTeam team : array) {
//			teams.add(team);
//		}
//		Collections.shuffle(teams);
//		while (teams.size() > amount) {
//			teams.remove(teams.size() - 1);
//		}
//		return Collections.unmodifiableList(teams);
		return ListUtils.chooseMultiple(GameTeam.values(), amount);
	}

}
