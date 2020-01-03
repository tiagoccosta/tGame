package com.tiagocc.tgame.tmx;

public class TMXLayer{
	public String name; 
	public int width, height;
	public int[][] data;

	@Override
	public String toString()
	{
		// TODO: Implement this method
		String r ="Layer "+name+"\n";
		r += "	*Width/Height: "+width+"/"+height+"\n";
		String strData = "";
		for(int[] lines : data){
			for(int gid : lines){
				strData += gid+"-";
			}
		}
		r += "	*Data: "+strData+"\n";
		return r;
	}
}
