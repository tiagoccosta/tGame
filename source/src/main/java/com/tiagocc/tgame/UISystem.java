package com.tiagocc.tgame;

import java.util.*;
import android.graphics.*;

public class UISystem extends GameComponent
{
	//public final int layer = 7;
	public static GameUIWidget selectedUI;
	public List<GameUIWidget> mWidgets = new ArrayList<GameUIWidget>();

	@Override
	public void Initialize(GameObject go, Game game)
	{
		// TODO: Implement this method
		super.Initialize(go, game);
		
		mGame.mInputSystem.addOnTouchDownListener(
			new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					selectedUI = null;
					
					GameUIWidget[] widgets = new GameUIWidget[mWidgets.size()];
					widgets = mWidgets.toArray(widgets);
					for(int i = 0; i < widgets.length; i++){
						if(widgets[i].selectableWidget){
							if(widgets[i].isInside(pos)){
								selectedUI = widgets[i];
							}
						}
					}
					if(selectedUI!=null){
						if(selectedUI.receiveInputs){
							selectedUI.touchDownListener.OnInputEvent(pos);
						}
					}
				}
			});
			
		mGame.mInputSystem.addOnTouchMoveListener(
			new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					selectedUI = null;

					GameUIWidget[] widgets = new GameUIWidget[mWidgets.size()];
					widgets = mWidgets.toArray(widgets);
					for(int i = 0; i < widgets.length; i++){
						if(widgets[i].selectableWidget){
							if(widgets[i].isInside(pos)){
								selectedUI = widgets[i];
							}else{
								if(widgets[i].clicked){
									widgets[i].touchUpListener.OnInputEvent(pos);
								}
							}
						}
					}
					if(selectedUI!=null){
						if(selectedUI.receiveInputs){
							selectedUI.touchMoveListener.OnInputEvent(pos);
						}
					}
				}
			});
			
		mGame.mInputSystem.addOnTouchUpListener(
			new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					selectedUI = null;

					GameUIWidget[] widgets = new GameUIWidget[mWidgets.size()];
					widgets = mWidgets.toArray(widgets);
					for(int i = 0; i < widgets.length; i++){
						if(widgets[i].selectableWidget){
							if(widgets[i].isInside(pos)){
								selectedUI = widgets[i];
							}else{
								if(widgets[i].clicked){
									widgets[i].touchUpListener.OnInputEvent(pos);
								}
							}
						}
					}
					if(selectedUI!=null){
						if(selectedUI.receiveInputs){
							selectedUI.touchUpListener.OnInputEvent(pos);
						}
					}
				}
			});
	}
	
	
	
	public void ShowAlertPainel(String title, GameButton okButton , int backgroundColor){

	}
	public void ShowAlertPainel(String title, GameButton cancelButton, GameButton okButton , int backgroundColor){

	}
	public void ShowSimpleVerticalPainel(String title, GameButton button1, GameButton button2){
		
	}
	
	public void ShowConversation(Bitmap avatar, String[] texts, int backgroundColor){
		
	}
	public void ShowConversation(Bitmap avatar, String[] texts, Bitmap backgroundImage){

	}

	/*
	@Override
	public void Draw(Canvas canvas, Vector2 cornerCam){
		for(int i=0; i < Components.size(); i++){
			//Components.get(i).Draw(canvas);
		}
		//canvas.drawBitmap(image, gameObject.getPosition().x, gameObject.getPosition().y,null);
	}
	*/
}

class GameUIWidget extends Drawer{
	
	public GameInputListener touchDownListener;
	public GameInputListener touchMoveListener;
	public GameInputListener touchUpListener;
	public boolean clicked = false;
	public boolean selectableWidget = true;
	public boolean receiveInputs = false;
	public VectorInt mPosition;
	Vector2 mPositionBilinear;
	public VectorInt mSize;
	Vector2 mSizeBilinear;

	@Override
	public void Initialize(GameObject go, Game game)
	{
		// TODO: Implement this method
		super.Initialize(go, game);
		mGame.uiSystem.mWidgets.add(this);
	}
	
	//public Bitmap background;
	//public Color backgroundColor;
	

	

	@Override
	public void onDestroy()
	{
		mGame.uiSystem.mWidgets.remove(this);
		//InputSystem mInputSystem = mGame.mInputSystem;
		//mInputSystem.removeOnTouchDownListener(touchDownListener);
		//mInputSystem.removeOnTouchMoveListener(touchMoveListener);
		//mInputSystem.removeOnTouchUpListener(touchUpListener);
		// TODO: Implement this method
		super.onDestroy();

	}
	
	@Override
	public void Draw(Canvas canvas, Vector2 cornerCam)
	{
		if(image!=null){
			canvas.drawBitmap(
				image,
				imageRectangle,
				rectangle,
				null);
		}else{
			//Paint paint = new Paint();
			//paint.setColor(color);
			canvas.drawRect(rectangle,paint);
		}
	}

