package xyz.derkades.minigames;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.JungleRun;
import xyz.derkades.minigames.games.Platform;
import xyz.derkades.minigames.games.RegeneratingSpleef;
import xyz.derkades.minigames.games.SaveTheSnowman;
import xyz.derkades.minigames.games.SnowFight;
import xyz.derkades.minigames.games.Speedrun;

public enum GameEnum {

	PLATFORM(new Platform()),
	JUNGLE_RUN(new JungleRun()),
	REGENERATING_SPLEEF(new RegeneratingSpleef()),
	SAVE_THE_SNOWMAN(new SaveTheSnowman()),
	SNOW_FIGHT(new SnowFight()),
	//PVP(new MazePvP()),
	//ELYTRA(new Elytra()),
	SPEEDRUN(new Speedrun()),
	//MINE(new Mine()),
	
	;
	
	private Game game;
	
	GameEnum(Game game){
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
	
	@Override
	public String toString(){
		return this.getGame().getName();
	}

}
