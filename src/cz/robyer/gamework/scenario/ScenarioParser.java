package cz.robyer.gamework.scenario;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.hook.Condition;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.area.MultiPointArea;
import cz.robyer.gamework.scenario.area.PointArea;
import cz.robyer.gamework.scenario.area.SoundArea;
import cz.robyer.gamework.scenario.reaction.EventReaction;
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

public class ScenarioParser {
	private static final String TAG = ScenarioParser.class.getSimpleName();
    private static final String ns = null; // We don't use namespaces
    	
    // Area constants
	public static final String AREA_TYPE_POINT = "point";
	public static final String AREA_TYPE_SOUND = "sound";
	public static final String AREA_TYPE_MULTIPOINT = "multipoint";
	
	// Variable constants
	public static final String VAR_TYPE_BOOLEAN = "boolean";
	public static final String VAR_TYPE_DECIMAL = "decimal";
	
	// Reaction constants
	public static final String REACTION_TYPE_MULTI = "multi";
	public static final String REACTION_TYPE_SOUND = "sound";
	public static final String REACTION_TYPE_VIBRATE = "vibrate";
	public static final String REACTION_TYPE_HTML = "html";
	public static final String REACTION_TYPE_VAR_SET = "var_set";
	public static final String REACTION_TYPE_VAR_INC = "var_increment";
	public static final String REACTION_TYPE_VAR_DEC = "var_decrement";
	public static final String REACTION_TYPE_VAR_MUL = "var_multiply";
	public static final String REACTION_TYPE_VAR_DIV = "var_divide";
	public static final String REACTION_TYPE_VAR_NEG = "var_negate";
	public static final String REACTION_TYPE_EVENT = "event";
	
	// Events constants
	public static final String EVENT_GAME_START = "game_start";
	public static final String EVENT_GAME_WIN = "game_win";
	public static final String EVENT_GAME_LOSE = "game_lose";
    
    // Hook constants
	public static final String HOOK_TYPE_AREA = "area";
	public static final String HOOK_TYPE_AREA_ENTER = "area_enter";
	public static final String HOOK_TYPE_AREA_LEAVE = "area_leave";
	public static final String HOOK_TYPE_VARIABLE = "variable";
	public static final String HOOK_TYPE_TIME = "time";
	
	// Trigger constants
	public static final String TRIGGER_CONDITIONS_NONE = "none";
	public static final String TRIGGER_CONDITIONS_ALL = "all";
	public static final String TRIGGER_CONDITIONS_ANY = "any";
	
	// Condition constants
	public static final String CONDITION_TYPE_EQUALS = "equals";
	public static final String CONDITION_TYPE_NOTEQUALS = "notequals";
	public static final String CONDITION_TYPE_GREATER = "greater";
	public static final String CONDITION_TYPE_SMALLER = "smaller";
	public static final String CONDITION_TYPE_GREATEREQUALS = "greaterequals";
	public static final String CONDITION_TYPE_SMALLEREQUALS = "smallerequals";
   
    private Context context;
    private XmlPullParser parser;
    private Scenario scenario;
    
    public static Scenario fromFile(Context context, String filename) {
    	return fromFile(context, filename, false);
    }
    
    public static Scenario fromAsset(Context context, String filename) {
    	return fromAsset(context, filename, false);
    }
    
	public static Scenario fromFile(Context context, String filename, boolean aboutOnly) {
		Log.i(TAG, String.format("Loading %1s from file '%2s'", (aboutOnly ? "info" : "scenario"), filename));
		Scenario scenario = null;
		try {
			ScenarioParser parser = new ScenarioParser(context, aboutOnly);
			File file = context.getFileStreamPath(filename);
			InputStream stream = new BufferedInputStream(new FileInputStream(file));
			scenario = parser.parse(stream, false);
			stream.close(); // TODO: put this into finally?
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return scenario;
	}
	
	public static Scenario fromAsset(Context context, String filename, boolean aboutOnly) {
		Log.i(TAG, String.format("Loading %1s from asset '%2s'", (aboutOnly ? "info" : "scenario"), filename));
		Scenario scenario = null;
		try {
			ScenarioParser parser = new ScenarioParser(context, false);
			InputStream stream = context.getAssets().open(filename);
			scenario = parser.parse(stream, aboutOnly);
			stream.close(); // TODO: put this into finally?
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return scenario;
	}
    
    public ScenarioParser(Context context, boolean namespaces) throws XmlPullParserException {
    	this.context = context;
    	this.parser = Xml.newPullParser();
    	this.parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, namespaces);
    }
	
	public Scenario parse(InputStream input, boolean aboutOnly) throws XmlPullParserException, IOException {
	    scenario = null;
		
		parser.setInput(input, null);
	    parser.nextTag();
	    parser.require(XmlPullParser.START_TAG, ns, "scenario");
	    
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();

	        if (scenario == null) {
	        	if (name.equalsIgnoreCase("about")) {
	        		readScenario();
	        	} else {
	        		throw new IllegalStateException("First child in <scenario> must be <about>");
	        	}
	        	
	        	if (aboutOnly)
	        		break;
	        } else {
		        if (name.equalsIgnoreCase("areas"))
		        	readAreas();
		    	else if (name.equalsIgnoreCase("variables"))
		    		readVariables();
				else if (name.equalsIgnoreCase("reactions"))
			    	readReactions();
				else if (name.equalsIgnoreCase("hooks"))
					readHooks();
		        else
		            skip();
	    	}
	    }
	    
	    return scenario;
	}
	
