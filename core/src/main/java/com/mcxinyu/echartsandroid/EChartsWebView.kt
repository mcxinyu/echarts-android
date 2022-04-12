package com.mcxinyu.echartsandroid

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.withStyledAttributes
import com.mcxinyu.echartsandroid.webview.careWebViewClient
import com.mcxinyu.echartsandroid.webview.evaluateJavascript
import kotlinx.coroutines.*
import org.intellij.lang.annotations.Language

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

    /**
     * 空时使用默认实现，可以模仿本项目放置在 assets 中，也可以将文件放置在云端。
     * [参考](https://echarts.apache.org/zh/api.html#echarts.init)
     */
    var initUrl: String = ""
        private set

    fun setInitUrl(value: String) {
        if (initUrl != value) {
            initUrl = value
            post { loadUrl(value) }
        }
    }

    /**
     * initScript 可自行实现初始化方式，逻辑上在 initUrl 之后。如果 initUrl 中实现了初始化，此字段可以忽略。
     * [参考](https://echarts.apache.org/zh/api.html#echarts.init)
     */
    @Language("JavaScript")
    lateinit var initScript: String
        private set

    fun setInitScript(value: String, block: ((String) -> Unit)? = null) {
        if (initScript != value) {
            initScript = value
            evaluateJavascript("javascript$initScript", block)
        }
    }

    /**
     * 支持主题。让你的图表整体换个装，除了官方提供的主题之外，还可以定制你自己的主题。
     * [参考](https://echarts.apache.org/zh/download-extension.html)
     */
    lateinit var themeScript: String
        private set

    fun setThemeScript(value: String, block: ((String) -> Unit)? = null) {
        if (themeScript != value) {
            themeScript = value
            evaluateJavascript(
                """javascript:
                $jsChartName.dispose();
                $jsChartName = echarts.init(
                    document.getElementById('$h5ChartDomId'),
                    $themeScript,
                    $optsScript
                );
                $jsChartName.setOption($option, true);
            """.trimIndent(),
                block
            )
        }
    }

    /**
     * 附加参数，本项目默认实现中 renderer 渲染模式默认采用 SVG。
     * [参考](https://echarts.apache.org/handbook/zh/best-practices/canvas-vs-svg/)
     */
    lateinit var optsScript: String
        private set

    /**
     * 支持扩展s。各类 ECharts 扩展插件，获取更丰富的图表类型和增强功能。多个扩展用 \n 隔开
     * [参考](https://echarts.apache.org/zh/download-extension.html)
     */
    private lateinit var extensionsScript: String
        private set

    /**
     * 附加脚本。可以在初始化完成后做些事，例如点击事件的初始化
     */
    lateinit var moreScript: String
        private set

    var jsChartName = "chart"
    var h5ChartDomId = "chart"

    /**
     * 初始化时可以带 option。[参考](https://echarts.apache.org/zh/option.html)
     */
    var option: String? = null
        private set

    fun setOption(value: String, block: ((String) -> Unit)? = null) {
        if (option != value) {
            option = value
            option?.let {
                runOnChecked {
                    if (isAttachedToWindow) {
                        evaluateJavascript("javascript:$jsChartName.setOption($it, true);", block)
                    }
                }
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.EChartsWebView, defStyleAttr) {
            val url = getString(R.styleable.EChartsWebView_initUrl) ?: "file:///android_asset/index.html"

            themeScript = getString(R.styleable.EChartsWebView_themeScript) ?: "null"
            optsScript = getString(R.styleable.EChartsWebView_optsScript) ?: "{renderer: 'svg'}"
            extensionsScript = getString(R.styleable.EChartsWebView_extensionsScript) ?: ""
            moreScript = getString(R.styleable.EChartsWebView_moreScript) ?: ""

            option = getString(R.styleable.EChartsWebView_option)

            initScript = getString(R.styleable.EChartsWebView_initScript) ?: """javascript:
                $extensionsScript
                var $jsChartName = echarts.init(
                    document.getElementById('$h5ChartDomId'),
                    $themeScript,
                    $optsScript
                );
                $moreScript
                ${if (option == null) "" else "$jsChartName.setOption($option, true);"}
                window.onresize = function() { $jsChartName.resize(); }
                void(0);
                """.trimIndent()

            kotlin.runCatching {
                settings.apply {
                    javaScriptEnabled = true
                    displayZoomControls = false
                }
                webViewClient = object : WebViewClient() {}
            }

            setInitUrl(url)
        }
    }

    override fun setWebViewClient(client: WebViewClient) {
        val inner = careWebViewClient(
            client,
            onPageFinished = object : (WebView?, String?) -> Unit {
                override fun invoke(view: WebView?, url: String?) {
                    // 初始化 echarts
                    evaluateJavascript(initScript, null)
                }
            },
        )
        super.setWebViewClient(inner)
    }

    fun runOnChecked(timeMillis: Long = 200, block: () -> Unit) {
        launch {
            while (!check()) {
                withContext(Dispatchers.IO) { delay(timeMillis) }
            }

            block.invoke()
        }
    }

    /**
     * 检查 chart 是否实例化成功
     *
     * @return Boolean
     */
    suspend fun check() = "null" != evaluateJavascript("javascript:$jsChartName.getWidth();")

    /**
     * 检查 chart 是否实例化成功
     *
     * @param onResult [@kotlin.ExtensionFunctionType] Function1<Boolean, Unit>
     */
    fun check(onResult: Boolean.() -> Unit) =
        evaluateJavascript("javascript:$jsChartName.getWidth();") {
            onResult.invoke("null" != it)
        }
}