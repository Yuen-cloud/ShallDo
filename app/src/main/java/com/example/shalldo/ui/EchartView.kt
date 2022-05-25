package com.example.shalldo.ui

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import com.github.abel533.echarts.json.GsonOption

class EchartView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    WebView(context!!, attrs, defStyleAttr) {
    private fun init() {
        val webSettings = settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportZoom(false)
        webSettings.displayZoomControls = false
        loadUrl("file:///android_asset/echarts.html")
    }

    /**刷新图表
     * java调用js的loadEcharts方法刷新echart
     * 不能在第一时间就用此方法来显示图表，因为第一时间html的标签还未加载完成，不能获取到标签值
     * @param option
     */
    fun refreshEchartsWithOption(option: GsonOption?) {
        if (option == null) {
            return
        }
        val optionString = option.toString()
        val call = "javascript:loadEcharts('$optionString')"
        loadUrl(call)
    }

    companion object {
        private val TAG = EchartView::class.java.simpleName
    }

    init {
        init()
    }
}