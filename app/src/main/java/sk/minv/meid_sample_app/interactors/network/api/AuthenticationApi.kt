package sk.minv.meid_sample_app.interactors.network.api

import retrofit2.Response
import retrofit2.http.*
import sk.minv.meid_sample_app.data.GetTokenResponse
import sk.minv.meid_sample_app.utils.AppConstants

interface AuthenticationApi {

    @FormUrlEncoded
    @POST(AppConstants.ENDPOINT_TOKEN)
    suspend fun getToken(@Header("Content-type") contentType: String,
                         @Field("grant_type") grantType: String,
                         @Field("client_id") clientId: String,
                         @Field("client_secret") clientSecret: String,
                         @Field("redirect_uri") redirectUri: String,
                         @Field("code") code: String,
                         @Field("code_verifier") codeVerifier: String): Response<GetTokenResponse>
}