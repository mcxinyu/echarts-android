package com.mcxinyu.echartsandroid.webview

import android.webkit.JavascriptInterface
import android.webkit.WebView

/**
 * 快速实现一个可绑定到 JavaScript 的对象
 *
 * 注意：绑定到 JavaScript 的对象在另一个线程中运行，而不是在构造它的线程中运行。
 *
 * @property interfaceName String. 这会为在 WebView 中运行的 JavaScript 创建名为 [interfaceName] 的接口。
 * 使用 [addJavascriptInterface] 扩展方法，会自动向 WebView 注册此接口。
 * @property onMessage Function1<String, Unit>
 * @constructor
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/3/24.
 */
class JavaScriptInterface(val interfaceName: String, private val onMessage: (String?) -> String?) {

    /**
     * 该方法暴露给 javaScript 调用。
     * 此时，Web 应用可以通过 [interfaceName] 访问 [JavaScriptInterface] 类。
     *
     * 假如 [interfaceName] = Android
     *```html
     *  <input type="button" value="Say hello" onClick="showAndroidToast('Hello Android!')" />
     *
     *  <script type="text/javascript">
     *      function showAndroidToast(toast) {
     *          Android.postMessage(JSON.stringify({
     *              type: 'showToast',
     *              payload: toast,
     *          }));
     *      }
     *  </script>
     *```
     *
     * 你甚至可以将处理后的数据直接返回给 Web 应用。
     *
     * 注意：绑定到 JavaScript 的对象在另一个线程中运行，而不是在构造它的线程中运行。
     * 因此，[onMessage] 如果需要修改 UI，应该在主线程中运行。
     *
     * @param message String 请求信息，建议内容序列化为 json 字符串
     * @return String 返回数据给 Web 应用
     */
    @JavascriptInterface
    fun postMessage(message: String?) = onMessage.invoke(message)
}

data class SampleMessage(val type: String?, val payload: Any?) : java.io.Serializable

/**
 * 快速与 Web 交互的方法，详见 [JavaScriptInterface]
 *
 * @receiver WebView
 * @param jsInterface [JavaScriptInterface]
 */
fun WebView.addJavascriptInterface(jsInterface: JavaScriptInterface) =
    addJavascriptInterface(jsInterface, jsInterface.interfaceName)
