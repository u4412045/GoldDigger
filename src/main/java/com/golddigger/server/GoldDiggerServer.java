package com.golddigger.server;

import java.io.PrintWriter;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.services.Service;
/**
 * The GoldDiggerServer is the gateway between the commands from the competitors and the Game objects themselves.
 * @author Brett Wandel
 */
public class GoldDiggerServer extends GameServer{

	/**
	 * Checks to make sure that the Player in the url exists, and that they are in a game.
	 * After the checks, the url is passed to the games services.
	 * @param url The "command" from the competitors
	 * @param out The output to the competitor
	 */
	public void process(String url, PrintWriter out){
		String target = Service.parseURL(url, Service.URL_TARGET);
		String name = Service.parseURL(url, Service.URL_PLAYER);
		Player player = null;
		Game game = null;

		if (target == null){
			System.err.println("[GoldDiggerServer.java] Could not get the target from the url: "+url);
			return;
		} else if (target.equalsIgnoreCase("admin")) {
			//just skipping the rest of the checks
		} else if (name == null) {
			System.err.println("[GoldDiggerServer.java] Could not get a player from the url: "+url);
			return;
		} else if ((player = this.getPlayer(name)) == null){
			System.err.println("[GoldDiggerServer.java] No player by the name:"+name+". The URL:"+url);
			return;
		} else if ((game = this.getGame(player)) == null){
			System.err.println("[GoldDiggerServer.java] No game for player: "+name);
			return;
		}

		synchronized (this) {
			Service[] services = this.getServices();
			boolean consumed = false;
			for (Service service : services){
				if (service.caresAboutConsumption() && consumed){
					break; //skip this service
				} else if (service.runnable(url)){
					consumed = service.execute(url, out);
				}
			}
		}

		/*
		 * I removed the synchronized block as the Servlet should do it for us.
		 * TODO: move the manual synchronizing here
		 * Should synchronize on units instead, so they can control multiple units later on.
		 */
		if (game != null){
			Service[] services = game.getServices();
			boolean consumed = false;
			for (Service service : services){
				if (service.caresAboutConsumption() && consumed){
					break; //skip this service
				} else if (service.runnable(url)){
					consumed = service.execute(url, out);
				}
			}
		}
	}
}
