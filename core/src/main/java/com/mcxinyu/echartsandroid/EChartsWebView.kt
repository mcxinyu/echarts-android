package com.mcxinyu.echartsandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.internal.AssetHelper

/**
 *
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/2/24.
 */
@SuppressLint("SetJavaScriptEnabled")
open class EChartsWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    init {
        val settings = settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setSupportZoom(true)
        settings.displayZoomControls = true

        loadUrl("file:///android_asset/index.html")

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                runnable?.run()
            }
        }
    }

    private var runnable: Runnable? = Runnable { runnable = null }

    public fun setOption(option: String) {
        if (runnable == null) {
            evaluateJavascript("javascript:setOption($option)", null)
        } else {
            runnable = Runnable {
                evaluateJavascript("javascript:setOption($option)", null)
                runnable = null
            }
        }
    }
}