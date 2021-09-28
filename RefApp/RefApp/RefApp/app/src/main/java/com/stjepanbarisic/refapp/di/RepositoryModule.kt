package com.stjepanbarisic.refapp.di

import com.stjepanbarisic.refapp.repositories.GameRepository
import com.stjepanbarisic.refapp.repositories.RefereeRepository
import com.stjepanbarisic.refapp.repositories.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository() }
    single { GameRepository() }
    single { RefereeRepository() }
}
