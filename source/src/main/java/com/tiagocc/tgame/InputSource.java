package com.tiagocc.tgame;

public class InputSource{
	public Vector2 position;
	public InputMotionType motionType;
	public InputSource(Vector2 posi, InputMotionType inputType){
		position = posi;
		motionType = inputType;
	}
}
