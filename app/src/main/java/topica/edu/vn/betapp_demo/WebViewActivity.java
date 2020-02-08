package topica.edu.vn.betapp_demo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        addControls();

        this.setTitle("MAIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String msg = bundle.getString("msg");

        loadWebView(msg);
    }

    private void addControls() {
        webView = (WebView) findViewById(R.id.WebViewPattern);

        dialog = new ProgressDialog(WebViewActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);
    }

    private void loadWebView(String link) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

        try {
            webView.loadUrl(link);
        }
        catch (Exception ex) {
            Log.e("LOI:", ex.toString());
        }

        webView.getSettings().setJavaScriptEnabled(true);
    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog != null) {
            dialog.cancel();
        }
        if(webView != null) {
            webView.destroy();
        }
    }
}
