package com.tiagocc.tgame;
import java.io.*;

public class VectorInt implements Serializable{
	public int x;
	public int y;
	public VectorInt(int X, int Y){
		x=X;y=Y;
	}

	public static VectorInt Zero(){ 
		return new VectorInt(0,0); 
	}
	public static VectorInt One(){ 
		return new VectorInt(1,1); 
	}
	public static VectorInt Two(){ 
		return new VectorInt(2,2); 
	}
	public static VectorInt Tree(){ 
		return new VectorInt(3,3); 
	}

	@Override
	public String toString()
	{
		// TODO: Implement this method
		return (this.x+"|"+this.y);
	}
	
	

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this.hashCode()==obj.hashCode();
		/*
		if(obj == this){return true;}
		if(obj==null){return false; }
		if(!(obj.getClass() != getClass())){
			return false;
		}
		VectorInt vec = (VectorInt)obj;
		return (vec.x == x && vec.y == y);
		*/
	}
	
	
}
