package com.tiagocc.tgame;

import android.content.Context;
import android.graphics.*;
import java.util.*;

public class TerrainEngine extends GameComponent implements Runnable {
	

    private boolean mRunning;
    private Thread mGameThread = null;
	//public boolean dirtyMap=  false;

	private static final int MAX_FRAME_TIME = (int)(1000.0/30.0);
	
	
	public Map mMap;
	public Hashtable<VectorInt, TileGroup> groundGroups;
	public Hashtable<VectorInt, TileGroup> topGroups;
	public int tileResolution;
	//public List<ResourceTileSet> rTileSetList = new  ArrayList<ResourceTileSet>();
	public Hashtable<String,TileSet> tileSetList = new  Hashtable<String,TileSet>();
	//public VectorInt camPositionIndex;
	public VectorInt tilesToView;
	public Vector2 viewSize;
	public Vector2 worldSizeToView;
	private Vector2 mLastWorldPosition;
	private boolean dirtyMap;
	//private List<Vector2> worldPositions = new ArrayList<Vector2>();
	//private Vector2 lastWorldPosition;
	public Vector2 getLastWorldPosition (){return mLastWorldPosition; }
	public VectorInt mapOffSet;
	public VectorInt borderTiles;
	
	
	//Layers
	
	GameObject layerUnderGroundGO;
	List<TerrainAnimatedObject> mapLayerUnderGroundList = new ArrayList<>();
	
	GameObject layerGroundGO;
	TerrainObject mapLayerGround;
	
	GameObject layerUpsideGroundGO;
	List<TerrainAnimatedObject> mapLayerUpsideGroundList = new ArrayList<>();
	
	GameObject layerUnderTopGO;
	List<TerrainAnimatedObject> mapLayerUnderTopList = new ArrayList<>();
	
	GameObject layerTopGO;
	TerrainObject mapLayerTop;
	
	GameObject layerUpsideTopGO;
	List<TerrainAnimatedObject> mapLayerUpsideTopList = new ArrayList<>();
	
	

	private List<ActionListener> onTerrainUpdateStartListener = new ArrayList<>();
	public void addOnTerrainUpdateStart(ActionListener listener){onTerrainUpdateStartListener.add(listener);}
	public void removeOnTerrainUpdateStart(ActionListener listener){onTerrainUpdateStartListener.remove(listener);}
	
	boolean waitingUpdateTerrainEnd = false;
	private List<ActionListener> onTerrainUpdateEndListener = new ArrayList<>();
	public void addOnTerrainUpdateEnd(ActionListener listener){onTerrainUpdateEndListener.add(listener);}
	public void removeOnTerrainUpdateEnd(ActionListener listener){onTerrainUpdateEndListener.remove(listener);}
	
