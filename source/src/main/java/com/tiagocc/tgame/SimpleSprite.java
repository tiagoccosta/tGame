package com.tiagocc.tgame;
import android.graphics.*;

public class SimpleSprite extends Drawer {
	//private Bitmap mAtlas;
	public int time=1000;
	private long timePerFrame;
	private long timeToLastChange;
	private int rCount;
	private int cCount;
	private int colIndex=0;
	private int rowIndex=0;
	private boolean end = false;
	public SimpleSprite(int rowCount, int colCount, Bitmap atlas)  {
		cCount = colCount;
		rCount = rowCount;
		this.image = atlas;
		setRect();
    }

	public void SetAnimationTime(int animationTime){
		time = animationTime;
		timePerFrame = (time)/(rCount*cCount);
		timePerFrame *= 1000000;
		timeToLastChange = System.nanoTime();
	}

	@Override
	public void Start()
	{
		// TODO: Implement this method
		timePerFrame = (time)/(rCount*cCount);
		timePerFrame *= 1000000;
		timeToLastChange = System.nanoTime();
	}

	@Override
	public void Update()
	{
		if(System.nanoTime()-timeToLastChange>timePerFrame){
			timeToLastChange = System.nanoTime();


			colIndex++;
			if(colIndex>=cCount){
				colIndex = 0;
				rowIndex++;
				if(rowIndex>=rCount){
					rowIndex = 0;
					end = true;
				}
			}
			setRect();
		}
	}

	public void setRect ()  {
		//VectorInt size = new VectorInt((mAtlas.getWidth()/cCount),(mAtlas.getHeight()/rCount));
        //Bitmap subImage = Bitmap.createBitmap(
		//	mAtlas, colIndex* size.x, rowIndex*  size.y, size.x,size.y);
        //setImage( subImage);
		VectorInt tSize = new VectorInt((this.image.getWidth()/cCount),(this.image.getHeight()/rCount));
		this.imageRectangle = new Rect(colIndex*tSize.x,rowIndex*tSize.y,(colIndex*tSize.x)+tSize.x,(rowIndex*tSize.y)+tSize.y);

    }
}
