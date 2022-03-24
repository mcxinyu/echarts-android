package com.mcxinyu.echartsandroid

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.withStyledAttributes
import kotlinx.coroutines.*

/**
 *
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/2/24.
 */
@SuppressLint("SetJavaScriptEnabled")
open class EChartsWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr), CoroutineScope by MainScope() {

    private var pending: () -> Unit = {}

    init {
        context.withStyledAttributes(attrs, R.styleable.EChartsWebView, defStyleAttr) {
            getString(R.styleable.EChartsWebView_option)?.let {
                option = it
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
        initSetting()
    }

    private fun initSetting() {
        kotlin.runCatching {
            settings.apply {
                javaScriptEnabled = true
                displayZoomControls = false
            }
        }

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                launch {
//                    val withContext = withContext(Dispatchers.IO) { check() }
                    if (check()) {
                        pending.invoke()
                    }
                }
            }
        }

        loadUrl("file:///android_asset/index.html")
    }

    /**
     * 检查 chart 是否实例化成功
     *
     * @return Boolean
     */
    suspend fun check() = (evaluateJavascript("javascript:chart.getWidth()") ?: "null") != "null"

    /**
     * 检查 chart 是否实例化成功
     *
     * @param onResult Function1<Boolean, Unit>
     */
    fun check(onResult: (Boolean) -> Unit) {
        evaluateJavascript("javascript:chart.getWidth()") {
            onResult.invoke("null" != (it ?: "null"))
        }
    }

    var option: String? = null
        set(value) {
            if (field != value) {
                field = value
                field?.let {
                    kotlin.runCatching {
                        pending = {
                            evaluateJavascript("javascript:chart.setOption($it, true)", null)
                        }
                        launch {
//                            val withContext = withContext(Dispatchers.IO) { check() }
                            if (check()) {
                                pending.invoke()
                            }
                        }
                    }
                }
            }
        }

    override fun onDetachedFromWindow() {
        cancel()
        super.onDetachedFromWindow()
    }
}