package br.com.devjmcn.shoplist.view.productsView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.devjmcn.shoplist.databinding.RecycleProductsBinding
import br.com.devjmcn.shoplist.domain.model.product.ProductModel

class AdapterProductAdd(private val onClickProductAddEvent: OnClickProductAddEvent) :
    ListAdapter<ProductModel, AdapterProductAdd.ProductAddViewHolder>(ProductDiffUtil()) {

    private val productsAlreadyInserted = mutableListOf<Long>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAddViewHolder {
        return ProductAddViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: ProductAddViewHolder, position: Int) {
        val selectedProduct = getItem(position)
        holder.bind(selectedProduct, onClickProductAddEvent, productsAlreadyInserted)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadAlreadyProducts(list: List<Long>) {
        productsAlreadyInserted.clear()
        productsAlreadyInserted.addAll(list)
        notifyDataSetChanged()
    }

    class ProductAddViewHolder(private val binding: RecycleProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun inflate(viewGroup: ViewGroup): ProductAddViewHolder {
                val binding = RecycleProductsBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return ProductAddViewHolder(binding)
            }
        }

        fun bind(
            productModel: ProductModel,
            onClickProductAddEvent: OnClickProductAddEvent,
            productsAlreadyInserted: List<Long>
        ) {
            binding.apply {
                txvProdName.text = productModel.prodName
                imgProdCheck.visibility =
                    if (productsAlreadyInserted.contains(productModel.prodId)) {
                        VISIBLE
                    } else {
                        GONE
                    }
                imgProdEdit.setOnClickListener { onClickProductAddEvent.optItem(productModel) }
                binding.root.setOnClickListener {
                    if (productsAlreadyInserted.contains(productModel.prodId)){
                        onClickProductAddEvent.removeItem(productModel)
                    }else{
                        onClickProductAddEvent.insertItem(productModel)
                    }
                }
            }
        }
    }

    class ProductDiffUtil : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(
            oldItem: ProductModel,
            newItem: ProductModel
        ): Boolean {
            return oldItem.prodId == newItem.prodId
        }

        override fun areContentsTheSame(
            oldItem: ProductModel,
            newItem: ProductModel
        ): Boolean {
            return oldItem.prodName == newItem.prodName
                    && oldItem.prodCategoryIndex == newItem.prodCategoryIndex
        }

    }

    interface OnClickProductAddEvent {
        fun insertItem(productModel: ProductModel)
        fun removeItem(productModel: ProductModel)
        fun optItem(productModel: ProductModel)
    }
}