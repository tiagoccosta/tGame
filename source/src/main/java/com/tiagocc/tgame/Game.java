package com.tiagocc.tgame;

import android.content.Context;
import android.graphics.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import android.view.*;
import android.text.*;
import android.widget.*;

public class Game  implements Runnable{

    private boolean mRunning;
	private boolean mInitialized;
    private Thread mGameThread = null;

	private static final int MAX_FRAME_TIME = (int)(1000.0/60.0);

	private List<GameObject> mGameObjects = new ArrayList<GameObject>();
	public GameObject SceneRoot;
	
	
	public GameView mGameView;
	public GameCamera camera;
	public InputSystem mInputSystem;
	public UISystem uiSystem;
	public TerrainEngine terrain;
	public boolean gameViewInitialized;
	
	public GameConfiguration configuration;
	public Context mContext;
	
	public Vector2 cameraPosition;
	public Vector2 cameraSize;
	
	public Game (GameConfiguration config, LinearLayout content) {
		configuration = config;
		mContext = config.activity;
		gameViewInitialized=false;
		cameraPosition= Vector2.Zero();
		mInputSystem = new InputSystem(this);
		terrain = new TerrainEngine();
		camera = new GameCamera(this,Vector2.Zero());
		
		uiSystem = new UISystem();
		
		
		SceneRoot = new GameObject();
		SceneRoot.addGameComponent(terrain);
		SceneRoot.addGameComponent(mInputSystem);
		SceneRoot.addGameComponent(uiSystem);
		InsertGameObject(SceneRoot);
		
		mGameView = new GameView(config.activity, this, mInputSystem, camera);
		content.addView(mGameView);

		if(!mInitialized){

		}
	}
	
	public void onInitializeGameView(VectorInt wSize, int unitsPerTile){
		gameViewInitialized = true;
		if(mGameThread == null){
			mRunning = true;
			mGameThread = new Thread(this);
			mGameThread.setName("GameThread");
			mGameThread.start();
			mGameView.resume();
		}
		
	}
	public void onWindowSizeChange(VectorInt wSize, int unitsPerTile){
			ListIterator i = mGameObjects.listIterator();
			while(i.hasNext()){
				GameObject go = (GameObject)(i.next());
				go.onWindowSizeChange(wSize,unitsPerTile);
			}
	}
	public void run() {
		
        long frameStartTime;
		long frameTime;
		
		

		if(!mInitialized){
			mInitialized = true;
			GameObject[] gameObjects = new GameObject[mGameObjects.size()];
			gameObjects = mGameObjects.toArray(gameObjects);
			for(int i = 0; i < gameObjects.length; i++){
				gameObjects[i].Initialize(this);
			}
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			for(int i = 0; i < gameObjects.length; i++){
				gameObjects[i].Start();
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		
			terrain.resume();
		}
		
        while (mRunning) {
			frameStartTime = System.nanoTime();
			
			
			GameObject[] gameObjects = new GameObject[mGameObjects.size()];
			gameObjects = mGameObjects.toArray(gameObjects);
			for(int i = 0; i < gameObjects.length; i++){
				if(gameObjects[i]!=null){
					gameObjects[i].Update();
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
	
	/**
     * Called by MainActivity.onPause() to stop the thread.
     */
    public void pause() {
		if(!mInitialized || mGameThread != null){
			return;
		}
		mGameView.pause();
		terrain.pause();
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
		if(!mInitialized || mGameThread != null){
			return;
		}
        mRunning = true;
        mGameThread = new Thread(this);
		mGameThread.setName("GameThread");
        mGameThread.start();
		mGameView.resume();
		terrain.resume();
    }
	
	public void destroy(){
		mRunning = false;
		if(mGameThread != null){
			mGameThread = null;
		}
		mGameView.destroy();
		terrain.destroy();
	}
	
	public void InsertGameObject(GameObject go){
		mGameObjects.add(go);
		if(mInitialized){
			go.Initialize(this);
			go.Start();
		}
	}
	public void DestroyGameObject(GameObject go){
			mGameObjects.remove(go);
		
	}
}


enum GameLayer {
	ground,
	detail,
	phisics,
	up,
	top,
	sky,
	ui
}


