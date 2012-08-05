package com.golddigger.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.golddigger.model.Game;
import com.golddigger.model.Player;

public class GoldDiggerServlet extends HttpServlet {
	private static final long serialVersionUID = 1219399141770957347L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURI();
		
		String name = Service.parseURL(url, Service.URL_PLAYER);
		Player player = AppContext.getPlayer(name);
		if (player == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Game game = AppContext.getGame(player);
		if (game == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		synchronized (player) { //should stop the same player executing concurrently
			Service[] services = game.getServices();
			boolean consumed = false;
			for (Service service : services){
				if (service.caresAboutConsumption() && consumed){
					break;
				} else if (service.runnable(url)){
					consumed = service.execute(url, resp.getWriter());
				} else {
				}
			}
		}
	}
}
