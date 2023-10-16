package com.example.taskflow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskflow.R
import com.example.taskflow.databinding.ItemCardSelectedMemberBinding
import com.example.taskflow.databinding.ItemTaskBinding
import com.example.taskflow.models.SelectedMembers
import de.hdodenhof.circleimageview.CircleImageView

class CardMemberListItemsAdapter
    (private val context: Context,
     private val list: ArrayList<SelectedMembers>,
     private val assignMembers: Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var onClickListener: OnClickListener? = null
    private lateinit var binding: ItemCardSelectedMemberBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemCardSelectedMemberBinding.inflate(LayoutInflater
            .from(parent.context), parent, false)
        return MyViewHolder(binding.root)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            if (position == list.size - 1 && assignMembers) {
                binding.ivAddMember.visibility = View.VISIBLE
                binding.ivSelectedMemberImage.visibility = View.GONE
            } else {
                binding.ivAddMember.visibility = View.GONE
                binding.ivSelectedMemberImage.visibility = View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding.ivSelectedMemberImage)
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick()
                }
            }
        }

    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick()
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}