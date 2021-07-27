package com.lhguang.wanandroid_kt.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Constructor

abstract open class BaseAdapter<E, VH : BaseViewHolder>
constructor(
    var viewHolderClass: Class<*>,//必传参数，该适配器需要使用的viewholder类class
    var recyclerView: RecyclerView? = null,
    itemData: MutableList<E>? = null
) :
    RecyclerView.Adapter<VH>() {

    var itemDatas: MutableList<E> = itemData ?: mutableListOf()//列表的数据

    var onRecycleViewItemClickListener: OnRecycleViewItemClickListener? = null//列表item点击响应
    var onRecycleViewItemLongClickListener: OnRecycleViewItemLongClickListener? =
        null//列表item长按响应


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val itemView =
            LayoutInflater.from(parent.context).inflate(getItemViewLayoutId(), parent, false)

        val clazz: Class<*>? = viewHolderClass
        val constructor: Constructor<*> =
            clazz?.getDeclaredConstructor(View::class.java) as Constructor<*>
        constructor.isAccessible = true

        val itemViewHolder: VH = constructor.newInstance(itemView) as VH
        onItemCreateViewHolder(itemViewHolder, viewType)
        return itemViewHolder
    }

    /**
     * （可选重写）当 item 的 ViewHolder创建完毕后，执行此方法。
     * 可在此对 ViewHolder 进行处理，例如进行 DataBinding 绑定 view
     *
     * @param holder VH
     * @param viewType Int
     */
    protected open fun onItemCreateViewHolder(holder: VH, viewType: Int) {}

    protected abstract fun getItemViewLayoutId(): Int//获取item视图的id
    protected abstract fun onBindItemViewHolder(holder: VH, itemData: E)//为item绑定数据

    override fun getItemCount(): Int = getDefItemCount()

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder?.itemView?.run {
            setOnClickListener { onItemClick(it) }//设置item点击响应
            setOnLongClickListener { onItemLongClick(it) }//设置item长按响应
        }

        getItemData(position)?.let { onBindItemViewHolder(holder, it) }
    }


    protected open fun onItemLongClick(v: View?): Boolean {
        val position = v?.let { recyclerView?.getChildAdapterPosition(it) }
        if (position != null)
            onRecycleViewItemLongClickListener?.onItemLongClick(v, position)

        return true
    }

    protected open fun onItemClick(v: View?) {
        val position = v?.let { recyclerView?.getChildAdapterPosition(it) }
        if (position != null)
            onRecycleViewItemClickListener?.onItemClick(v, position)
    }

    /**
     * 添加新数据
     * @param newData 新数据
     */
    public fun setNewData(newData: MutableList<E>?) {
        newData?.let {
            itemDatas.clear()
            itemDatas.addAll(it)
        }
        notifyDataSetChanged()
    }

    /**
     * 获取item数据
     */
    protected open fun getItemData(position: Int): E? =
        if (position >= itemDatas.size) null else itemDatas[position]


    /**
     * 可重写列表的item数量
     */
    protected open fun getDefItemCount(): Int = itemDatas.size


}


interface OnRecycleViewItemClickListener {
    fun onItemClick(view: View, position: Int)
}

interface OnRecycleViewItemLongClickListener {
    fun onItemLongClick(view: View, position: Int)
}