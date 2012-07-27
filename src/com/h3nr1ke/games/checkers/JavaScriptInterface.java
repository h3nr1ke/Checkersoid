package com.h3nr1ke.games.checkers;

import java.util.Random;

import android.content.Context;
import android.widget.Toast;

public class JavaScriptInterface {
	private Context mContext;

	private Random randomGenerator = new Random();

	/** Instantiate the interface and set the context */
	JavaScriptInterface(Context c) {
		mContext = c;
	}

	/** Show a toast from the web page */
	public void showToast(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}

	/**
	 * devolge um numero randomico ate o limite passado
	 * */
	public int getRandom(int pLimit) {
		return randomGenerator.nextInt(pLimit);
	}

	/**
	 * Return the translated string
	 */
	public String getTransString(String str) {
		String ret;
		int resID = mContext.getResources().getIdentifier(str, "string",
				mContext.getPackageName());
		ret = mContext.getString(resID);
		return ret;
	}

}
