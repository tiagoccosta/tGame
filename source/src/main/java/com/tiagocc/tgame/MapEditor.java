package com.tiagocc.tgame;

import java.util.*;
import java.util.function.*;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;

public class MapEditor extends GameComponent
{
	
	boolean clickInGUI = false;
	
	boolean editingTerrain;
	public List<TileSet> tileSetList = new  ArrayList<TileSet>();
	int selectedTileSetIndex =0;
	String selectedTileSetName = "";
	VectorInt selectedTile = VectorInt.Zero();
	int layerEditing= 1;
	int orderEditing=1;
	//List<GameButton> tileSetSelectorList= new ArrayList<>();
	
	//drag Mapa
	boolean moving;
	Vector2 lastPos;
	
	//List<GameUIWidget> guiComponents = new ArrayList<>();
	public MapEditor(){}
	
	@Override
	public void Awake()
	{
		
	}

	@Override
	public void Start()
	{
		//Resources res = mGame.mContext.getResources();
		//image = BitmapFactory.decodeResource(res,R.drawable.landscape);
		
		//for(int i = 0; i < mGame.configuration.rTileSets.size(); i++){
		//	tileSetList.add(GetTileSet(mGame.configuration.rTileSets.get(i)));
		//}
		mGame.mInputSystem.addOnTouchDownListener( new GameInputListener(){
			@Override
			public void OnInputEvent(Vector2 pos){
				moving = false;
				lastPos = new Vector2(pos.x,pos.y);
				//CheckIfClickInGUI(pos);
				clickInGUI = UISystem.selectedUI!=null;
				
			}
		});
		
		mGame.mInputSystem.addOnTouchMoveListener( new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					if(clickInGUI){return;}
					moving = true;
					int tileSize = mGame.camera.mTileSize;
					Vector2 diference = new Vector2((pos.x-lastPos.x)/tileSize,(pos.y-lastPos.y)/tileSize);
					if(!moving){
						if(Math.abs(diference.x)>10 || Math.abs(diference.y)>10){
							moving = true;
						}else{
							return;
						}
					}
					lastPos = new Vector2(pos.x,pos.y);
					Vector2 tPos = mGame.camera.getPosition();
					Vector2 posToMove = new Vector2(tPos.x-diference.x,tPos.y-diference.y);
					//mGame.terrain.UpdateCameraWorldPosition(posToMove);
					mGame.camera.setPosition(posToMove);
					
				}
		});
		mGame.mInputSystem.addOnTouchUpListener( new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					if(moving){
						moving = false;
					}else{
						if(editingTerrain && !clickInGUI){
							//Alterar o tile do local pelo
							Vector2 wPosition = mGame.camera.getPointWorldPosition(pos);
							/*
							mGame.terrain.SetTileInTerrain(
								wPosition,
								layerEditing, 
								orderEditing,
								selectedTile, 
								tileSetList.get(selectedTileSetIndex).name
							);
							*/
							/*
							
							*/
						}else{
							/*
							GameObject go3= new GameObject(mGame.camera.getPointWorldPosition(pos), Vector2.One());
							//TextureRenderer sprite = new TextureRenderer();
							SimpleSprite sprite = new SimpleSprite(5,5,BitmapFactory.decodeResource(mGame.mContext.getResources(), R.drawable.explosion));
							//	simpleTexture.SetAnimationTime(1500);
							sprite.setColor(Color.GREEN);//.setImageInAtlas(9,7,16,16,image);
							go3.addGameComponent(sprite);
							mGame.InsertGameObject(go3);
							*/
						}
						clickInGUI = false;
					}
				}
			});
		GameObject menu = new GameObject();
		GameButton bt = new GameButton(Color.GRAY,Color.RED,VectorInt.Zero(), new VectorInt(60,30));
		bt.setOnClick(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					/*mGame.terrain.OpenMap(
					
						new Map(
							new VectorInt(256,256)
						)
					);
					*/
				}
			});
		menu.addGameComponent(bt);
		GameText text = new GameText("Start",VectorInt.Zero(), new VectorInt(60,30));
		menu.addGameComponent(text);
		
		
		GameButton bt2 = new GameButton(Color.GRAY,Color.RED,new VectorInt(0,35), new VectorInt(60,30));
		bt2.setOnClick(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					//Vector2 pos = (Vector2) e.data;
					//System.out.println("Botao clickado, posi = "+pos.x+"|"+pos.y);
					//mGame.terrain.OpenMap(mGame.configuration.mapList.get(0));
					OpenTileSetMenuList();
				}
			});
		menu.addGameComponent(bt2);
		GameText text2 = new GameText("Selector",new VectorInt(0,35), new VectorInt(60,30));
		menu.addGameComponent(text2);
		
		GameButton bt3 = new GameButton(Color.GRAY,Color.RED,new VectorInt(0,70), new VectorInt(60,30));
		bt3.setOnClick(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					//mGame.terrain.LoadMap();
				}
			});
		menu.addGameComponent(bt3);
		GameText text3 = new GameText("Load",new VectorInt(0,70), new VectorInt(60,30));
		menu.addGameComponent(text3);
		
		GameButton bt4 = new GameButton(Color.GRAY,Color.RED,new VectorInt(0,105), new VectorInt(60,30));
		bt4.setOnClick(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					//mGame.terrain.SaveMap();
				}
			});
		menu.addGameComponent(bt4);
		GameText text4 = new GameText("Save",new VectorInt(0,105), new VectorInt(60,30));
		menu.addGameComponent(text4);
		
		mGame.InsertGameObject(menu);
			
	}
	
	void CreateSelectorPanel(final TileSet tSet){
		System.out.println("Criando Painel TileSetSelector ("+tSet.name+")");
		final GameObject selectorPainel = new GameObject();
		Vector2 camSize = mGame.camera.getCameraSizePixel();
		
		final VectorInt size = new VectorInt((int)((tSet.imageSize.x/tSet.imageSize.y)*camSize.y),(int)camSize.y);
		final VectorInt posi = new VectorInt((int)((camSize.x/2)-(size.x/2)),0);
		final GameButton bt = new GameButton(tSet.image, posi, size);
		final GamePanel mark = new GamePanel(Color.argb(130,200,0,0),posi , VectorInt.Zero());
		mark.SetBlockTouch(false);
		final VectorInt markSize= new VectorInt(size.x/tSet.size.x,size.y/tSet.size.y);
		final VectorInt markPosition = VectorInt.Zero();
		if(selectedTileSetName.equals(tSet.name)){
			markPosition.x =
				posi.x+(selectedTile.x*markSize.x)+(markSize.x/2);
			markPosition.y =posi.y+(selectedTile.y*markSize.y)+(markSize.y/2);
		}
		
		//bt.setSizeBilinear(size);
		//bt.setPositionBilinear(posi);
		bt.setOnClickDown(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					Vector2 pos = (Vector2) e.data;
					Vector2 posBilinear = bt.getInsideBilinearPosition(new VectorInt((int)pos.x,(int)pos.y));
					VectorInt tilePos = new VectorInt(
						(int)((tSet.imageSize.x*posBilinear.x)/tSet.tileSize.x),
						(int)((tSet.imageSize.y*posBilinear.y)/tSet.tileSize.y)
					);
					markPosition.x = posi.x+(tilePos.x*markSize.x)+(markSize.x/2);
					markPosition.y = posi.y+(tilePos.y*markSize.y)+(markSize.y/2);
					mark.setPosition(markPosition);
				}
			});
		bt.setOnClickStay(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Vector2 pos = (Vector2) e.data;
				Vector2 posBilinear = bt.getInsideBilinearPosition(new VectorInt((int)pos.x,(int)pos.y));
				VectorInt tilePos = new VectorInt(
					(int)((tSet.imageSize.x*posBilinear.x)/tSet.tileSize.x),
					(int)((tSet.imageSize.y*posBilinear.y)/tSet.tileSize.y)
				);
				markPosition.x = posi.x+(tilePos.x*markSize.x)+(markSize.x/2);
				markPosition.y = posi.y+(tilePos.y*markSize.y)+(markSize.y/2);
				mark.setPosition(markPosition);
			}
		});
		bt.setOnClick(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					Vector2 pos = (Vector2) e.data;
					Vector2 posBilinear = bt.getInsideBilinearPosition(new VectorInt((int)pos.x,(int)pos.y));
					VectorInt tilePos = new VectorInt(
						(int)((tSet.imageSize.x*posBilinear.x)/tSet.tileSize.x),
						(int)((tSet.imageSize.y*posBilinear.y)/tSet.tileSize.y)
					);
					markPosition.x = posi.x+(tilePos.x*markSize.x)+(markSize.x/2);
				markPosition.y = posi.y+(tilePos.y*markSize.y)+(markSize.y/2);
				mark.setPosition(markPosition);
					selectedTile = tilePos;
					selectedTileSetName = tSet.name;
					selectedTileSetIndex = tileSetList.indexOf(tSet);
					editingTerrain = true;
					//guiComponents.remove(bt);
					selectorPainel.Destroy();
					clickInGUI = true;
				}
			});
		selectorPainel.addGameComponent(bt);
		mark.setPosition(markPosition);
		mark.setSize(markSize);
		selectorPainel.addGameComponent(mark);
		mGame.InsertGameObject(selectorPainel);
	}
	void OpenTileSetMenuList(){
		final GameObject menuPainel = new GameObject();
		//final List<GameUIWidget> btns = new ArrayList<>();
		Vector2 size = new Vector2(0.3f,0.6f/tileSetList.size());
		//btns.add();
		GamePanel base = new GamePanel(
			Color.GREEN, 
			new Vector2(0.5f -(size.x/2),0),
			new Vector2(size.x+0.05f, (size.y*tileSetList.size())+0.05f)
		);
		menuPainel.addGameComponent(base);
		for(int i = 0; i < tileSetList.size(); i++){
			//final TileSet tSet = tileSetList.get(i);
			final int tSetIndex = i;
			Vector2 posi = new Vector2(0.5f-(size.x/2),(size.y*i)+size.y);
			GameButton bt = new GameButton(Color.GRAY,Color.RED,VectorInt.Zero(), new VectorInt(60,30));
			bt.setSizeBilinear(size);
			bt.setPositionBilinear(posi);
			//btns.add(bt);
			bt.setOnClick(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						//guiComponents.removeAll(btns);
						menuPainel.Destroy();
						CreateSelectorPanel(tileSetList.get(tSetIndex));
						clickInGUI = true;
					}
				});
			menuPainel.addGameComponent(bt);
			GameText text = new GameText(tileSetList.get(i).name,VectorInt.Zero(), new VectorInt(60,30));
			text.setSizeBilinear(size);
			text.setPositionBilinear(posi);
			//btns.add(text);
			menuPainel.addGameComponent(text);
		}
		//guiComponents.addAll(btns);
		mGame.InsertGameObject(menuPainel);
	}

	@Override
	public void Update()
	{
		
	}
	/*
	void CheckIfClickInGUI(Vector2 pos){
		guiComponents.remove(null);
		GameUIWidget[] comps = new GameUIWidget[guiComponents.size()];
		comps = guiComponents.toArray(comps);
		for(int i = 0; i < comps.length; i++){
			if(comps[i]!=null){
				clickInGUI = comps[i].isInside(pos);
			}else{
				//guiComponents.remove(i);
			}
		}
		
	}
	*/
	
	TileSet GetTileSetByName(String name){
		TileSet tileSet = null;
		for(int i=0;i<tileSetList.size(); i++){
			if(tileSetList.get(i).name.equals(name)){
				tileSet = tileSetList.get(i);
			}
		}
		return tileSet;
	}

	public TileSet GetTileSet(ResourceTileSet rTileSet){
		return new TileSet(
			BitmapFactory.decodeResource(this.mGame.mContext.getResources(),rTileSet.resourceID),
			rTileSet);
	}
}
