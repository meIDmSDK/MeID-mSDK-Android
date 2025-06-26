package sk.minv.meid_sample_app.di

import org.koin.dsl.module
import retrofit2.Retrofit
import sk.minv.meid_sample_app.interactors.usecase.getToken.GetTokenUseCase
import sk.minv.meid_sample_app.interactors.usecase.getToken.GetTokenUseCaseImpl
import sk.minv.meid_sample_app.interactors.network.api.AuthenticationApi
import sk.minv.meid_sample_app.interactors.network.service.AuthenticationService
import sk.minv.meid_sample_app.interactors.network.service.AuthenticationServiceImpl
import sk.minv.meid_sample_app.interactors.repository.AuthenticationRepository
import sk.minv.meid_sample_app.interactors.repository.AuthenticationRepositoryImpl
import sk.minv.meid_sample_app.interactors.usecase.login.LoginUseCase
import sk.minv.meid_sample_app.interactors.usecase.login.LoginUseCaseImpl
import sk.minv.meid_sample_app.interactors.usecase.logout.LogoutUseCase
import sk.minv.meid_sample_app.interactors.usecase.logout.LogoutUseCaseImpl

val useCaseModule = module {
    factory<LoginUseCase> { LoginUseCaseImpl(get(), get()) }
    factory<GetTokenUseCase> { GetTokenUseCaseImpl(get(), get(), get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get()) }
}

val repositoryModule = module {
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get()) }
}

val serviceModule = module {
    single<AuthenticationService> { AuthenticationServiceImpl(get(), get()) }
}

val apiModule = module {
    single { get<Retrofit>().create(AuthenticationApi::class.java) }
}