package com.example.taskflow.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.Resource
import com.example.taskflow.R
import com.example.taskflow.activities.TaskListActivity
import com.example.taskflow.databinding.ActivityBaseBinding
import com.example.taskflow.databinding.ItemTaskBinding
import com.google.android.gms.tasks.Task
import org.w3c.dom.Text

open class TaskListItemsAdapter(
    private val context: Context,

    private var list: ArrayList<com.example.taskflow.models.Task>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemTaskBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins((15.toDp()).toPx(), 0, (40.toDp()).toPx(), 0)
        binding.llTaskItem.layoutParams = layoutParams
        return MyViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            with(binding) {
                if (position == list.size - 1) {
                    tvAddTaskList.visibility = View.VISIBLE
                    llTaskItem.visibility = View.GONE
                } else {
                    tvAddTaskList.visibility = View.GONE
                    llTaskItem.visibility = View.VISIBLE
                }
                tvTaskListTitle.text = model.title
                tvAddTaskList.setOnClickListener {
                    tvAddTaskList.visibility = View.GONE
                    cvAddTaskListName.visibility = View.VISIBLE
                }
                ibCloseListName.setOnClickListener {
                    tvAddTaskList.visibility = View.VISIBLE
                    cvAddTaskListName.visibility = View.GONE
                }

                ibCloseListName.setOnClickListener {
                    val listName = binding.etTaskListName.text.toString()
                    if (listName.isNotEmpty()) {
                        (context as? TaskListActivity)?.createTaskList(listName)
                    } else {
                        Toast.makeText(context, "Please Enter List Name.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                ibEditListName.setOnClickListener {
                    binding.etEditTaskListName.setText(model.title)
                    binding.llTitleView.visibility = View.GONE
                    binding.cvEditTaskListName.visibility = View.VISIBLE
                }

                ibCloseEditableView.setOnClickListener {
                    binding.llTitleView.visibility = View.VISIBLE
                    binding.cvEditTaskListName.visibility = View.GONE
                }

                ibDoneEditListName.setOnClickListener {
                    val listName = binding.etEditTaskListName.text.toString()
                    if (listName.isNotEmpty()) {
                        (context as? TaskListActivity)?.updateTaskList(position, listName, model)
                    } else {
                        Toast.makeText(context, "Please Enter a List Name.", Toast.LENGTH_SHORT).show()
                    }
                }

                binding.ibDeleteList.setOnClickListener {
                    alertDialogForDeleteList(position, model.title)
                }
            }
        }
    }

    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()


    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
            if (context is TaskListActivity) {
                context.deleteTaskList(position)
            }
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}