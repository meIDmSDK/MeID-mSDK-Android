package sk.minv.meid_sample_app.interactors.usecase.getToken

import sk.minv.base.base.interactor.Result
import sk.minv.meid_sample_app.data.GetTokenResponse
import sk.minv.meid_sample_app.data.GetTokenUseCaseParams

interface GetTokenUseCase {

    suspend operator fun invoke(params: GetTokenUseCaseParams): Result<GetTokenResponse>
}