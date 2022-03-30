package com.mcxinyu.echartsandroid

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import androidx.core.content.withStyledAttributes
import com.mcxinyu.echartsandroid.webview.evaluateJavascript
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

    init {
        context.withStyledAttributes(attrs, R.styleable.EChartsWebView, defStyleAttr) {
            getString(R.styleable.EChartsWebView_option)?.let {
                option = it
            }
        }

        initSetting()
    }

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
    }

    private fun initSetting() {
        kotlin.runCatching {
            settings.apply {
                javaScriptEnabled = true
                displayZoomControls = false
            }
        }

        post {
            loadUrl("file:///android_asset/index.html")
        }
    }

    /**
     * 检查 chart 是否实例化成功
     *
     * @return Boolean
     */
    suspend fun check() = "null" != evaluateJavascript("javascript:chart.getWidth()")

    /**
     * 检查 chart 是否实例化成功
     *
     * @param onResult [@kotlin.ExtensionFunctionType] Function1<Boolean, Unit>
     */
    fun check(onResult: Boolean.() -> Unit) =
        evaluateJavascript("javascript:chart.getWidth()") {
            onResult.invoke("null" != it)
        }

    var option: String? = null
        set(value) {
            if (field != value) {
                field = value
                field?.let {
                    launch {
                        while (!check()) {
                            if (!isAttachedToWindow) {
                                cancel()
                            }
                            withContext(Dispatchers.IO) {
                                delay(100)
                            }
                        }
                        if (isAttachedToWindow) {
                            evaluateJavascript("javascript:chart.setOption($it, true)")
                        }
                    }
                }
            }
        }
}