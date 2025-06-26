package sk.minv.meid_sample_app.interactors.repository

import sk.minv.base.base.interactor.Result
import sk.minv.meid_sample_app.data.GetTokenParams
import sk.minv.meid_sample_app.data.GetTokenResponse

interface AuthenticationRepository {

    suspend fun getToken(params: GetTokenParams): Result<GetTokenResponse>
}