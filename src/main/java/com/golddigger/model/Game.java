package com.golddigger.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.golddigger.services.GameService;
import com.golddigger.utils.Container;
import com.golddigger.model.tiles.BaseTile;

/**
 * This is the object that contains all the needed information about a game.
 * 
 * <p>Each game contains its own services, which allow the services
 * to customized for each game. These services are the actual objects that modify
 * the data in the game, and this class is simply a container for all the data needed. They are
 * what actually contain the "business logic" behind how a game is run.</p>
 * 
 * @author Brett Wandel
 * @see Service
 */
public class Game {
	/**
	 * All the services that this game uses.
	 * @see Service
	 */
	private Container<GameService> services;

	/**
	 * All the players in this game
	 * @see Player
	 */
	private List<Player> players;

	/**
	 * All the units in this game
	 * @see Unit
	 */
	private List<Unit> units;

	/**
	 * The "field" or map of this game
	 * @see Map
	 */
	private Map map;

	/**
	 * Used to determine what game or "level" the player is currently up to.
	 */
	private int templateID;

	public Game(int templateID){
		this.templateID = templateID;
		services = new Container<GameService>();
		players = new ArrayList<Player>();
		units = new ArrayList<Unit>();
	}

	/**
	 * The if of the {@link GameTemplate} this game was generated from.
	 * @return The position of the GameTemplate in the server's template queue
	 */
	public int getTemplateID(){
		return this.templateID;
	}

	/**
	 * Add a {@link Service} to the game
	 * @param service The service to be added
	 * @see Service
	 */
	public void add(GameService service){
		service.setGame(this);
		this.services.add(service);
		Collections.sort(services);
	}

	/**
	 * @return the services used by this game.
	 * @see Service
	 */
	public GameService[] getServices(){
		return this.services.toArray(new GameService[]{});
	}

	/**
	 * Returns all the services of a particular class
	 * @param classOfPlugin the class of the service, must extend {@link GameService}
	 * @see GameService
	 */
	public <T extends GameService> List<T> getServices(Class<T> classOfService){
		return services.filter(classOfService);
	}

	/**
	 * Check to see if a {@link Player} is currently in this game.
	 * @param player The Player to check for
	 * @return <b>true</b> if they are, <false> if they are not.
	 */
	public boolean hasPlayer(Player player){
		return players.contains(player);
	}

	/**
	 * Add a {@link Player} to the game. checks to make sure the player isn't already in the game.
	 * @param player The player to add.
	 * @see Player
	 */
	public boolean add(Player player){
		synchronized(this){
			BaseTile base = getUnownedBase();
			if (base == null) {
				System.err.println("Game: tried to add "+player.getSecret()+ " but there were no free bases");
				return false;
			}
			else add(player, base);
			this.players.add(player);
		}
		return true;
	}

	/**
	 * Add a {@link Player} to the game. checks to make sure the player isn't already in the game.
	 * Places their first unit at the given location
	 * @param player The player to add.
	 * @param base The base to assign to the player
	 * @see Player
	 */
	private void add(Player player, BaseTile base){
		base.setOwner(player);
		Unit unit = new Unit(player, map.getPosition(base));
		units.add(unit);
	}

	/**
	 * Returns the first unowned base
	 * @return <b>null</b> if there is no unowned bases.
	 */
	protected BaseTile getUnownedBase() {
		for (BaseTile base : getBases()){
			if (base.getOwner() == null) return base;
		}
		return null;
	}

	/**
	 * Check to see if there is any unowned bases left in the game
	 * mainly used for checking to see if a multiplayer map is can take more players
	 * @return true if there is a a base without a owner.
	 */
	public boolean hasUnownedBase(){
		return getUnownedBase() != null;
	}

	/**
	 * get all the bases in the game
	 * @return all the bases.
	 */
	public BaseTile[] getBases(){
		ArrayList<BaseTile> bases = new ArrayList<BaseTile>();
		for (Tile[] row : map.getTiles()){
			for (Tile t : row){
				if (t instanceof BaseTile) bases.add((BaseTile) t);
			}
		}
		return bases.toArray(new BaseTile[]{});
	}

	/**
	 * @return the {@link Map} of the game.
	 */
	public Map getMap(){
		return this.map;
	}

	/**
	 * Set the map for the game
	 * @param map the {@link Map} for the game.
	 */
	public void setMap(Map map){
		this.map = map;
	}

	/**
	 * gets the unit for a particular player
	 * @param player The player who owns the unit.
	 * @return <b>null</b> if there is no unit or player in this game.
	 */
	public Unit getUnit(Player player){
		if (player == null || !players.contains(player)) return null;
		for (Unit unit : units){
			if (unit.isOwnedBy(player)) return unit;
		}
		return null;
	}

	/**
	 * remove a player from the game
	 * used when a player want to move to the next game.
	 * @param player The player to remove
	 * @return the number of players left in the game
	 */
	public int remove(Player player) {
		this.players.remove(player);
		this.units.remove(getUnit(player));
		return players.size();
	}

	/**
	 * check to see if there is a unit at the given coordinate
	 * @param lat the latitude
	 * @param lng the longitude
	 * @return true if there is a unit at that location.
	 */
	public boolean isUnitAt(int lat, int lng) {
		for (Unit unit : units){
			if (unit.getLat() == lat && unit.getLng() == lng){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if there is a unit at the given location
	 * @param location The location to check
	 * @return true if there is a unit at the location
	 */
	public boolean isUnitAt(Coordinate location){
		return isUnitAt(location.lat, location.lng);
	}

	/**
	 * Return the player in this game with the given secret
	 * @param secret The name of the player
	 * @return <b>null</b> if there is no player with the given secret.
	 * @see Player
	 */
	public Player getPlayer(String secret){
		for (Player player : players){
			if (player.getSecret().equalsIgnoreCase(secret)){
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Get all the units in the game.
	 * @see Unit
	 */
	public Unit[] getUnits(){
		return units.toArray(new Unit[]{});
	}
}
