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
    defStyleAttr: Int = 0,
) : WebView(context, attrs, defStyleAttr), CoroutineScope by MainScope() {

    /**
     * 空时使用默认实现，可以模仿本项目放置在 assets 中，也可以将文件放置在云端。
     * 这里可以设置自己需要的 echarts 版本，详细参考 core/src/main/assets/index.html
     * [参考](https://echarts.apache.org/zh/api.html#echarts.init)
     *
     * 1.2.0 已从 [jsDelivr CDN](https://www.jsdelivr.com/package/npm/echarts) 获取 echarts.js。
     * 目的是减少包体积，为了快速加载建议采用本地内置，权衡在你。
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
    var initScript: String? = null
        get() = field ?: """javascript:
            ${extensionsScript ?: ""}
            ${themeScript ?: ""}
            var $echartsInstance = echarts.init(
                document.getElementById('$h5ChartDomId'),
                ${themeName?.let { "'$themeName'" }},
                $optsScript
            );
            ${moreScript ?: ""}
            ${if (option == null) "" else "$echartsInstance.setOption($option, true);"}
            window.onresize = function() { $echartsInstance.resize(); }
            void(0);
            """.trimIndent()
        private set

    @JvmOverloads
    fun setInitScript(value: String, block: ((String) -> Unit)? = null) {
        if (initScript != value) {
            initScript = value
            refresh(block)
        }
    }

    /**
     * 支持主题。让你的图表整体换个装，除了官方提供的主题之外，还可以定制你自己的主题。
     * [参考](https://echarts.apache.org/zh/download-extension.html)
     */
    @Language("JavaScript")
    var themeScript: String? = null
        private set

    /**
     * 注册主题脚本后需要设置 [themeName] 生效
     * @param value String
     * @param block Function1<String, Unit>?
     */
    @JvmOverloads
    fun registerThemeScript(value: String, block: ((String) -> Unit)? = null) {
        if (themeScript != value) {
            themeScript = value
            runOnChecked { evaluateJavascript("javascript:$themeScript", block) }
        }
    }

    var themeName: String? = null
        private set

    /**
     * ECharts5 除了一贯的默认主题外，还内置了 'dark' 主题，所以 dark 无需注册主题脚本。
     * @param value String
     * @param block Function1<String, Unit>?
     */
    @JvmOverloads
    fun setThemeName(value: String, block: ((String) -> Unit)? = null) {
        if (themeName != value) {
            themeName = value
            refresh(block)
        }
    }

    /**
     * 附加参数，本项目默认实现中 renderer 渲染模式默认采用 SVG。
     * [参考](https://echarts.apache.org/handbook/zh/best-practices/canvas-vs-svg/)
     */
    @Language("JavaScript")
    lateinit var optsScript: String
        private set

    @JvmOverloads
    fun setOptsScript(value: String, block: ((String) -> Unit)? = null) {
        if (optsScript != value) {
            optsScript = value
            refresh(block)
        }
    }

    /**
     * 支持扩展s。各类 ECharts 扩展插件，获取更丰富的图表类型和增强功能。多个扩展用 \n 隔开
     * [参考](https://echarts.apache.org/zh/download-extension.html)
     */
    @Language("JavaScript")
    var extensionsScript: String? = null
        private set

    @JvmOverloads
    fun setExtensionsScript(value: String, block: ((String) -> Unit)? = null) {
        if (extensionsScript != value) {
            extensionsScript = value
            refresh(block)
        }
    }

    /**
     * 附加脚本。可以在初始化完成后做些事，例如点击事件的初始化
     */
    @Language("JavaScript")
    var moreScript: String? = null
        private set

    @JvmOverloads
    fun setMoreScript(value: String, block: ((String) -> Unit)? = null) {
        if (moreScript != value) {
            moreScript = value
            refresh(block)
        }
    }

    private fun refresh(block: ((String) -> Unit)? = null) {
        runOnChecked {
            evaluateJavascript(
                """javascript:
                    try {
                      $echartsInstance.dispose();
                    } catch(e) {}
                    $echartsInstance = echarts.init(
                        document.getElementById('$h5ChartDomId'),
                        ${themeName?.let { "'$themeName'" }},
                        $optsScript
                    );
                    $echartsInstance.setOption($option, true);
                """.trimIndent(),
                block
            )
        }
    }

    var echartsInstance = "chart"
    var h5ChartDomId = "chart"

    /**
     * 初始化时可以带 option。[参考](https://echarts.apache.org/zh/option.html)
     */
    var option: String? = null
        private set

    @JvmOverloads
    fun setOption(value: String, block: ((String) -> Unit)? = null) {
        if (option != value) {
            option = value
            option?.let {
                runOnChecked {
                    if (isAttachedToWindow) {
                        evaluateJavascript("javascript:$echartsInstance.setOption($it, true);", block)
                    }
                }
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.EChartsWebView, defStyleAttr) {
            val url = getString(R.styleable.EChartsWebView_initUrl) ?: "file:///android_asset/index.html"

            themeScript = getString(R.styleable.EChartsWebView_themeScript)
            themeName = getString(R.styleable.EChartsWebView_themeName)
            optsScript = getString(R.styleable.EChartsWebView_optsScript) ?: "{renderer: 'svg'}"
            extensionsScript = getString(R.styleable.EChartsWebView_extensionsScript)
            moreScript = getString(R.styleable.EChartsWebView_moreScript)

            option = getString(R.styleable.EChartsWebView_option)

            initScript = getString(R.styleable.EChartsWebView_initScript)

            initEcharts(url)
        }
    }

    protected open fun initEcharts(url: String) {
        kotlin.runCatching {
            settings.apply {
                javaScriptEnabled = true
                displayZoomControls = false
            }
            webViewClient = object : WebViewClient() {}
        }

        setInitUrl(url)
    }

    override fun setWebViewClient(client: WebViewClient) {
        val inner = careWebViewClient(
            client,
            onPageFinished = object : (WebView?, String?) -> Unit {
                override fun invoke(view: WebView?, url: String?) {
                    evaluateJavascript(initScript!!, null)
                }
            }
        )
        super.setWebViewClient(inner)
    }

    fun runOnChecked(timeMillis: Long = 200, block: () -> Unit) {
        launch {
            while (!check()) {
                if (!isAttachedToWindow) {
                    cancel()
                }
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
    suspend fun check() = "null" != evaluateJavascript("javascript:$echartsInstance.getWidth();")

    /**
     * 检查 chart 是否实例化成功
     *
     * @param onResult [@kotlin.ExtensionFunctionType] Function1<Boolean, Unit>
     */
    fun check(onResult: Boolean.() -> Unit) =
        evaluateJavascript("javascript:$echartsInstance.getWidth();") {
            onResult.invoke("null" != it)
        }
}