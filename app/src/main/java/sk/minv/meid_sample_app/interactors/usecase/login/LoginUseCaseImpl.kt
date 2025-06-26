package sk.minv.meid_sample_app.interactors.usecase.login

import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.withContext
import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.meid_sample_app.utils.AppConstants
import sk.minv.meid_sample_app.utils.LocalPreferences
import sk.minv.meid_sample_app.utils.PKCEUtils
import java.security.SecureRandom
import androidx.core.net.toUri

class LoginUseCaseImpl(
    private val appDispatchers: AppDispatchers,
    private val preferences: LocalPreferences
) : LoginUseCase {

    override suspend fun invoke(): Result<Uri> {
        return withContext(appDispatchers.IO) {
            val authBaseUrl = AppConstants.BASE_URL.plus(AppConstants.ENDPOINT_AUTH)
            val clientId = AppConstants.CLIENT_ID
            val redirectUri = AppConstants.REDIRECT_URI

            val pkceVerifier = PKCEUtils.generatePKCEVerifier()
            val pkceChallenge = PKCEUtils.generatePKCEChallenge(pkceVerifier)

            val (pkceVerfierEncoded, pkceVerfierIVEncoded) = PKCEUtils.encryptPKCEVerifier(pkceVerifier)
            preferences.savePKCEVerifier(pkceVerfierEncoded, pkceVerfierIVEncoded)

            val generator = SecureRandom()
            val nonce = ByteArray(32)
            generator.nextBytes(nonce)
            val nonceEncoded = Base64.encodeToString(nonce, Base64.NO_WRAP or Base64.URL_SAFE)

            val url = authBaseUrl
                .plus(AppConstants.CLIENT_ID_PARAMETER).plus(clientId)
                .plus(AppConstants.REDIRECT_URI_PARAMETER).plus(redirectUri)
                .plus(AppConstants.RESPONSE_TYPE_PARAMETER).plus(AppConstants.RESPONSE_TYPE_CODE)
                .plus(AppConstants.RESPONSE_MODE_PARAMETER).plus(AppConstants.RESPONSE_MODE_QUERY)
                .plus(AppConstants.SCOPE_PARAMETER).plus(AppConstants.SCOPE)
                .plus(AppConstants.NONCE_PARAMETER).plus(nonceEncoded)
                .plus(AppConstants.STATE_PARAMETER).plus(1)
                .plus(AppConstants.CODE_CHALLENGE_PARAMETER).plus(pkceChallenge)
                .plus(AppConstants.CODE_CHALLENGE_METHOD_PARAMETER).plus(AppConstants.CODE_CHALLENGE_METHOD_S256)

            Success(url.toUri())
        }
    }
}