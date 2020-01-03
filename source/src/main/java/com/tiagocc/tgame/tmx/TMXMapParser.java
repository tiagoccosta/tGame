package com.tiagocc.tgame.tmx;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import java.util.zip.*;

public class TMXMapParser{
	public static TMXMap parse(String fileName, OnXMLRequestListener xmlSourceListener){
		TMXMap map = new TMXMap();
		String xml = xmlSourceListener.getXML(fileName);
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new StringReader(xml)));
			Element root = document.getDocumentElement();

			map. width = Integer.parseInt(root.getAttribute("width")) ;    //returns specific attribute
			map. height = Integer.parseInt(root.getAttribute("height"));
			//map.name = root.getAttribute("name");
			//map.nextobjectid =Integer.parseInt( root.getAttribute("nextobjectid"));
			map.infinite = Integer.parseInt(root.getAttribute("infinite"));
			map.renderorder = root.getAttribute("renderorder");

			map.tilewidth = Integer.parseInt(root.getAttribute("tilewidth")) ; 
			map.tileheight = Integer.parseInt(root.getAttribute("tileheight"));

			map.version = root.getAttribute("version");
			map.tiledversion = root.getAttribute("tiledversion");

			map.orientation = root.getAttribute("orientation");

			//////////// GET TILESETS \\\\\\\\\\
			NodeList nodesTilesets = root.getElementsByTagName("tileset");
			map.tilesets = new TMXTileset[nodesTilesets.getLength()];
			for(int i = 0; i < nodesTilesets.getLength(); i++){
				TMXTileset tileset = new TMXTileset();
				map.tilesets[i] = tileset;
				//String tilesetName, imageSource; 
				//int firstgid, tilewidth, tileheight, tilecount, columns, imagewidth, imageheight;
				Element element = (Element)nodesTilesets.item(i);
				Element tilesetElement = null;
				//NamedNodeMap tilesetAttrs = element.getAttributes();
				tileset.firstgid = Integer.parseInt(element.getAttribute("firstgid"));
				Node source = element.getAttributeNode("source");
				if(source == null){
					tilesetElement = element;
				}else{
					String xmlTileset = xmlSourceListener.getXML(source.getNodeValue());
					tilesetElement = getTilesetElementFromTSXFile(xmlTileset);
				}

				if(tilesetElement!=null){
					//tilesetAttrs = tilesetElement.getAttributes();
					tileset.name = tilesetElement.getAttribute("name");
					tileset.tilewidth = Integer.parseInt(tilesetElement.getAttribute("tilewidth"));
					tileset.tileheight = Integer.parseInt(tilesetElement.getAttribute("tileheight"));

					Element imageElement = (Element)tilesetElement.getElementsByTagName("image").item(0);
					NamedNodeMap imageAttrs = imageElement.getAttributes();
					tileset.imageSource = imageAttrs.getNamedItem("source").getNodeValue();
					tileset.imageWidth = Integer.parseInt(imageAttrs.getNamedItem("width").getNodeValue());
					tileset.imageHeight = Integer.parseInt(imageAttrs.getNamedItem("height").getNodeValue());

					Node columns =tilesetElement.getAttributeNode("columns");
					if(columns != null){
						tileset.columns = Integer.parseInt(columns.getNodeValue());
					}else{
						tileset.columns = tileset.imageWidth/tileset.tilewidth;
					}
					Node tilecount =tilesetElement.getAttributeNode("tilecount") ;
					if(tilecount!=null){
						tileset.tilecount = Integer.parseInt(tilecount.getNodeValue());
					}else{
						tileset.tilecount = tileset.columns * (tileset.imageHeight/tileset.tileheight);
					}

					NodeList tileList = tilesetElement.getElementsByTagName("tile");
					List<TMXTilesetAnimation> animations= new ArrayList<TMXTilesetAnimation>();
					//tileset.animations = new TMXTilesetAnimation[tileList.getLength()];
					for(int tileIndex = 0; tileIndex<tileList.getLength(); tileIndex++){
						//System.out.println("Openning Tile...");
						Element tile = (Element)tileList.item(tileIndex);
						NodeList tileChildNodes = tile.getElementsByTagName("animation");
						if(tileChildNodes.getLength()>0){
							Element tileChild = ((Element)(tileChildNodes.item(0)));
							//System.out.println(tileChild.getTagName());
							if(tileChild.getTagName().equalsIgnoreCase("animation")){
								//System.out.println("Openning Animation...");
								TMXTilesetAnimation anim = new TMXTilesetAnimation();
								animations.add(anim);
								anim.id = Integer.parseInt(tile.getAttribute("id"));
								NodeList frameList = tileChild.getElementsByTagName("frame");
								anim.frames = new int[frameList.getLength()][2];
								for(int frameIndex=0; frameIndex < frameList.getLength(); frameIndex++){
									Element frame = (Element)frameList.item(frameIndex);
									anim.frames[frameIndex][0]=Integer.parseInt(frame.getAttribute("tileid"));
									anim.frames[frameIndex][1]=Integer.parseInt(frame.getAttribute("duration"));
								}
							}
						}
					}
					tileset.animations = animations.toArray(new TMXTilesetAnimation[animations.size()]);
					//System.out.println("Tileset opened: "+tileset.name);
				}else{
					System.out.println("Invalid tileset");
				}


			}


			//////////// GET LAYERS \\\\\\\\\\\
			NodeList nodesLayer = root.getElementsByTagName("layer"); //returns a list of sub-elements of specified name
			map.layers = new TMXLayer[nodesLayer.getLength()];
			//Hashtable<String,int[][]> layers = new Hashtable<String,int[][]>();
			for( int i = 0; i < nodesLayer.getLength(); i++){
				TMXLayer layer =new TMXLayer();
				map.layers[i]=layer;
				Element node = (Element)nodesLayer.item(i);
				NamedNodeMap layerAttrs = node.getAttributes();
				layer.name = layerAttrs.getNamedItem("name").getNodeValue();
				layer.width = Integer.parseInt(layerAttrs.getNamedItem("width").getNodeValue()) ;    //returns specific attribute
				layer.height = Integer.parseInt(layerAttrs.getNamedItem("height").getNodeValue());
				Element dataNode = (Element)node.getElementsByTagName("data").item(0);
				String layerData = dataNode.getTextContent().trim();

				NamedNodeMap dataAttrs = dataNode.getAttributes();
				boolean isZlib = false; boolean isGzip = false;
				Node encoding = dataAttrs.getNamedItem("encoding");
				boolean isBase64 = false;
				if(encoding!=null && "base64".equalsIgnoreCase(encoding.getNodeValue())){isBase64 = true;}
				if(isBase64){
					String compression = dataAttrs.getNamedItem("compression").getNodeValue();
					if(compression.equalsIgnoreCase("zlib")){isZlib = true;}
					if(compression.equalsIgnoreCase("gzip")){isGzip = true;}
				}

				layer.data = GetTilesFromTMXFormatLayerData(layerData,layer.width,layer.height,isBase64,isZlib,isGzip);
			}

			//root.getChildNodes();                        //returns a list of all child nodes
		}
		catch (ParserConfigurationException e)
		{e.printStackTrace();}
		catch (IOException e)
		{e.printStackTrace();}
		catch (SAXException e)
		{e.printStackTrace();}


		return map;
	}



	private static int[][] GetTilesFromTMXFormatLayerData(String data, int width, int height, boolean isBase64, boolean isZlib, boolean isGzip){
		int [][] layer = new int[height][width];
		if(isBase64){
			try{
				org.apache.commons.codec.binary.Base64 codec = new org.apache.commons.codec.binary.Base64();
				byte[] dec = codec.decodeBase64( data.getBytes());
				ByteArrayInputStream bais = new ByteArrayInputStream(dec);
				InputStream is = null;
				if (isGzip) {
					is = new GZIPInputStream(bais);
				}
				if(isZlib){
					is = new InflaterInputStream(bais);
				}
				
				if(is.equals(null)){
					is = bais;
				}

				final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
				final int FLIPPED_VERTICALLY_FLAG = 0x40000000;
				final int FLIPPED_DIAGONALLY_FLAG = 0x20000000;
				final int MASK_CLEAR = 0xE0000000;
				byte[] temp = new byte[4];

				String layerSTR = "";
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int tileId = 0;


						tileId |= is.read();
						tileId |= is.read() <<  8;
						tileId |= is.read() << 16;
						tileId |= is.read() << 24;

						
    					tileId &= ~(FLIPPED_HORIZONTALLY_FLAG |
                        	FLIPPED_VERTICALLY_FLAG |
                        	FLIPPED_DIAGONALLY_FLAG);


						boolean flipHorizontally = ((tileId & FLIPPED_HORIZONTALLY_FLAG) != 0);
						boolean flipVertically = ((tileId & FLIPPED_VERTICALLY_FLAG) != 0);
						boolean flipDiagonally = ((tileId & FLIPPED_DIAGONALLY_FLAG) != 0);

						//tileId &= ~(MASK_CLEAR);
						layer[y][x] = (tileId & ~MASK_CLEAR);

						layerSTR += tileId +"|";

					}
				}
				System.out.println(layerSTR);
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			String[] tiles = data.split(",");
			int index =0;
			//String layerSTR = "";
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int tileId = Integer.parseInt(tiles[index]);
					layer[y][x] = tileId;
					index++;
					//layerSTR += tileId +"|";
				}
			}
			//System.out.println(layerSTR);
		}

		return layer;
	}

	

	private static Element getTilesetElementFromTSXFile(String tsx){
		Element root = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new StringReader(tsx)));
			root = document.getDocumentElement();
		}
		catch (ParserConfigurationException e)
		{e.printStackTrace();}
		catch (IOException e)
		{e.printStackTrace();}
		catch (SAXException e)
		{e.printStackTrace();}
		return root;
	}

}
