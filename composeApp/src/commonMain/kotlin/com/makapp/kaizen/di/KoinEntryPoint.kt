package com.makapp.kaizen.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            commonModule + targetModule +
                    useCaseModule + serviceModule +
                    viewModelModule +
                    repositoryModule + dataSourceModule + daoModule
        )
    }
}

expect val targetModule: Module
