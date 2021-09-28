package com.stjepanbarisic.refapp

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.stjepanbarisic.refapp.di.repositoryModule
import com.stjepanbarisic.refapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance()
            .setPersistenceEnabled(true)

        startKoin {
            androidContext(this@App)
            modules(
                arrayListOf(
                    viewModelModule,
                    repositoryModule
                )
            )
        }
    }

}