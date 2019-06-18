package xyz.derkades.minigames.worlds;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(final World world, final Random random, final int x, final int z, final BiomeGrid biome) {
		final ChunkData data = this.createChunkData(world);
//		if (x == 0 && z == 0) {
//			data.setRegion(0, 64, 0, 16, 64, 16, Material.STONE);
//		}
		return data;
	}
}
