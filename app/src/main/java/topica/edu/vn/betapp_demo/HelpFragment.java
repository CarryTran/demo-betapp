package topica.edu.vn.betapp_demo;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    private WebView webView;
    private ProgressDialog dialog;
    private View v;
    private final static String link = "https://tawk.to/chat/5e168eae27773e0d832c9482/default";

    public HelpFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_help, container, false);
        getActivity().setTitle("TRUNG TÂM HỖ TRỢ");

        webView = (WebView) v.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);

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
            Toast.makeText(getContext(), "LỖI: Không thể kết nối với trung tâm hỗ trợ.", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null) {
            dialog.cancel();
        }
        if (webView != null) {
            webView.destroy();
        }
    }
}
