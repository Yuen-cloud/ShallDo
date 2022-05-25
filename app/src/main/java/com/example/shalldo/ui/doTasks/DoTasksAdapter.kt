package com.example.shalldo.ui.doTasks

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.shalldo.R
import com.example.shalldo.ShallDoApplication
import com.example.shalldo.logic.model.Task
import com.example.shalldo.ui.todoList.TodoListAdapter
import com.google.android.material.card.MaterialCardView

class DoTasksAdapter(private val fragment: Fragment, private val taskList: List<Task>) :
    RecyclerView.Adapter<DoTasksAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemState: ImageView = view.findViewById(R.id.item_state)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val item: MaterialCardView = view.findViewById(R.id.do_tasks_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.do_tasks_item, parent, false)
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
        if (task.state == 0) {
            holder.itemState.setImageResource(R.drawable.unfinished)
        } else {
            holder.itemState.setImageResource(R.drawable.finished)
        }
        val csl = ColorStateList(arrayOf(intArrayOf(task.state), intArrayOf()), intArrayOf(Color.RED, Color.GREEN))
        holder.item.setStrokeColor(csl)
        holder.itemName.text = task.name
        holder.item.setOnClickListener {
            if (task.state == 1)
                Toast.makeText(ShallDoApplication.context, "该任务已完成", Toast.LENGTH_SHORT).show()
            else
                mOnItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = taskList.size
}