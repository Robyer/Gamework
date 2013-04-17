package cz.robyer.gw_example.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cz.robyer.gw_example.service.JavaScriptHandler;
import cz.robyer.gw_example.R;

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

}