	@Override
	public void Initialize(GameObject go, Game game)
	{
		// TODO: Implement this method
		this.gameObject = go;
		this.mGame=game;
		System.out.println("Inicializando Terreno System..");
		
		borderTiles = VectorInt.Two();
		mapOffSet = VectorInt.Zero();
		//camPositionIndex = VectorInt.Zero();
		mLastWorldPosition = mGame.camera.getPosition();
		//worldPositions.add(mWorldPosition);
		//lastWorldPosition = mWorldPosition;
		
		tileResolution = mGame.camera.mTileSize;
		System.out.println("Tile Resolution"+tileResolution);
		viewSize = mGame.camera.getCameraSizePixel();
		System.out.println("viewSizePixels = "+viewSize.x+"|"+viewSize.y);
		worldSizeToView = mGame.camera.getSize();// new Vector2(viewSize.x/tileResolution,viewSize.y/tileResolution);
		System.out.println("WorldSizeToView = "+worldSizeToView.x+"|"+worldSizeToView.y);
		tilesToView = new VectorInt(((int)worldSizeToView.x)+(borderTiles.x*2),((int)worldSizeToView.y)+(borderTiles.y*2));
		System.out.println("tilesToView = "+tilesToView.x+"|"+tilesToView.y);
		mGame.camera.addUpdatePositionListener( new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event ){
					onUpdateCameraPosition((Vector2)event.data);
				}
			});
		//InitializeDrawers();
		this.mInitialized = true;
		//System.out.println("Inicialozado!!!");
		this.Awake();
		
	}
	
	
	public void InitializeDrawers(){

		if(layerUnderGroundGO!=null){layerUnderGroundGO.Destroy();}
		layerUnderGroundGO = new GameObject(Vector2.Zero(),new Vector2(tilesToView.x*3,tilesToView.y*3));
		layerUnderGroundGO.layer = 1;
		//mapLayerGround = new MapLayerDrawer(mMap);
		mGame.InsertGameObject(layerUnderGroundGO);
		layerUnderGroundGO.Initialize(this.mGame);

		if(layerGroundGO!=null){layerGroundGO.Destroy();}
		layerGroundGO = new GameObject(Vector2.Zero(),new Vector2(tilesToView.x*3,tilesToView.y*3));
		layerGroundGO.layer = 1;
		//mapLayerGround = new MapLayerDrawer(mMap);
		mapLayerGround = new TerrainObject();
		layerGroundGO.addGameComponent(mapLayerGround);
		mGame.InsertGameObject(layerGroundGO);
		layerGroundGO.Initialize(this.mGame);		

		if(layerUpsideGroundGO!=null){layerUpsideGroundGO.Destroy();}
		layerUpsideGroundGO = new GameObject(Vector2.Zero(),new Vector2(tilesToView.x*3,tilesToView.y*3));
		layerUpsideGroundGO.layer = 1;
		//mapLayerGround = new MapLayerDrawer(mMap);
		mGame.InsertGameObject(layerUpsideGroundGO);
		layerUpsideGroundGO.Initialize(this.mGame);
		

		if(layerUnderTopGO!=null){layerUnderTopGO.Destroy();}
		layerUnderTopGO = new GameObject(Vector2.Zero(),new Vector2(tilesToView.x*3,tilesToView.y*3));
		layerUnderTopGO.layer = 4;
		//mapLayerGround = new MapLayerDrawer(mMap);
		mGame.InsertGameObject(layerUnderTopGO);
		layerUnderTopGO.Initialize(this.mGame);		
		
		if(layerTopGO!=null){layerTopGO.Destroy();}
		layerTopGO = new GameObject(Vector2.Zero(),new Vector2(tilesToView.x*3,tilesToView.y*3));
		layerTopGO.layer =4;
		//mapLayerUp = new MapLayerDrawer(mMap);
		mapLayerTop = new TerrainObject();
		layerTopGO.addGameComponent(mapLayerTop);
		mGame.InsertGameObject(layerTopGO);
		layerTopGO.Initialize(this.mGame);

		if(layerUpsideTopGO!=null){layerUpsideTopGO.Destroy();}
		layerUpsideTopGO = new GameObject(Vector2.Zero(),new Vector2(tilesToView.x*3,tilesToView.y*3));
		layerUpsideTopGO.layer = 4;
		//mapLayerGround = new MapLayerDrawer(mMap);
		mGame.InsertGameObject(layerUpsideTopGO);
		layerUpsideTopGO.Initialize(this.mGame);		
		
	}
	
	public void OpenMap(Map map, TileSet[] sets){
		mMap = map;
		groundGroups = map.layerDown.getTilesTable();
		topGroups = map.layerUp.getTilesTable();
		for(int i = 0; i < sets.length; i++){
			TileSet set = sets[i];
			tileSetList.put(set.name,set);
			//tileSetList.add(GetTileSet(rTileSetList.get(i)));
		}
		InitializeDrawers();
		setDirty();
	}

	@Override
	public void Update()
	{
		if(waitingUpdateTerrainEnd && !dirtyMap){
			waitingUpdateTerrainEnd=false;

			ActionListener[] actions = new ActionListener[onTerrainUpdateEndListener.size()];
			actions = onTerrainUpdateEndListener.toArray(actions);
			for(ActionListener action : actions){
				action.actionPerformed(new ActionEvent(this,mMap));
			}
		}
	}
	
	/*
	public void SaveMap(){
		FileManager.SaveMapJSON(mMap);
	}
	public void LoadMap(){
		Map m =FileManager.GetMapToJSON();
		if(m!=null){
			OpenMap(m);
		}else{
			System.out.println("Mapa não encontrado...");
		}
	}
	*/
	
	
	
	public void run() {
		long frameStartTime = 0;
		long frameTime =0;
		
		//int totalTileCount = this.tilesToView.x * this.tilesToView.y;
		//?System.out.println("Começando run...");
		//TileObject testetile = new TileObject(VectorInt.One(),1, new VectorInt(10,0), GetTileSetByName("Landscape"));
		//mGame.InsertGameObject(testetile.gameObject);
        //TileSet tileSet = tileSetList.get(0);
        while (this.mRunning) {
			frameStartTime = System.nanoTime();
			
			
		    if(mInitialized && mMap != null && dirtyMap){// worldPositions.size()>0){
				Vector2 posi = mLastWorldPosition;
				
				UpdateTerrainObject(posi,new VectorInt(tilesToView.x*3, tilesToView.y*3));
				
				if(posi == mLastWorldPosition){
					dirtyMap = false;
				}
				
			}

			frameTime = (System.nanoTime() - frameStartTime) / 1000000;
			if(frameTime < MAX_FRAME_TIME){
				try {
					// Stop the thread == rejoin the main thread.
					Thread.sleep(MAX_FRAME_TIME - frameTime);
				} catch (InterruptedException e) {
				}
			}
        }
    }

	//@Override
	public void onUpdateCameraPosition(Vector2 position)
	{
		// TODO: Implement this method
		//super.onUpdateCameraPosition(position);
		float difX = Math.abs(mLastWorldPosition.x-position.x);
		float difY = Math.abs(mLastWorldPosition.y-position.y);
		if(difX > tilesToView.x || difY > tilesToView.y){
			mLastWorldPosition = position;
			setDirty();
		}
		
		
	}

	void setDirty(){
		dirtyMap = true;
		ActionListener[] actions = new ActionListener[onTerrainUpdateStartListener.size()];
		actions = onTerrainUpdateStartListener.toArray(actions);
		for(ActionListener action : actions){
			action.actionPerformed(new ActionEvent(this,mMap));
		}
		waitingUpdateTerrainEnd = true;
	}
	
	@Override
	public void onWindowSizeChange(VectorInt wSize, int unitsPerTile)
	{
		// TODO: Implement this method
		//super.onWindowSizeChange(wSize, unitsPerTile);
		
		tileResolution = unitsPerTile;
		viewSize = mGame.camera.getCameraSizePixel();
	//	worldSizeToView = new Vector2(wSize.x/unitsPerTile,wSize.y/unitsPerTile);
	//	tilesToView = new VectorInt(((int)worldSizeToView.x)+(borderTiles.x*2),((int)worldSizeToView.y)+(borderTiles.y*2));
		onUpdateCameraPosition(mGame.camera.getPosition());
		
	}
	
	public void RefreshMap(){
		mLastWorldPosition = mGame.camera.getPosition();
		setDirty();
	}
	
	public boolean SetTileInTerrain(Vector2 wPosition, int terrainLayer, int order, Tile tile){
		if(mMap==  null){return false;}
		VectorInt tPosi = new VectorInt ((int)(wPosition.x+0.5f),(int)(wPosition.y+0.5f));
		if(tPosi.x < 0 || tPosi.y < 0 || tPosi.x > mMap.size.x - 1 || tPosi.y > mMap.size.y - 1){
			return false;
		}
		
		TileGroup group = null;
		MapLayer layer = mMap.layerDown;
		Hashtable<VectorInt,TileGroup> groups= groundGroups;
		if(terrainLayer == 1){
			groups = groundGroups;
			layer = mMap.layerDown;
		}
		if(terrainLayer == 2){
			groups=topGroups;
			layer = mMap.layerUp;
		}
		if(groups.containsKey(tPosi)){
			group = groups.get(tPosi);
		}
		if(group==null){
			group = new TileGroup(tPosi);
			//group.EditTile(order,new Tile(tileSetName, posiInTileSet));
			groups.put(tPosi,group);
			layer.AddTileGroup(group);
		}
		group.EditTile(order, tile);
		mLastWorldPosition = mGame.camera.getPosition();
		setDirty();
		return true;
	}

	public boolean SetAnimatedTileInTerrain(Vector2 wPosition, String layerName){
		if(mMap==  null){return false;}
		VectorInt tPosi = new VectorInt ((int)(wPosition.x+0.5f),(int)(wPosition.y+0.5f));
		if(tPosi.x < 0 || tPosi.y < 0 || tPosi.x > mMap.size.x - 1 || tPosi.y > mMap.size.y - 1){
			return false;
		}
		AnimatedLayer layer = null;
		for(AnimatedLayer l : mMap.animatedLayers){
			if(l.name.equals(layerName)){
				layer = l;
			}
		}
		if(layer==null){return false;}
		if(layer.positions.contains(tPosi)){
			return false;
		}
		
		layer.positions.add(tPosi);

		mLastWorldPosition = mGame.camera.getPosition();
		setDirty();
		return true;
	}
	
	public void createNewAnimatedLayer(AnimatedLayer layer){
		TerrainAnimatedObject mapLayer = new TerrainAnimatedObject();
		if(layer.order==AnimatedLayer.UNDERGROUND){
			layerUnderGroundGO.addGameComponent(mapLayer);
			mapLayerUnderGroundList.add(mapLayer);
		}		
		if(layer.order==AnimatedLayer.UPSIDEGROUND){
			layerUpsideGroundGO.addGameComponent(mapLayer);
			mapLayerUpsideGroundList.add(mapLayer);
		}		
		if(layer.order==AnimatedLayer.UNDERTOP){
			layerUnderTopGO.addGameComponent(mapLayer);
			mapLayerUnderTopList.add(mapLayer);
		}		
		if(layer.order==AnimatedLayer.UPSIDETOP){
			layerUpsideTopGO.addGameComponent(mapLayer);
			mapLayerUpsideTopList.add(mapLayer);
		}		
		
		mMap.animatedLayers.add(layer);
	}

	public void removeAnimatedLayer(String layerName){
		TerrainAnimatedObject mapLayer = null;
		AnimatedLayer layer = null;
		for(AnimatedLayer l : mMap.animatedLayers){
			if(l.name.equals(layerName)){
				layer = l;
			}
		}
		if(layer==null){return;}
		if(layer.order==AnimatedLayer.UNDERGROUND){
			int index = -1;
			for(AnimatedLayer l : mMap.animatedLayers){
				if(l.order == layer.order && mapLayer == null){
					index++;
					if(l.equals(layer)){
						mapLayer = mapLayerUnderGroundList.get(index);
					}
				}
			}
			if(mapLayer != null){
				layerUnderGroundGO.RemoveComponent(mapLayer);
				mapLayerUnderGroundList.remove(mapLayer);
			}
		}		
		if(layer.order==AnimatedLayer.UPSIDEGROUND){
			int index = -1;
			for(AnimatedLayer l : mMap.animatedLayers){
				if(l.order == layer.order && mapLayer == null){
					index++;
					if(l.equals(layer)){
						mapLayer = mapLayerUpsideGroundList.get(index);
					}
				}
			}
			if(mapLayer != null){
				layerUpsideGroundGO.RemoveComponent(mapLayer);
				mapLayerUpsideGroundList.remove(mapLayer);
			}
		}		
		if(layer.order==AnimatedLayer.UNDERTOP){
			int index = -1;
			for(AnimatedLayer l : mMap.animatedLayers){
				if(l.order == layer.order && mapLayer == null){
					index++;
					if(l.equals(layer)){
						mapLayer = mapLayerUnderTopList.get(index);
					}
				}
			}
			if(mapLayer != null){
				layerUnderTopGO.RemoveComponent(mapLayer);
				mapLayerUnderTopList.remove(mapLayer);
			}
		}		
		if(layer.order==AnimatedLayer.UPSIDETOP){
			int index = -1;
			for(AnimatedLayer l : mMap.animatedLayers){
				if(l.order == layer.order && mapLayer == null){
					index++;
					if(l.equals(layer)){
						mapLayer = mapLayerUpsideTopList.get(index);
					}
				}
			}
			if(mapLayer != null){
				layerUpsideTopGO.RemoveComponent(mapLayer);
				mapLayerUpsideTopList.remove(mapLayer);
			}
		}		
		
		mMap.animatedLayers.remove(layer);
	}
	
	TileSet GetTileSetByName(String name){
		//TileSet tileSet = null;
		if(tileSetList.containsKey(name)){
			return tileSetList.get(name);
		}else{
			System.out.println("Erro: TileSet \""+name+"\" não encontrado. ");
			return null;
		}
		/*
		for(int i=0;i<tileSetList.size(); i++){
			if( tileSetList.get(i).name.compareTo(name)==0){
				tileSet = tileSetList.get(i);
			}
		}
		
		if(tileSet==null){System.out.println("Erro: TileSet \""+name+"\" não encontrado. ");}
		return tileSet;
		*/
	}
	/*
	public TileSet GetTileSet(ResourceTileSet rTileSet){
		return new TileSet(
			BitmapFactory.decodeResource(this.mGame.mContext.getResources(),rTileSet.resourceID),
			rTileSet);
	}*/

	
	
	/*
	public Bitmap getLayerImage (int layer){
		Tile[][] tiles = mMap.layers.get(layer - 1).tiles;
		boolean isGround = false;
		if(layer == 1){isGround = true;}
		int resolutionPerUnit = 18; //tileResolution;
		if(resolutionPerUnit>32){resolutionPerUnit = 32;}
		VectorInt resolution = mMap.getResolution;
		Bitmap img = Bitmap.createBitmap(
			resolution.x*resolutionPerUnit,//tileResolution,
			resolution.y*resolutionPerUnit,//tileResolution,
			Bitmap.Config.ARGB_8888
		);
		Canvas cv = new Canvas(img);
		TileSet tileSet =GetTileSetByName("Landscape");
		for(int y = startY; y< endY; y++){
			for(int x = startX; x< endX; x++){
				//VectorInt tileIndex= new VectorInt(x,y);
				

				Tile gTile = mMap.layers.get(0).tiles[y][x];
				if(gTile.exist){	
					if(tileSet.name!=gTile.ResourceTileSetName){
						tileSet = GetTileSetByName(gTile.ResourceTileSetName);
					}
					RectF position = new RectF();
					position.set(
						x*resolutionPerUnit,
						y*resolutionPerUnit,
						(x*resolutionPerUnit)+resolutionPerUnit,
						(y*resolutionPerUnit)+resolutionPerUnit
					);
					Rect imageRect = tileSet.getTileRect(tiles[y][x].PositionInTileSet);
					cv.drawBitmap(tileSet.Image,imageRect, position,null);
				}else{
					if(isGround){
						if(tileSet.name!="Landscape"){
							tileSet = GetTileSetByName("Landscape");
						}
						RectF position = new RectF();
						position.set(
							x*resolutionPerUnit,
							y*resolutionPerUnit,
							(x*resolutionPerUnit)+resolutionPerUnit,
							(y*resolutionPerUnit)+resolutionPerUnit
						);
						Rect imageRect = tileSet.getTileRect(new VectorInt(10,0));
						cv.drawBitmap(tileSet.Image,imageRect, position,null);
					}
				}
			}
		}
		System.out.println("Imgem gerada");
		return img;
	}
	*/
	
	
	void UpdateTerrainObject(Vector2 wPos, VectorInt tilesView){
		//VectorInt mapObjectSize 
		VectorInt tileIndice = new VectorInt((int)wPos.x,(int)wPos.y);
		Vector2 objPos=  new Vector2((tileIndice.x)-0.5f,(tileIndice.y)-0.5f);
		//Vector2 objSize = new Vector2(tilesView.x,tilesView.y);
		
		/*
		Vector2 objSize = new Vector2(tilesToView.x,tilesToView.y);
		GameObject tGround = new GameObject(objPos,objSize);
		TerrainObject tGroundDrawer = new TerrainObject();
		tGround.addGameComponent(tGroundDrawer);
		GameObject tUp = new GameObject(objPos,objSize);
		*/

		//RectF cameraRect = mGame.camera.getRectCameraView();
		int startX = tileIndice.x-(tilesView.x/2); // (int)cameraRect.left-2; //(int)(position.x-(mTilesToView.x/2));
		int startY = tileIndice.y-(tilesView.y/2); // (int)cameraRect.top-2; //(int)(position.y-(mTilesToView.y/2));
		int endX = tileIndice.x+(tilesView.x/2); // (int)cameraRect.right+2; //(int)(position.x+(mTilesToView.x/2));
		int endY = tileIndice.y+(tilesView.y/2) ; // (int)cameraRect.bottom+2; //(int)(position.y+(mTilesToView.y/2));
		
		int resolutionPerUnit = tileResolution;
		if(resolutionPerUnit>32){resolutionPerUnit = 32;}
		
		
		
		
		List<AnimatedLayer> undergroundLayers = new ArrayList<>();
		List<AnimatedLayer> upsidegroundLayers = new ArrayList<>();
		List<AnimatedLayer> undertopLayers = new ArrayList<>();
		List<AnimatedLayer> upsideTopLayers = new ArrayList<>();
		AnimatedLayer[] aLayers = new AnimatedLayer[mMap.animatedLayers.size()];
		aLayers = mMap.animatedLayers.toArray(aLayers);
		for(AnimatedLayer layer : aLayers){
			if(layer.order==AnimatedLayer.UNDERGROUND){
				undergroundLayers.add(layer);
			}
			if(layer.order==AnimatedLayer.UPSIDEGROUND){
				upsidegroundLayers.add(layer);
			}
			if(layer.order==AnimatedLayer.UNDERTOP){
				undertopLayers.add(layer);
			}
			if(layer.order==AnimatedLayer.UPSIDETOP){
				upsideTopLayers.add(layer);
			}
		}

		List<Bitmap[]> imgUnderGroundList = new ArrayList<>();
		List<Canvas[]> cvUnderGroundList = new ArrayList<>();
		if(undergroundLayers.size()>0){
			for(int i = 0; i< undergroundLayers.size(); i++){
				int framesCount = undergroundLayers.get(i).animatedTile.frames.size();
				Bitmap[] imgsUnderGround = new Bitmap[framesCount];
				Canvas[] cvsUnderGround = new Canvas[framesCount];
				for(int frameIndex = 0; frameIndex < framesCount; frameIndex++){
					imgsUnderGround[frameIndex] = Bitmap.createBitmap(
						tilesView.x*resolutionPerUnit,//tileResolution,
						tilesView.y*resolutionPerUnit,//tileResolution,
						Bitmap.Config.ARGB_8888
					);
					cvsUnderGround[frameIndex] = new Canvas(imgsUnderGround[frameIndex]);
				}
				imgUnderGroundList.add(imgsUnderGround);
				cvUnderGroundList.add(cvsUnderGround);
			}
		}

		List<Bitmap[]> imgUpsideGroundList = new ArrayList<>();
		List<Canvas[]> cvUpsideGroundList = new ArrayList<>();
		if(upsidegroundLayers.size()>0){
			for(int i = 0; i< upsidegroundLayers.size(); i++){
				int framesCount = upsidegroundLayers.get(i).animatedTile.frames.size();
				Bitmap[] imgsUpsideGround = new Bitmap[framesCount];
				Canvas[] cvsUpsideGround = new Canvas[framesCount];
				for(int frameIndex = 0; frameIndex < framesCount; frameIndex++){
					imgsUpsideGround[frameIndex] = Bitmap.createBitmap(
						tilesView.x*resolutionPerUnit,//tileResolution,
						tilesView.y*resolutionPerUnit,//tileResolution,
						Bitmap.Config.ARGB_8888
					);
					cvsUpsideGround[frameIndex] = new Canvas(imgsUpsideGround[frameIndex]);
				}
				
				imgUpsideGroundList.add(imgsUpsideGround);
				cvUpsideGroundList.add(cvsUpsideGround);
			}
		}

		List<Bitmap[]> imgUnderTopList = new ArrayList<>();
		List<Canvas[]> cvUnderTopList = new ArrayList<>();
		if(undertopLayers.size()>0){
			for(int i = 0; i< undertopLayers.size(); i++){
				int framesCount = undertopLayers.get(i).animatedTile.frames.size();
				Bitmap[] imgsUnderTop = new Bitmap[framesCount];
				Canvas[] cvsUnderTop = new Canvas[framesCount];
				for(int frameIndex = 0; frameIndex < framesCount; frameIndex++){
					imgsUnderTop[frameIndex] = Bitmap.createBitmap(
						tilesView.x*resolutionPerUnit,//tileResolution,
						tilesView.y*resolutionPerUnit,//tileResolution,
						Bitmap.Config.ARGB_8888
					);
					cvsUnderTop[frameIndex] = new Canvas(imgsUnderTop[frameIndex]);
				}
				imgUnderTopList.add(imgsUnderTop);
				cvUnderTopList.add(cvsUnderTop);
			}
		}

		List<Bitmap[]> imgUpsideTopList = new ArrayList<>();
		List<Canvas[]> cvUpsideTopList = new ArrayList<>();
		if(upsideTopLayers.size()>0){
			for(int i = 0; i< upsideTopLayers.size(); i++){
				int framesCount = upsideTopLayers.get(i).animatedTile.frames.size();
				Bitmap[] imgsUpsideTop = new Bitmap[framesCount];
				Canvas[] cvsUpsideTop = new Canvas[framesCount];
				for(int frameIndex = 0; frameIndex < framesCount; frameIndex++){
					imgsUpsideTop[frameIndex] = Bitmap.createBitmap(
						tilesView.x*resolutionPerUnit,//tileResolution,
						tilesView.y*resolutionPerUnit,//tileResolution,
						Bitmap.Config.ARGB_8888
					);
					cvsUpsideTop[frameIndex] = new Canvas(imgsUpsideTop[frameIndex]);
				}
				imgUpsideTopList.add(imgsUpsideTop);
				cvUpsideTopList.add(cvsUpsideTop);
			}
		}
		
		
		
		
		Bitmap imgGround = Bitmap.createBitmap(
			tilesView.x*resolutionPerUnit,//tileResolution,
			tilesView.y*resolutionPerUnit,//tileResolution,
			Bitmap.Config.ARGB_8888
		);
		Canvas cvGround = new Canvas(imgGround);
		
		Bitmap imgUp = Bitmap.createBitmap(
			tilesView.x*resolutionPerUnit,//tileResolution,
			tilesView.y*resolutionPerUnit,//tileResolution,
			Bitmap.Config.ARGB_8888
		);
		Canvas cvUp = new Canvas(imgUp);
		TileSet tileSet;
		int indexX = 0;
		int indexY = 0;
		for(int y = startY; y< endY; y++){
			indexX = 0;
			for(int x = startX; x< endX; x++){
				//VectorInt tileIndex= new VectorInt(x,y);
				
				
				if(x >= 0 && y >= 0
				&& x < mMap.size.x
				&& y < mMap.size.y){
				
					//System.out.println("Posi exist = " +x+"|"+y);
					
				 	RectF position = new RectF();
				 	position.set(
					 indexX*resolutionPerUnit,
					 indexY*resolutionPerUnit,
					 (indexX*resolutionPerUnit)+resolutionPerUnit,
					 (indexY*resolutionPerUnit)+resolutionPerUnit
				 	);
					//System.out.println("Position Rect"+position);
				 	//System.out.println("Index = " +indexX+"|"+indexY);
				VectorInt posi = new VectorInt(x,y);
				
				boolean posiContainUnderground = false;
				 for(AnimatedLayer layer : undergroundLayers){
					 int index = undergroundLayers.indexOf(layer);
					if(layer.positions.contains(posi)){
						posiContainUnderground = true;
						for(TileAnimationFrame frame : layer.animatedTile.frames){
							int frameIndex = layer.animatedTile.frames.indexOf(frame);
							Tile tile = frame.tile;
							tileSet = tileSetList.get(tile.tileset);

							Rect imageRect = tileSet.getTileRect(tile.position);
							cvUnderGroundList.get(index)[frameIndex].drawBitmap(tileSet.image,imageRect, position,null);
						}
						
					}
				}

				 for(AnimatedLayer layer : upsidegroundLayers){
					 int index = upsidegroundLayers.indexOf(layer);
					 if(layer.positions.contains(posi)){
						 for(TileAnimationFrame frame : layer.animatedTile.frames){
							 int frameIndex = layer.animatedTile.frames.indexOf(frame);
							 Tile tile = frame.tile;
							 tileSet = tileSetList.get(tile.tileset);

							 Rect imageRect = tileSet.getTileRect(tile.position);
							 cvUpsideGroundList.get(index)[frameIndex].drawBitmap(tileSet.image,imageRect, position,null);
						 }
					 }
				 }

				 for(AnimatedLayer layer : undertopLayers){
					 int index = undertopLayers.indexOf(layer);
					 if(layer.positions.contains(posi)){
						 for(TileAnimationFrame frame : layer.animatedTile.frames){
							 int frameIndex = layer.animatedTile.frames.indexOf(frame);
							 Tile tile = frame.tile;
							 tileSet = tileSetList.get(tile.tileset);

							 Rect imageRect = tileSet.getTileRect(tile.position);
							 cvUnderTopList.get(index)[frameIndex].drawBitmap(tileSet.image,imageRect, position,null);
						 }
					 }
				 }

				 for(AnimatedLayer layer : upsideTopLayers){
					 int index = upsideTopLayers.indexOf(layer);
					 if(layer.positions.contains(posi)){
						 for(TileAnimationFrame frame : layer.animatedTile.frames){
							 int frameIndex = layer.animatedTile.frames.indexOf(frame);
							 Tile tile = frame.tile;
							 tileSet = tileSetList.get(tile.tileset);

							 Rect imageRect = tileSet.getTileRect(tile.position);
							 cvUpsideTopList.get(index)[frameIndex].drawBitmap(tileSet.image,imageRect, position,null);
						 }
					 }
				 }
				 
				 
				if(groundGroups.containsKey(posi)){
					TileGroup groupGround = groundGroups.get(posi);
				 Iterator iterGround = groupGround.tiles.iterator();
				 int i = 0;
				 while(iterGround.hasNext()){
					 Tile gTile = (Tile)iterGround.next(); //mMap.layers.get(0).tiles[y][x];
					 
						if(gTile != null){	
							tileSet = tileSetList.get(gTile.tileset);
						/*
							if(tileSet.name.compareTo(gTile.ResourceTileSetName)!=0){
								tileSet = GetTileSetByName(gTile.ResourceTileSetName);
							}
							*/
							Rect imageRect = tileSet.getTileRect(gTile.position);
							cvGround.drawBitmap(tileSet.image,imageRect, position,null);
						}else{
							if(i==0){
								if(mMap.tileBase != null && !posiContainUnderground){
									tileSet = tileSetList.get(mMap.tileBase.tileset);
									/*
									if(tileSet.name.compareTo(mMap.tileBase.ResourceTileSetName)!=0){
										tileSet = GetTileSetByName(mMap.tileBase.ResourceTileSetName);
									}
									*/
									Rect imageRect = tileSet.getTileRect(mMap.tileBase.position);
									cvGround.drawBitmap(tileSet.image,imageRect, position,null);
								}
							}
						}
						i++;
					}
				}else{
					tileSet = tileSetList.get(mMap.tileBase.tileset);
					Rect imageRect = tileSet.getTileRect(mMap.tileBase.position);
					cvGround.drawBitmap(tileSet.image,imageRect, position,null);
				}
				
				if(topGroups.containsKey(posi)){
				 TileGroup groupTop = topGroups.get(new VectorInt(x,y));
				 Iterator iterTop = groupTop.tiles.iterator();
				 while(iterTop.hasNext()){
					 Tile dTile = (Tile)iterTop.next();
					 //Tile dTile = mMap.layers.get(1).tiles[y][x];
					 if(dTile != null){
						 tileSet = tileSetList.get(dTile.tileset);
						 /*
						 if(tileSet.name.compareTo( dTile.ResourceTileSetName)!=0){
							 tileSet = GetTileSetByName(dTile.ResourceTileSetName);
						 }
						 */
						 Rect imageRect = tileSet.getTileRect(dTile.position);
						 cvGround.drawBitmap(tileSet.image,imageRect, position,null);
					 }
				}
			}
					 
					 
				}
				indexX++;
			}
			indexY++;
		}
		
		
		try {
			 // Stop the thread == rejoin the main thread.
			 Thread.sleep(20);
		} catch (InterruptedException e) {
		}
		

		for(int i=0; i<imgUnderGroundList.size(); i++){
			layerUnderGroundGO.setPosition(objPos);
			mapLayerUnderGroundList.get(i).setImages(imgUnderGroundList.get(i));
		}

		for(int i=0; i<imgUpsideGroundList.size(); i++){
			layerUpsideGroundGO.setPosition(objPos);
			mapLayerUpsideGroundList.get(i).setImages(imgUpsideGroundList.get(i));
		}

		for(int i=0; i<imgUnderTopList.size(); i++){
			layerUnderTopGO.setPosition(objPos);
			mapLayerUnderTopList.get(i).setImages(imgUnderTopList.get(i));
		}

		for(int i=0; i<imgUpsideTopList.size(); i++){
			layerUpsideTopGO.setPosition(objPos);
			mapLayerUpsideTopList.get(i).setImages(imgUpsideTopList.get(i));
		}
		
		//GameObject tempGround = layerGroundGO;
		//layerGroundGO = new GameObject(objPos,objSize);
		//mapLayerGround = new TerrainObject();
		layerGroundGO.setPosition(objPos);
		//layerGroundGO.setSize(objSize);
		mapLayerGround.setImage(imgGround);
		//if(tempGround != null){
		//	tempGround.Destroy();
		//}
		
		//GameObject tempUp = layerUpGO;
		//layerUpGO = new GameObject(objPos,objSize);
		//mapLayerUp = new TerrainObject();
		layerTopGO.setPosition(objPos);
		//layerUpGO.setSize(objSize);
		mapLayerTop.setImage(imgUp);
		//if(tempUp != null){
		//	tempUp.Destroy();
		//}
		
		
		/*
		System.out.println("Terreno atualizado!");
		System.out.println("Position = "+objPos.x+"|"+objPos.y);
		System.out.println("Size = "+objSize.x+"|"+objSize.y);
		System.out.println("Tile Indice = "+tileIndice.x+"|"+tileIndice.y);
		System.out.println("Posicao Global = "+wPos.x+"|"+wPos.y);*
		*/
	}

	/**
     * Called by MainActivity.onPause() to stop the thread.
     */
    public void pause() {
        mRunning = false;
		
        try {
            // Stop the thread == rejoin the main thread.
            mGameThread.join();
        } catch (InterruptedException e) {
        }
		
    }

    /**
     * Called by MainActivity.onResume() to start a thread.
     */
    public void resume() {
        mRunning = true;
        mGameThread = new Thread(this);
		mGameThread.setName("TerrainThread");
        mGameThread.start();
    }
	public void destroy(){
		mRunning = false;
		if(mGameThread != null){
			mapLayerGround.ClearImage();
			mapLayerTop.ClearImage();
			tileSetList = null;
			mMap = null;
			mGameThread = null;
		}
	}
}

