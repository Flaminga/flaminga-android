package com.flaminga.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.flaminga.client.R;

/**
 * Created by mjanes on 1/30/14.
 *
 * I am copying this from something that was originally a bit more flexible to allow OAuth1
 * authentication with multiple websites. Can simplify this later, but may be better to just
 * simplify.
 *
 * This Activity will display a webview, loading Twitter's authentication page. This is not the page
 * within Flaminga from which the user initializes the intent to authenticate.
 */
public class TwitterAuthenticationWebViewActivity extends Activity {

    public static final String URL = "url";

    private static final String TAG = "TwitterAuthenticationWebViewActivity";
    public static final String VERIFIER = "verifier";

    private String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // set the main layout
        setContentView(R.layout.activity_web_view);


        Bundle incomingBundle = getIntent().getExtras();

        if (incomingBundle == null) {
            finish();
            return;
        }

        // get the url
        // This is a url specific to the current erquest
        mUrl = incomingBundle.getString(URL);
        if (mUrl == null) {
            finish();
            return;
        }

        // find the WebView
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) {
                    return false;
                } else if (url.contains(getResources().getString(R.string.oauth_callback))) {

                    Uri uri = Uri.parse(url);
                    String verifier = uri.getQueryParameter("oauth_verifier");

                    Intent returnIntent = new Intent();
                    if (verifier != null && verifier.length() > 0) {
                        returnIntent.putExtra(VERIFIER, url);
                        setResult(RESULT_OK, returnIntent);
                        returnIntent.putExtra(VERIFIER, verifier);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                    return true;
                } // May need another if, I forget what Twitter does, but incase it redirects to different callback

                return false;
            }
        });

        webView.loadUrl(mUrl);

    }
}
