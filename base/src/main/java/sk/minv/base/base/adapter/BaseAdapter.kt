package sk.minv.base.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ITEM, VIEW_HOLDER> protected constructor(protected val context: Context) :
		RecyclerView.Adapter<VIEW_HOLDER>(),
    sk.minv.base.base.adapter.BaseRVAdapter<ITEM, VIEW_HOLDER>
		where VIEW_HOLDER : RecyclerView.ViewHolder {

	/*-------------------------*/
	/*         FIELDS          */
	/*-------------------------*/

	protected var items: List<ITEM>? = null

	private var clickCallback: OnItemClick<ITEM>? = null

	/*-------------------------*/
	/*   OVERRIDDEN METHODS    */
	/*-------------------------*/

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VIEW_HOLDER {
		val view = createView(parent, viewType)
		return createViewHolder(view, viewType)
	}

	override fun onBindViewHolder(holder: VIEW_HOLDER, position: Int) {
		if (clickCallback == null) {
			holder.itemView.setOnClickListener(null)
		} else {
			holder.itemView.setOnClickListener { clickCallback!!.onItemClick(holder, items!![holder.adapterPosition]) }
		}

		if (position < items!!.size) {
			onBindViewHolder(holder, items!![position])
			onBindViewHolder(holder, items!![position], position)
		} else {
			onBindViewHolder(holder)
		}
	}

	override fun getItemCount() = items?.size ?: 0
	override fun getData() = items
	override fun setData(items: List<ITEM>) {
		this.items = items
		notifyDataSetChanged()
	}

	override fun setOnClickCallback(callback: OnItemClick<ITEM>) {
		this.clickCallback = callback
	}

	/*-------------------------*/
	/*    PROTECTED METHODS    */
	/*-------------------------*/

	@LayoutRes
	protected abstract fun getLayoutXml(viewType: Int): Int
	protected abstract fun createViewHolder(view: View, viewType: Int): VIEW_HOLDER
	protected open fun onBindViewHolder(holder: VIEW_HOLDER) {}
	protected open fun onBindViewHolder(holder: VIEW_HOLDER, item: ITEM) {}
	protected open fun onBindViewHolder(holder: VIEW_HOLDER, item: ITEM, position: Int) {}

	/*-------------------------*/
	/*     PRIVATE METHODS     */
	/*-------------------------*/

	private fun createView(parent: ViewGroup, viewType: Int): View {
		val inflater = LayoutInflater.from(parent.context)
		return inflater.inflate(getLayoutXml(viewType), parent, false)
	}

	interface OnItemClick<T> {
		fun onItemClick(holder: RecyclerView.ViewHolder, t: T)
	}
}