package com.h3nr1ke.games.checkers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

//public class DamaActivity extends DroidGap {
public class DamaActivity extends Activity {
	// menu constants
	private static final int RESTART = Menu.FIRST;
	private static final int RESET_COUNT = RESTART + 1;
	private static final int ABOUT = RESET_COUNT + 1;
	private static final int SHARE = ABOUT + 1;

	// dialogs
	private static final int DIALOG_ABOUT = 0;

	private WebView webView;

	private AdView adView;

	private JavaScriptInterface mJSI;

	private WebClient mWC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// get the webview element
		webView = (WebView) findViewById(R.id.page_loader);

		// enable the javascript
		webView.getSettings().setJavaScriptEnabled(true);

		// enable zoom support and zoom controls
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setBuiltInZoomControls(false);

		// remove the scrollbars
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);

		// performance adjust
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);

		// point to our mapa.html file
		webView.loadUrl("file:///android_asset/www/dama.html");

		mJSI = new JavaScriptInterface(this);

		// ponte entre javascript e javacode
		webView.addJavascriptInterface(mJSI, "Android");

		// define our "browser"
		mWC = new WebClient(this);
		webView.setWebViewClient(mWC);

		if (Constantes.ADD_AD) {
			// get the ad dimensions
			LinearLayout adContainer = (LinearLayout) findViewById(R.id.adViewLayout);

			// Create the adView
			adView = new AdView(this, AdSize.BANNER, Constantes.AD_ID);

			// include the ad inside a linear layout in the main.xml
			adContainer.addView(adView);

			// create the ad request conf
			AdRequest request = new AdRequest();
			if (Constantes.AD_TEST) {
				for (String td : Constantes.AD_TEST_DEVICE) {
					request.addTestDevice(td);
				}

				request.setTesting(true);
			}

			// load the ad
			adView.loadAd(request);
		}

	}

	public void onDestroy() {
		// Destroy the AdView.
		if (Constantes.ADD_AD) {
			adView.destroy();
		}

		super.onDestroy();
	}

	// ----- Dialog control -----
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_ABOUT:
			dialog = new Dialog(this);

			// do some replaces
			String desc = getString(R.string.txt_desc);
			desc = desc.replace("@app_name@", getString(R.string.app_name));
			desc = desc.replace("@apps@",
					"<a href=\"market://search?q=pub:h3nr1ke\">h3nr1ke</a>");
			desc = desc.replace("\n", "<br />");

			String title = getString(R.string.txt_about_title);
			title = title.replace("@app_name@", getString(R.string.app_name));

			dialog.setContentView(R.layout.about_dialog);
			dialog.setTitle(title);

			TextView text = (TextView) dialog.findViewById(R.id.text);

			// there is a link in the text view
			text.setText(Html.fromHtml(desc));
			// include the clickable way
			text.setMovementMethod(LinkMovementMethod.getInstance());

			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	// -------Create the menu-------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// menu.add(0, RESTART, 0, R.string.restart).setIcon(
		// android.R.drawable.ic_menu_revert);
		// menu.add(0, RESET_COUNT, 0,
		// R.string.reset_counters).setIcon(R.drawable.icon);
		menu.add(0, SHARE, 0, R.string.share).setIcon(android.R.drawable.ic_menu_share);
		menu.add(0, ABOUT, 0, R.string.about).setIcon(
				android.R.drawable.ic_menu_info_details);

		return super.onCreateOptionsMenu(menu);
	}

	// handle the menu buttons
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;

		switch (item.getItemId()) {
		case SHARE:
			i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_msg));

			try {
				startActivity(Intent.createChooser(i, getString(R.string.share_title)));
			} catch (android.content.ActivityNotFoundException ex) {
				// (handle error)
			}
			return true;

		case RESTART:
			webView.reload();
			return true;
		case ABOUT:
			this.showDialog(DIALOG_ABOUT);
			return true;
		case RESET_COUNT:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// ------- end of menu creation --------

}
