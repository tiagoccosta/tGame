package com.tiagocc.tgame;
import java.util.*;
import org.xmlpull.v1.*;
import java.io.*;
import android.util.*;

public class XmlMapManager
{
	
	
	public static String writeTileSetXml(TileSet tileset, String imgPath){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			
			serializer.startTag("", "tileset");
			
			serializer.startTag("","name");
			serializer.text(tileset.name);
			serializer.endTag("","name");
			
			serializer.startTag("","image");
			serializer.text(imgPath);
			serializer.endTag("","image");
			
			serializer.startTag("","size");
			serializer.attribute("", "number", String.valueOf(tileset.tileCount));
			serializer.startTag("","x");
			serializer.text(String.valueOf(tileset.size.x));
			serializer.endTag("","x");
			serializer.startTag("","y");
			serializer.text(String.valueOf(tileset.size.y));
			serializer.endTag("","y");
			serializer.endTag("","size");
			
			serializer.endTag("","tileset");
			
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	


	public static String writeMapXml(TileSet tileset, String imgPath){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);

			serializer.startTag("", "tileset");

			serializer.startTag("","name");
			serializer.text(tileset.name);
			serializer.endTag("","name");

			serializer.startTag("","image");
			serializer.text(imgPath);
			serializer.endTag("","image");

			serializer.startTag("","size");
			serializer.attribute("", "number", String.valueOf(tileset.tileCount));
			serializer.startTag("","x");
			serializer.text(String.valueOf(tileset.size.x));
			serializer.endTag("","x");
			serializer.startTag("","y");
			serializer.text(String.valueOf(tileset.size.y));
			serializer.endTag("","y");
			serializer.endTag("","size");

			serializer.endTag("","tileset");

			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
