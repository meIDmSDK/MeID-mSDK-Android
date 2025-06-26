package sk.minv.meid_sample_app.interactors.network.service

import sk.minv.base.base.interactor.BaseNetworkService
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.base.base.interactor.Result
import sk.minv.meid_sample_app.data.GetTokenParams
import sk.minv.meid_sample_app.data.GetTokenResponse
import sk.minv.meid_sample_app.interactors.network.api.AuthenticationApi

class AuthenticationServiceImpl(
    appDispatchers: AppDispatchers,
    private val api: AuthenticationApi
) : BaseNetworkService(appDispatchers), AuthenticationService {

    override suspend fun getToken(params: GetTokenParams): Result<GetTokenResponse> = callApi {
        api.getToken(
            params.contentType,
            params.grantType,
            params.clientId,
            params.clientSecret,
            params.redirectUri,
            params.code,
            params.pkceVerifier!!
        )
    }
}