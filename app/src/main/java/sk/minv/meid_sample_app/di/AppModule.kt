package sk.minv.meid_sample_app.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.meid_sample_app.utils.AppConstants
import sk.minv.meid_sample_app.utils.LocalPreferences
import sk.minv.meid_sample_app.utils.LocalPreferencesImpl
import java.util.concurrent.TimeUnit

val appModule = module {

    single { AppDispatchers() }

    single<LocalPreferences> { LocalPreferencesImpl(get()) }

    single { GsonConverterFactory.create() }

    single { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get())
            .build()
    }
}