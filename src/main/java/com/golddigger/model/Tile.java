package com.golddigger.model;

public abstract class Tile {
	public static final int DEFAULT_MOVEMENT_COST = 100;
	
	private int movementCost;
	private Coordinate teleportDestination = null;
	
	/**
	 * New Tile with the default movement cost set
	 * @param movementCost The delay for a successful move in milliseconds
	 */
	public Tile(int movementCost){
		this.movementCost = movementCost;
	}
	
	/**
	 * Is a unit allowed to move onto this tile
	 * @return true if yes, false if no
	 */
	public boolean isTreadable(){
		return true;
	}
	
	/**
	 * Used to store "tile types" in a hashmap in MoveService.java
	 */
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Return the default movement cost of this tile
	 * @return
	 */
	public int getDefaultMovementCost(){
		return this.movementCost;
	}

	/** The disadvantagous teleport location
	 * @return the destination coordinate, or null if this tile does not
	 * teleport.
	 */
	public Coordinate getTeleportDestination() {
		return teleportDestination;
	}
	
	/** Set the destination location of the disadvantageous teleport.
	 * @return the destination coordinate, or null if this tile should not
	 * teleport.
	 */
	public void setTeleportDestination(Coordinate teleportDestination) {
		this.teleportDestination = teleportDestination;
	}
}
