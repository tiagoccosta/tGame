package com.tiagocc.tgame.tmx;

public class TMXTileset{
	public String name;
	public int firstgid, tilewidth, tileheight, tilecount, columns;

	public String imageSource;
	public int imageWidth, imageHeight;

	public TMXTilesetAnimation[] animations;

	@Override
	public String toString()
	{
		// TODO: Implement this method
		String r ="Tileset "+name+"\n";
		r += "	*FirstGID: "+firstgid+"\n";
		r += "	*TileCount: "+tilecount+"\n";
		r += "	*Columns: "+columns+"\n";
		r += "	*TileWidth/TileHeight: "+tilewidth+"/"+tileheight+"\n";
		r += "	*ImageWidth/ImageHeight: "+imageWidth+"/"+imageHeight+"\n";
		r += "	*Source: "+imageSource+"\n";
		for(TMXTilesetAnimation animation : animations){r += animation.toString();}
		return r;
	}

}
