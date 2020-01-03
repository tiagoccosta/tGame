package com.tiagocc.tgame.tmx;
import java.util.*;
import com.tiagocc.tgame.*;

public class TMXMapConverter{

	private OnXMLRequestListener xmlRequestListener = null;
	private OnBitmapRequestListener bmpRequestListener = null;
	
	public TMXMapConverter( OnXMLRequestListener xmlRequestListener , OnBitmapRequestListener bmpRequestListener){
		this.xmlRequestListener = xmlRequestListener;
		this.bmpRequestListener = bmpRequestListener;
	} 
	
	public com.tiagocc.tgame.Map convertTMXMapToMap(TMXMap tmxMap){
		
		onConvertionProgressChange("Creating base map...");
		com.tiagocc.tgame.Map map = new com.tiagocc.tgame.Map("ConvertedMap",new VectorInt(tmxMap.width,tmxMap.height));
		map.tileResolution = tmxMap.tilewidth*tmxMap.tileheight;
		
		/*
		 HashMap<Integer,int[][]> animatedGids = new HashMap<Integer,int[][]>();
		 HashMap<Integer,AnimatedTile> animatedTiles = new HashMap<Integer,AnimatedTile>();
		 for(TMXTileset set: tmxMap.tilesets){
		 for(TMXTilesetAnimation anim : set.animations){
		 animatedGids.put(anim.id,anim.frames);
		 AnimatedTile animTile = new AnimatedTile();
		 List <TileAnimationFrame> tileFrames = new ArrayList<TileAnimationFrame>();
		 for(int[] frame : anim.frames){
		 TileAnimationFrame tileFrame = new TileAnimationFrame();
		 java.util.AbstractMap.SimpleEntry<TMXTileset,VectorInt> tileInfo = getPositionOfGidInTileset(frame[0],tmxMap.tilesets);
		 tileFrame.tile = new Tile( tileInfo.getKey().name, tileInfo.getValue());
		 tileFrame.time = frame[1];
		 tileFrames.add(tileFrame);
		 }
		 animTile.frames = tileFrames;
		 animatedTiles.put(anim.id,animTile);
		 }
		 }
		 */
		HashMap<Integer,Integer> countGidUse = new HashMap<Integer,Integer>();
		//int[] countGid = new int[];
		//int[] countGidValue = new int[];
		//Tile[][][] tiles = new Tile[tmxMap.layers.length][tmxMap.height][tmxMap.width];

		HashMap<Integer,Tile> tileGidMap = new HashMap<Integer,Tile>();
		Tile tileBase = null;
		//int layerIndex =-1;
		onConvertionProgressChange("Converting layers...");
		for(TMXLayer tmxLayer:  tmxMap.layers){
			//layerIndex++;
			MapLayer layer;

			onConvertionProgressChange("converting layer "+tmxLayer.name+"...");
			if(tmxLayer.name.toLowerCase().contains("ground") || tmxLayer.name.toLowerCase().contains("fringe")){
				layer = map.layerDown;
			}else{
				layer = map.layerUp;
			}

			for(int y=0; y < tmxLayer.data.length; y++){
				for(int x=0; x < tmxLayer.data[y].length; x++){
					Integer gid = tmxLayer.data[y][x];
					if(gid>0){
						VectorInt posi = new VectorInt(x,y);
						//System.out.println("position "+x+"|"+y);

						if(countGidUse.containsKey(gid)){
							int count = countGidUse.get(gid);
							countGidUse.put(gid,count+1);
							layer.addTile(posi,tileGidMap.get(gid));
						}else{
							//if(!animatedGids.containsKey(gid)){
							AbstractMap.SimpleEntry<TMXTileset,VectorInt> tileInfo = getPositionOfGidInTileset(gid,tmxMap.tilesets);
							Tile tile = new Tile(tileInfo.getKey().name,tileInfo.getValue());
							layer.addTile(posi,tile);
							countGidUse.put(gid,1);
							tileGidMap.put(gid,tile);
							/*
							 }else{
							 AnimatedLayer animLayer = null;
							 int order = AnimatedLayer.UPSIDETOP;
							 if(layer.equals(map.layerDown)){order--;}
							 if(!layer.containsTile(posi)){order--;}
							 for(AnimatedLayer l : map.animatedLayers){
							 if(l.order==order && l.animatedTile.equals(animatedTiles.get(gid))){
							 animLayer = l;
							 }
							 }

							 if(animLayer==null){
							 animLayer = new AnimatedLayer();
							 map.animatedLayers.add(animLayer);
							 animLayer.order = order;
							 animLayer.animatedTile = animatedTiles.get(gid);
							 }
							 if(!animLayer.positions.contains(posi)){
							 animLayer.positions.add(posi);
							 }
							 }*/
						}

					}

				}
				//System.out.println("x: "+x);
			}
			try
			{
				Thread.currentThread().sleep(5);
			}
			catch (InterruptedException e)
			{e.printStackTrace();}
			onConvertionProgressChange("Layer "+tmxLayer.name+" converted!");
		}


		onConvertionProgressChange("Finding tile base...");
		int baseGid = 0;
		int baseGidCount = 0;
		for(java.util.Map.Entry<Integer,Integer> entry : countGidUse.entrySet()){
			if(entry.getValue()>baseGidCount){
				baseGid = entry.getKey();
				baseGidCount = entry.getValue();
			}
		}
		tileBase = tileGidMap.get(baseGid);
		map.tileBase = tileBase;
		onConvertionProgressChange("Tile base finded!");
		//System.out.println("TileBase: "+baseGid+" | Count: "+baseGidCount);
		System.out.println("Base Name: "+tileBase.tileset+" | Base Position in Set: x=" +tileBase.position.x+" y="+tileBase.position.y);
		onConvertionProgressChange("Map converted!");
		return map;
	}
	public TileSet[] convertTMXMapToTilesets(TMXMap tmxMap){
		TileSet[] tilesets = new TileSet[tmxMap.tilesets.length];
		for(int i =0;i<tilesets.length;i++){
			TMXTileset tmxTileset = tmxMap.tilesets[i];
			VectorInt size = new VectorInt(
				(tmxTileset.columns),
				(tmxTileset.tilecount/tmxTileset.columns));
			TileSet tileset = new TileSet(bmpRequestListener.getBitmap(tmxTileset.imageSource),tmxTileset.name,size);
			tileset.imageSize = new VectorInt(tmxTileset.imageWidth,tmxTileset.imageHeight);
			//tileset.image = listener.getBitmap(tmxTileset.imageSource);
			tilesets[i] = tileset;
		}
		return tilesets;
	}

	void onConvertionProgressChange(String state){
		
	}
	
	private AbstractMap.SimpleEntry<TMXTileset, VectorInt> getPositionOfGidInTileset(int gid, TMXTileset[] sets){
		TMXTileset retSet = null;
		VectorInt retPosi = VectorInt.Zero();

		for(int i = 0; i < sets.length; i++){
			if(sets.length==1){
				retSet=sets[0];
			}else{
				if(gid > sets[i].firstgid){
					if(sets.length>i+1){
						if(gid<sets[i+1].firstgid){
							retSet = sets[i];
						}
					}
				}
			}

			if(retSet!=null){
				int gidLocal = gid-retSet.firstgid;
				int y = gidLocal/retSet.columns;
				int x = gidLocal%retSet.columns;
				if(x==0){y--; x=retSet.columns;}

				retPosi.x = x+1;
				retPosi.y = y+1;
			}
		}

		return new AbstractMap.SimpleEntry<TMXTileset, VectorInt>(retSet,retPosi);
	}
}
