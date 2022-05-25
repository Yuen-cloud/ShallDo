package com.example.shalldo.ui.todoList

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.shalldo.MenuActivity
import com.example.shalldo.R
import com.example.shalldo.ShallDoApplication
import com.example.shalldo.logic.model.Task
import com.google.android.material.card.MaterialCardView
import com.wx.wheelview.adapter.ArrayWheelAdapter
import com.wx.wheelview.adapter.BaseWheelAdapter
import com.wx.wheelview.widget.WheelView
import kotlinx.android.synthetic.main.todolist_item.view.*

class TodoListAdapter(private val fragment: Fragment, private val taskList: List<Task>) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemLast: TextView = view.findViewById(R.id.item_last)
        val item: MaterialCardView = view.findViewById(R.id.todolist_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)
        return ViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    public fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.itemName.text = task.name
        holder.itemLast.text = "时长: " + task.last + "分钟"
        holder.item.setOnClickListener {
            mOnItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = taskList.size
}