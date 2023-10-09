package com.example.taskflow.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.R
import com.example.taskflow.databinding.ActivityCardDetailsBinding
import com.example.taskflow.databinding.ItemLabelColorBinding
import com.example.taskflow.databinding.ItemMemeberBinding

class LabelColorListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private val mSelectedColor: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemLabelColorBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemLabelColorBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding.root)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is MyViewHolder) {
            binding.viewMain.setBackgroundColor(Color.parseColor(item))
            if (item == mSelectedColor) {
                binding.ivSelectedColor.visibility = View.VISIBLE
            } else {
                binding.ivSelectedColor.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onClick(position, item)
                }
            }
        }
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun onClick(position: Int, color: String)
    }
}