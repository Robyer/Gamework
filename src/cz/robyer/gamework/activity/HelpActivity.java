package cz.robyer.gamework.activity;

import cz.robyer.gamework.R;
import cz.robyer.gamework.service.JavaScriptHandler;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
//import android.content.Intent;
//import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class HelpActivity extends BaseActivity {

	WebView webview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		// Show the Up button in the action bar.
		setupActionBar();
		
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
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	        webview.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
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
