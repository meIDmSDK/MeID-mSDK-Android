package sk.minv.meid_sample_app.interactors.network.service

import sk.minv.base.base.interactor.Result
import sk.minv.meid_sample_app.data.GetTokenParams
import sk.minv.meid_sample_app.data.GetTokenResponse

interface AuthenticationService {

    suspend fun getToken(params: GetTokenParams): Result<GetTokenResponse>
}