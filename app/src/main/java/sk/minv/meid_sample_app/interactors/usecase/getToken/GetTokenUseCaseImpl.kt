package sk.minv.meid_sample_app.interactors.usecase.getToken

import android.text.TextUtils
import android.util.Base64
import kotlinx.coroutines.withContext
import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.base.base.interactor.onFailed
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.meid_sample_app.App
import sk.minv.meid_sample_app.data.GetTokenParams
import sk.minv.meid_sample_app.data.GetTokenResponse
import sk.minv.meid_sample_app.data.GetTokenUseCaseParams
import sk.minv.meid_sample_app.interactors.repository.AuthenticationRepository
import sk.minv.meid_sample_app.utils.AppConstants
import sk.minv.meid_sample_app.utils.KeyStoreUtils
import sk.minv.meid_sample_app.utils.LocalPreferences

class GetTokenUseCaseImpl(
    private val appDispatchers: AppDispatchers,
    private val preferences: LocalPreferences,
    private val authenticationRepository: AuthenticationRepository
) : GetTokenUseCase {

    override suspend fun invoke(params: GetTokenUseCaseParams): Result<GetTokenResponse> {
        // Get saved PKCE verifier
        val pkceVerifier = withContext(appDispatchers.IO) {
            val secretKey = KeyStoreUtils.getOrCreateKey()
            val encodedPKCEVerifier = preferences.getPKCEVerifier()
            val encodedPKCEVerifierIV = preferences.getPKCEVerifierIV()

            if (TextUtils.isEmpty(encodedPKCEVerifier) || TextUtils.isEmpty(encodedPKCEVerifierIV)) {
                return@withContext null
            }

            val encryptedPKCEVerifier = Base64.decode(encodedPKCEVerifier, Base64.DEFAULT)
            val iv = Base64.decode(encodedPKCEVerifierIV, Base64.DEFAULT)

            KeyStoreUtils.decryptData(encryptedPKCEVerifier, iv, secretKey)
        }

        // Set parameters
        val clientId = AppConstants.CLIENT_ID
        val redirectUri = AppConstants.REDIRECT_URI
        val getTokenParams = GetTokenParams(clientId = clientId, redirectUri = redirectUri, code = params.authorizationCode, pkceVerifier = pkceVerifier)

        // API call
        val tokenResponse = authenticationRepository.getToken(getTokenParams).onFailed {
            return it
        }

        // Remove PKCE verifier
        preferences.deletePKCEVerifier()

        // Save tokens
        val (encodedAccessToken, encodedAccessTokenIv) = encryptToken(tokenResponse.accessToken)
        App.accessTokenEncoded = encodedAccessToken
        App.accessTokenIvEncoded = encodedAccessTokenIv

        val (encodedRefreshToken, encodedRefreshTokenIv) = encryptToken(tokenResponse.refreshToken)
        App.refreshTokenEncoded = encodedRefreshToken
        App.refreshTokenIvEncoded = encodedRefreshTokenIv

        val (encodedIdToken, encodedIdTokenIv) = encryptToken(tokenResponse.idToken)
        App.idTokenEncoded = encodedIdToken
        App.idTokenIvEncoded = encodedIdTokenIv

        return Success(tokenResponse)
    }

    private fun encryptToken(token: String): Pair<String, String> {
        val secretKey = KeyStoreUtils.getOrCreateKey()
        val (encryptedData, iv) = KeyStoreUtils.encryptData(token, secretKey)
        return Pair(Base64.encodeToString(encryptedData, Base64.DEFAULT), Base64.encodeToString(iv, Base64.DEFAULT))
    }
}