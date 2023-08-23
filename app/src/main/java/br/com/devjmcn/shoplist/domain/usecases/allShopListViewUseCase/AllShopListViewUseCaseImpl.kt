package br.com.devjmcn.shoplist.domain.usecases.allShopListViewUseCase

import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.repository.shopListRepository.ShopListRepositoryInterface
import br.com.devjmcn.shoplist.util.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllShopListViewUseCaseImpl(
    private val shopListRepositoryInterface: ShopListRepositoryInterface
) : AllShopListViewUseCaseInterface {

    override fun shopNameIsValid(shopName: String): Boolean {
        return shopName.isNotEmpty()
    }

    override suspend fun saveShopList(shopListModel: ShopListModel): Long {
        return shopListRepositoryInterface.saveShopList(shopListModel)
    }

    override suspend fun deleteShopList(shopListModel: ShopListModel) {
        shopListRepositoryInterface.deleteShopList(shopListModel)
    }

    override fun getAllShopList(): Flow<ResponseStatus<List<ShopListWithItemsModel>?>> {
        return shopListRepositoryInterface.getAllShopList().map { allShopListWithItems ->
            if (allShopListWithItems.isNullOrEmpty()) {
                ResponseStatus.EmptyData
            } else {
                ResponseStatus.ShowData(allShopListWithItems)
            }
        }
    }
}