package br.com.devjmcn.shoplist.domain

import br.com.devjmcn.shoplist.data.RepositoryInterface
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.util.preferences.PreferencesSumValuesInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UseCaseImpl(
    private val repositoryInterface: RepositoryInterface,
    private val preferencesSumValuesInterface: PreferencesSumValuesInterface
):UseCaseInterface {
    override fun shopNameIsValid(shopName: String): Boolean {
        return shopName.isNotEmpty()
    }

    override suspend fun saveShopList(shopListModel: ShopListModel): Long {
        return repositoryInterface.saveShopList(shopListModel)
    }

    override suspend fun deleteShopList(shopListModel: ShopListModel) {
        repositoryInterface.deleteShopList(shopListModel)
    }

    override suspend fun getAllShopList(): Flow<List<ShopListWithItemsModel>> {
        return repositoryInterface.getAllShopList().catch { emit(emptyList()) }
            .map { shopListWithItemsModel ->
                shopListWithItemsModel
            }
    }

    override fun getShopList(shopId: Long): Flow<ShopListWithItemsModel?> {
        return repositoryInterface.getShopListById(shopId)
    }

    override suspend fun deleteItem(itemSelected: ItemShopListModel) {
        repositoryInterface.deleteItemShopList(itemSelected.prodId, itemSelected.shopId)
    }

    override suspend fun renameShopList(shopListModel: ShopListModel) {
        repositoryInterface.saveShopList(shopListModel)
    }

    override fun getShopListById(shopId: Long): Flow<ShopListWithItemsModel?> {
        return repositoryInterface.getShopListById(shopId)
    }

    override fun isNameValid(newShopName: String): Boolean {
        return newShopName.isNotEmpty()
    }

    override fun isInvalidAmount(amountText: String): Boolean {
        return amountText.toInt() == 0
    }

    override suspend fun updateShopList(shopListModel: ShopListModel) {
        repositoryInterface.updateShopList(shopListModel)
    }

    override suspend fun deleteItemShopList(itemShopListModel: ItemShopListModel) {
        repositoryInterface.deleteItemShopList(
            itemShopListModel.prodId,
            itemShopListModel.shopId
        )
    }

    override suspend fun saveItem(itemShopListModel: ItemShopListModel) {
        repositoryInterface.saveItemShopList(itemShopListModel)
    }

    override fun getAllProduct(): Flow<List<ProductModel>> {
        return repositoryInterface.getAllProducts().catch {
            emit(emptyList())
        }.map { listProductModel ->
            listProductModel
        }
    }

    override fun getProductByName(name: String?): Flow<List<ProductModel>> {
        return repositoryInterface.getAllProductsByName("%$name%")
            .map{ listProductModel ->
                listProductModel
            }.catch { emptyList<ProductModel>() }
    }

    override fun getItemsList(shopId: Long): Flow<List<Long>?> {
        return repositoryInterface.getItemsList(shopId)
    }

    override suspend fun insertItemShopList(itemShopListModel: ItemShopListModel) {
        repositoryInterface.saveItemShopList(itemShopListModel)
    }

    override fun isEmpty(text: String): Boolean {
        return text.isEmpty()
    }

    override suspend fun deleteItem(prodId: Long, shopId: Long) {
        repositoryInterface.deleteItemShopList(prodId, shopId)
    }

    override suspend fun deleteProduct(productModel: ProductModel) {
        repositoryInterface.deleteProduct(productModel)
    }

    override suspend fun insertProduct(editedProduct: ProductModel) {
        repositoryInterface.saveProduct(editedProduct)
    }

    override suspend fun setPreferenceData(status: Boolean) {
        preferencesSumValuesInterface.saveData(status)
    }

    override fun getPreferenceData(): Flow<Boolean?> {
        return preferencesSumValuesInterface.getData()
    }
}