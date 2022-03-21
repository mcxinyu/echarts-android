package com.mcxinyu.echartsandroid

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.withStyledAttributes

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

    private var runnable: Runnable? = Runnable { runnable = null }

    var option: String? = null
        set(value) {
            field = value
            if (runnable == null) {
                evaluateJavascript("javascript:chart.setOption($field, true)", null)
            } else {
                runnable = Runnable {
                    evaluateJavascript("javascript:chart.setOption($field, true)", null)
                    runnable = null
                }
            }
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.EChartsWebView, defStyleAttr) {
            option = getString(R.styleable.EChartsWebView_option)
        }
    }

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
}