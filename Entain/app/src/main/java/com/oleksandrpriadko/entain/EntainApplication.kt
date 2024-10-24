package com.oleksandrpriadko.entain

import android.app.Application
import com.oleksandrpriadko.Kermit
import com.oleksandrpriadko.entain.di.apiModule
import com.oleksandrpriadko.entain.di.dataSourceModule
import com.oleksandrpriadko.entain.di.repositoryModule
import com.oleksandrpriadko.entain.di.useCaseModule
import com.oleksandrpriadko.entain.di.viewModelModule
import com.oleksandrpriadko.kermit
import com.oleksandrpriadkoentain.BuildConfig.DEBUG
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class EntainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        kermit = Kermit(DEBUG)

        startKoin {
            androidContext(this@EntainApplication)
            androidLogger()
            modules(apiModule, dataSourceModule, repositoryModule, useCaseModule, viewModelModule)
        }
    }
}