package com.tiagocc.tgame;
import java.io.*;

public class Property implements Serializable{
	public final static int STRING = 0;
	public final static int FLOAT = 1;
	public final static int INT = 2;
	public final static int BOOL = 3;
	public final static int COLOR = 4;
	public final static int FILE = 5;
	public final static String[] TYPES =new String[]{"string","float","int","bool","color","file"};

	public int type;
	public String name;
	public String value;
}
