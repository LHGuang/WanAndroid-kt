package com.lhguang.wanandroid_kt.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

open class BaseBindingViewHolder<VB : ViewBinding>(vb: VB) : BaseViewHolder(vb.root) {
    val binding = vb
}