package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class ForestTile extends Tile {
	public static int DEFAULT_MOVEMENT_COST = 300;
	public static int DEFAULT_HEIGHT = 1;
	
	public ForestTile() {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
	}
}
