package sk.minv.meid_sample_app.data

import sk.minv.meid_sample_app.utils.AppConstants

data class GetTokenUseCaseParams(
    val authorizationCode: String
)

data class GetTokenParams(
    val contentType: String = AppConstants.CONTENT_TYPE,
    val grantType: String = AppConstants.GRANT_TYPE,
    val clientId: String,
    val clientSecret: String = AppConstants.CLIENT_SECRET,
    val redirectUri: String,
    val scope: String = AppConstants.SCOPE,
    val code: String,
    val pkceVerifier: String? = null
)