package com.tiagocc.tgame;

import android.graphics.*;
import android.view.View.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.util.*;
import java.io.*;
import android.graphics.drawable.*;
import android.view.ViewGroup.*;


public class TileSet
{
	public Bitmap image;
	//Numero de linhas e colunas
	public VectorInt size;
	public String name;
	public int spacing;
	public int margin;
	public VectorInt imageSize;
	public VectorInt tileSize;
	public int tileCount;
	public TileSet (Bitmap image, ResourceTileSet rTileSet){
		this.image = image;
		this.name = rTileSet.Name;
		this.size = rTileSet.RowColumnCount;
		this.imageSize = new VectorInt(this.image.getWidth(),this.image.getHeight());
		this.tileSize = new VectorInt(imageSize.x/this.size.x,imageSize.y/this.size.y);
		this.tileCount=this.size.x*this.size.y;
	}
	public TileSet (Bitmap image, String name, VectorInt size){
		this.image = image;
		this.name = name;
		this.size = size;
		this.imageSize = new VectorInt(this.image.getWidth(),this.image.getHeight());
		this.tileSize = new VectorInt(this.imageSize.x/this.size.x,this.imageSize.y/this.size.y);
		this.tileCount =this.size.x*this.size.y;
	}
	public TileSet (Bitmap image, String Name, VectorInt size, int margin, int spacing){
		this.image = image;
		this.name = Name;
		this.margin = margin;
		this.spacing = spacing;
		this.size = size;
		this.imageSize = new VectorInt(this.image.getWidth(),this.image.getHeight());
		this.tileSize = new VectorInt(this.imageSize.x/this.size.x,this.imageSize.y/this.size.y);
		this.tileCount=this.size.x*this.size.y;
	}
	public Bitmap GetTileBitmap(VectorInt pos){
		// createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(this.image, (pos.x-1)* this.tileSize.x, (pos.y-1)* this.tileSize.y ,this.tileSize.x,this.tileSize.y);
        return subImage;
	}
	
	public Rect getTileRect (VectorInt pos){
		VectorInt posInitial = new VectorInt((pos.x-1)*tileSize.x,(pos.y-1)*tileSize.y);
		return new Rect (posInitial.x,posInitial.y,posInitial.x+tileSize.x,posInitial.y+tileSize.y);
	}
	
	public Bitmap[][] GetTiles(){
		return GetTiles(
			VectorInt.Zero(),
			new VectorInt(size.x,size.y)
		);
	}

	public Bitmap[][] GetTiles( VectorInt initialPosition, VectorInt sizePart){
		Bitmap[][] lista= new Bitmap[size.y][size.x];
		for(int y = initialPosition.y; y <size.y && y < initialPosition.y+sizePart.y ; y++){
			Bitmap[] lineTiles = new Bitmap[size.x];
			for(int x = initialPosition.x; x <size.x  && x < initialPosition.x+sizePart.x ; x++){
				Bitmap tile = GetTileBitmap(new VectorInt(x,y));
				lineTiles[x] = tile;
			}

			lista[y]= lineTiles;

		}
		return lista;
	}
}
