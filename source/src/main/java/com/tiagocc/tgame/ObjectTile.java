package com.tiagocc.tgame;
import java.util.*;
import java.io.*;

public class ObjectTile implements Serializable{
	public String name;
	public String type;
	public VectorInt position;
	public List<Property> properties = new ArrayList<>();
	public ObjectTile(String name, String type, VectorInt position){
		this.name= name;
		this.type = type;
		this.position = position;
	}
	public ObjectTile(String name, String type, VectorInt position, List<Property> properties){
		this.name= name;
		this.type = type;
		this.position = position;
		this.properties = properties;
	}
}
