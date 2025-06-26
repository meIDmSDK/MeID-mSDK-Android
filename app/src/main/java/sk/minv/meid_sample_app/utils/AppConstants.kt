package sk.minv.meid_sample_app.utils

object AppConstants {
    // Rest API values
    const val BASE_URL = "https://tmeid.minv.sk/"
    const val ENDPOINT_AUTH = "realms/meid/protocol/openid-connect/auth"
    const val ENDPOINT_TOKEN = "realms/meid/protocol/openid-connect/token"
    const val ENDPOINT_LOGOUT = "realms/meid/protocol/openid-connect/logout"
    const val CONNECTION_TIMEOUT = 60L
    const val CONTENT_TYPE = "application/x-www-form-urlencoded"
    const val GRANT_TYPE = "authorization_code"
    const val DEEPLINK_SCHEME = "sk.test.tmeid"
    const val DEEPLINK_HOST_ACCOUNT = "account"
    const val DEEPLINK_HOST_LOGOUT = "logout"
    const val CLIENT_ID_PARAMETER = "?client_id="
    const val CLIENT_ID = "test-client"
    const val CLIENT_SECRET = "udPb01a5N6f8cq1hMv9IqQIoEE0SMt6S"
    const val REDIRECT_URI_PARAMETER = "&redirect_uri="
    const val REDIRECT_URI = DEEPLINK_SCHEME.plus("://").plus(DEEPLINK_HOST_ACCOUNT)
    const val RESPONSE_TYPE_PARAMETER = "&response_type="
    const val RESPONSE_TYPE_CODE = "code"
    const val RESPONSE_MODE_PARAMETER = "&response_mode="
    const val RESPONSE_MODE_QUERY = "query"
    const val SCOPE_PARAMETER = "&scope="
    const val SCOPE = "openid"
    const val NONCE_PARAMETER = "&nonce="
    const val STATE_PARAMETER = "&state="
    const val CODE_CHALLENGE_PARAMETER = "&code_challenge="
    const val CODE_CHALLENGE_METHOD_PARAMETER = "&code_challenge_method="
    const val CODE_CHALLENGE_METHOD_S256 = "S256"
    const val ID_TOKEN_PARAMETER = "&id_token_hint="
    const val LOGOUT_REDIRECT_URI_PARAMETER = "&post_logout_redirect_uri="
    const val REDIRECT_URI_LOGOUT = DEEPLINK_SCHEME.plus("://").plus(DEEPLINK_HOST_LOGOUT)
}