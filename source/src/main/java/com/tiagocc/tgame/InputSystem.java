package com.tiagocc.tgame;

import android.content.Context;
import android.graphics.*;

import android.view.*;
import android.app.*;
import android.content.res.*;
import java.util.*;
import java.util.function.*;
import org.apache.http.client.entity.*;
import javax.xml.transform.*;
import java.util.concurrent.*;

public class InputSystem extends GameComponent //implements Runnable
{
	private Game mGame;
	private Context context;
	
	private boolean mRunning;
    //private Thread mGameThread = null;

	private static final int MAX_FRAME_TIME = (int)(1000.0/60.0);
	
	//private List<InputSource> inputActions = new ArrayList <InputSource>();
	protected List inputActions = new CopyOnWriteArrayList <InputSource>();
	
	protected List< GameInputListener> OnTouchDownListenerList = new CopyOnWriteArrayList<GameInputListener>();
	//protected List<GameInputListener> downListeners = new CopyOnWriteArrayList<>();
	public void addOnTouchDownListener(GameInputListener evt){OnTouchDownListenerList.add(evt);}
	public void removeOnTouchDownListener(GameInputListener evt){OnTouchDownListenerList.remove(evt);}

	
	protected List< GameInputListener> OnTouchUpListenerList = new CopyOnWriteArrayList<GameInputListener>();
	public void addOnTouchUpListener(GameInputListener evt){OnTouchUpListenerList.add(evt);}
	public void removeOnTouchUpListener(GameInputListener evt){OnTouchUpListenerList.remove(evt);}

	
	protected List< GameInputListener> OnTouchMoveListenerList = new CopyOnWriteArrayList<GameInputListener>();
	public void addOnTouchMoveListener(GameInputListener evt){OnTouchMoveListenerList.add(evt);}
	public void removeOnTouchMoveListener(GameInputListener evt){OnTouchMoveListenerList.remove(evt);}

	
	
	
	//public List<Predicate<Vector2>> onTouchDownEvents= new ArrayList<Predicate<Vector2>>();

	public InputSystem (Game game){
		mGame = game;
		context = mGame.mContext;
		
		
	}

	@Override
	public void Update()
	{
		//System.out.println("Input System Update chamado... Thread: "+Thread.currentThread().getName());
		synchronized (inputActions){
			ListIterator i = inputActions.listIterator();
			if(i.hasNext()){
				InputSource inputAction = (InputSource)i.next();

				if(inputAction.motionType == InputMotionType.DOWN){
					FireEvent(OnTouchDownListenerList,inputAction.position);

				}
				if(inputAction.motionType == InputMotionType.UP){
					FireEvent(OnTouchUpListenerList,inputAction.position);

				}
				if(inputAction.motionType == InputMotionType.MOVE){
					FireEvent(OnTouchMoveListenerList,inputAction.position);

				}
				//i.remove();
				inputActions.remove(0);
			}
			
		}
		

	}
	
	public void run() {

        long frameStartTime;
		long frameTime;

        while (mRunning) {
			frameStartTime = System.nanoTime();

			synchronized (inputActions){
				ListIterator i = inputActions.listIterator();
				while(i.hasNext()){
					InputSource inputAction = (InputSource)i.next();
					
					if(inputAction.motionType == InputMotionType.DOWN){
						FireEvent(OnTouchDownListenerList,inputAction.position);

					}
					if(inputAction.motionType == InputMotionType.UP){
						FireEvent(OnTouchUpListenerList,inputAction.position);

					}
					if(inputAction.motionType == InputMotionType.MOVE){
						FireEvent(OnTouchMoveListenerList,inputAction.position);

					}
				}
				
			}
			
			/*
			InputSource[] actions = new InputSource[inputActions.size()];
			actions = inputActions.toArray(actions);
			if(this.mInitialized && actions.length>0){

				//System.out.println(inputActions.get(0));
				
				InputSource inputAction = actions[0];
				if(inputAction != null){

					if(inputAction.motionType == InputMotionType.DOWN){
						FireEvent(OnTouchDownListenerList,inputAction.position);

					}
					if(inputAction.motionType == InputMotionType.UP){
						FireEvent(OnTouchUpListenerList,inputAction.position);

					}
					if(inputAction.motionType == InputMotionType.MOVE){
						FireEvent(OnTouchMoveListenerList,inputAction.position);

					}
				}
				
				inputActions.remove(0);
				inputActions.remove(null);
			}
			*/

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
	 /*
    public void pause() {
        mRunning = false;

        try {
            // Stop the thread == rejoin the main thread.
            mGameThread.join();
        } catch (InterruptedException e) {
        }

    }
	*/

    /**
     * Called by MainActivity.onResume() to start a thread.
     */
	 /*
    public void resume() {
        mRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }
	*/
	
	

	protected void FireEvent(List<GameInputListener> listenerList, Vector2 pos){
		/*
		GameInputListener[] listeners = new GameInputListener[listenerList.size()];
		listeners = listenerList.toArray(listeners);
		for(int i = 0; i < listeners.length; i++){
			listeners[i].OnInputEvent(pos);
		}
		*/
		
		synchronized (listenerList){
			ListIterator i = listenerList.listIterator();
			while(i.hasNext()){
				GameInputListener listener = (GameInputListener)i.next();
				listener.OnInputEvent(pos);
			}
		}
	}

	public void onTouchDown(float x, float y){
		//FireEvent(OnTouchDownListenerList,new Vector2(x,y));
		
		InputSource source = 
			new InputSource(
			new Vector2 (x,y),
			InputMotionType.DOWN
		);
		synchronized(inputActions){
			inputActions.add(source);
		}
		
	}
	public void onTouchMove(float x, float y){
		//FireEvent(OnTouchMoveListenerList,new Vector2(x,y));
		InputSource source = 
			new InputSource(
			new Vector2 (x,y),
			InputMotionType.MOVE
		);
		synchronized(inputActions){
			inputActions.add(source);
		}
	}
	public void  onTouchUp(float x, float y ){
		//FireEvent(OnTouchUpListenerList,new Vector2(x,y));
		InputSource source = 
			new InputSource(
			new Vector2 (x,y),
			InputMotionType.UP
		);
		synchronized(inputActions){
			inputActions.add(source);
		}
		//obj.Destroy();
		//obj = null;
	}
	
}

/*
class GameInputEvent extends EventObject{
	public GameInputEvent (Object Source){
		super(Source);
	}
}
*/









