package com.example.trendingrepos.ui.trendingrepos.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trendingrepos.R
import com.example.trendingrepos.data.TrendingRepo
import com.example.trendingrepos.databinding.ViewItemTrendingReposBinding

class TrendingReposAdapter(
    private val trendingRepoList: MutableList<TrendingRepo>,
    private val callbackListener: (Int) -> Unit
) :
    RecyclerView.Adapter<TrendingReposAdapter.ViewHolder>() {

    fun updateSelectedItem(updatedUserList: List<TrendingRepo>) {
        val diffResult =
            calculateDiff(TrendingReposDiffUtilCallback(this.trendingRepoList, updatedUserList))
        diffResult.dispatchUpdatesTo(this)
        this.trendingRepoList.clear()
        this.trendingRepoList.addAll(updatedUserList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ViewItemTrendingReposBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(trendingReposHolder: ViewHolder, position: Int) {
        trendingReposHolder.bind(callbackListener, trendingRepoList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return trendingRepoList.size
    }

    inner class ViewHolder(val binding: ViewItemTrendingReposBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(callbackListener: (Int) -> (Unit), trendingRepo: TrendingRepo) {
            binding.apply {
                root.isSelected = trendingRepo.isSelected
                Glide.with(itemView)
                    .load(trendingRepo.owner.avatarUrl)
                    .centerCrop()
                    .error(android.R.drawable.stat_notify_error)
                    .into(avatar)

                val str = SpannableString(trendingRepo.owner.login + " / " + trendingRepo.name)
                str.setSpan(
                    StyleSpan(Typeface.BOLD),
                    trendingRepo.owner.login.length,
                    str.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                name.text = str
                description.text = trendingRepo.description
                language.text = itemView.context.getString(R.string.language,trendingRepo.language)
                ViewCompat.setTransitionName(this.avatar, "avatar_${trendingRepo.id}")
                root.setOnClickListener {
                    callbackListener.invoke(adapterPosition)
                }
            }

        }
    }
}