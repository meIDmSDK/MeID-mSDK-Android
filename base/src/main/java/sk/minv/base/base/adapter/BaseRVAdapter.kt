package sk.minv.base.base.adapter

import androidx.recyclerview.widget.RecyclerView

interface BaseRVAdapter<ITEM, VIEW_HOLDER> where VIEW_HOLDER : RecyclerView.ViewHolder {
	fun getData(): List<ITEM>?
	fun setData(items: List<ITEM>)
	fun setOnClickCallback(callback: sk.minv.base.base.adapter.BaseAdapter.OnItemClick<ITEM>)
}