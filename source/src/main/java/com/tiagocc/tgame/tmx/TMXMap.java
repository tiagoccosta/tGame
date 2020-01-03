package com.tiagocc.tgame.tmx;

public class TMXMap{
	public String version, tiledversion,
	orientation, renderorder;
	public int width, height, tilewidth,
	tileheight, infinite, nextobjectid;
	public TMXTileset[] tilesets = new TMXTileset[0];
	public TMXLayer[] layers = new TMXLayer[0];
	public TMXObjectGroup[] groups = new TMXObjectGroup[0];

	@Override
	public String toString()
	{
		// TODO: Implement this method
		String r = "------>>>> Map <<<<<------ \n";
		r += "Version: "+version+" || TiledVersion: "+tiledversion+"\n";
		r += "Orientatio: "+orientation+"\n";
		r += "RenderOrder: "+renderorder+"\n";
		r += "Width/Height: "+width+"/"+height+"\n";
		r += "TileWidth/TileHeight: "+tilewidth+"/"+tileheight+"\n";
		r += "Infinite: "+infinite+"\n";
		r += "NextObjectId: "+nextobjectid+"\n";
		for(TMXTileset tileset : tilesets){r += tileset.toString();}
		for(TMXLayer layer : layers){r += layer.toString();}
		for(TMXObjectGroup group : groups){r += group.toString();}
		return r;
	}

}
