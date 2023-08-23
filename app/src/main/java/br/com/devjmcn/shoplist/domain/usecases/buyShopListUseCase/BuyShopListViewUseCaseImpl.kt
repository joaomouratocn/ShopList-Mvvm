package br.com.devjmcn.shoplist.domain.usecases.buyShopListUseCase

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.repository.itemShopListRepository.ItemShopListRepositoryInterface
import br.com.devjmcn.shoplist.util.preferences.PreferencesSumValuesInterface
import kotlinx.coroutines.flow.Flow

class BuyShopListViewUseCaseImpl(
    private val itemShopListRepositoryInterface: ItemShopListRepositoryInterface,
    private val preferencesSumValuesInterface: PreferencesSumValuesInterface
    ) : BuyShopListViewUseCaseInterface {

    override fun getShopList(shopId: Long): Flow<ShopListWithItemsModel?> {
        return itemShopListRepositoryInterface.getShopList(shopId)
    }

    override suspend fun saveItem(itemShopListModel: ItemShopListModel) {
        itemShopListRepositoryInterface.saveItemShopList(itemShopListModel)
    }

    override suspend fun deleteItem(itemSelected: ItemShopListModel) {
        itemShopListRepositoryInterface.deleteItemShopList(itemSelected.prodId, itemSelected.shopId)
    }

    override suspend fun setSumValueStatus(status: Boolean) {
        preferencesSumValuesInterface.saveData(status)
    }

    override fun getStatusSumValues(): Flow<Boolean?> {
        return preferencesSumValuesInterface.getData()
    }

    override fun isEmpty(text: String): Boolean {
        return text.isEmpty()
    }
}