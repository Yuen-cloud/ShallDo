package com.example.shalldo.ui.todoList

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shalldo.MenuActivity
import com.example.shalldo.R
import com.example.shalldo.ShallDoApplication
import com.wx.wheelview.adapter.ArrayWheelAdapter
import com.wx.wheelview.adapter.BaseWheelAdapter
import com.wx.wheelview.widget.WheelView
import kotlinx.android.synthetic.main.fragment_todolist.*
import kotlin.collections.ArrayList

class TodoListFragment : Fragment() {

    private val todoListViewModel by lazy { ViewModelProvider(this).get(TodoListViewModel::class.java) }
    private lateinit var adapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todolist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = TodoListAdapter(this, todoListViewModel.taskList)
        adapter.setOnItemClickListener(object : TodoListAdapter.OnItemClickListener {
           override fun onItemClick(position: Int) {
                val task = todoListViewModel.taskList[position]
                setDialog(task.id, task.name, task.last)
            }
        })
        val layoutManager = LinearLayoutManager(ShallDoApplication.context)
        todolist_view.layoutManager = layoutManager
        todolist_view.adapter = adapter
        addTask.setOnClickListener {
            setAddDialog()
        }
        todoListViewModel.tasksLiveData.observe(viewLifecycleOwner, Observer { result ->
            val tasks = result.getOrNull()
            if (tasks != null) {
                todolist_view.visibility = View.VISIBLE
                todoListViewModel.taskList.clear()
                todoListViewModel.taskList.addAll(tasks)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(ShallDoApplication.context, "暂时还没有任务", Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        todoListViewModel.queryAllTasks()
    }

    private fun setDialog(id: Int, name: String, last: Int) {
        val data = 15 until 95 step 5
        val str = ArrayList<String>()
        for (i in data) {
            str.add(i.toString())
        }
        val dialogView: View = LayoutInflater.from(activity).inflate(R.layout.todolst_dialog, null)
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("新添任务").setView(dialogView)
        val taskNameEditText: EditText = dialogView.findViewById(R.id.edit_text) as EditText
        taskNameEditText.setText(name)
        val taskLastWheelView: WheelView<Any> = dialogView.findViewById(R.id.wheel_view) as WheelView<Any>
        taskLastWheelView.setWheelAdapter(ArrayWheelAdapter(activity) as BaseWheelAdapter<Any>)
        taskLastWheelView.setWheelData(str as List<Any>?)
        taskLastWheelView.skin = WheelView.Skin.Holo
        taskLastWheelView.selection = (last - 15) / 5
        dialog.setPositiveButton("提交",
            DialogInterface.OnClickListener { dialogInterface, i ->
                val taskName = taskNameEditText.text.toString()
                val taskLast = taskLastWheelView.selectionItem.toString().toInt()
                if (taskName == "")
                    Toast.makeText(ShallDoApplication.context, "任务名不能为空", Toast.LENGTH_SHORT).show()
                else {
                    MenuActivity.db.execSQL(
                        "update tasks set name = ?, last = ? where id = ?",
                        arrayOf(taskName, taskLast, id)
                    )
                    Toast.makeText(ShallDoApplication.context, "更新任务成功", Toast.LENGTH_SHORT).show()
                    todoListViewModel.queryAllTasks()
                }
            }
        )
        dialog.setNegativeButton("删除",
            DialogInterface.OnClickListener { dialogInterface, i ->
                MenuActivity.db.execSQL(
                    "delete from tasks where id = ?",
                    arrayOf(id)
                )
                Toast.makeText(ShallDoApplication.context, "删除任务成功", Toast.LENGTH_SHORT).show()
                todoListViewModel.queryAllTasks()
            }
        )
        dialog.setNeutralButton("取消",
            DialogInterface.OnClickListener { dialogInterface, i ->

            }
        )
        dialog.show()
    }

    private fun setAddDialog() {
        val data = 15 until 95 step 5
        val str = ArrayList<String>()
        for (i in data) {
            str.add(i.toString())
        }
        val dialogView: View = LayoutInflater.from(activity).inflate(R.layout.todolst_dialog, null)
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("新添任务").setView(dialogView)
        val taskNameEditText: EditText = dialogView.findViewById(R.id.edit_text) as EditText
        val taskLastWheelView: WheelView<Any> = dialogView.findViewById(R.id.wheel_view) as WheelView<Any>
        taskLastWheelView.setWheelAdapter(ArrayWheelAdapter(activity) as BaseWheelAdapter<Any>)
        taskLastWheelView.setWheelData(str as List<Any>?)
        taskLastWheelView.skin = WheelView.Skin.Holo
        dialog.setPositiveButton("提交",
            DialogInterface.OnClickListener { dialogInterface, i ->
                val taskName = taskNameEditText.text.toString()
                val taskLast = taskLastWheelView.selectionItem.toString().toInt()
                if (taskName == "")
                    Toast.makeText(ShallDoApplication.context, "任务名不能为空", Toast.LENGTH_SHORT).show()
                else {
                    MenuActivity.db.execSQL(
                        "insert into tasks(name, last, state) values (?, ?, ?)",
                        arrayOf(taskName, taskLast, 0)
                    )
                    Toast.makeText(ShallDoApplication.context, "添加任务成功", Toast.LENGTH_SHORT).show()
                    todoListViewModel.queryAllTasks()
                }
            }
        )
        dialog.setNegativeButton("取消",
            DialogInterface.OnClickListener { dialogInterface, i ->

            }
        )
        dialog.show()
    }
}
