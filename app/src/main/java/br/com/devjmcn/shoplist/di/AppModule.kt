package br.com.devjmcn.shoplist.di

import androidx.room.Room
import br.com.devjmcn.shoplist.domain.usecases.allShopListViewUseCase.AllShopListViewUseCaseImpl
import br.com.devjmcn.shoplist.domain.usecases.buyShopListUseCase.BuyShopListViewUseCaseImpl
import br.com.devjmcn.shoplist.domain.usecases.editShopListViewUseCase.EditShopListViewUseCaseImpl
import br.com.devjmcn.shoplist.domain.usecases.productViewUseCase.ProductViewUseCaseImpl
import br.com.devjmcn.shoplist.repository.itemShopListRepository.ItemShopListRepositoryImpl
import br.com.devjmcn.shoplist.repository.itemShopListRepository.ItemShopListRepositoryInterface
import br.com.devjmcn.shoplist.repository.productRepository.ProductRepositoryImpl
import br.com.devjmcn.shoplist.repository.productRepository.ProductRepositoryInterface
import br.com.devjmcn.shoplist.repository.room.appdatabase.AppDataBase
import br.com.devjmcn.shoplist.repository.shopListRepository.ShopListRepositoryImpl
import br.com.devjmcn.shoplist.repository.shopListRepository.ShopListRepositoryInterface
import br.com.devjmcn.shoplist.util.preferences.PreferenceSumValuesImpl
import br.com.devjmcn.shoplist.util.preferences.PreferencesSumValuesInterface
import br.com.devjmcn.shoplist.view.allShopListView.ViewModelAllShopList
import br.com.devjmcn.shoplist.view.buyShopListView.ViewModelBuyShopList
import br.com.devjmcn.shoplist.view.editShopListView.ViewModelEditShopList
import br.com.devjmcn.shoplist.view.productsView.ViewModelProduct
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

// ==================== VIEW MODELS ==================== //
val viewModelModules = module {
    viewModel(named("ALL_SHOP_LIST_VIEW_MODEL")){
        ViewModelAllShopList(
            allShopListViewUseCaseInterface = AllShopListViewUseCaseImpl(
                shopListRepositoryInterface = ShopListRepositoryImpl(shopListDao = get())
            )
        )
    }

    viewModel(named("EDIT_SHOP_LIST_VIEW_MODEL")){(shopId : Long) ->
        ViewModelEditShopList(
            editShopListViewUseCaseInterface = EditShopListViewUseCaseImpl(get(),get()),
            receivedShopId = shopId
        )
    }

    viewModel(named("PRODUCT_VIEW_MODEL")){(shopId : Long) ->
        ViewModelProduct(
            productViewUseCaseInterface = ProductViewUseCaseImpl(get(),get()),
            shopId = shopId
        )
    }

    viewModel(named("BUY_SHOP_VIEW_MODEL")){(shopId:Long) ->
        ViewModelBuyShopList(
            buyShopListUseCaseInterface = BuyShopListViewUseCaseImpl(get(), get()),
            shopId = shopId
        )
    }
}

// ==================== REPOSITORY ==================== //
val repositoryModule = module {
    single<ShopListRepositoryInterface>{
        ShopListRepositoryImpl(get())
    }

    single<ItemShopListRepositoryInterface> {
        ItemShopListRepositoryImpl(get(), get())
    }

    single<ProductRepositoryInterface> {
        ProductRepositoryImpl(get())
    }
}


// ==================== DATABASE ==================== //
val databaseModule = module {
    single {
        val db = get<AppDataBase>()
        db.shopListDao()
    }

    single {
        val db = get<AppDataBase>()
        db.itemShopListDao()
    }

    single {
        val db = get<AppDataBase>()
        db.productDao()
    }

    single{
        Room.databaseBuilder(
            androidApplication(),
            AppDataBase::class.java,
            "shopList.db"
        ).createFromAsset("database/productsDb")
            .build()
    }
}

// ==================== DATABASE ==================== //
val dataStoreModule = module {
    single<PreferencesSumValuesInterface>{
        PreferenceSumValuesImpl(androidApplication())
    }
}