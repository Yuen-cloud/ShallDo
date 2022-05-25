package com.example.shalldo.ui.myInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shalldo.LoginActivity
import com.example.shalldo.R
import com.example.shalldo.ShallDoApplication
import com.example.shalldo.logic.model.User
import com.example.shalldo.ui.EchartOptionUtil.getLineChartOptions
import kotlinx.android.synthetic.main.fragment_my_info.*
import kotlinx.android.synthetic.main.fragment_todolist.*


class MyInfoFragment : Fragment() {

    private val myInfoViewModel: MyInfoViewModel by lazy { ViewModelProvider(this).get(MyInfoViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_info, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        username.text = User.getName()
        exit_btn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("cancel_auto_login", true)
            startActivity(intent)
            activity?.finish()
        }
        sit_chart.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                refreshLineChart()
            }
        }
        myInfoViewModel.sitLiveData.observe(viewLifecycleOwner, Observer { result ->
            val sit = result.getOrNull()
            if (sit != null) {
                myInfoViewModel.finishedList = sit.finishedList
                myInfoViewModel.unfinishedList = sit.unfinishedList
                myInfoViewModel.dateList = sit.dateList
            } else {
                Toast.makeText(ShallDoApplication.context, "暂时还没有情况统计", Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        myInfoViewModel.querySit()

    }

    private fun refreshLineChart() {
        sit_chart.refreshEchartsWithOption(
            getLineChartOptions(
                myInfoViewModel.finishedList.toArray(),
                myInfoViewModel.unfinishedList.toArray(),
                myInfoViewModel.dateList.toArray()
            )
        )
    }
}