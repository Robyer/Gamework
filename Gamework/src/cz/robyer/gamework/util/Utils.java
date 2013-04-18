package cz.robyer.gamework.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Class with various useful methods.
 * @author Robert Pösel
 */
public class Utils {
	
	/**
	 * Checks if there exists activity required for particular intent.
	 * @param context
	 * @param intent to be checked.
	 * @return true if intent can be called, false otherwise.
	 */
	public static boolean isIntentCallable(Context context, Intent intent) {
        return context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null;
	}

}
