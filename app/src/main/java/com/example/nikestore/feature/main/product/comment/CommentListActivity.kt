package com.example.nikestore.feature.main.product.comment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.NikeActivity

import com.example.nikestore.common.EXTRA_KEY_Id
import com.example.nikestore.data.Comment

import com.example.nikestore.databinding.ActivityCommentListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {
    private lateinit var binding: ActivityCommentListBinding
    val viewModel: CommentListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_Id
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }
        viewModel.commentsLiveData.observe(this) {
            val adapter = CommentAdapter(true)
            binding.commentRv.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapter.comments = it as ArrayList<Comment>
            binding.commentRv.adapter = adapter
        }
        binding.commentListToolbar.onBackButtonClickListener = View.OnClickListener {
            this.finish()
        }
    }
}