/*
class MapLayerDrawer extends Drawer{
	
	Map mMap;
	
	public MapLayerDrawer (Map map){
		mMap = map;
	}
	
	@Override
	public void CalculeRects(){
		float left = 0;
		float right = mMap.getResolution.x*this.sizeTile;
		float top = 0;
		float bottom = mMap.getResolution.y*this.sizeTile;

		rectangle.set(
			left,
			top,
			right,
			bottom
		);
	}

	@Override
	public void setImage(Bitmap bitmap)
	{
		ClearImage();
		// TODO: Implement this method
		super.setImage(bitmap);
	}
	
	
	public void ClearImage(){
		if(image!=null){
			image.recycle();
			image = null;
		}
	}
}
*/



class TerrainAnimatedObject extends Drawer{

	List<Bitmap> images = new ArrayList<>();
	private int index = 0;
	//private int time=1000;
	private long[] timesPerFrame;
	private long timeToLastChange;


	public void SetAnimatedTile(AnimatedTile animatedTile){
		timesPerFrame = new long[animatedTile.frames.size()];
		for(TileAnimationFrame frame  : animatedTile.frames){
			int time = frame.time;
			long timePerFrame = (time)/(images.size());
			timePerFrame *= 1000000;
			timesPerFrame[animatedTile.frames.indexOf(frame)] = timePerFrame;
		} 
		
		timeToLastChange = System.nanoTime();
	}
	
