package com.projemanag.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskflow.R
import com.example.taskflow.databinding.ItemMemeberBinding
import com.example.taskflow.models.User
import com.example.taskflow.utils.Constants

open class MemberListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<User>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemMemeberBinding
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemMemeberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            with (binding) {
                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(ivMemberImage)

                tvMemberName.text = model.name
                tvMemberEmail.text = model.email

                if (model.selected) {
                    ivSelectedMember.visibility = View.VISIBLE
                } else {
                    ivSelectedMember.visibility = View.GONE
                }

                holder.itemView.setOnClickListener {
                    if (onClickListener != null) {
                        if (model.selected) {
                            onClickListener!!.onClick(position, model, Constants.UN_SELECT)
                        } else {
                            onClickListener!!.onClick(position, model, Constants.SELECT)
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int  = list.size

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }
}