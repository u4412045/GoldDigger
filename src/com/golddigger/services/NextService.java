package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.server.GameService;
/**
 * This service will progress the player on to the next map.
 * Will return: <br />
 * <ul>
 * 	<li>"FAILED" if there is still gold on the map</li>
 * 	<li>"FAILED" if the player's unit is still carrying gold</li>
 * </ul>
 * @author Brett Wandel
 * @see Player
 * @see Unit
 */
public class NextService extends GameService {
	public static final String ACTION_TEXT = "next";
	public NextService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).contentEquals(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		String name = parseURL(url, URL_PLAYER);
		Player player;
		Unit unit;
		
		if ((player = game.getPlayer(name))== null){
			out.println("ERROR: Invalid Player Given");
			return true;
		}

		if ((unit = game.getUnit(player)) == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}

		synchronized (game){
			if (unit.getGold() != 0){
				out.println("FAILED");
			} else if (game.getMap().hasGoldLeft()) {
				out.println("FAILED");
			} else {
//				server.progress(player);
				out.println("OK");
			}
		}
		return true;
	}

}