	private void skip() throws XmlPullParserException, IOException {
	    Log.d(TAG, "Skipping unknown child");
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

	private void readHooks() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading hooks");

		parser.require(XmlPullParser.START_TAG, ns, "hooks");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			
			String name = parser.getName();

	        if (name.equalsIgnoreCase("hook")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "hook");
	        	
	        	String type = parser.getAttributeValue(null, "type");
	        	String value = parser.getAttributeValue(null, "value");
	        	int itype;
	        	
	        	if (type.equalsIgnoreCase(HOOK_TYPE_AREA)) {
	        		itype = Hook.TYPE_AREA;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_AREA_ENTER)) {
	        		itype = Hook.TYPE_AREA_ENTER;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_AREA_LEAVE)) {
	        		itype = Hook.TYPE_AREA_LEAVE;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_TIME)) {
	        		itype = Hook.TYPE_TIME;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_VARIABLE)) {
	        		itype = Hook.TYPE_VARIABLE;
	        	} else {
	        		Log.e(TAG, "Hook of type '" + type + "' is unknown");
	        		skip();
	        		return;
	        	}
	        	
	        	while (parser.next() != XmlPullParser.END_TAG) {
        			if (parser.getEventType() != XmlPullParser.START_TAG) {
        	            continue;
        	        }
        			
        			if (parser.getName().equalsIgnoreCase("trigger")) {
        				Hook hook = readTrigger(itype, value);
        				scenario.addHook(hook, itype, value);
        			} else {
        				Log.e(TAG, "Expected <trigger>, got <" + parser.getName() + ">");
        				skip();
        			}
        		}

	        	parser.require(XmlPullParser.END_TAG, ns, "hook");
	        } else {
	        	skip();
	        }
	    }
		parser.require(XmlPullParser.END_TAG, ns, "hooks");
	}

	private Hook readTrigger(int hook_type, String hook_value) throws XmlPullParserException, IOException {
		Hook hook = null;
		parser.require(XmlPullParser.START_TAG, ns, "trigger");
		
		String reaction = parser.getAttributeValue(null, "reaction");
		String conditions = parser.getAttributeValue(null, "conditions");
		String run = parser.getAttributeValue(null, "run");
		
		int conditions_type;
		if (conditions == null || conditions.equalsIgnoreCase(TRIGGER_CONDITIONS_NONE)) {
			conditions_type = Hook.CONDITIONS_NONE;
		} else if (conditions.equalsIgnoreCase(TRIGGER_CONDITIONS_ALL)) {
			conditions_type = Hook.CONDITIONS_ALL;
		} else if (conditions.equalsIgnoreCase(TRIGGER_CONDITIONS_ANY)) {
			conditions_type = Hook.CONDITIONS_ANY;
		} else {
			Log.d(TAG, "Conditions='" + conditions + "' is unknown. Used no conditions");
			conditions_type = Hook.CONDITIONS_NONE;
		}
		
		int runs = Hook.RUN_ALWAYS;
		if (run != null)
			runs = Integer.parseInt(run);
				
		hook = new Hook(hook_type, hook_value, reaction, conditions_type, runs);
		Log.d(TAG, "- Trigger reaction='" + reaction + "' conditions='" + conditions + "' run='" + runs + "'");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			
			if (parser.getName().equalsIgnoreCase("condition")) {
				parser.require(XmlPullParser.START_TAG, ns, "condition");
				
				String type = parser.getAttributeValue(null, "type");
				String variable = parser.getAttributeValue(null, "variable");
				String value = parser.getAttributeValue(null, "value");
				int itype;
				
				if (type.equalsIgnoreCase(CONDITION_TYPE_EQUALS)) {
					itype = Condition.TYPE_EQUALS;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_NOTEQUALS)) {
					itype = Condition.TYPE_NOTEQUALS;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_GREATER)) {
					itype = Condition.TYPE_GREATER;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_SMALLER)) {
					itype = Condition.TYPE_SMALLER;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_GREATEREQUALS)) {
					itype = Condition.TYPE_GREATEREQUALS;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_SMALLEREQUALS)) {
					itype = Condition.TYPE_SMALLEREQUALS;
				} else {
					Log.e(TAG, "Condition of type '" + type + "' is unknown");
	        		skip();
	        		continue;
				}
				
				Log.d(TAG, "--- Condition type='" + type + "' variable='" + variable + "' value='" + value + "'");
				hook.addCondition(new Condition(itype, variable, value));
				
				parser.nextTag();
				parser.require(XmlPullParser.END_TAG, ns, "condition");
			} else {
				Log.e(TAG, "Expected <condition>, got <" + parser.getName() + ">");
				skip();
			}
		}
    		
    	parser.require(XmlPullParser.END_TAG, ns, "trigger");
    	return hook;
	}

	private void readReactions() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading reactions");
		
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
	        	
	        	if (type.equalsIgnoreCase(REACTION_TYPE_MULTI)) {
	        		Log.d(TAG, "Got MultiReaction id='" + id + "'");
	        		
	        		reaction = new MultiReaction(id);
	        		
	        		while (parser.next() != XmlPullParser.END_TAG) {
	        			if (parser.getEventType() != XmlPullParser.START_TAG) {
	        	            continue;
	        	        }
	        			
	        			if (parser.getName().equalsIgnoreCase("reaction")) {
		        			((MultiReaction)reaction).addReaction(readReaction(""));
	        			} else {
	        				Log.e(TAG, "Expected <reaction>, got <" + parser.getName() + ">");
	        				skip();
	        			}
	        		}
	        		
	        	} else {
	        		reaction = readReaction(id);
	        	}

	        	scenario.addReaction(id, reaction);
	        	
	        	parser.require(XmlPullParser.END_TAG, ns, "reaction");
	        } else {
	        	skip();
	        }
	    }
	}

	private Reaction readReaction(String id) throws XmlPullParserException, IOException {
		Reaction reaction = null;
		parser.require(XmlPullParser.START_TAG, ns, "reaction");
		
		String type = parser.getAttributeValue(null, "type");
		String value = parser.getAttributeValue(null, "value"); // null for game reactions
		String variable = parser.getAttributeValue(null, "variable"); // only for variable reactions, null otherwise
		
		Log.d(TAG, "Got Reaction id='" + id + "' type='" + type + "'");
		
		if (type.equalsIgnoreCase(REACTION_TYPE_SOUND)) {
			reaction = new SoundReaction(id, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VIBRATE)) {
    		reaction = new VibrateReaction(id, Integer.parseInt(value));
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_HTML)) {
    		reaction = new HtmlReaction(id, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_DEC)) {
    		reaction = new VariableReaction(id, VariableReaction.DECREMENT, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_DIV)) {
    		reaction = new VariableReaction(id, VariableReaction.DIVIDE, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_INC)) {
    		reaction = new VariableReaction(id, VariableReaction.INCREMENT, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_MUL)) {
    		reaction = new VariableReaction(id, VariableReaction.MULTIPLY, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_NEG)) {
    		reaction = new VariableReaction(id, VariableReaction.NEGATE, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_SET)) {
    		reaction = new VariableReaction(id, VariableReaction.SET, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_EVENT)) {
    		if (value.equalsIgnoreCase(EVENT_GAME_START)) {
    			reaction = new EventReaction(id, GameEvent.GAME_START);
    		} else if (value.equalsIgnoreCase(EVENT_GAME_WIN)) {
    			reaction = new EventReaction(id, GameEvent.GAME_WIN);
    		} else if (value.equalsIgnoreCase(EVENT_GAME_LOSE)) {
    			reaction = new EventReaction(id, GameEvent.GAME_LOSE);
    		}
    	} else {
    		Log.e(TAG, "Reaction of type '" + type + "' is unknown");
    		skip();
    		return null;
    	}
    		
		parser.nextTag();

    	parser.require(XmlPullParser.END_TAG, ns, "reaction");
    	return reaction;
	}

	private void readVariables() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading variables");
		
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
	        	
	        	if (type.equalsIgnoreCase(VAR_TYPE_BOOLEAN)) {
	        		String value = parser.getAttributeValue(null, "value");
	        		
	        		variable = BooleanVariable.fromString(id, value);
	        		Log.d(TAG, "Got BooleanVariable");
	        		
	        		parser.nextTag();
	        	} else if (type.equalsIgnoreCase(VAR_TYPE_DECIMAL)) {
	        		String value = parser.getAttributeValue(null, "value");
	        		String min = parser.getAttributeValue(null, "min");
	        		String max = parser.getAttributeValue(null, "max");
	        		
	        		variable = DecimalVariable.fromString(id, value, min, max);
	        		Log.d(TAG, "Got DecimalVariable");
	        		
	        		parser.nextTag();
	        	} else {
	        		Log.e(TAG, "Variable of type '" + type + "' is unknown");
	        		skip();
	        		continue;
	        	}
	        	
	        	scenario.addVariable(id, variable);
	        	
	        	parser.require(XmlPullParser.END_TAG, ns, "variable");
	        } else {
	        	skip();
	        }
	    }
	}

	private void readAreas() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading areas");
		
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
	        	
	        	if (type.equalsIgnoreCase(AREA_TYPE_POINT)) {
	        		String latitude = parser.getAttributeValue(null, "lat");
	        		String longitude = parser.getAttributeValue(null, "lon");
	        		String radius = parser.getAttributeValue(null, "radius");
	        		
	        		if (latitude != null && longitude != null && radius != null) {
		        		LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
		        		area = new PointArea(id, point, Integer.parseInt(radius));
		        		Log.d(TAG, "Got PointArea");
	        		} else {
	        			Log.e(TAG, "Area type point must contains lat, lon and radius");
	        		}
	        			        		
	        		parser.nextTag();
	        	} else if (type.equalsIgnoreCase(AREA_TYPE_SOUND)) {
	        		String latitude = parser.getAttributeValue(null, "lat");
	        		String longitude = parser.getAttributeValue(null, "lon");
	        		String radius = parser.getAttributeValue(null, "radius");
	        		String value = parser.getAttributeValue(null, "value");
	        		String soundRadius = parser.getAttributeValue(null, "soundRadius");
	        		
	        		if (latitude != null && longitude != null && radius != null && value != null && soundRadius != null) {
		        		LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
		        		area = new SoundArea(id, point, Integer.parseInt(radius), value, Integer.parseInt(soundRadius));
		        		Log.d(TAG, "Got SoundArea");
	        		} else {
	        			Log.e(TAG, "Area type soundArea must contains lat, lon, radius, value and soundRadius");
	        		}
	        			        		
	        		parser.nextTag();
	        	} else if (type.equalsIgnoreCase(AREA_TYPE_MULTIPOINT)) {
	        		area = new MultiPointArea(id);
	        		
	        		while (parser.next() != XmlPullParser.END_TAG) {
	        			if (parser.getEventType() != XmlPullParser.START_TAG) {
	        	            continue;
	        	        }
	        		
	        			if (parser.getName().equalsIgnoreCase("point")) {			
			        		String latitude = parser.getAttributeValue(null, "lat");
			        		String longitude = parser.getAttributeValue(null, "lon");
			        		
			        		if (latitude != null && longitude != null) {
				        		LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
				        		((MultiPointArea)area).addPoint(point);
				        		Log.d(TAG, "Got PointArea->Point");
			        		} else {
			        			Log.e(TAG, "Point must contains lat and lon");
			        		}
			        		
			        		parser.nextTag();
	        			} else {
	        				Log.e(TAG, "Expected <point>, got <" + parser.getName() + ">");
	        				skip();
	        			}
	        		}
	        		
	        	} else {
	        		Log.e(TAG, "Area of type '" + type + "' is unknown");
	        		skip();
	        		continue;
	        	}
	        	
	        	scenario.addArea(id, area);
	        	
	        	parser.require(XmlPullParser.END_TAG, ns, "area");
	        } else {
	        	skip();
	        }
	    }
	}

	private void readScenario() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading about");
		
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
	            title = readText("title");
	        } else if (name.equals("author")) {
	        	author = readText("author");
	        } else if (name.equals("version")) {
	        	version = readText("version");
	        } else if (name.equals("location")) {
	        	location = readText("location");
	        } else if (name.equals("duration")) {
	        	duration = readText("duration");
	        } else if (name.equals("difficulty")) {
	        	difficulty = readText("difficulty");
	        } else {
	            skip();
	        }
	    }
		
		ScenarioInfo info = new ScenarioInfo(title, author, version, location, duration, difficulty);
	    scenario = new Scenario(context, info);
	}
	
	private String readText(String tag) throws IOException, XmlPullParserException {
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
