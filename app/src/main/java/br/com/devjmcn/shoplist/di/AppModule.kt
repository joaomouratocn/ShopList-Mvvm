package br.com.devjmcn.shoplist.di

import androidx.room.Room
import br.com.devjmcn.shoplist.data.RepositoryImpl
import br.com.devjmcn.shoplist.data.RepositoryInterface
import br.com.devjmcn.shoplist.data.dataSource.appdatabase.AppDataBase
import br.com.devjmcn.shoplist.domain.UseCaseImpl
import br.com.devjmcn.shoplist.presenter.allShopListView.ViewModelAllShopList
import br.com.devjmcn.shoplist.presenter.buyShopListView.ViewModelBuyShopList
import br.com.devjmcn.shoplist.presenter.editShopListView.ViewModelEditShopList
import br.com.devjmcn.shoplist.presenter.productsView.ViewModelProduct
import br.com.devjmcn.shoplist.util.preferences.PreferenceSumValuesImpl
import br.com.devjmcn.shoplist.util.preferences.PreferencesSumValuesInterface
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

// ==================== VIEW MODELS ==================== //
val viewModelModules = module {
    viewModel(named("ALL_SHOP_LIST_VIEW_MODEL")) {
        ViewModelAllShopList(
            useCaseInterface = UseCaseImpl(
                repositoryInterface = RepositoryImpl(get(), get(), get()),
                get()
            )
        )
    }

    viewModel(named("EDIT_SHOP_LIST_VIEW_MODEL")) { (shopId: Long) ->
        ViewModelEditShopList(
            useCaseInterface = UseCaseImpl(get(), get()),
            receivedShopId = shopId
        )
    }

    viewModel(named("PRODUCT_VIEW_MODEL")) { (shopId: Long) ->
        ViewModelProduct(
            useCaseInterface = UseCaseImpl(get(), get()),
            shopId = shopId
        )
    }

    viewModel(named("BUY_SHOP_VIEW_MODEL")) { (shopId: Long) ->
        ViewModelBuyShopList(
            useCaseInterface = UseCaseImpl(get(), get()),
            shopId = shopId
        )
    }
}

// ==================== REPOSITORY ==================== //
val repositoryModule = module {
    single<RepositoryInterface> {
        RepositoryImpl(get(), get(), get())
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

    single {
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
    single<PreferencesSumValuesInterface> {
        PreferenceSumValuesImpl(androidApplication())
    }
}