package com.dangchuvmnewb;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dangchuvmnewb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private long extime;

    /**
     * Initializes the activity, sets up view binding, configures the main WebView's settings, and loads the initial URL.
     *
     * @param savedInstanceState a Bundle containing the activity's previously saved state, or null if none
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        WebView webView = binding.webview;
        WebSettings ws = webView.getSettings();
        
        ws.setMediaPlaybackRequiresUserGesture(false);
        ws.setBuiltInZoomControls(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setLoadWithOverviewMode(true);
        ws.setLoadsImagesAutomatically(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setJavaScriptEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setDefaultTextEncodingName("utf-8");
        ws.setDisplayZoomControls(false);
        ws.setSupportMultipleWindows(true);
        ws.setSafeBrowsingEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setSupportZoom(true);
        ws.setUseWideViewPort(true);
        
        binding.webview.loadUrl("https://google.com");
    }

    /**
     * Handles the system back button by navigating the embedded WebView back when possible; if the WebView cannot go back, requires a double press within 2 seconds to exit the activity.
     *
     * If the WebView can navigate back, this performs a back navigation. Otherwise, the first back press shows a short toast using R.string.exitmess and a second back press within 2000 milliseconds calls finish().
     */
    @Override
    public void onBackPressed() {
        if(binding.webview.canGoBack()) {
        	binding.webview.goBack();
        } else {
        if(extime + 2000 > System.currentTimeMillis()) { //Mặc định sẽ là 2 giây
        	finish();
        } else {
        	Toast.makeText(this, getString(R.string.exitmess),Toast.LENGTH_SHORT).show();
        }
        extime = System.currentTimeMillis();
        }
    }
}