package com.tiagocc.tgame;
import java.util.*;
import java.io.*;

public class AnimatedTile implements Serializable{
	public List<TileAnimationFrame> frames;

	@Override
	public String toString()
	{
		// TODO: Implement this method
		String ret = "";
		for(TileAnimationFrame frame : frames){
			ret += frame.tile.toString()+"&";
		}
		return ret;
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
		return this.hashCode()==obj.hashCode();
	}
	
}
