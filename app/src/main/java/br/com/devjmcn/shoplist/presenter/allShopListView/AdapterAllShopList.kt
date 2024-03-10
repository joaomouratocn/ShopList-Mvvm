package br.com.devjmcn.shoplist.presenter.allShopListView

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.databinding.RecycleAllShopListBinding
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.presenter.allShopListView.AdapterAllShopList.*
import java.text.DateFormat

class AdapterAllShopList(private val allShopListAdapterOnClickEvent:AllShopListAdapterOnClickEvent)
    : ListAdapter<ShopListWithItemsModel, ViewHolderAllShopList>(AllShopListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAllShopList {
        return ViewHolderAllShopList.inflate(parent)
    }

    override fun onBindViewHolder(holder: ViewHolderAllShopList, position: Int) {
        val selectedShopListWithItemsModel = getItem(position)
        holder.bind(selectedShopListWithItemsModel, allShopListAdapterOnClickEvent)
    }

    class ViewHolderAllShopList(private val binding: RecycleAllShopListBinding)
        :RecyclerView.ViewHolder(binding.root){

        companion object {
            fun inflate(parent: ViewGroup): ViewHolderAllShopList {
                val binding = RecycleAllShopListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
                return ViewHolderAllShopList(binding)
            }
        }

        fun bind(
            selectedShopListWithItemsModel: ShopListWithItemsModel,
            allShopListAdapterOnClickEvent: AllShopListAdapterOnClickEvent
        ) {
            val amountItems = selectedShopListWithItemsModel.listItems.size
            val amountIsOk = selectedShopListWithItemsModel.listItems.filter { it.isOk }.size

            binding.apply {
                txvNameList.text = selectedShopListWithItemsModel.shopName
                txvDate.text = DateFormat.getDateInstance().format(selectedShopListWithItemsModel.shopDate)
                txvAmountItems.text = root.context.getString(R.string.str_amount_check, amountItems, amountIsOk)
                prbAmountItems.max = amountItems
                prbAmountItems.progress = amountIsOk
                if (amountItems == amountIsOk){
                    prbAmountItems.progressTintList = ColorStateList.valueOf(Color.GREEN)
                }else{
                    prbAmountItems.progressTintList =
                        ColorStateList.valueOf(root.context.getColor(R.color.orange_secondary_variant))
                }

                imgMoreOpt.setOnClickListener {
                    val popupMenu = PopupMenu(binding.root.context, binding.imgMoreOpt)
                    with(popupMenu){
                        inflate(R.menu.menu_view_all_shop_list)
                        setOnMenuItemClickListener {menuItem ->
                            when(menuItem.itemId){
                                R.id.item_edit_shop_list ->{
                                    allShopListAdapterOnClickEvent.editShopList(
                                        shopId = selectedShopListWithItemsModel.shopId)
                                    true
                                }
                                R.id.item_delete_shop_list -> {
                                    allShopListAdapterOnClickEvent.deleteShopList(
                                        shopListModel = ShopListModel(
                                            shopId = selectedShopListWithItemsModel.shopId,
                                            shopName = selectedShopListWithItemsModel.shopName,
                                            shopDate = selectedShopListWithItemsModel.shopDate
                                        )
                                    )
                                    true
                                }
                                else -> false
                            }
                        }
                        show()
                    }
                }
            }

            binding.root.setOnClickListener {
                allShopListAdapterOnClickEvent.openBuyShopList(selectedShopListWithItemsModel.shopId)
            }
        }
    }

    class AllShopListDiffUtil:DiffUtil.ItemCallback<ShopListWithItemsModel>(){
        override fun areItemsTheSame(
            oldItem: ShopListWithItemsModel,
            newItem: ShopListWithItemsModel
        ): Boolean {
            return oldItem.shopId == newItem.shopId
        }

        override fun areContentsTheSame(
            oldItem: ShopListWithItemsModel,
            newItem: ShopListWithItemsModel
        ): Boolean {
            return oldItem.shopName == newItem.shopName
                    && oldItem.listItems == newItem.listItems
        }

    }

    interface AllShopListAdapterOnClickEvent{
        fun editShopList(shopId:Long)
        fun openBuyShopList(shopId: Long)
        fun deleteShopList(shopListModel: ShopListModel)
    }
}