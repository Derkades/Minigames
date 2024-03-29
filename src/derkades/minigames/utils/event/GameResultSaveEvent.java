package derkades.minigames.utils.event;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.google.gson.stream.JsonWriter;

import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;

public class GameResultSaveEvent extends GameEvent {

    @NotNull
    private static final HandlerList handlers = new HandlerList();

    private final Set<Player> winners;
    private final JsonWriter gameResultWriter;

    public GameResultSaveEvent(Game<? extends GameMap> game, Set<Player> winners, JsonWriter gameResultWriter) {
        super(game);
        this.winners = winners;
        this.gameResultWriter = gameResultWriter;
    }

    public Set<Player> getWinners() {
        return winners;
    }

    public JsonWriter getResultWriter() {
        return this.gameResultWriter;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
