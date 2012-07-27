package com.h3nr1ke.games.checkers;

import android.app.ProgressDialog;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class WebClient extends WebViewClient {
	private ProgressDialog mDialog;
	private Context mC;

	public WebClient(Context c) {
		super();
		this.mC = c;
		mDialog = new ProgressDialog(this.mC);
		mDialog.setCancelable(false);
		mDialog.setTitle(R.string.loading_tilte);
		mDialog.setMessage(this.mC.getString(R.string.loading_message));
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
		mDialog.show();
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		super.onPageFinished(view, url);
		mDialog.dismiss();
	}
}