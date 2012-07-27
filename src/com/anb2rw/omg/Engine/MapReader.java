package com.anb2rw.omg.Engine;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.anb2rw.omg.Gameplay.Map;


public class MapReader {

	public static void read(Resources r, int xmlResourceId, Map map) {
		read(r.getXml(xmlResourceId), xmlResourceId, map);
	}
	
	private static void read(XmlResourceParser xrp, int xmlResourceId, final Map map) {
//		final TMXMap map = new TMXMap();
//		map.xmlResourceId = xmlResourceId;
		try {
			// Map format: http://sourceforge.net/apps/mediawiki/tiled/index.php?title=Examining_the_map_format
			int eventType;
//			final ArrayList<TMXLayer> layers = new ArrayList<TMXLayer>();
//			final ArrayList<TMXTileSet> tileSets = new ArrayList<TMXTileSet>();
			while ((eventType = xrp.next()) != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {
					String s = xrp.getName();
					if (s.equals("map")) {
						map.name = xrp.getAttributeValue(null, "name");
						map.BackColor = xrp.getAttributeIntValue(null, "color", -1);
						map.RoomW = xrp.getAttributeIntValue(null, "width", -1);
						map.RoomH = xrp.getAttributeIntValue(null, "height", -1);
						map.Gravity = xrp.getAttributeFloatValue(null, "gravity", 1);
//						map.tilewidth = xrp.getAttributeIntValue(null, "tilewidth", -1);
						readCurrentTagUntilEnd(xrp, new TagHandler() {
							public void handleTag(XmlResourceParser xrp, String tagName) throws XmlPullParserException, IOException {
								if (tagName.equals("layer")) {
									readTMXMapLayer(xrp, map, false);
								} else if (tagName.equals("ai")) {
									readTMXMapLayer(xrp, map, true);
								}
							}
						});
					} 
				}
            }
            xrp.close();
//            map.layers = layers.toArray(new TMXLayer[layers.size()]);
//            map.tileSets = tileSets.toArray(new TMXTileSet[tileSets.size()]);
		} catch (XmlPullParserException e) {
//			L.log("Error reading map \"" + name + "\": XmlPullParserException : " + e.toString());
		} catch (IOException e) {
//			L.log("Error reading map \"" + name + "\": IOException : " + e.toString());
		}
	}
	
	private static void readTMXMapLayer(XmlResourceParser xrp, final Map map, final boolean ai) throws XmlPullParserException, IOException {
//		String name = xrp.getAttributeValue(null, "name");
		readCurrentTagUntilEnd(xrp, new TagHandler() {
			public void handleTag(XmlResourceParser xrp, String tagName) throws XmlPullParserException, IOException {
				if (tagName.equals("data")) {
					readTMXMapLayerData(xrp, map, ai);
				}
			}
		});
	}
	
	private static void readTMXMapLayerData(XmlResourceParser xrp, Map map, boolean ai) throws XmlPullParserException, IOException {
//		String compressionMethod = xrp.getAttributeValue(null, "compression");
		xrp.next();
		String data = xrp.getText().trim();
		data=data.replaceAll("\t", "");
		data=data.replaceAll("\n", "");
		data=data.replaceAll(" ", "");
		
		int[][] tiles=new int[map.RoomH][map.RoomW];
		int x=0,y=0;
		for(int i=0;i<data.length();i++) {
			
			String tile_id = data.charAt(i)+"";
			
			if(x<map.RoomW && y<map.RoomH)
				tiles[y][x]=Integer.parseInt(tile_id);
			
			if((++x)>=map.RoomW) {
				x=0;
				if((++y)>=map.RoomH) {
					break;
				}
			}
		}
		
		if(!ai)
			map.map=tiles;
		else
			map.ai=tiles;
//		final int len = layer.width * layer.height * 4;
		
//		ByteArrayInputStream bi = new ByteArrayInputStream(Base64.decode(data));
//		if (compressionMethod == null) compressionMethod = "none";
//		
//		InflaterInputStream zi;
//		if (compressionMethod.equalsIgnoreCase("zlib")) {
//			zi = new InflaterInputStream(bi);
//		} else if (compressionMethod.equalsIgnoreCase("gzip")) {
//			zi = new GZIPInputStream(bi, len);
//		} else {
//			throw new IOException("Unhandled compression method \"" + compressionMethod + "\" for map layer " + layer.name);
//		}
//		
//		byte[] buffer = new byte[len];
//		copyStreamToBuffer(zi, buffer, len);
//		
//		zi.close();
//		bi.close();
//		int i = 0;
//		for(int y = 0; y < layer.height; ++y) {
//			for(int x = 0; x < layer.width; ++x, i += 4) {
//				int gid = readIntLittleEndian(buffer, i);
//				//if (gid != 0) L.log(getHexString(buffer, i) + " -> " + gid);
//				layer.gids[x][y] = gid;
//				//L.log("(" + x + "," + y + ") : " + layer.gids[x][y]);
//			}
//		}
	}
	
	private interface TagHandler {
		void handleTag(XmlResourceParser xrp, String tagName) throws XmlPullParserException, IOException;
	}
	private static void readCurrentTagUntilEnd(XmlResourceParser xrp, TagHandler handler) throws XmlPullParserException, IOException {
		String outerTagName = xrp.getName();
		String tagName;
		int eventType;
		while ((eventType = xrp.next()) != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_TAG) {
				tagName = xrp.getName();
				handler.handleTag(xrp, tagName);
			} else if (eventType == XmlResourceParser.END_TAG) {
				tagName = xrp.getName();
				if (tagName.equals(outerTagName)) return;
			}
		}
	}
	
}
