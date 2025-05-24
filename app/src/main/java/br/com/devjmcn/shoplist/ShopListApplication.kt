package br.com.devjmcn.shoplist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ShopListApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@ShopListApplication)
            modules(
                br.com.devjmcn.shoplist.di.viewModelModules,
                br.com.devjmcn.shoplist.di.repositoryModule,
                br.com.devjmcn.shoplist.di.databaseModule,
                br.com.devjmcn.shoplist.di.dataStoreModule
            )
        }
    }
}