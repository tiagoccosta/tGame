package com.tiagocc.tgame;
import android.graphics.*;

public class Drawer extends GameComponent{

	protected Paint paint;
	protected Bitmap image;
	protected int color =Color.RED;
	protected VectorInt windowSize;
	protected int sizeTile;
	public int getSizeTile(){return sizeTile;}
	protected RectF rectangle;
	public RectF getRectangle(){return rectangle; }
	protected RectF rectangleRelative;
	public RectF getRectangleRelative(){return rectangleRelative; }
	protected Rect imageRectangle;
	public Rect getRectangleInImage(){return imageRectangle;}
	//public int layer;

	@Override
	public void Initialize(GameObject go, Game game){
		this.gameObject = go;
		this.mGame=game;
		//color = Color.YELLOW;
		this.mGame.mGameView.InsertGameObject(this, gameObject.layer);
		paint = new Paint();
		paint.setColor(color);
		rectangle = new RectF();
		rectangleRelative= new RectF();
		sizeTile = this.mGame.mGameView.sizeTile;
		windowSize = this.mGame.mGameView.mViewSize;
		if(image != null){
			CalculeRects();
		}

		this.mInitialized = true;
		this.Awake();
	}
	@Override
	public void onDestroy(){
		if(this.mGame == null){
			System.out.println("Drawer não destruido pois mGame = null");
			return;}
		mGame.mGameView.DestroyGameObject(this, this.gameObject.layer);
		removed = true;
		//System.out.println("Drawer destruido...");
	}

	public void setImage(Bitmap bitmap){
		image = bitmap;
		imageRectangle = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		if(this.mInitialized){
			CalculeRects();
		}
	}
	public void setColor(int color){
		this.color = color;
		if(paint == null){paint = new Paint();}
		paint.setColor(color);
	}

	@Override
	public void onUpdatePosition(Vector2 position)
	{
		//if(image != null){
		CalculeRects();
		//}
	}



	@Override
	public void onUpdateSize(Vector2 size)
	{
		//if(image != null){
		CalculeRects();
		//}
	}

	@Override
	public void onWindowSizeChange(VectorInt wSize, int unitsPerTile){
		sizeTile = unitsPerTile;
		windowSize = wSize;
		if(this.mInitialized){
			CalculeRects();
		}
	}

	public void CalculeRects(){
		//image = Bitmap.createScaledBitmap(image,size.x,size.y,false);
		Vector2 pos= new Vector2(
			(this.gameObject.getPosition().x*this.sizeTile),
			(this.gameObject.getPosition().y*this.sizeTile));

		Vector2 size= new Vector2(
			(this.gameObject.getSize().x*this.sizeTile),
			(this.gameObject.getSize().y*this.sizeTile));


		//VectorInt size = this.gameObject.getSize()*this.sizeTile;
		float left = pos.x-(size.x/2);
		float right = pos.x + (size.x /2);
		float top = pos.y -(size.y/2);
		float bottom = pos.y +(size.y/2);

		rectangle.set(
			left,
			top,
			right,
			bottom
		);
	}

	public void Draw(Canvas canvas, Vector2 cornerCam){
		if(!this.mInitialized){return;}
		rectangleRelative.set(
			(rectangle.left)-cornerCam.x,
			(rectangle.top)-cornerCam.y,
			(rectangle.right)-cornerCam.x,
			(rectangle.bottom)-cornerCam.y);
		if(image!=null){
			//Vector2 camCornerPosi = mGame.camera.getStartCornerPixel();

			canvas.drawBitmap(
				image,
				imageRectangle,
				rectangleRelative,
				null);

		}else{

			canvas.drawRect(rectangleRelative,paint);
		}
	}

	public Bitmap getImage(){
		return image;
	}
}
