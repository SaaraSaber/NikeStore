package com.example.nikestore.feature.main.product.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.data.Comment

class CommentAdapter(val showAll: Boolean = false) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    var comments = ArrayList<Comment>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.commentTitleTv)
        val dateTv = itemView.findViewById<TextView>(R.id.commentDateTv)
        val authorTv = itemView.findViewById<TextView>(R.id.commentAuthor)
        val contectTv = itemView.findViewById<TextView>(R.id.commentContent)

        fun bindComment(comment: Comment) {
            titleTv.text = comment.title
            dateTv.text = comment.date
            authorTv.text = comment.author.email
            contectTv.text = comment.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComment(comments[position])
    }

    override fun getItemCount(): Int {
        return if (comments.size > 3 && !showAll) 3 else comments.size
    }
}