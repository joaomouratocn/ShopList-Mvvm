package br.com.devjmcn.shoplist.presenter.buyShopListView

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.databinding.RecycleBuyShopListBinding
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.util.numberFormat

class AdapterBuyShopList(private val onBuyShopListOnClickEvent: OnBuyShopListOnClickEvent) :
    ListAdapter<ItemShopListModel, AdapterBuyShopList.BuyShopListViewHolder>(BuyShopListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyShopListViewHolder {
        return BuyShopListViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: BuyShopListViewHolder, position: Int) {
        val itemSelected = getItem(position)
        holder.bind(itemSelected, onBuyShopListOnClickEvent)
    }

    class BuyShopListViewHolder(private val binding: RecycleBuyShopListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val typeArray = binding.root.context.resources.getStringArray(R.array.types)

        companion object {
            fun inflate(parent: ViewGroup): BuyShopListViewHolder {
                val binding = RecycleBuyShopListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BuyShopListViewHolder(binding)
            }
        }

        fun bind(
            itemSelected: ItemShopListModel,
            onBuyShopListOnClickEvent: OnBuyShopListOnClickEvent
        ) {
            with(binding) {
                txvNameItem.text = itemSelected.prodName
                txvAmountText.text =
                    root.context.getString(R.string.str_amount_item, itemSelected.amount)
                txvDescription.text = if (itemSelected.description.isNotEmpty()) root.context.getString(
                    R.string.str_description_item,
                    itemSelected.description
                ) else ""
                txvType.text = root.context.getString(
                    R.string.str_type_item,
                    typeArray[itemSelected.typeIndex]
                )
                txvPrice.text = root.context.getString(
                    R.string.str_price_item,
                    numberFormat.format(itemSelected.price)
                )
                imgCheck.visibility = if (itemSelected.isOk) VISIBLE else GONE
                imgDeleteItem.setOnClickListener { onBuyShopListOnClickEvent.deleteItem(itemSelected) }
                root.setOnClickListener {
                    if (itemSelected.isOk) {
                        onBuyShopListOnClickEvent.unCheckItem(itemSelected.copy(isOk = false))
                    } else {
                        onBuyShopListOnClickEvent.checkItem(itemSelected.copy(isOk = true))
                    }
                }
            }
        }
    }

    class BuyShopListDiffUtil : DiffUtil.ItemCallback<ItemShopListModel>() {
        override fun areItemsTheSame(
            oldItem: ItemShopListModel,
            newItem: ItemShopListModel
        ): Boolean {
            return oldItem.prodId == newItem.prodId
        }

        override fun areContentsTheSame(
            oldItem: ItemShopListModel,
            newItem: ItemShopListModel
        ): Boolean {
            return oldItem.isOk == newItem.isOk
        }
    }

    interface OnBuyShopListOnClickEvent {
        fun deleteItem(itemSelected: ItemShopListModel)
        fun checkItem(itemSelected: ItemShopListModel)
        fun unCheckItem(itemSelected: ItemShopListModel)
    }
}