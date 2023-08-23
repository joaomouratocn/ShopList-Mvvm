package br.com.devjmcn.shoplist.domain.usecases.editShopListViewUseCase

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.repository.itemShopListRepository.ItemShopListRepositoryInterface
import br.com.devjmcn.shoplist.repository.shopListRepository.ShopListRepositoryInterface
import kotlinx.coroutines.flow.Flow

class EditShopListViewUseCaseImpl(
    private val shopListRepositoryInterface: ShopListRepositoryInterface,
    private val itemShopListRepositoryInterface: ItemShopListRepositoryInterface
) : EditShopListViewUseCaseInterface {

    override suspend fun renameShopList(shopListModel: ShopListModel) {
        shopListRepositoryInterface.saveShopList(shopListModel)
    }

    override fun getShopListById(shopId: Long): Flow<ShopListWithItemsModel?> {
        return shopListRepositoryInterface.getShopListById(shopId)
    }

    override fun isNameValid(newShopName: String): Boolean {
        return newShopName.isNotEmpty()
    }

    override fun isInvalidAmount(amountText: String): Boolean {
        return amountText.toInt() == 0
    }

    override suspend fun deleteShopList(shopListWithItemsModel: ShopListWithItemsModel?) {
        shopListWithItemsModel?.let {
            val shopListModel = ShopListModel(
                shopId = shopListWithItemsModel.shopId,
                shopName = shopListWithItemsModel.shopName,
                shopDate = shopListWithItemsModel.shopDate
            )
            shopListRepositoryInterface.deleteShopList(shopListModel)
        }
    }

    override suspend fun updateShopList(shopListWithItemsModel: ShopListWithItemsModel) {
        val editShopList = ShopListModel(
            shopId = shopListWithItemsModel.shopId,
            shopName = shopListWithItemsModel.shopName,
            shopDate = shopListWithItemsModel.shopDate
        )
        shopListRepositoryInterface.updateShopList(editShopList)
    }

    override suspend fun deleteItemShopList(itemShopListModel: ItemShopListModel) {
        itemShopListRepositoryInterface.deleteItemShopList(itemShopListModel.prodId, itemShopListModel.shopId)
    }

    override suspend fun saveItem(editedItem: ItemShopListModel) {
        itemShopListRepositoryInterface.saveItemShopList(editedItem)
    }
}