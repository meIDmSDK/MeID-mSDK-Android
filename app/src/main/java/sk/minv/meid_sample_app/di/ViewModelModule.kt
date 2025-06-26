package sk.minv.meid_sample_app.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import sk.minv.meid_sample_app.ui.main.MainViewModel

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
}