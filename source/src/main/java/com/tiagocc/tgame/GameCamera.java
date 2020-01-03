package com.tiagocc.tgame;
import java.util.*;
import android.graphics.*;

public class GameCamera{
	protected Game mGame;
	public boolean initialized;

	private List<ActionListener> onUpdatePositionListeners = new ArrayList<>();

	public VectorInt windowSize;
	public int mTileSize;
	private Vector2 cameraPosition;

	private Vector2 cameraPositionPixel;
	public Vector2 getCameraPositionPixel(){
		return cameraPositionPixel;
		//return new VectorInt((int)(cameraPosition.x*mTileSize),(int)(cameraPosition.y*mTileSize));
	}

	private Vector2 cameraSize;
	public Vector2 getCameraSize(){ return cameraSize; }
	private Vector2 cameraSizePixel;
	public Vector2 getCameraSizePixel(){
		return cameraSizePixel;
		//return new VectorInt((int)(cameraSize.x*mTileSize),(int)(cameraSize.y*mTileSize));
	}
	private RectF rectCameraView;
	public RectF getRectCameraView(){return rectCameraView;}
	private Rect rectCameraViewPixel;
	public Rect getRectCaneraViewPixel(){return rectCameraViewPixel;}
	public Vector2 getStartCornerPixel(){
		return new Vector2(
			(getCameraPositionPixel().x-(getCameraSizePixel().x/2)),
			(getCameraPositionPixel().y-(getCameraSizePixel().y/2)));
	}
	public Vector2 getEndCornerPixel(){
		return new Vector2(
			(getCameraPositionPixel().x+(getCameraSizePixel().x/2)),
			(getCameraPositionPixel().y+(getCameraSizePixel().y/2)));
	}
	/*
	 public Rect getRectanglePixel(){
	 return new Rect((int)(cameraSize.x*mTileSize),(int)(cameraSize.x*mTileSize));
	 }*/

	public GameView mGameView;
	public GameCamera(Game game, Vector2 position){
		mGame = game;
		cameraPosition = position;
	}
	public void Initialize(GameView view, VectorInt wSize, int tileSize){
		mGameView = view;
		//cameraPosition = position;
		windowSize = wSize;
		mTileSize = tileSize;
		cameraSize = new Vector2(
			wSize.x/tileSize,
			wSize.y /tileSize
		);
		//cameraSizePixel=new Vector2((cameraSize.x*mTileSize),(cameraSize.y*mTileSize));
		cameraSizePixel = new Vector2( wSize.x,wSize.y);
		cameraPositionPixel = new Vector2((cameraPosition.x*mTileSize),(cameraPosition.y*mTileSize));
		CalculeRects();

		initialized= true;
		System.out.println("Iniciando Camera...");
		System.out.println("cameraSize = "+cameraSize.x+"|"+cameraSize.y);
		System.out.println("cameraSizePixel = "+cameraSizePixel.x+"|"+cameraSizePixel.y);
		System.out.println("cameraPositionPixel = "+cameraPositionPixel.x+"|"+cameraPositionPixel.y);
		System.out.println("windowSize = "+windowSize.x+"|"+windowSize.y);
		System.out.println("mTileSize = "+mTileSize);
	}
	public void addUpdatePositionListener(ActionListener updatePpsitionListener){
		onUpdatePositionListeners.add(updatePpsitionListener);
	}
	public void removeUpdatePositionListener(ActionListener updatePpsitionListener){
		onUpdatePositionListeners.remove(updatePpsitionListener);
	}
	void fireOnUpdatePositionEvent(Vector2 posi){
		/*
		 Iterator i = onUpdatePositionListeners.iterator();
		 while(i.hasNext()){
		 ActionListener listener = (ActionListener)i.next();
		 listener.actionPerformed(new ActionEvent(this,posi));
		 }
		 */

		ActionListener[] listners = new ActionListener[onUpdatePositionListeners.size()];
		listners = onUpdatePositionListeners.toArray(listners);
		for(int i = 0; i < listners.length; i++){
			listners[i].actionPerformed(new ActionEvent(this,posi));
		}
	}
	public void setPosition(Vector2 posi){
		cameraPosition = posi;
		cameraPositionPixel = new Vector2((cameraPosition.x*mTileSize),(cameraPosition.y*mTileSize));
		CalculeRects();
		fireOnUpdatePositionEvent(posi);
		//mGame.UpdateCameraPosition(cameraPosition);
	}

	void CalculeRects (){
		rectCameraView = new RectF(
			cameraPosition.x -(cameraSize.x/2),
			cameraPosition.y -(cameraSize.y/2),
			cameraPosition.x +(cameraSize.x/2),
			cameraPosition.y +(cameraSize.y/2)
		);
		rectCameraViewPixel = new Rect(
			(int)(cameraPositionPixel.x -(cameraSizePixel.x/2)),
			(int)(cameraPositionPixel.y -(cameraSizePixel.y/2)),
			(int)(cameraPositionPixel.x +(cameraSizePixel.x/2)),
			(int)(cameraPositionPixel.y +(cameraSizePixel.y/2))
		);
	}

	public Vector2 getPosition(){
		return cameraPosition;
	}
	public Vector2 getSize(){
		return cameraSize;
	}
	public Vector2 getPointWorldPosition(Vector2 screenPoint){
		Vector2 cornerPix = getStartCornerPixel();
		Vector2 posi = new Vector2(
			(cornerPix.x+screenPoint.x)/mTileSize,
			(cornerPix.y+screenPoint.y)/mTileSize
		);

		//	System.out.println("Calculando posiçao global");
		//	System.out.println("PointX -> "+screenPoint.x);
		//	System.out.println("Calculo X = (("+screenPoint.x+"-("+screenPoint.x+"/"+2+"))+"+cameraPosition.x+")/"+mTileSize);
		//	System.out.println("PointY -> "+screenPoint.y);
		//	System.out.println("PosiX -> "+posi.x);
		//	System.out.println("PosiY -> "+posi.y);

		return posi;
	}
}
