package com.tiagocc.tgame;
import android.widget.*;
import java.util.*;
import java.io.*;

public class MapLayer implements Serializable{
	public List<TileGroup> groups = new ArrayList<TileGroup>();
	/*
	public Tile[][] tiles;
	public MapLayer(VectorInt resolution){
		tiles = new Tile[resolution.y][resolution.x];
		for(int y = 0 ; y < resolution.y; y++){
			for(int x= 0; x < resolution.x;x++){
				tiles[y][x]=null;
			}
		}
	}
	 */
	 public boolean containsTile(VectorInt position){
		 TileGroup g = null;
		 for(TileGroup group : groups){
			 if(group.position.equals(position)){
				 g = group;
			 }
		 }
		 if(g!=null){
			 return true;
		 }else{
			 return false;
		 }
	 }
	public Hashtable getTilesTable(){
		Hashtable<VectorInt,TileGroup> table = new Hashtable<VectorInt,TileGroup>();
		for(int i = 0; i < groups.size(); i++){
			table.put(groups.get(i).position,groups.get(i));
		}
		return table;
	}
	public void EditTile(VectorInt position, int order, Tile tile){
		TileGroup group = getTileGroup(position);
		if(group==null){
			group = new TileGroup(position);
			groups.add(group);
		}
		group.EditTile(order,tile);
	}
	public void addTile(VectorInt position, Tile tile){
		TileGroup group = getTileGroup(position);
		if(group==null){
			group = new TileGroup(position);
			groups.add(group);
		}
		group.addTile(tile);
	}
	private TileGroup getTileGroup(VectorInt position){
		TileGroup group = null;
		for(int i = 0; i < groups.size(); i++){
			if(groups.get(i).position.equals(position)){
				group = groups.get(i);
			}
		}
		return group;
	}
	
	public void AddTileGroup(TileGroup group){
		groups.add(group);
	}
}
