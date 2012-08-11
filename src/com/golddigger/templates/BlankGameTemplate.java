package com.golddigger.templates;

import com.golddigger.model.BlankMap;
import com.golddigger.model.Game;
import com.golddigger.server.GameTemplate;
import com.golddigger.services.CarryingService;
import com.golddigger.services.DropService;
import com.golddigger.services.GoldService;
import com.golddigger.services.GrabService;
import com.golddigger.services.MoveService;
import com.golddigger.services.ScoreService;
import com.golddigger.services.ViewService;

public class BlankGameTemplate extends GameTemplate {

	@Override
	public Game build(){
		Game game = new Game(getID());
		game.setMap(new BlankMap(10, 10));
		
		addServices(game);
		return game;
	}
	
	public void addServices(Game game){
		game.add(new ViewService());
		game.add(new MoveService());
		game.add(new GoldService());
//		game.add(new GrabService());
//		game.add(new DropService());
//		game.add(new ScoreService());
//		game.add(new CarryingService());
	}
}
