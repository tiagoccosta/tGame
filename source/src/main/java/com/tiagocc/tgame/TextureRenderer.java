package com.tiagocc.tgame;
import android.graphics.*;

public class TextureRenderer extends Drawer{

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

}
