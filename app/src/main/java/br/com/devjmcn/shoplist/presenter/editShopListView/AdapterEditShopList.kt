package br.com.devjmcn.shoplist.presenter.editShopListView

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.databinding.RecycleEditShopListBinding
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel

class AdapterEditShopList(private val onButtonClickEvent: OnButtonClickEvent):
    ListAdapter<ItemShopListModel, AdapterEditShopList.ViewHolderItemShopListEditShopListAdapter>(EditShopListDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ViewHolderItemShopListEditShopListAdapter {
        return ViewHolderItemShopListEditShopListAdapter.inflate(parent)
    }

    override fun onBindViewHolder(holder: ViewHolderItemShopListEditShopListAdapter, position: Int) {
        val selectedItem = getItem(position)
        holder.bind(selectedItem, onButtonClickEvent)
    }

    class ViewHolderItemShopListEditShopListAdapter(private val binding: RecycleEditShopListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val arrayTypes: Array<String> =
            binding.root.context.resources.getStringArray(R.array.types)

        companion object {
            fun inflate(parent: ViewGroup): ViewHolderItemShopListEditShopListAdapter {
                val binding = RecycleEditShopListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderItemShopListEditShopListAdapter(binding)
            }
        }

        fun bind(selectedItem: ItemShopListModel, onButtonClickEvent: OnButtonClickEvent) {
            with(binding){
                txvItemName.text = selectedItem.prodName
                txvAmountItems.text = root.context.getString(R.string.str_amount_item,selectedItem.amount)
                txvType.text = root.context.getString(R.string.str_type_item,arrayTypes[selectedItem.typeIndex])
                if(selectedItem.description.isEmpty()){
                    txvDescription.visibility = GONE
                }else{
                    txvDescription.visibility = VISIBLE
                    txvDescription.text = root.context.getString(R.string.str_description_item,selectedItem.description)
                }

                imgEditItem.setOnClickListener { onButtonClickEvent.editItemClickEvent(selectedItem) }
                imgDeleteItem.setOnClickListener { onButtonClickEvent.deleteItemClickEvent(selectedItem) }
            }
        }
    }

    class EditShopListDiffUtil: DiffUtil.ItemCallback<ItemShopListModel>() {
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
            return oldItem.prodName == newItem.prodName
                    && oldItem.amount == newItem.amount
                    && oldItem.typeIndex == newItem.typeIndex
                    && oldItem.isOk == newItem.isOk
                    && oldItem.description == newItem.description
        }

    }

    interface OnButtonClickEvent{
        fun editItemClickEvent(itemShopListModel: ItemShopListModel)
        fun deleteItemClickEvent(itemShopListModel: ItemShopListModel)
    }
}