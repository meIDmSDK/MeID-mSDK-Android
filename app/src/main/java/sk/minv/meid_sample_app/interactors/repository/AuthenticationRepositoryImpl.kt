package sk.minv.meid_sample_app.interactors.repository

import sk.minv.meid_sample_app.data.GetTokenParams
import sk.minv.meid_sample_app.data.GetTokenResponse
import sk.minv.meid_sample_app.interactors.network.service.AuthenticationService
import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.base.base.interactor.onFailed

class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService
) : AuthenticationRepository {

    override suspend fun getToken(params: GetTokenParams): Result<GetTokenResponse> {
        return authenticationService.getToken(params).onFailed {
            return it
        }.let {
            Success(it)
        }
    }
}