	@Override
	public void Start()
	{
		// TODO: Implement this method
		
		index = 0;
	}

	@Override
	public void Update()
	{
		if(images.size()<1){return;}
		if(System.nanoTime()-timeToLastChange>timesPerFrame[index]){
			timeToLastChange = System.nanoTime();
			setImage(images.get(index));
			index++;
			if(index>=images.size()){
				index = 0;
			}
		}
	}
	
	@Override
	public void Draw(Canvas canvas, Vector2 cornerCam){
		if(!this.mInitialized){return;}
		if(image!=null){
			//Vector2 camCornerPosi = mGame.camera.getStartCornerPixel();
			rectangleRelative.set(
				(rectangle.left)-cornerCam.x,
				(rectangle.top)-cornerCam.y,
				(rectangle.right)-cornerCam.x,
				(rectangle.bottom)-cornerCam.y);
			canvas.drawBitmap(
				image,
				imageRectangle,
				rectangleRelative,
				null);

		}
	}

	public void setImages(Bitmap[] bitmaps)
	{
		List<Bitmap> tmpImages = this.images;
		this.images.clear();
		index = 0;
		//ClearImage();
		// TODO: Implement this method
		if(bitmaps.length<1 || bitmaps == null){
			return;
		}
		images.addAll(Arrays.asList( bitmaps));
		super.setImage(bitmaps[0]);


		for (Bitmap tmpImage : tmpImages){
			if(tmpImage!=null){
				tmpImage.recycle();
				tmpImage = null;
			}
		}
		tmpImages.clear();
		tmpImages = null;
	}


