package com.tiagocc.tgame.tmx;

public class TMXObject{
	public int id;
	public String name, type;
	public int x, y, width, height;

	@Override
	public String toString()
	{
		// TODO: Implement this method
		String r = "  *Object "+id+"\n";
		r += "	*Name: "+name+"\n";
		r += "	*Type: "+type+"\n";
		r += "	*Width/Height: "+width+"/"+height+"\n";
		r += "	*Position(X/Y): "+x+"/"+y+"\n";
		return r;
	}

}
