package com.tiagocc.tgame;
import java.util.*;
import java.io.*;

public class TileGroup implements Serializable {
	public VectorInt position;
	// Se o tile de indice 0 for null, é usado o tile base
	public List<Tile> tiles = new ArrayList<Tile>();
	public TileGroup(VectorInt position){
		this.position=position;
	}
	public void EditTile(int order,Tile tile){
		while(this.tiles.size()<=order){
			this.tiles.add(null);
		}
		this.tiles.set(order,tile);
	}
	public void addTile(Tile tile){
		this.tiles.add(tile);
	}
}