	@Override
	public void CalculeRects()
	{
		if(!mInitialized){return;}
		if(mPositionBilinear!=null){
			Vector2 camSize = mGame.camera.getCameraSizePixel();
			mPosition = new VectorInt(
				(int)(mPositionBilinear.x*camSize.x),
				(int)(mPositionBilinear.y*camSize.y));
		}
		if(mSizeBilinear!=null){
			Vector2 camSize = mGame.camera.getCameraSizePixel();
			mSize = new VectorInt(
				(int)(mSizeBilinear.x*camSize.x),
				(int)(mSizeBilinear.y*camSize.y));
		}
		float left = mPosition.x;
		float right = mPosition.x + mSize.x;
		float top = mPosition.y;
		float bottom = mPosition.y + mSize.y;

		rectangle.set(
			left,
			top,
			right,
			bottom
		);
	}
	
	public  void setImageInAtlas(int row, int col, int rowCount, int colCount, Bitmap atlas)  {
        // createBitmap(bitmap, x, y, width, height).
		VectorInt tSize = new VectorInt((atlas.getWidth()/colCount),(atlas.getHeight()/rowCount));
        //Bitmap subImage = Bitmap.createBitmap(atlas, col* size.x, row*  size.y, 
		//	size.x,size.y);
		this.image = atlas;
		this.imageRectangle = new Rect(col*tSize.x,row*tSize.y,(col*tSize.x)+tSize.x,(row*tSize.y)+tSize.y);

		if(this.mInitialized){
			CalculeRects();
		}

    }

	public  void setImageInAtlas(Rect rectangle, Bitmap atlas)  {
   		this.image = atlas;
		this.imageRectangle = rectangle;

		if(this.mInitialized){
			CalculeRects();
		}

    }
	
	public void setPosition(VectorInt position){
		mPosition = position;
		mPositionBilinear = null;
		CalculeRects();
	}
	public void setPositionBilinear(Vector2 position){
		mPositionBilinear = position;
		CalculeRects();
	}
	public void setSize(VectorInt size){
		mSize = size;
		mSizeBilinear = null;
		CalculeRects();
	}
	public void setSizeBilinear(Vector2 size){
		mSizeBilinear = size;
		CalculeRects();
	}
	
	public boolean isInside(Vector2 pos){
		if(rectangle==null){return false;}
		if(pos.x > rectangle.left &&
		   pos.x< rectangle.right &&
		   pos.y > rectangle.top &&
		   pos.y < rectangle.bottom){
			return true;
		}else{
			return false;
		}
	}
	
	public void SetBlockTouch(boolean block){
		this.selectableWidget= block;
	}
	
	// Retorna um valos de sendo 0 o comeco e 1 o final do objeto
	public Vector2 getInsideBilinearPosition(VectorInt pos){
		return new Vector2(
				(pos.x-rectangle.left)/mSize.x,
				(pos.y-rectangle.top)/mSize.y
			);
	}
}

class GamePanel extends GameUIWidget{
	public GamePanel (Bitmap image, Vector2 position, Vector2 size){
		this.image = image;
		mPositionBilinear = position; 
		mSizeBilinear = size;
	}
	public GamePanel (Bitmap image, VectorInt position, VectorInt size){
		this.image = image;
		mPosition = position; 
		mSize = size;
	}
	public GamePanel (int color, Vector2 position, Vector2 size){
		this.color = color;
		mPositionBilinear = position; 
		mSizeBilinear = size;
	}
	public GamePanel (int color, VectorInt position, VectorInt size){
		this.color = color;
		mPosition = position; 
		mSize = size;
	}
}

class GameText extends GameUIWidget{
	String mText;
	Rect textRectangle;
	public GameText (String text, VectorInt position, VectorInt size){
		mText = text;
		mPosition = position; 
		mSize = size;
		selectableWidget=false;
	}

	@Override
	public void CalculeRects()
	{
		if(!this.mInitialized){return;}
		// TODO: Implement this method
		super.CalculeRects();
		paint.setTextAlign(Paint.Align.CENTER);
	}
	
	@Override
	public void Draw(Canvas canvas, Vector2 cornerCam)
	{
		//   Paint paint = new Paint();
		//paint.breakText(mText,true,mSize.x,null);
		//canvas.drawText(mText,mPosition.x,mPosition.y,paint);
		canvas.drawText(mText,rectangle.centerX(),rectangle.centerY(),paint);
	}
	
}

class GameButton extends GameUIWidget{
	
	private List<ActionListener> onClickListeners = new ArrayList<>();
	private List<ActionListener> onClickStayListeners = new ArrayList<>();
	private List<ActionListener> onClickDownListeners = new ArrayList<>();
	
