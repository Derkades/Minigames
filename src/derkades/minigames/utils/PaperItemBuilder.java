package derkades.minigames.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.AbstractItemBuilder;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaperItemBuilder extends AbstractItemBuilder<PaperItemBuilder> {

	public PaperItemBuilder(@NotNull Material material) {
		super(material);
	}

	public PaperItemBuilder(@NotNull ItemStack item) {
		super(item);
	}

	@Override
	public @NotNull PaperItemBuilder getInstance() {
		return this;
	}

	public PaperItemBuilder canPlaceOnMinecraft(String... canPlaceOn) {
		ItemMeta meta = item.getItemMeta();
		meta.setPlaceableKeys(Arrays.stream(canPlaceOn).map(NamespacedKey::minecraft).collect(Collectors.toList()));
		item.setItemMeta(meta);
		return this;
	}

	public PaperItemBuilder canDestroyMinecraft(String... canDestroy) {
		ItemMeta meta = item.getItemMeta();
		meta.setDestroyableKeys(Arrays.stream(canDestroy).map(NamespacedKey::minecraft).collect(Collectors.toList()));
		item.setItemMeta(meta);
		return this;
	}

	public PaperItemBuilder itemFlags(ItemFlag... flags) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(flags);
		item.setItemMeta(meta);
		return this;
	}
	
	@NotNull
	public PaperItemBuilder skullTexture(@NotNull String skullTexture) {
		PlayerProfile profile = Bukkit.getServer().createProfile(UUID.randomUUID());
		profile.setProperty(new ProfileProperty("textures", skullTexture));
		return this.skullProfile(profile);
	}

	@NotNull
	public PaperItemBuilder skullProfile(@NotNull PlayerProfile profile) {
		if (item.getItemMeta() instanceof SkullMeta meta) {
			meta.setPlayerProfile(profile);
			item.setItemMeta(meta);
			return this;
		} else {
			throw new IllegalStateException("Not a skull");
		}
	}

}
