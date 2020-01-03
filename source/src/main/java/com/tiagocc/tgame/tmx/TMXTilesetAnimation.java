package com.tiagocc.tgame.tmx;

public class TMXTilesetAnimation {
	public int id;
	public int[][] frames;

	@Override
	public String toString()
	{
		// TODO: Implement this method
		String r = "	*Animation \n";
		r += "		-ID: "+id+"\n";
		r += "		-FrameCount: "+frames.length+"\n";
		r += "		-Frames\n";
		for(int[] frame : frames){
			r += "			-Gid/Duration: "+frame[0]+"/"+frame[1]+"\n";
		}
		return r;
	}
}
