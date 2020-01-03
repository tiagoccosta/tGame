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
/*

public class Explosion extends GameObject {
	private int rowIndex = 0 ;
	private int colIndex = 0 ;
	private boolean finish= false;
//	private GameSurface gameSurface;

	public Explosion(GameView gameView, Bitmap image, Vector2 position) {
	//	super(gameView, image, 5, 5, position);
	//	this.image = this.createSubImageAt(2,2);
		//this.gameSurface= GameSurface;
	}
	

	@Override
	public void Update() {
		finish = true;
		if(this.finish){return;}
		

		// Play sound explosion.wav.
		if(this.colIndex==0 && this.rowIndex==0) {
			//this.gameSurface.playSoundExplosion();
		}

		if(this.colIndex >= this.colCount) {
			this.colIndex =0;
			this.rowIndex++;
			
			if(this.rowIndex >= this.rowCount) {
				this.finish= true;
				Destroy();
				return;
			}
		}
		//image = this.createSubImageAt(rowIndex,colIndex);
		this.colIndex++;
	}
	
	public boolean isFinish() {
		return finish;
	}

}

*/
