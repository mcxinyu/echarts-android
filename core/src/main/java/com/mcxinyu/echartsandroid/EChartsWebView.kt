package com.mcxinyu.echartsandroid

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.BufferedReader
import java.io.InputStreamReader

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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initSetting()
    }

    private fun initSetting() {
        val settings = settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setSupportZoom(true)
        settings.displayZoomControls = true

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                runnable?.run()
            }
        }

        loadUrl("file:///android_asset/index.html")
    }

    private var runnable: Runnable? = Runnable { runnable = null }

    public fun setOption(option: String) {
        if (runnable == null) {
            evaluateJavascript("javascript:myChart.setOption($option, true)", null)
        } else {
            runnable = Runnable {
                evaluateJavascript("javascript:myChart.setOption($option, true)", null)
                runnable = null
            }
        }
    }
}