package derkades.minigames.games;

import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@NotNull
	private final ChatColor color;
	@NotNull
	private final TextColor textColor;
	@NotNull
	private final Color bukkitColor;
	@NotNull
	private final String string;
	@NotNull
	private final Material glassBlock;
	@NotNull
	private final Material glassPane;
	@NotNull
	private final Material concrete;
	@NotNull
	private final Material terracotta;

	@SuppressWarnings("null")
	GameTeam(@NotNull final String name, final ChatColor color, @NotNull final String materialPrefix) {
		this.color = color;
		this.textColor = TextColor.color(color.getColor().getRGB());
		this.bukkitColor = Color.fromRGB(color.getColor().getRGB() & 0x00FFFFFF); // need to remove alpha bits or bukkit will complain
		this.string = color + "" + ChatColor.BOLD + name;
		this.glassBlock = Material.valueOf(materialPrefix + "_STAINED_GLASS");
		this.glassPane = Material.valueOf(materialPrefix + "_STAINED_GLASS_PANE");
		this.concrete = Material.valueOf(materialPrefix + "_CONCRETE");
		this.terracotta = Material.valueOf(materialPrefix + "_TERRACOTTA");
	}

	@Deprecated
	public @NotNull ChatColor getColor() {
		return this.color;
	}

	@NotNull
	public ChatColor getChatColor() {
		return this.color;
	}

	@NotNull
	public TextColor getTextColor() {
		return this.textColor;
	}

	@NotNull
	public Color getBukkitColor() {
		return this.bukkitColor;
	}

	@NotNull
	public String getDisplayName() {
		return this.string;
	}

	@NotNull
	public Component getColoredDisplayName() {
		return Component.text(this.getDisplayName(), this.getTextColor());
	}

	@Override
	public String toString() {
		return this.string;
	}

	@NotNull
	public Material getGlassBlock() {
		return this.glassBlock;
	}

	@NotNull
	public Material getGlassPane() {
		return this.glassPane;
	}

	@NotNull
	public Material getConcrete() {
		return this.concrete;
	}

	@NotNull
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

	@NotNull
	public static List<@NotNull GameTeam> getTeams(final int amount) {
		return ListUtils.chooseMultiple(GameTeam.values(), amount);
	}

}
