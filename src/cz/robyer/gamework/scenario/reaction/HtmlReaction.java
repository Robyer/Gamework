package cz.robyer.gamework.scenario.reaction;

import android.app.Dialog;
import android.webkit.WebView;

public class HtmlReaction extends Reaction {
	protected String value;
	
	public HtmlReaction(String id, String value) {
		super(id);
		this.value = value;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		Dialog dialog = new Dialog(getContext());
		dialog.addContentView(new WebView(getContext()), null);
		
	}

}
