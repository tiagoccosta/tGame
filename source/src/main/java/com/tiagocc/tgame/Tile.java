package com.tiagocc.tgame;
import java.io.*;

public class Tile
implements Serializable{
	//public boolean exist;
	public String tileset;
	public VectorInt position;
	
	public Tile(String resourceTileSetName, VectorInt positionInTileSet){
		this.position  = positionInTileSet;
		this.tileset = resourceTileSetName;
		//exist = true;
	}

	@Override
	public String toString()
	{
		// TODO: Implement this method
		return tileset+":"+position.toString();
	}
	
	

	@Override
	public int hashCode()
	{
		// TODO: Implement this method
		return toString().hashCode();
	}

	
	
	@Override
	public boolean equals(Object obj)
	{
		// TODO: Implement this method
		return this.hashCode() == obj.hashCode();
	}
	
	
}
