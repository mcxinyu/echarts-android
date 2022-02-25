package com.mcxinyu.echartsandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/2/24.
 */
public class EChartsWebview extends WebView {
    public EChartsWebview(@NonNull Context context) {
        super(context);
    }

    public EChartsWebview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EChartsWebview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(true);

        loadUrl("file:///android_asset/index.html");
    }

    private void setOption(String option) {
        evaluateJavascript("javascript:setOption(" + option + ")", null);
    }

}
