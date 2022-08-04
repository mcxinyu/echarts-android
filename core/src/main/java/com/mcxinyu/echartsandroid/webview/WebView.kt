package com.mcxinyu.echartsandroid.webview

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.view.KeyEvent
import android.webkit.*
import android.webkit.WebView
import androidx.annotation.RequiresApi
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

@Suppress("DEPRECATION")
internal fun careWebViewClient(
    origin: WebViewClient,
    onPageStarted: ((view: WebView?, url: String?, favicon: Bitmap?) -> Unit)? = null,
    onPageFinished: ((view: WebView?, url: String?) -> Unit)? = null
): WebViewClient {
    return object : WebViewClient() {
        @Deprecated("Deprecated in Java", ReplaceWith("client.shouldOverrideUrlLoading(view, url)"))
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return origin.shouldOverrideUrlLoading(view, url)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return origin.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            onPageStarted?.invoke(view, url, favicon)
            origin.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            onPageFinished?.invoke(view, url)
            origin.onPageFinished(view, url)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            origin.onLoadResource(view, url)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onPageCommitVisible(view: WebView?, url: String?) {
            origin.onPageCommitVisible(view, url)
        }

        @Deprecated("Deprecated in Java", ReplaceWith("client.shouldInterceptRequest(view, url)"))
        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            return origin.shouldInterceptRequest(view, url)
        }

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            return origin.shouldInterceptRequest(view, request)
        }

        @Deprecated("Deprecated in Java", ReplaceWith("client.onTooManyRedirects(view, cancelMsg, continueMsg)"))
        override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
            origin.onTooManyRedirects(view, cancelMsg, continueMsg)
        }

        @Deprecated(
            "Deprecated in Java",
            ReplaceWith("client.onReceivedError(view, errorCode, description, failingUrl)")
        )
        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            origin.onReceivedError(view, errorCode, description, failingUrl)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            origin.onReceivedError(view, request, error)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            origin.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
            origin.onFormResubmission(view, dontResend, resend)
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            origin.doUpdateVisitedHistory(view, url, isReload)
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            origin.onReceivedSslError(view, handler, error)
        }

        override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
            origin.onReceivedClientCertRequest(view, request)
        }

        override fun onReceivedHttpAuthRequest(
            view: WebView?,
            handler: HttpAuthHandler?,
            host: String?,
            realm: String?
        ) {
            origin.onReceivedHttpAuthRequest(view, handler, host, realm)
        }

        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            return origin.shouldOverrideKeyEvent(view, event)
        }

        override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
            origin.onUnhandledKeyEvent(view, event)
        }

        override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
            origin.onScaleChanged(view, oldScale, newScale)
        }

        override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
            origin.onReceivedLoginRequest(view, realm, account, args)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
            return origin.onRenderProcessGone(view, detail)
        }

        @RequiresApi(Build.VERSION_CODES.O_MR1)
        override fun onSafeBrowsingHit(
            view: WebView?,
            request: WebResourceRequest?,
            threatType: Int,
            callback: SafeBrowsingResponse?
        ) {
            origin.onSafeBrowsingHit(view, request, threatType, callback)
        }
    }
}