package cz.robyer.gamework.scenario.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameEvent.EventType;
import cz.robyer.gamework.hook.Condition;
import cz.robyer.gamework.hook.Condition.ConditionType;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.hook.Hook.HookConditions;
import cz.robyer.gamework.hook.Hook.HookType;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.ScenarioInfo;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.area.MultiPointArea;
import cz.robyer.gamework.scenario.area.PointArea;
import cz.robyer.gamework.scenario.area.SoundArea;
import cz.robyer.gamework.scenario.message.Message;
import cz.robyer.gamework.scenario.reaction.ActivityReaction;
import cz.robyer.gamework.scenario.reaction.EventReaction;
import cz.robyer.gamework.scenario.reaction.MessageReaction;
import cz.robyer.gamework.scenario.reaction.MultiReaction;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.reaction.ReferenceReaction;
import cz.robyer.gamework.scenario.reaction.SoundReaction;
import cz.robyer.gamework.scenario.reaction.VariableReaction;
import cz.robyer.gamework.scenario.reaction.VariableReaction.OperatorType;
import cz.robyer.gamework.scenario.reaction.VibrateReaction;
import cz.robyer.gamework.scenario.variable.BooleanVariable;
import cz.robyer.gamework.scenario.variable.DecimalVariable;
import cz.robyer.gamework.scenario.variable.Variable;
import cz.robyer.gamework.util.GPoint;
import cz.robyer.gamework.util.Log;

/**
 * This class is for parsing scenario from XML files.
 * @author Robert Pösel
 */
public class XmlScenarioParser {
	private static final String TAG = XmlScenarioParser.class.getSimpleName();
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
	public static final String REACTION_TYPE_MESSAGE = "message";
	public static final String REACTION_TYPE_VAR_SET = "var_set";
	public static final String REACTION_TYPE_VAR_INC = "var_increment";
	public static final String REACTION_TYPE_VAR_DEC = "var_decrement";
	public static final String REACTION_TYPE_VAR_MUL = "var_multiply";
	public static final String REACTION_TYPE_VAR_DIV = "var_divide";
	public static final String REACTION_TYPE_VAR_NEG = "var_negate";
	public static final String REACTION_TYPE_EVENT = "event";
	public static final String REACTION_TYPE_ACTIVITY = "activity";
	public static final String REACTION_TYPE_REFERENCE = "ref";
	
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
	public static final String HOOK_TYPE_EVENT = "event";
	public static final String HOOK_TYPE_SCANNER = "scanner";
	
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
    
    /**
     * Factory for parsing scenario from file.
     * @param context
     * @param filename - path to file
     * @return Scenario or null
     */
    public static Scenario fromFile(Context context, String filename) {
    	return fromFile(context, filename, false);
    }
    
    /**
     * Factory for parsing scenario from assets.
     * @param context
     * @param filename - path to file in assets directory
     * @return Scenario or null
     */
    public static Scenario fromAsset(Context context, String filename) {
    	return fromAsset(context, filename, false);
    }
    
    /**
     * Factory for parsing scenario from file.
     * @param context
     * @param filename - path to file
     * @param aboutOnly - parse only About section
     * @return Scenario or null
     */
	public static Scenario fromFile(Context context, String filename, boolean aboutOnly) {
		Log.i(TAG, String.format("Loading %s from file '%s'", (aboutOnly ? "info" : "scenario"), filename));
		Scenario scenario = null;
		InputStream stream = null;
		try {
			XmlScenarioParser parser = new XmlScenarioParser(context, false);
			File file = context.getFileStreamPath(filename);
			stream = new BufferedInputStream(new FileInputStream(file));
			scenario = parser.parse(stream, aboutOnly);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
	        try {
	        	if (stream != null)
	        		stream.close();
	        } catch (IOException ioe) {
	        	Log.e(TAG, ioe.getMessage(), ioe);
	        }
		}
		return scenario;
	}
	
