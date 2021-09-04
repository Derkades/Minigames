package derkades.minigames.games.teams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import xyz.derkades.derkutils.ListUtils;

public enum GameTeam {

	RED(ChatColor.RED, Material.RED_STAINED_GLASS, Material.RED_CONCRETE, Material.RED_TERRACOTTA),
	BLUE(ChatColor.BLUE, Material.BLUE_STAINED_GLASS, Material.BLUE_CONCRETE, Material.BLUE_TERRACOTTA),
	GREEN(ChatColor.GREEN, Material.LIME_STAINED_GLASS, Material.LIME_CONCRETE, Material.LIME_TERRACOTTA),
	ORANGE(ChatColor.GOLD, Material.ORANGE_STAINED_GLASS, Material.ORANGE_CONCRETE, Material.ORANGE_TERRACOTTA),
	PURPLE(ChatColor.DARK_PURPLE, Material.PURPLE_STAINED_GLASS, Material.PURPLE_CONCRETE, Material.PURPLE_TERRACOTTA),
	LIGHT_BLUE(ChatColor.BLUE, Material.LIGHT_BLUE_STAINED_GLASS, Material.LIGHT_BLUE_CONCRETE, Material.LIGHT_BLUE_TERRACOTTA),
	YELLOW(ChatColor.YELLOW, Material.YELLOW_STAINED_GLASS, Material.YELLOW_CONCRETE, Material.YELLOW_TERRACOTTA),
	PINK(ChatColor.LIGHT_PURPLE, Material.PINK_STAINED_GLASS, Material.PINK_CONCRETE, Material.PINK_TERRACOTTA),
	LIME(ChatColor.GREEN, Material.LIME_STAINED_GLASS, Material.LIME_CONCRETE, Material.LIME_TERRACOTTA),
	WHITE(ChatColor.WHITE, Material.WHITE_STAINED_GLASS, Material.WHITE_CONCRETE, Material.WHITE_TERRACOTTA),

	;

	private ChatColor color;
	private String string;
	private Material glassBlock;
	private Material glassPane;
	private Material concrete;
	private Material terracotta;

	GameTeam(final ChatColor color, final Material glassBlock, final Material concrete, final Material terracotta) {
		this.color = color;
		this.string = color + "" + ChatColor.BOLD + this.name();
		this.glassBlock = glassBlock;
		this.glassPane = Material.valueOf(glassBlock.name() + "_PANE");
		this.concrete = concrete;
		this.terracotta = terracotta;
	}

	public ChatColor getColor() {
		return this.color;
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