	public void ClearImage(){
		if(image!=null){
			image.recycle();
			image = null;
		}
		for (Bitmap tmpImage : images){
			if(tmpImage!=null){
				tmpImage.recycle();
				tmpImage = null;
			}
		}
		images.clear();
	}

	/*
	 @Override
	 public void onUpdateCameraPosition(Vector2 position)
	 {
	 if(!mInitialized){return;}
	 // TODO: Implement this metod
	 //super.onUpdateCameraPosition(position);
	 Vector2 pos = gameObject.getPosition();
	 Vector2 size = gameObject.getSize();
	 float goStartX = pos.x-(size.x/2);
	 float goEndX = pos.x+(size.x/2);
	 float goStartY = pos.y-(size.y/2);
	 float goEndY = pos.y+(size.y/2);
	 RectF cameraRect = mGame.camera.getRectCameraView();
	 int startX = (int)cameraRect.left; //(int)(position.x-(mTilesToView.x/2));
	 int startY = (int)cameraRect.top; //(int)(position.y-(mTilesToView.y/2));
	 int endX = (int)cameraRect.right; //(int)(position.x+(mTilesToView.x/2));
	 int endY = (int)cameraRect.bottom; //(int)(position.y+(mTilesToView.y/2));
	 if(goEndX < startX || goStartX > endX
	 || goEndY<startY || goStartY>endY){

	 gameObject.Destroy();
	 }
	 }
	 */
}