	Bitmap backgroundOriginal;
	public Bitmap backgroundHover;
	int backgroundColorOriginal;
	public int backgroundColorHover;
	public GameButton (VectorInt position, VectorInt size){
		mPosition = position; 
		mSize = size;
	}
	public GameButton (Bitmap image, VectorInt position, VectorInt size){
		this.image = image;
		backgroundHover = image;
		backgroundOriginal = image;
		mPosition = position; 
		mSize = size;
	}
	public GameButton (Bitmap image,Bitmap imageHover, VectorInt position, VectorInt size){
		this.image = image;
		backgroundHover = imageHover;
		backgroundOriginal = image;
		mPosition = position; 
		mSize = size;
	}
	public GameButton (int color, VectorInt position, VectorInt size){
		this.color = color;
		backgroundColorOriginal = color;
		backgroundColorHover = color;
		mPosition = position; 
		mSize = size;
	}
	public GameButton (int color, int colorHover, VectorInt position, VectorInt size){
		this.color = color;
		backgroundColorOriginal = color;
		backgroundColorHover = colorHover;
		mPosition = position; 
		mSize = size;
	}
	
	@Override
	public void Initialize(GameObject go, Game game)
	{
		// TODO: Implement this method
		super.Initialize(go, game);
		receiveInputs = true;
		selectableWidget = true;
		CreateInputEvents();
	}

	@Override
	public void Draw(Canvas canvas, Vector2 cornerCam)
	{
		// TODO: Implement this method
		super.Draw(canvas, cornerCam);
		
	}
	
	

	void CreateInputEvents(){
		//InputSystem mInputSystem = mGame.mInputSystem;
		touchDownListener =
			new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					clicked= isInside(pos);
					if(clicked){
						System.out.println("Botao apertado");
						if(image!=null){
							image = backgroundHover;
						}else{
							color = backgroundColorHover;
						}
						FireEventsOnClickDown(pos);
					}
				}
			};
		//mInputSystem.addOnTouchDownListener( touchDownListener );
		touchMoveListener = 
			new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					if(clicked){
						if(isInside(pos)){
							System.out.println("Moveu dentrp do botao");
							if(image!=null){
								image = backgroundHover;
							}else{
								color = backgroundColorHover;
							}
							FireEventsOnClickStay(pos);
						}else{
							System.out.println("Moveu fora do botao");
							if(image!=null){
								image = backgroundOriginal;
							}else{
								color = backgroundColorOriginal;
							}
						}
					}
				}
			};
		//mInputSystem.addOnTouchMoveListener(touchMoveListener);
		touchUpListener = 
			new GameInputListener(){
				@Override
				public void OnInputEvent(Vector2 pos){
					if(clicked){
						System.out.println("Botao liberado");
						if(image!=null){
							image = backgroundOriginal;
						}else{
							color = backgroundColorOriginal;
						}
						if(isInside(pos)){
							FireEventsOnClick(pos);
						}
					}
				}
			};
		//mInputSystem.addOnTouchUpListener( touchUpListener);
	}
	
	void FireEventsOnClick(Vector2 pos){
		ActionListener[] actions = new ActionListener[onClickListeners.size()];
		actions = onClickListeners.toArray(actions);
		for (int i=0; i< actions.length; i++) {
			ActionListener listener = actions[i];
			if(listener != null){
				
				listener.actionPerformed(new ActionEvent(this, pos));
			}
			
		}
	}

	public void setOnClick(ActionListener listener) {
		onClickListeners.add(listener);
	}

	public void removeOnClick(ActionListener listener) {
		onClickListeners.remove(listener);
	}
	void FireEventsOnClickStay(Vector2 pos){
		ActionListener[] actions = new ActionListener[onClickStayListeners.size()];
		actions = onClickStayListeners.toArray(actions);
		for (int i=0; i< actions.length; i++) {
			ActionListener listener = actions[i];
			if(listener != null){

				listener.actionPerformed(new ActionEvent(this, pos));
			}

		}
	}

	public void setOnClickStay(ActionListener listener) {
		onClickStayListeners.add(listener);
	}

	public void removeOnClickStay(ActionListener listener) {
		onClickStayListeners.remove(listener);
	}


	void FireEventsOnClickDown(Vector2 pos){
		ActionListener[] actions = new ActionListener[onClickDownListeners.size()];
		actions = onClickDownListeners.toArray(actions);
		for (int i=0; i< actions.length; i++) {
			ActionListener listener = actions[i];
			if(listener != null){

				listener.actionPerformed(new ActionEvent(this, pos));
			}

		}
	}

	public void setOnClickDown(ActionListener listener) {
		onClickDownListeners .add(listener);
	}

	public void removeOnClickDown(ActionListener listener) {
		onClickDownListeners.remove(listener);
	}
	
	
	
}


