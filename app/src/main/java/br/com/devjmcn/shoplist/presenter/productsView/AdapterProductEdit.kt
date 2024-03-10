package br.com.devjmcn.shoplist.presenter.productsView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.devjmcn.shoplist.databinding.RecycleEditProductsBinding
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.presenter.productsView.AdapterProductAdd.ProductDiffUtil

class AdapterProductEdit(private val onEditProductClickEvent: OnEditProductClickEvent) :
    ListAdapter<ProductModel, AdapterProductEdit.ProductEditViewHolder>(ProductDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductEditViewHolder {
        return ProductEditViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: ProductEditViewHolder, position: Int) {
        val selectedProduct = getItem(position)
        holder.bind(selectedProduct, onEditProductClickEvent)
    }

    class ProductEditViewHolder(private val binding: RecycleEditProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflate(viewGroup: ViewGroup): ProductEditViewHolder {
                val binding = RecycleEditProductsBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return ProductEditViewHolder(binding)
            }
        }

        fun bind(selectedProduct: ProductModel, onEditProductClickEvent: OnEditProductClickEvent) {
            binding.apply {
                txvProdName.text = selectedProduct.prodName
                imgProdDelete.setOnClickListener {
                    onEditProductClickEvent.deleteProduct(selectedProduct)
                }

                imgProdEdit.setOnClickListener {
                    onEditProductClickEvent.editProduct(selectedProduct)
                }
            }

        }
    }

    interface OnEditProductClickEvent {
        fun deleteProduct(selectedProduct: ProductModel)
        fun editProduct(selectedProduct: ProductModel)
    }
}