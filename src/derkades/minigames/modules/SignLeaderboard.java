package derkades.minigames.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import derkades.minigames.Logger;
import derkades.minigames.Minigames;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignLeaderboard {

    private static final int FIRST_GAME_NUMBER = 0;
    private static final Map<UUID, Integer> PLAYER_WINS = new HashMap<>();

    private void loadFromJson() {
        final int lastGameNumber = Minigames.getInstance().getConfig().getInt("last-game-number");

        for (int i = FIRST_GAME_NUMBER; i < lastGameNumber; i++) {
            final File file = new File("game_results", i + ".json");
            try (final Reader reader = new FileReader(file)) {
                final JsonObject json = (JsonObject) JsonParser.parseReader(reader);
                final JsonArray winners = json.getAsJsonArray("winners");
                for (final JsonElement winner : winners) {
                    final UUID uuid = UUID.fromString(winner.getAsJsonObject().get("uuid").getAsString());
                    PLAYER_WINS.putIfAbsent(uuid, 0);
                    PLAYER_WINS.put(uuid, PLAYER_WINS.get(uuid) + 1);
                }
            } catch (final IOException e) {
                Logger.warning("Failed to read game result file %s", file.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    // TODO event to increment PLAYER_WINS live, without reloading from disk
}
