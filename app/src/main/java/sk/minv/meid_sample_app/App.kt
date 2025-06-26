package sk.minv.meid_sample_app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import sk.minv.meid_sample_app.di.apiModule
import sk.minv.meid_sample_app.di.appModule
import sk.minv.meid_sample_app.di.repositoryModule
import sk.minv.meid_sample_app.di.serviceModule
import sk.minv.meid_sample_app.di.useCaseModule
import sk.minv.meid_sample_app.di.viewModelModule

class App : Application() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val appModules = listOf(appModule, viewModelModule)
    private val interactionModules = listOf(useCaseModule, repositoryModule, serviceModule, apiModule)

    companion object {
        var accessTokenEncoded: String? = null
        var accessTokenIvEncoded: String? = null
        var refreshTokenEncoded: String? = null
        var refreshTokenIvEncoded: String? = null
        var idTokenEncoded: String? = null
        var idTokenIvEncoded: String? = null
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModules + interactionModules)
        }
    }
}