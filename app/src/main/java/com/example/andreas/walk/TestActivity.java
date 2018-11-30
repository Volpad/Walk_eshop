package com.example.andreas.walk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.andreas.walk_eshop.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    WebView wv;
    Handler uiHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wv = (WebView) findViewById(R.id.webview1);

        new BackgroundWorker().execute();








    }


    // load links in WebView instead of default browser
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @RequiresApi(21)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return false;
        }
    }


    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            getMainBanner();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            wv.setWebViewClient(new MyWebViewClient());
            WebSettings webSettings = wv.getSettings();
            webSettings.setJavaScriptEnabled(true);


        }



        public void getMainBanner() {

            try {
                Document htmlDocument = Jsoup.connect("https://walk.gr/").get();
                Element element = htmlDocument.select("div.mainbanner").first();
                Log.d("element", String.valueOf(element));

                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();
                Log.d("html element", html);

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        wv.loadData(html, "text/html", "UTF-8");

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
