package com.mcxinyu.echartsandroid

import android.webkit.WebView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 *
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/3/24.
 */
private class WebView

/**
 * 协程版本的 [WebView.evaluateJavascript]
 *
 * 在当前显示页面的上下文中异步运行 JavaScript。
 *
 * @receiver WebView
 * @param script String
 * @return String?
 */
suspend inline fun WebView.evaluateJavascript(script: String): String? =
    suspendCancellableCoroutine { continuation ->
        evaluateJavascript(script) { continuation.resume(it) }
    }

/**
 * 快速与 Web 交互的方法，详见 [JavaScriptInterface]
 *
 * @receiver WebView
 * @param jsInterface JavaScriptInterface
 */
fun WebView.addJavascriptInterface(jsInterface: JavaScriptInterface) =
    addJavascriptInterface(jsInterface, jsInterface.interfaceName)