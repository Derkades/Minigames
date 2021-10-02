package derkades.minigames.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.AbstractItemBuilder;

import java.util.Arrays;
import java.util.List;
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
		item.editMeta(meta -> meta.setPlaceableKeys(Arrays.stream(canPlaceOn).map(NamespacedKey::minecraft).collect(Collectors.toList())));
		return this;
	}

	public PaperItemBuilder canDestroyMinecraft(String... canDestroy) {
		item.editMeta(meta -> meta.setDestroyableKeys(Arrays.stream(canDestroy).map(NamespacedKey::minecraft).collect(Collectors.toList())));
		return this;
	}

	public PaperItemBuilder itemFlags(ItemFlag... flags) {
		item.editMeta(meta -> meta.addItemFlags(flags));
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
		item.editMeta(SkullMeta.class, meta -> meta.setPlayerProfile(profile));
		return this;
	}

	@NotNull
	public PaperItemBuilder nameAdventure(@Nullable Component name) {
		item.editMeta(meta -> meta.displayName(name));
		return this;
	}

	@NotNull
	public PaperItemBuilder loreAdventure(@Nullable List<Component> lore) {
		item.editMeta(meta -> meta.lore(lore));
		return this;
	}

}
