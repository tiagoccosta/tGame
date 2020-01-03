package com.tiagocc.tgame;
import java.util.*;
import java.io.*;

public class AnimatedLayer implements Serializable{
	public static final int UNDERGROUND = 0;
	public static final int UPSIDEGROUND = 1;
	public static final int UNDERTOP = 2;
	public static final int UPSIDETOP = 3;

	public String name;
	public AnimatedTile animatedTile;
	public List<VectorInt> positions = new ArrayList<>();
	public int order;
	
	

	@Override
	public int hashCode()
	{
		return (name+"|"+order).hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return this.hashCode()==obj.hashCode();
	}
	
}
