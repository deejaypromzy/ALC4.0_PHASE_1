package com.alc4.alc40challengeone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Set;
import java.util.TreeSet;

public class ALC_Connect extends AppCompatActivity {

    public static final String ANDELA_URL = "https://andela.com/alc/";
    private WebView mWebView;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    private String currentUrl;
    private static final Set<String> unsecureURLSet = new TreeSet<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alc__connect);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        progressBar = findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        mWebView = findViewById(R.id.alc_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setDomStorageEnabled(true);

//        // Javascript inabled on webview

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //Additional Webview Properties
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(mWebView.getSettings().getLayoutAlgorithm().NORMAL);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setKeepScreenOn(true);
        mWebView.setScrollbarFadingEnabled(true);


        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(true);

        swipeRefreshLayout = findViewById(R.id.refresh);
        if (haveNetworkConnection()) {

            if (currentUrl != null) {
                mWebView.loadUrl(currentUrl);
                Log.i("urlString", currentUrl);
            } else
                mWebView.loadUrl(ANDELA_URL);
        } else {
            mWebView.loadUrl("file:///android_asset/error.html");
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (haveNetworkConnection()) {
                    mWebView.loadUrl(ANDELA_URL);
                } else {
                    mWebView.loadUrl("file:///android_asset/error.html");
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                view.loadUrl(uri);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                mWebView.loadUrl("file:///android_asset/error.html");
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                mWebView.loadUrl("file:///android_asset/error.html");
                super.onReceivedHttpError(view, request, errorResponse);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                currentUrl = mWebView.getUrl();

                String pageTitle = mWebView.getTitle();
                String[] separated = pageTitle.split("-");
                if (separated[0].equals("404")) {
                    mWebView.loadUrl("file:///android_asset/error.html");

                }

            }

        });

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    // Prevent the back-button from closing the app

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
