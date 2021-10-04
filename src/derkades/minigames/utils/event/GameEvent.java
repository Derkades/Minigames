package derkades.minigames.utils.event;

import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;
import org.bukkit.event.Event;

public abstract class GameEvent extends Event {

    private final Game<? extends GameMap> game;

    public GameEvent(Game<? extends GameMap> game) {
        this.game = game;
    }

    public Game<? extends GameMap> getGame() {
        return game;
    }

}
