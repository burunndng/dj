package com.wave.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  private lateinit var webView: WebView
  private lateinit var splashScreen: View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    splashScreen = findViewById(R.id.splash_container)
    webView = findViewById(R.id.main_webview)

    webView.settings.javaScriptEnabled = true
    webView.settings.domStorageEnabled = true
    webView.settings.loadsImagesAutomatically = true
    webView.settings.useWideViewPort = true
    webView.settings.loadWithOverviewMode = true
    webView.webChromeClient = WebChromeClient()
    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
      }
    }

    Handler(Looper.getMainLooper()).postDelayed({
      splashScreen.visibility = View.GONE
      webView.visibility = View.VISIBLE
      if (savedInstanceState == null) {
        webView.loadUrl(TARGET_URL)
      }
    }, 1200)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    webView.saveState(outState)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    webView.restoreState(savedInstanceState)
  }

  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    if (::webView.isInitialized && webView.visibility == View.VISIBLE && webView.canGoBack()) {
      webView.goBack()
    } else {
      super.onBackPressed()
    }
  }

  companion object {
    private const val TARGET_URL = "https://github.com/burunndng/dj/tree/main"
  }
}
