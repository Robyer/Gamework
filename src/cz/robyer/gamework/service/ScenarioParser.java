package cz.robyer.gamework.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
import cz.robyer.gamework.scenario.Hook;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.area.MultiPointArea;
import cz.robyer.gamework.scenario.area.PointArea;
import cz.robyer.gamework.scenario.reaction.GameReaction;
import cz.robyer.gamework.scenario.reaction.HtmlReaction;
import cz.robyer.gamework.scenario.reaction.MultiReaction;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.reaction.SoundReaction;
import cz.robyer.gamework.scenario.reaction.VariableReaction;
import cz.robyer.gamework.scenario.reaction.VibrateReaction;
import cz.robyer.gamework.scenario.variable.BooleanVariable;
import cz.robyer.gamework.scenario.variable.DecimalVariable;
import cz.robyer.gamework.scenario.variable.Variable;
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

	private HashMap<String, Reaction> readReactions(XmlPullParser parser) throws XmlPullParserException, IOException {
		Log.i("ScenarioParser", "Reading reactions.");
		
		HashMap<String, Reaction> reactions = new HashMap<String, Reaction>();
		
		parser.require(XmlPullParser.START_TAG, ns, "reactions");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			
			Reaction reaction = null;
	        String name = parser.getName();

	        if (name.equalsIgnoreCase("reaction")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "reaction");
	        	String id = parser.getAttributeValue(null, "id");
	        	String type = parser.getAttributeValue(null, "type");
	        	
	        	if (type.equalsIgnoreCase(Reaction.TYPE_MULTI)) {
	        		Log.i(TAG, "Got MultiReaction id='" + id + "'.");
	        		
	        		reaction = new MultiReaction(id);
	        		
	        		while (parser.next() != XmlPullParser.END_TAG) {
	        			if (parser.getEventType() != XmlPullParser.START_TAG) {
	        	            continue;
	        	        }
	        			
	        			if (parser.getName().equalsIgnoreCase("reaction")) {			
	        				Reaction innerReaction = readReaction(parser, "");
		        			((MultiReaction)reaction).addReaction(innerReaction);
	        			} else {
	        				Log.e(TAG, "Expected <reaction>, got <" + parser.getName() + ">.");
	        				skip(parser);
	        			}
	        		}
	        		
	        	} else {
	        		reaction = readReaction(parser, id);
	        	}

	        	reactions.put(id, reaction);
	        	parser.require(XmlPullParser.END_TAG, ns, "reaction");
	        } else {
	        	skip(parser);
	        }
	    }
		
	    return reactions;
	}

	private Reaction readReaction(XmlPullParser parser, String id) throws XmlPullParserException, IOException {
		Reaction reaction = null;
		parser.require(XmlPullParser.START_TAG, ns, "reaction");
		
		String type = parser.getAttributeValue(null, "type");
		String value = parser.getAttributeValue(null, "value"); // null for game reactions
		String variable = parser.getAttributeValue(null, "variable"); // only for variable reactions, null otherwise
		
		Log.i(TAG, "Got Reaction id='" + id + "' type='" + type + "'.");
		
		if (type.equalsIgnoreCase(Reaction.TYPE_SOUND)) {
			reaction = new SoundReaction(id, value);
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_VIBRATE)) {
    		reaction = new VibrateReaction(id, Integer.parseInt(value));
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_HTML)) {
    		reaction = new HtmlReaction(id, value);
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_DECREMENT)) {
    		reaction = new VariableReaction(id, VariableReaction.DECREMENT, variable, value);
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_INCREMENT)) {
    		reaction = new VariableReaction(id, VariableReaction.INCREMENT, variable, value);
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_SET)) {
    		reaction = new VariableReaction(id, VariableReaction.SET, variable, value);
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_GAME_START)) {
    		reaction = new GameReaction(id, GameReaction.START);
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_GAME_WIN)) {
    		reaction = new GameReaction(id, GameReaction.WIN);
    	} else if (type.equalsIgnoreCase(Reaction.TYPE_GAME_LOSE)) {
    		reaction = new GameReaction(id, GameReaction.LOSE);
    	} else {
    		Log.e(TAG, "Reaction of type '" + type + "' is unknown.");
    		skip(parser);
    		return null;
    	}
    		
		parser.nextTag();

    	parser.require(XmlPullParser.END_TAG, ns, "reaction");
    	return reaction;
	}

	private HashMap<String, Variable> readVariables(XmlPullParser parser) throws XmlPullParserException, IOException {
		Log.i("ScenarioParser", "Reading variables.");
		HashMap<String, Variable> variables = new HashMap<String, Variable>();
		
		parser.require(XmlPullParser.START_TAG, ns, "variables");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			
			Variable variable = null;
	        String name = parser.getName();

	        if (name.equalsIgnoreCase("variable")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "variable");
	        	String id = parser.getAttributeValue(null, "id");
	        	String type = parser.getAttributeValue(null, "type");
	        	
	        	if (type.equalsIgnoreCase(Variable.TYPE_BOOLEAN)) {
	        		String value = parser.getAttributeValue(null, "value");
	        		
	        		variable = BooleanVariable.fromString(id, value);
	        		Log.i(TAG, "Got BooleanVariable.");
	        		
	        		parser.nextTag();
	        	} else if (type.equalsIgnoreCase(Variable.TYPE_DECIMAL)) {
	        		String value = parser.getAttributeValue(null, "value");
	        		String min = parser.getAttributeValue(null, "min");
	        		String max = parser.getAttributeValue(null, "max");
	        		
	        		variable = DecimalVariable.fromString(id, value, min, max);
	        		Log.i(TAG, "Got DecimalVariable.");
	        		
	        		parser.nextTag();
	        	} else {
	        		Log.e(TAG, "Variable of type '" + type + "' is unknown.");
	        		skip(parser);
	        		continue;
	        	}
	        	
	        	variables.put(id, variable);
	        	parser.require(XmlPullParser.END_TAG, ns, "variable");
	        } else {
	        	skip(parser);
	        }
	    }
		
	    return variables;
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
