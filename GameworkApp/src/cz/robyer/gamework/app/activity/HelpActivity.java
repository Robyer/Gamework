package cz.robyer.gamework.app.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cz.robyer.gamework.app.R;
import cz.robyer.gamework.app.service.JavaScriptHandler;

/**
 * Represents help with informations of how to use this application.
 * @author Robert Pösel
 */
@SuppressLint("SetJavaScriptEnabled")
public class HelpActivity extends BaseActivity {	
	//private static final String TAG = HelpActivity.class.getSimpleName();

	WebView webview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		webview = (WebView)findViewById(R.id.webview);
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new JavaScriptHandler(this), "Gamework");
		webview.setWebViewClient(new WebViewClient());/* {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        if (Uri.parse(url).getHost().equals("www.example.com")) {
		            // This is my web site, so do not override; let my WebView load the page
		            return false;
		        }
		        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
		        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		        startActivity(intent);
		        return true;
		    }
		});*/
		
		webview.loadUrl("file:///android_asset/help/index.html");
		
		setupActionBar();
	}
	
	/**
	 * Rewrite 'back' button behavior to get back in {@link WebView}.
	 */
	@Override
	public void onBackPressed() {
		if (webview.canGoBack())
			webview.goBack();
		else
			super.onBackPressed();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
