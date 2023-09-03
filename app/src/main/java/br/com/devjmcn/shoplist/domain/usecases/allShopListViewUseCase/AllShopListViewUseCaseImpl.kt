package br.com.devjmcn.shoplist.domain.usecases.allShopListViewUseCase

import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.repository.shopListRepository.ShopListRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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

    override suspend fun getAllShopList(): Flow<List<ShopListWithItemsModel>> {
        return shopListRepositoryInterface.getAllShopList().catch { emit(emptyList()) }
            .map{shopListWithItemsModel ->
                    shopListWithItemsModel
            }
    }
}