package cz.robyer.gamework.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
import cz.robyer.gamework.scenario.Area;
import cz.robyer.gamework.scenario.Hook;
import cz.robyer.gamework.scenario.MultiPointArea;
import cz.robyer.gamework.scenario.PointArea;
import cz.robyer.gamework.scenario.Reaction;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.Variable;
import cz.robyer.gamework.util.Log;
import cz.robyer.gamework.util.Point;

public class ScenarioParser {
	private static final String TAG = ScenarioParser.class.getSimpleName();
	
	// We don't use namespaces
    private static final String ns = null;
	
	public Scenario parse(InputStream input) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
	    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	    parser.setInput(input, null);
	    parser.nextTag();
	    
	    return readFeed(parser);
	}
	
	private Scenario readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
	    Scenario scenario = null;
	    
	    parser.require(XmlPullParser.START_TAG, ns, "scenario");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the entry tag
	        if (scenario == null) {
	        	if (name.equalsIgnoreCase("about")) {
	        		scenario = readAbout(parser);
	        	} else {
	        		throw new IllegalStateException("First child in <scenario> must be <about>.");
	        	}
	        } else {
		        if (name.equalsIgnoreCase("areas"))
		        	scenario.setAreas(readAreas(parser));
		    	else if (name.equalsIgnoreCase("variables"))
		    		scenario.setVariables(readVariables(parser));
				else if (name.equalsIgnoreCase("reactions"))
			    	scenario.setReactions(readReactions(parser));
				else if (name.equalsIgnoreCase("hooks"))
					scenario.setHooks(readHooks(parser));
		        else
		            skip(parser);
	    	}
	    }
	    
	    return scenario;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    Log.i(TAG, "Skipping unknown child.");
		if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	}

	private ArrayList<Hook> readHooks(XmlPullParser parser) {
		Log.i("ScenarioParser", "Reading hooks");
		// TODO Auto-generated method stub
		return null;
	}

	private HashMap<String, Reaction> readReactions(XmlPullParser parser) {
		Log.i("ScenarioParser", "Reading reactions.");
		// TODO Auto-generated method stub
		return null;
	}

	private HashMap<String, Variable> readVariables(XmlPullParser parser) {
		Log.i("ScenarioParser", "Reading variables.");
		// TODO Auto-generated method stub
		return null;
	}

	private HashMap<String, Area> readAreas(XmlPullParser parser) throws XmlPullParserException, IOException {
		Log.i("ScenarioParser", "Reading areas.");
		
		HashMap<String, Area> areas = new HashMap<String, Area>();
		
		parser.require(XmlPullParser.START_TAG, ns, "areas");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			
			Area area = null;
	        String name = parser.getName();

	        if (name.equalsIgnoreCase("area")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "area");
	        	String id = parser.getAttributeValue(null, "id");
	        	String type = parser.getAttributeValue(null, "type");
	        	
	        	if (type.equalsIgnoreCase(Area.TYPE_POINT)) {
	        		String latitude = parser.getAttributeValue(null, "lat");
	        		String longitude = parser.getAttributeValue(null, "lon");
	        		String radius = parser.getAttributeValue(null, "radius");
	        		
	        		Point point = new Point(Double.parseDouble(latitude), Double.parseDouble(longitude));
	        		area = new PointArea(id, point, Integer.parseInt(radius));
	        		Log.i(TAG, "Got PointArea.");
	        		
	        		parser.nextTag();
	        	} else if (type.equalsIgnoreCase(Area.TYPE_MULTIPOINT)) {
	        		area = new MultiPointArea(id);
	        		
	        		while (parser.next() != XmlPullParser.END_TAG) {
	        			if (parser.getEventType() != XmlPullParser.START_TAG) {
	        	            continue;
	        	        }
	        		
	        			if (parser.getName().equalsIgnoreCase("point")) {			
			        		String latitude = parser.getAttributeValue(null, "lat");
			        		String longitude = parser.getAttributeValue(null, "lon");
			        		
			        		Point point = new Point(Double.parseDouble(latitude), Double.parseDouble(longitude));
			        		((MultiPointArea)area).addPoint(point);
			        		Log.i(TAG, "Got PointArea->Point.");
			        		
			        		parser.nextTag();
	        			} else {
	        				Log.e(TAG, "Expected <point>, got <" + parser.getName() + ">.");
	        				skip(parser);
	        			}
	        		}
	        		
	        	} else {
	        		Log.e(TAG, "Area of type '" + type + "' is unknown.");
	        		skip(parser);
	        		continue;
	        	}
	        	
	        	areas.put(id, area);
	        	parser.require(XmlPullParser.END_TAG, ns, "area");
	        } else {
	        	skip(parser);
	        }
	    }
		
	    return areas;
	}

	private Scenario readAbout(XmlPullParser parser) throws XmlPullParserException, IOException {
		Log.i(TAG, "Reading about.");
		
		parser.require(XmlPullParser.START_TAG, ns, "about");
		
		String title = null;
		String author = null;
		String version = null;
		String location = null;
		String duration = null;
		String difficulty = null;
		
		while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("title")) {
	            title = readText(parser, "title");
	        } else if (name.equals("author")) {
	        	author = readText(parser, "author");
	        } else if (name.equals("version")) {
	        	version = readText(parser, "version");
	        } else if (name.equals("location")) {
	        	location = readText(parser, "location");
	        } else if (name.equals("duration")) {
	        	duration = readText(parser, "duration");
	        } else if (name.equals("difficulty")) {
	        	difficulty = readText(parser, "difficulty");
	        } else {
	            skip(parser);
	        }
	    }
		
	    return new Scenario(title, author, version, location, duration, difficulty);
	}
	
	private String readText(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, tag);
		
		String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    
	    parser.require(XmlPullParser.END_TAG, ns, tag);
	    return result;
	}

}
