package cz.robyer.gw_example.util;

import android.content.Context;
import android.content.Intent;
import cz.robyer.gw_example.game.GameService;

/**
 * Helper class for easily creating {@link Intent}s.
 * @author Robert Pösel
 */
public class IntentFactory {
	
	public static Intent createGameServiceIntent(Context context) {
		return new Intent(context, GameService.class); 
	}
	
	public static Intent createGameServiceIntent(Context context, String filename) {
		Intent intent = new Intent(context, GameService.class);
		intent.putExtra("filename", filename);
		intent.putExtra("package", "cz.robyer.gw_example.activity");
		intent.putExtra("class", ".GameMapActivity");
		return intent;
	}
}
