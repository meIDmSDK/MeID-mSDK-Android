package sk.minv.meid_sample_app.interactors.usecase.logout

import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import kotlinx.coroutines.withContext
import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.meid_sample_app.App
import sk.minv.meid_sample_app.utils.AppConstants
import sk.minv.meid_sample_app.utils.KeyStoreUtils
import androidx.core.net.toUri

class LogoutUseCaseImpl(
    private val appDispatchers: AppDispatchers
) : LogoutUseCase {

    override suspend fun invoke(): Result<Uri> {
        return withContext(appDispatchers.IO) {
            val logoutBasUrl = AppConstants.BASE_URL.plus(AppConstants.ENDPOINT_LOGOUT)
            val clientId = AppConstants.CLIENT_ID
            val redirectUri = AppConstants.REDIRECT_URI_LOGOUT

            val secretKey = KeyStoreUtils.getOrCreateKey()
            val encodedIdToken = App.idTokenEncoded
            val encodedIdTokenIV = App.idTokenIvEncoded

            val idToken = if (TextUtils.isEmpty(encodedIdToken) || TextUtils.isEmpty(encodedIdTokenIV)) {
                ""
            } else {
                val encryptedIdToken = Base64.decode(encodedIdToken, Base64.DEFAULT)
                val iv = Base64.decode(encodedIdTokenIV, Base64.DEFAULT)
                KeyStoreUtils.decryptData(encryptedIdToken, iv, secretKey)
            }

            val url = logoutBasUrl
                .plus(AppConstants.CLIENT_ID_PARAMETER).plus(clientId)
                .plus(AppConstants.ID_TOKEN_PARAMETER).plus(idToken)
                .plus(AppConstants.LOGOUT_REDIRECT_URI_PARAMETER).plus(redirectUri)

            App.idTokenEncoded = null
            App.idTokenIvEncoded = null
            App.accessTokenEncoded = null
            App.accessTokenIvEncoded = null
            App.refreshTokenEncoded = null
            App.refreshTokenIvEncoded = null

            Success(url.toUri())
        }
    }
}