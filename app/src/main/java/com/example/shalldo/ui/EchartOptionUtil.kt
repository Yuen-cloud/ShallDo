package com.example.shalldo.ui

import com.github.abel533.echarts.axis.CategoryAxis
import com.github.abel533.echarts.axis.ValueAxis
import com.github.abel533.echarts.code.Trigger
import com.github.abel533.echarts.json.GsonOption
import com.github.abel533.echarts.series.Line

object EchartOptionUtil {
    fun getLineChartOptions(
        finishedList: Array<Any?>,
        unfinishedList: Array<Any?>,
        dateList: Array<Any?>
    ): GsonOption {
        val option = GsonOption()
        option.title("近期日完成情况")
        option.tooltip().trigger(Trigger.axis)
        val valueAxis = ValueAxis()
        option.yAxis(valueAxis)
        val categorxAxis = CategoryAxis()
        categorxAxis.axisLine().onZero(false)
        categorxAxis.boundaryGap(true)
        categorxAxis.data(*dateList)
        option.xAxis(categorxAxis)
        val line1 = Line()
        line1.smooth(false).name("完成任务数").data(*finishedList).itemStyle().normal()
            .shadowColor("rgba(0,0,0,0.4)").color("#00EE76")
        val line2 = Line()
        line2.smooth(false).name("未完成任务数").data(*unfinishedList).itemStyle().normal()
            .shadowColor("rgba(0,0,0,0.4)").color("#EE2C2C")
        option.series(line1, line2)
        return option
    }
}