class TerrainObject extends Drawer{
	
	@Override
	public void Draw(Canvas canvas, Vector2 cornerCam){
		if(!this.mInitialized){return;}
		if(image!=null){
			//Vector2 camCornerPosi = mGame.camera.getStartCornerPixel();
			rectangleRelative.set(
				(rectangle.left)-cornerCam.x,
				(rectangle.top)-cornerCam.y,
				(rectangle.right)-cornerCam.x,
				(rectangle.bottom)-cornerCam.y);
			canvas.drawBitmap(
				image,
				imageRectangle,
				rectangleRelative,
				null);

		}
	}
	
	
	@Override
	public void setImage(Bitmap bitmap)
	{
		Bitmap tmpImage = this.image;
		//ClearImage();
		// TODO: Implement this method
		super.setImage(bitmap);

		
		if(tmpImage!=null){
			tmpImage.recycle();
			tmpImage = null;
		}
	}
	
	
	public void ClearImage(){
		if(image!=null){
			image.recycle();
			image = null;
		}
	}
	
/*
	@Override
	public void onUpdateCameraPosition(Vector2 position)
	{
		if(!mInitialized){return;}
		// TODO: Implement this metod
		//super.onUpdateCameraPosition(position);
		Vector2 pos = gameObject.getPosition();
		Vector2 size = gameObject.getSize();
		float goStartX = pos.x-(size.x/2);
		float goEndX = pos.x+(size.x/2);
		float goStartY = pos.y-(size.y/2);
		float goEndY = pos.y+(size.y/2);
		RectF cameraRect = mGame.camera.getRectCameraView();
		int startX = (int)cameraRect.left; //(int)(position.x-(mTilesToView.x/2));
		int startY = (int)cameraRect.top; //(int)(position.y-(mTilesToView.y/2));
		int endX = (int)cameraRect.right; //(int)(position.x+(mTilesToView.x/2));
		int endY = (int)cameraRect.bottom; //(int)(position.y+(mTilesToView.y/2));
		if(goEndX < startX || goStartX > endX
			|| goEndY<startY || goStartY>endY){
				
				gameObject.Destroy();
			}
	}
	*/
}

