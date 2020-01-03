package com.tiagocc.tgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.graphics.drawable.*;
import android.graphics.*;
import android.util.*;
import java.util.*;
import java.util.concurrent.*;

public class GameObject  {

	public String ID;
	private Game mGame;
	public int layer = 3;
   // protected Bitmap image;
	protected boolean initialized;
	public boolean isInitialized(){return initialized;}

//    protected int rowCount = 1;
  //  protected int colCount = 1;
	
	protected Vector2 mPosition;
//	protected Vector2 mImageSize;
	protected Vector2 mSize;
	
	private List<GameComponent> mGameComponents = Collections.synchronizedList( new ArrayList<GameComponent>());
	public List<GameComponent>getComponentList(){return mGameComponents;}
//	private List<GameComponent> mGameComponentsToAdd = new ArrayList<GameComponent>();
//	private List<GameComponent> mGameComponentsToRemove = new ArrayList<GameComponent>();


    public GameObject ()  {
		mPosition = Vector2.Zero();
		mSize = Vector2.One();
		//Initialize(gameView,image,1,1,position);
	}

    public GameObject (Vector2 position)  {
		mPosition = position;
		mSize = Vector2.One();
		//Initialize(gameView,image,1,1,position);
	}

    public GameObject (Vector2 position, Vector2 size)  {
		mPosition= position;
		mSize= size;
		//Initialize(gameView,image,rowCount,colCount,position);
	}
    public void Initialize (Game game)  {
		mGame = game;
        //this.image = image;
        //this.rowCount= rowCount;
        //this.colCount= colCount;
		//this.mImageSize = new Vector2(image.getWidth(),image.getHeight());

        //int width = this.mImageSize.x/ colCount;
        //int height= this.mImageSize.y/ rowCount;
		//mTileSize = new Vector2(width,height);
		
		//this.mPosition = position;
		
		//SincronizeComponents();
		
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			components[i].Initialize(this,mGame);
		}
		/*
		for(int i=0; i < mGameComponents.size(); i++){
			mGameComponents.get(i).Initialize(this,mGame);
			//mGameComponents.get(i).onUpdatePosition(this.mPosition);
			//mGameComponents.get(i).onUpdateSize(this.mSize);
		}
		*/
		setPosition(this.mPosition);
		setSize(this.mSize);
		
		this.initialized = true;
    }
	/*
	public void SincronizeComponents(){
		mGameComponents.addAll(mGameComponentsToAdd);
		mGameComponentsToAdd.clear();
		mGameComponents.removeAll(mGameComponentsToRemove);
		mGameComponentsToRemove.clear();
	}
	*/

	public void Start(){
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			components[i].Start();
		}
	}
	
	
	public void Update(){
	//	SincronizeComponents();
		//System.out.println("Update chamado... Thread: "+Thread.currentThread().getName());
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			if(!components[i].isRemoved()){
				components[i].Update();
			}else{
				mGameComponents.remove(components[i]);
			}
		}
	}
	
	public void addGameComponent(GameComponent component){
		mGameComponents.add(component);
		//mGameComponents.add(component);
		if(initialized){
			component.Initialize(this, mGame);
			component.Start();
		}
	}
	
	public void RemoveComponent(GameComponent component){
		if(mGameComponents.contains(component)){
			mGameComponents.remove(mGameComponents.indexOf(component)); // component);
			component.onDestroy();
			//System.out.println("Component removido do gameObject");
		}else{
			System.out.println("Component não existe no gameObject");
		}
	}
	/*
	public void Draw(Canvas canvas){
		for(int i=0; i < mGameComponents.size(); i++){
			mGameComponents.get(i).Draw(canvas);
		}
		SincronizeComponents();
		
	}
	*/

	public void Destroy(){
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			components[i].onDestroy();
		}
		mGame.DestroyGameObject(this);
	}
	
	public void onWindowSizeChange(VectorInt wSize, int unitsPerTile){
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			components[i].onWindowSizeChange(wSize,unitsPerTile);
		}
	}
	
	/*
    protected Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col* mTileSize.x, row* mTileSize.y , mTileSize.x,mTileSize.x);
        return subImage;
    }
	*/
	/*
	public void onUpdateCameraPosition(Vector2 camPosition){
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			components[i].onUpdateCameraPosition(camPosition);
		}
	}
	*/
	public void setPosition(Vector2 position)  {
        this.mPosition=position;
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			components[i].onUpdatePosition(position);
		}
    }
	public void setSize(Vector2 size)  {
        this.mSize=size;
		GameComponent[] components = new GameComponent[mGameComponents.size()];
		components = mGameComponents.toArray(components);
		for(int i = 0; i < components.length; i++){
			components[i].onUpdateSize(size);
		}
    }
    public Vector2 getPosition()  {
        return this.mPosition;
    }
	
	public Vector2 getSize(){
		return this.mSize;
	}
}




