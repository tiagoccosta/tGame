package com.tiagocc.tgame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import android.graphics.*;
import android.view.View.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.util.*;
import java.io.*;
import android.graphics.drawable.*;
import android.view.ViewGroup.*;

public class Map implements Serializable
{
	public String name;
	public List<Property> properties = new ArrayList<>();
	public List<AnimatedLayer> animatedLayers = new ArrayList<>();
	//public List<MapLayer> layers = new ArrayList<MapLayer>();
	public MapLayer layerDown;
	public MapLayer layerUp;
	public Tile tileBase;
	public int tileResolution; // 8, 16 or 32 pixels
	public List<ObjectTile> objects = new ArrayList<>();
	public List<VectorInt> physics = new ArrayList<>();
	public VectorInt size;
	public Map(String name, VectorInt size){
		this.name=name;
		//tileBase = base;
		this.size = size;
		this.layerDown = new MapLayer();
		this.layerUp = new MapLayer();
	}
	public void setBase(Tile tile){
		this.tileBase = tile;
	}
	
}





class ResourceTileSet implements Serializable{
	public int resourceID;
	public String Name;
	public VectorInt RowColumnCount;
	public ResourceTileSet(String name, int imageId, VectorInt rowColumnCount){
		resourceID=imageId;
		Name =name;
		RowColumnCount =  rowColumnCount;
	}
}
/*
class SelectableTile {
	public VectorInt position;
	public ResourceTileSet tileSet;
	//	public Button btn;
	//public boolean walkable;
	public int layer =0;
	public Bitmap image;
}*/



/*
class PhysicTile{
	public VectorInt position;
	public boolean top;
	public boolean left;
	public boolean right;
	public boolean bottom;
	public PhysicTile(boolean Top, boolean Left, 
						boolean Right, boolean Bottom,
						VectorInt Position){
		position = Position;
		top=Top;
		left=Left;
		right=Right;
		bottom=Bottom;
	}
}
*/







