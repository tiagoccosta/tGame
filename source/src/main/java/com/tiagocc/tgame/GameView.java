package com.tiagocc.tgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.*;
import org.apache.http.conn.scheme.*;
import android.graphics.*;



public class GameView extends SurfaceView implements Runnable{

	public Game mGame;
	public InputSystem mInputSystem;
    private boolean mRunning;
	private boolean mInitialized;
    private Thread mGameViewThread = null;
	private static final int MAX_FRAME_TIME = (int)(1000.0/60.0);
	
	private List<GameViewLayer> layers = new ArrayList<GameViewLayer>();
    private boolean layerDirty = true;
	
    private Context mContext;

	public VectorInt mViewSize;
	public int sizeTile;
    private SurfaceHolder mSurfaceHolder;
	public GameCamera mCamera;
	

    public GameView(Context context, Game game, InputSystem inputSystem, GameCamera camera) {
	    super(context, null);
		mGame = game;
		mCamera = camera;
        mContext = context;
        mSurfaceHolder = getHolder();
		mInputSystem = inputSystem;
        //mPaint = new Paint();
        //mPaint.setColor(Color.DKGRAY);
        //mPath = new Path();
		CreateBasicLayers();
    }
	
	private void CreateBasicLayers(){
		GameViewLayer groundLayer = new GameViewLayer("ground");
		layers.add(groundLayer);
		GameViewLayer detailLayer = new GameViewLayer("detail");
		layers.add(detailLayer);
		GameViewLayer physicLayer = new GameViewLayer("physic");
		layers.add(physicLayer);
		GameViewLayer upLayer = new GameViewLayer("up");
		layers.add(upLayer);
		GameViewLayer topLayer = new GameViewLayer("top");
		layers.add(topLayer);
		GameViewLayer skyLayer = new GameViewLayer("sky");
		layers.add(skyLayer);
		GameViewLayer uiLayer = new GameViewLayer("ui");
		layers.add(uiLayer);
		
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

		mViewSize=new VectorInt(w,h);
		sizeTile = h/10;
		
		if(!mInitialized){
			//SincronizeGameObjects();
			/*
			for(int i=0; i < mGameObjects.size(); i++){
				mGameObjects.get(i).Initialize(this);
			}*/
	    	
		  mInitialized=true;
		  mCamera.Initialize(this,mViewSize,sizeTile);
			mGame.onInitializeGameView(new VectorInt(w,h),sizeTile);
		}else{
			mCamera.Initialize(this,mViewSize,sizeTile);
			mGame.onWindowSizeChange(new VectorInt(w,h),sizeTile);
			
		}
    }

	public void InsertGameObject(Drawer go, int Layer){
		layerDirty = true;
		layers.get(Layer-1).mGameObjects.add(go);
		//mGameObjects.add(go);
	}
	public void DestroyGameObject(Drawer go, int layer){
		layerDirty = true;
		layers.get(layer-1).mGameObjects.remove(go);
	}
	
	
    /**
     * Runs in a separate thread.
     * All drawing happens here.
     */
    public void run() {

        Canvas canvas = null;
		long frameStartTime;
		long frameTime;

        while (mRunning) {
			frameStartTime = System.nanoTime();
            // If we can obtain a valid drawing surface...
			
			
            if (mSurfaceHolder.getSurface().isValid()) {
				try {
					synchronized(mSurfaceHolder){
						canvas = mSurfaceHolder.lockCanvas();
						
						if(canvas == null){
							return;
						}
						canvas.drawColor(Color.BLUE);
						if(layerDirty){
							//SincronizeGameObjects();
						}
						Vector2 camCornerPosi = mGame.camera.getStartCornerPixel();
						for(int l = 0; l < layers.size(); l++){
							Drawer[] gameObjects = new Drawer[layers.get(l).mGameObjects.size()];
							gameObjects = layers.get(l).mGameObjects.toArray(gameObjects);
							for(int i = 0; i < gameObjects.length; i++){
								//int mGameObjectsSize  = layers.get(l).mGameObjects.size();
								//for(int i = 0 ; i <mGameObjectsSize ; i++){
								if(gameObjects[i]!=null){
									Drawer go = gameObjects[i];
									//go.Update();
									go.Draw(canvas, camCornerPosi);
								}

							}
						}
						canvas.save();
						canvas.restore();
					}
				} catch (Exception ex){
					System.out.println("ERROR "+ex);
				} finally {
					if(canvas!=null){
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					}
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
        mRunning = false;
		
        try {
            // Stop the thread == rejoin the main thread.
            mGameViewThread.join();
        } catch (InterruptedException e) {
        }
		
    }

    /**
     * Called by MainActivity.onResume() to start a thread.
     */
    public void resume() {
        mRunning = true;
        mGameViewThread = new Thread(this);
		mGameViewThread.setName("ViewThread");
        mGameViewThread.start();
    }

	public void destroy(){
		mRunning = false;
		if(mGameViewThread != null){
			mGameViewThread = null;
		}
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		
        float x = event.getX();
        float y = event.getY();

        // Invalidate() is inside the case statements because there are
        // many other motion events, and we don't want to invalidate
        // the view for those.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
				//System.out.println("Nome:"+mGameViewThread.getName());
				mInputSystem.onTouchDown(x,y);
				
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
				mInputSystem.onTouchMove(x,y);
                invalidate();
                break;
				
			case MotionEvent.ACTION_UP:
				mInputSystem.onTouchUp(x,y);
                invalidate();
                break;
            default:
                // Do nothing.
        }
        return true;
    }
	
}

class GameViewLayer{
	public String name;
	public List<Drawer> mGameObjects = new ArrayList<Drawer>();
	public List<Drawer> mGameObjectsToDestroy = new ArrayList<Drawer>();
    public List<Drawer> mGameObjectsToAdd = new ArrayList<Drawer>();
	public GameViewLayer(String layerName){
		name= layerName;
	}
}
