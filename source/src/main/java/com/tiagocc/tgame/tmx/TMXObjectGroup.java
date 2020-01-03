package com.tiagocc.tgame.tmx;

public class TMXObjectGroup{
	public String name;
	public TMXObject[] objects;

	@Override
	public String toString()
	{
		// TODO: Implement this method
		String r ="ObjectGroup "+name+"\n";
		for(TMXObject object : objects){
			r += object.toString();
		}
		return r;
	}
}
