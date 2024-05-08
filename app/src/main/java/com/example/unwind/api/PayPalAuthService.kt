package com.example.unwind.api

import android.util.Base64
import com.example.unwind.model.AccessTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PayPalAuthService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.sandbox.paypal.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(PayPalAuthApiService::class.java)

    fun getAccessToken(clientId: String, clientSecret: String, callback: (String?) -> Unit) {
        val credentials = "$clientId:$clientSecret"
        val encodedCredentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        apiService.getAccessToken("Basic $encodedCredentials", "client_credentials")
            .enqueue(object : Callback<AccessTokenResponse> {
                override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                    if (response.isSuccessful) {
                        val accessToken = response.body()?.accessToken
                        callback(accessToken)
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                    callback(null)
                }
            })
    }
}
