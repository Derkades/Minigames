package com.robinmc.minigames;

import com.robinmc.minigames.games.Elytra;
import com.robinmc.minigames.games.Game;
import com.robinmc.minigames.games.JungleRun;
import com.robinmc.minigames.games.Platform;
import com.robinmc.minigames.games.RegeneratingSpleef;
import com.robinmc.minigames.games.SaveTheSnowman;
import com.robinmc.minigames.games.SnowFight;
import com.robinmc.minigames.games.Speedrun;

public enum GameEnum {

	PLATFORM(new Platform()),
	JUNGLE_RUN(new JungleRun()),
	REGENERATING_SPLEEF(new RegeneratingSpleef()),
	SAVE_THE_SNOWMAN(new SaveTheSnowman()),
	SNOW_FIGHT(new SnowFight()),
	//PVP(new MazePvP()),
	ELYTRA(new Elytra()),
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
