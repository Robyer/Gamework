package cz.robyer.gamework.util;

/**
 * Logging class.
 * @author Robert Pösel
 */
public class Log {
  
	private static final boolean debug = true;
	
	public static boolean loggingEnabled() {
		return debug;
	}
	
	public static void d(String tag, String msg) {
		if (loggingEnabled())
			android.util.Log.d(tag, msg);
	}
	
	public static void i(String tag, String msg) {
		if (loggingEnabled())
			android.util.Log.i(tag, msg);
	}
	
	public static void e(String tag, String msg) {
		android.util.Log.e(tag, msg);
	}
	
	public static void e(String tag, String msg, Exception e) {
		android.util.Log.e(tag, msg, e);
	}
	
	public static void w(String tag, String msg) {
		android.util.Log.w(tag, msg);
	}
	
	public static void println(int severity, String tag, String message) {
		if (loggingEnabled())
			android.util.Log.println(severity, tag, message);
	}

}