	/**
     * Factory for parsing scenario from assets.
     * @param context
     * @param filename - path to file in assets directory
     * @param aboutOnly - parse only About section
     * @return Scenario or null
     */
	public static Scenario fromAsset(Context context, String filename, boolean aboutOnly) {
		Log.i(TAG, String.format("Loading %s from asset '%s'", (aboutOnly ? "info" : "scenario"), filename));
		Scenario scenario = null;
		InputStream stream = null;
		try {
			XmlScenarioParser parser = new XmlScenarioParser(context, false);
			stream = context.getAssets().open(filename);
			scenario = parser.parse(stream, aboutOnly);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
	        try {
	        	if (stream != null)
	        		stream.close();
	        } catch (IOException ioe) {
	        	Log.e(TAG, ioe.getMessage(), ioe);
	        }
		}
		return scenario;
	}
    
	/**
	 * Class constructor.
	 * @param context
	 * @param namespaces - use namespaces
	 * @throws XmlPullParserException
	 */
    public XmlScenarioParser(Context context, boolean namespaces) throws XmlPullParserException {
    	this.context = context;
    	this.parser = Xml.newPullParser();
    	this.parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, namespaces);
    }
	
    /**
     * Parses inputstream into scenario object.
     * @param input - input stream
     * @param aboutOnly - parse only About section
     * @return Scenario object
     * @throws XmlPullParserException
     * @throws IOException
     */
	public Scenario parse(InputStream input, boolean aboutOnly) throws XmlPullParserException, IOException {
	    scenario = null;
		
		parser.setInput(input, null);
	    parser.nextTag();
	    parser.require(XmlPullParser.START_TAG, ns, "scenario");
	    
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;

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
				else if (name.equalsIgnoreCase("messages"))
					readMessages();
		        else
		            skip();
	    	}
	    }
	    
	    scenario.onLoaded();
	    return scenario;
	}
	
	/**
	 * Skips whole element and subelements.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void skip() throws XmlPullParserException, IOException {
	    Log.d(TAG, "Skipping unknown child '" + parser.getName() + "'");
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

	/**
	 * Read hooks section.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readHooks() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading hooks");

		parser.require(XmlPullParser.START_TAG, ns, "hooks");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;

	        if (parser.getName().equalsIgnoreCase("hook")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "hook");
	        	
	        	String type = parser.getAttributeValue(null, "type");
	        	String value = parser.getAttributeValue(null, "value");
	        	HookType itype;
	        	
	        	if (type.equalsIgnoreCase(HOOK_TYPE_AREA)) {
	        		itype = HookType.AREA;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_AREA_ENTER)) {
	        		itype = HookType.AREA_ENTER;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_AREA_LEAVE)) {
	        		itype = HookType.AREA_LEAVE;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_VARIABLE)) {
	        		itype = HookType.VARIABLE;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_TIME)) {
	        		itype = HookType.TIME;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_EVENT)) {
	        		itype = HookType.EVENT;
	        	} else if (type.equalsIgnoreCase(HOOK_TYPE_SCANNER)) {
	        		itype = HookType.SCANNER;
	        	} else {
	        		Log.e(TAG, "Hook type '" + type + "' is unknown");
	        		skip();
	        		return;
	        	}
	        	
	        	while (parser.next() != XmlPullParser.END_TAG) {
        			if (parser.getEventType() != XmlPullParser.START_TAG)
        	            continue;
        			
        			if (parser.getName().equalsIgnoreCase("trigger")) {
        				Hook hook = readTrigger(itype, value);
        				scenario.addHook(hook);
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

	/**
	 * Read triggers.
	 * @param hook_type
	 * @param hook_value
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Hook readTrigger(HookType hook_type, String hook_value) throws XmlPullParserException, IOException {
		Hook hook = null;
		parser.require(XmlPullParser.START_TAG, ns, "trigger");
		
		String reaction = parser.getAttributeValue(null, "reaction");
		String conditions = parser.getAttributeValue(null, "conditions");
		String run = parser.getAttributeValue(null, "run");
		
		HookConditions conditions_type;
		if (conditions == null || conditions.equalsIgnoreCase(TRIGGER_CONDITIONS_NONE)) {
			conditions_type = HookConditions.NONE;
		} else if (conditions.equalsIgnoreCase(TRIGGER_CONDITIONS_ALL)) {
			conditions_type = HookConditions.ALL;
		} else if (conditions.equalsIgnoreCase(TRIGGER_CONDITIONS_ANY)) {
			conditions_type = HookConditions.ANY;
		} else {
			Log.d(TAG, "Conditions='" + conditions + "' is unknown. Used no conditions");
			conditions_type = HookConditions.NONE;
		}
		
		int runs = Hook.RUN_ALWAYS;
		if (run != null)
			runs = Integer.parseInt(run);
				
		hook = new Hook(hook_type, hook_value, reaction, conditions_type, runs);
		Log.d(TAG, "Got trigger reaction='" + reaction + "' conditions='" + conditions + "' run='" + runs + "'");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;
			
			if (parser.getName().equalsIgnoreCase("condition")) {
				parser.require(XmlPullParser.START_TAG, ns, "condition");
				
				String type = parser.getAttributeValue(null, "type");
				String variable = parser.getAttributeValue(null, "variable");
				String value = parser.getAttributeValue(null, "value");
				ConditionType itype;
				
				if (type.equalsIgnoreCase(CONDITION_TYPE_EQUALS)) {
					itype = ConditionType.EQUALS;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_NOTEQUALS)) {
					itype = ConditionType.NOTEQUALS;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_GREATER)) {
					itype = ConditionType.GREATER;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_SMALLER)) {
					itype = ConditionType.SMALLER;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_GREATEREQUALS)) {
					itype = ConditionType.GREATEREQUALS;
				} else if (type.equalsIgnoreCase(CONDITION_TYPE_SMALLEREQUALS)) {
					itype = ConditionType.SMALLEREQUALS;
				} else {
					Log.e(TAG, "Condition type '" + type + "' is unknown");
	        		skip();
	        		continue;
				}
				
				Log.d(TAG, "- Condition type='" + type + "' variable='" + variable + "' value='" + value + "'");
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

	/**
	 * Read reactions section.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readReactions() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading reactions");
		
		parser.require(XmlPullParser.START_TAG, ns, "reactions");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;
			
			Reaction reaction = null;

	        if (parser.getName().equalsIgnoreCase("reaction")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "reaction");
	        	String id = parser.getAttributeValue(null, "id");
	        	String type = parser.getAttributeValue(null, "type");
	        	
	        	if (type.equalsIgnoreCase(REACTION_TYPE_MULTI)) {
	        		Log.d(TAG, "Got MultiReaction id='" + id + "'");	
	        		reaction = new MultiReaction(id);
	        		
	        		while (parser.next() != XmlPullParser.END_TAG) {
	        			if (parser.getEventType() != XmlPullParser.START_TAG)
	        	            continue;
	        			
	        			if (parser.getName().equalsIgnoreCase("reaction")) {
		        			((MultiReaction)reaction).addReaction(readReaction(id, true));
	        			} else {
	        				Log.e(TAG, "Expected <reaction>, got <" + parser.getName() + ">");
	        				skip();
	        			}
	        		}
	        	} else {
	        		reaction = readReaction(id, false);
	        	}

	        	scenario.addReaction(id, reaction);
	        	
	        	parser.require(XmlPullParser.END_TAG, ns, "reaction");
	        } else {
	        	skip();
	        }
	    }
	}

	/**
	 * Read single reaction.
	 * @param id - identificator of reaction (or parent multireaction)
	 * @param isChild - is this child reaction inside multireaction?
	 * @return Reaction
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Reaction readReaction(String id, boolean isChild) throws XmlPullParserException, IOException {
		Reaction reaction = null;
		parser.require(XmlPullParser.START_TAG, ns, "reaction");
		
		String type = parser.getAttributeValue(null, "type");
		String value = parser.getAttributeValue(null, "value"); // null for game reactions
		String variable = parser.getAttributeValue(null, "variable"); // only for variable reactions, null otherwise
		
		if (isChild)
			Log.d(TAG, "Got Reaction (parent id='" + id + "') type='" + type + "' value='" + value + "'");
		else
			Log.d(TAG, "Got Reaction id='" + id + "' type='" + type + "' value='" + value + "'");
		
		if (type.equalsIgnoreCase(REACTION_TYPE_SOUND)) {
			reaction = new SoundReaction(id, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VIBRATE)) {
    		String[] values = value.split(", ?");
    		if (values.length == 1) {
    			// simple vibrate
    			reaction = new VibrateReaction(id, Integer.parseInt(value));
    		} else if (values.length > 1) {
    			// vibrating pattern
    			long[] pattern = new long[values.length];
    			int i = 0;
    			for (String val : values) {
    				pattern[i++] = Long.parseLong(val);
    			}
    			reaction = new VibrateReaction(id, pattern);
    		} else {
    			Log.e(TAG, "Wrong vibrate value/pattern '" + value + "'");
        		skip();
        		return null;
    		}
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_MESSAGE)) {
    		reaction = new MessageReaction(id, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_DEC)) {
    		reaction = new VariableReaction(id, OperatorType.DECREMENT, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_DIV)) {
    		reaction = new VariableReaction(id, OperatorType.DIVIDE, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_INC)) {
    		reaction = new VariableReaction(id, OperatorType.INCREMENT, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_MUL)) {
    		reaction = new VariableReaction(id, OperatorType.MULTIPLY, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_NEG)) {
    		reaction = new VariableReaction(id, OperatorType.NEGATE, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_VAR_SET)) {
    		reaction = new VariableReaction(id, OperatorType.SET, variable, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_EVENT)) {
    		if (value.equalsIgnoreCase(EVENT_GAME_START)) {
    			reaction = new EventReaction(id, EventType.GAME_START);
    		} else if (value.equalsIgnoreCase(EVENT_GAME_WIN)) {
    			reaction = new EventReaction(id, EventType.GAME_WIN);
    		} else if (value.equalsIgnoreCase(EVENT_GAME_LOSE)) {
    			reaction = new EventReaction(id, EventType.GAME_LOSE);
    		} else {
    			reaction = new EventReaction(id, new GameEvent(EventType.CUSTOM, value));
    		}
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_ACTIVITY)) {
    		reaction = new ActivityReaction(id, value);
    	} else if (type.equalsIgnoreCase(REACTION_TYPE_REFERENCE)) {
    		reaction = new ReferenceReaction(id, value);
    	} else {
    		Log.e(TAG, "Reaction type '" + type + "' is unknown");
    		skip();
    		return null;
    	}
    		
		parser.nextTag();
    	parser.require(XmlPullParser.END_TAG, ns, "reaction");
    	return reaction;
	}

	/**
	 * Read variables section.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readVariables() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading variables");
		
		parser.require(XmlPullParser.START_TAG, ns, "variables");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;

	        if (parser.getName().equalsIgnoreCase("variable")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "variable");
	        	String id = parser.getAttributeValue(null, "id");
	        	String type = parser.getAttributeValue(null, "type");
	        	Variable variable = null;
	        	
	        	if (type.equalsIgnoreCase(VAR_TYPE_BOOLEAN)) {
	        		String value = parser.getAttributeValue(null, "value");
	        		
	        		variable = BooleanVariable.fromString(id, value);
	        		Log.d(TAG, "Got BooleanVariable");
	        		
	        		parser.nextTag();
	        	} else if (type.equalsIgnoreCase(VAR_TYPE_DECIMAL)) {
	        		String value = parser.getAttributeValue(null, "value");
	        		String min = parser.getAttributeValue(null, "min");
	        		String max = parser.getAttributeValue(null, "max");
	        		
	        		if (min != null && max != null)
	        			variable = DecimalVariable.fromString(id, value, min, max);
	        		else
	        			variable = DecimalVariable.fromString(id, value);
	        		
	        		Log.d(TAG, "Got DecimalVariable");
	        		
	        		parser.nextTag();
	        	} else {
	        		Log.e(TAG, "Variable type '" + type + "' is unknown");
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

	/**
	 * Read areas section.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readAreas() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading areas");
		
		parser.require(XmlPullParser.START_TAG, ns, "areas");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;

	        if (parser.getName().equalsIgnoreCase("area")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "area");
	        	String id = parser.getAttributeValue(null, "id");
	        	String type = parser.getAttributeValue(null, "type");
	        	Area area = null;
	        	
	        	if (type.equalsIgnoreCase(AREA_TYPE_POINT)) {
	        		String latitude = parser.getAttributeValue(null, "lat");
	        		String longitude = parser.getAttributeValue(null, "lon");
	        		String radius = parser.getAttributeValue(null, "radius");
	        		
	        		if (latitude != null && longitude != null && radius != null) {
		        		GPoint point = new GPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
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
		        		GPoint point = new GPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
		        		area = new SoundArea(id, point, Integer.parseInt(radius), value, Integer.parseInt(soundRadius));
		        		Log.d(TAG, "Got SoundArea");
	        		} else {
	        			Log.e(TAG, "Area type soundArea must contains lat, lon, radius, value and soundRadius");
	        		}
	        			        		
	        		parser.nextTag();
	        	} else if (type.equalsIgnoreCase(AREA_TYPE_MULTIPOINT)) {
	        		area = new MultiPointArea(id);
	        		
	        		int i = 0;
	        		while (parser.next() != XmlPullParser.END_TAG) {
	        			if (parser.getEventType() != XmlPullParser.START_TAG)
	        	            continue;
	        		
	        			if (parser.getName().equalsIgnoreCase("point")) {			
			        		String latitude = parser.getAttributeValue(null, "lat");
			        		String longitude = parser.getAttributeValue(null, "lon");
			        		
			        		if (latitude != null && longitude != null) {
				        		GPoint point = new GPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
				        		((MultiPointArea)area).addPoint(point);
				        		i++;
				        		Log.d(TAG, "Got PointArea->GPoint");
			        		} else {
			        			Log.e(TAG, "GPoint must contains lat and lon");
			        		}
			        		
			        		parser.nextTag();
	        			} else {
	        				Log.e(TAG, "Expected <point>, got <" + parser.getName() + ">");
	        				skip();
	        			}
	        		}
	        		if (i < 3) {
	        			Log.e(TAG, "MultiPointArea must contain at least 3 points");
	        			continue;
	        		}
	        		
	        	} else {
	        		Log.e(TAG, "Area type '" + type + "' is unknown");
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
	
	/**
	 * Read messages section.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readMessages() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading messages");
		
		parser.require(XmlPullParser.START_TAG, ns, "messages");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;

	        if (parser.getName().equalsIgnoreCase("message")) {
	        	parser.require(XmlPullParser.START_TAG, ns, "message");
	        	String id = parser.getAttributeValue(null, "id");
	        	String tag = parser.getAttributeValue(null, "tag");
	        	String title = parser.getAttributeValue(null, "title");
	        	String value = parser.getAttributeValue(null, "value");
	        	String def = parser.getAttributeValue(null, "default");
	        	boolean bdef = (def != null && def.equalsIgnoreCase("true"));
	        	
	        	Log.d(TAG, "Got Message");
        		scenario.addMessage(id, new Message(id, tag, title, value, bdef));
        		
        		parser.nextTag();
	        	parser.require(XmlPullParser.END_TAG, ns, "message");
	        } else {
	        	skip();
	        }
	    }
	}

	/**
	 * Read About section.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readScenario() throws XmlPullParserException, IOException {
		Log.d(TAG, "Reading about");
		
		parser.require(XmlPullParser.START_TAG, ns, "about");
		
		String title = null;
		String author = null;
		String version = null;
		String location = null;
		String duration = null;
		String difficulty = null;
		String description = null;
		
		while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG)
	            continue;

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
	        } else if (name.equals("description")) {
	        	description = readText("description");
	        } else {
	            skip();
	        }
	    }
		
		ScenarioInfo info = new ScenarioInfo(title, author, version, location, duration, difficulty, description);
	    scenario = new Scenario(context, info);
	}
	
	/**
	 * Read text value of some element.
	 * @param tag
	 * @return value of element
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
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
