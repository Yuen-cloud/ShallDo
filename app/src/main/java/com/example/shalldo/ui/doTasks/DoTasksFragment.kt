package com.example.shalldo.ui.doTasks

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shalldo.MenuActivity
import com.example.shalldo.R
import com.example.shalldo.ShallDoApplication
import com.example.shalldo.TimerActivity
import com.example.shalldo.ui.todoList.TodoListAdapter
import com.example.shalldo.ui.todoList.TodoListViewModel
import kotlinx.android.synthetic.main.fragment_do_tasks.*
import kotlinx.android.synthetic.main.fragment_todolist.*

class DoTasksFragment : Fragment() {

    private val doTasksViewModel by lazy { ViewModelProvider(this).get(DoTasksViewModel::class.java) }
    private lateinit var adapter: DoTasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_do_tasks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = DoTasksAdapter(this, doTasksViewModel.taskList)
        adapter.setOnItemClickListener(object : DoTasksAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val task = doTasksViewModel.taskList[position]
                setDialog(task.id, task.name, task.last)
            }
        })
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        do_tasks_view.layoutManager = layoutManager
        do_tasks_view.adapter = adapter
        doTasksViewModel.tasksLiveData.observe(viewLifecycleOwner, Observer { result ->
            val tasks = result.getOrNull()
            if (tasks != null) {
                do_tasks_view.visibility = View.VISIBLE
                doTasksViewModel.taskList.clear()
                doTasksViewModel.taskList.addAll(tasks)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(ShallDoApplication.context, "暂时还没有任务", Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        doTasksViewModel.queryAllTasks()
    }

    private fun setDialog(id: Int, name: String, last: Int) {
        val dialog = AlertDialog.Builder(activity)
        val text: TextView = TextView(activity)
        text.setText("是否开启任务--${name}？\n时长--${last}分钟")
        dialog.setView(text)
        dialog.setPositiveButton("确定",
            DialogInterface.OnClickListener { dialogInterface, i ->
                val intent = Intent(activity, TimerActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("name", name)
                intent.putExtra("last", last)
                startActivity(intent)
            }
        )
        dialog.setNegativeButton("取消",
            DialogInterface.OnClickListener { dialogInterface, i ->

            }
        )
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        doTasksViewModel.queryAllTasks()
    }

    override fun onStart() {
        super.onStart()
        doTasksViewModel.queryAllTasks()
    }
}