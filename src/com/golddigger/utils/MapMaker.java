package com.golddigger.utils;

import com.golddigger.model.Map;
import com.golddigger.model.Tile;
import com.golddigger.model.tiles.*;

public class MapMaker {
	public static Map parse(String string) {
		String[] lines = string.split("\n");
		Map map = new Map(lines.length, lines[0].trim().length());
		for (int i = 0; i < lines.length; i++){
			String line = lines[i].trim();
			for (int j = 0; j < line.length(); j++){
				map.set(i, j, convert(line.charAt(j)));
			}
		}
		
		return map;
	}
	
	private static Tile convert(char t){
		switch (t){
		case 'b': return new BaseTile();
		case 'c': return new CityTile();
		case 'd': return new DeepWaterTile();
		case 'f': return new ForestTile();
		case '1': return new GoldTile(1);
		case '2': return new GoldTile(2);
		case '3': return new GoldTile(3);
		case '4': return new GoldTile(4);
		case '5': return new GoldTile(5);
		case '6': return new GoldTile(6);
		case '7': return new GoldTile(7);
		case '8': return new GoldTile(8);
		case '9': return new GoldTile(9);
		case 'h': return new HillTile();
		case 's': return new ShallowWaterTile();
		case 'm': return new MountainTile();
		case 'r': return new RoadTile();
		case 't': return new TeleportTile();
		case 'w': return new WallTile();
		default: return new GoldTile();
		}
	}
}
