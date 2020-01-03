package com.tiagocc.tgame;
import java.io.*;

public class Vector2 implements Serializable{
	public float x;
	public float y;
	public Vector2(float X, float Y){
		x=X;y=Y;
	}

	public static Vector2 Zero(){ 
		return new Vector2(0,0); 
	}
	public static Vector2 One(){ 
		return new Vector2(1,1); 
	}
	public static Vector2 Two(){ 
		return new Vector2(2,2); 
	}
	public static Vector2 Tree(){ 
		return new Vector2(3,3); 
	}

	@Override
	public int hashCode()
	{
		return (this.x+"|"+this.y).hashCode();
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

