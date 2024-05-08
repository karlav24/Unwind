package com.example.unwind.api

import com.example.unwind.model.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PayPalAuthApiService {
    @Headers(
        "Accept: application/json",
        "Accept-Language: en_US"
    )
    @FormUrlEncoded
    @POST("/v1/oauth2/token")
    fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String
    ): Call<AccessTokenResponse>
}
