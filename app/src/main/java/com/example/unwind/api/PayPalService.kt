package com.example.unwind.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.unwind.model.AccessTokenResponse
import com.example.unwind.model.Amount
import com.example.unwind.model.CreateOrderRequest
import com.example.unwind.model.OrderResponse
import com.example.unwind.model.PurchaseUnitRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PayPalService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.sandbox.paypal.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(PayPalApiService::class.java)

    fun createOrder(accessToken: String, context: Context, callback: (String?, String?) -> Unit) {
        val request = CreateOrderRequest(
            intent = "CAPTURE",
            purchaseUnits = listOf(
                PurchaseUnitRequest(
                    amount = Amount("USD", "10.00")
                )
            )
        )

        val headers = mapOf(
            "Authorization" to "Bearer $accessToken",
            "Content-Type" to "application/json"
        )

        val call = apiService.createOrder(headers, request)
        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    Log.d("CreateOrderResponse", orderResponse.toString())

                    // Retrieve the approve link
                    val approveLink = orderResponse?.links?.find { it.rel == "approve" }?.href
                    if (!approveLink.isNullOrEmpty()) {
                        // Redirect the user to the approve link
                        // Implement redirection logic here, such as opening a web browser with the approve link
                        // For example:
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(approveLink))
                        context.startActivity(intent)
                    } else {
                        // If the approve link is not found, handle the error
                        callback(null, "Approve link not found")
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("CreateOrderError", "Failed to create order: ${response.code()}")
                    callback(null, "Failed to create order: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                // Handle failure
                val errorMessage = t.message ?: "Unknown error"
                callback(null, errorMessage)
            }
        })
    }

    fun authorizePayment(accessToken: String, orderId: String, callback: (Boolean, String?) -> Unit) {
        // Modify the call to pass the access token as a header
        val call = apiService.authorizeOrder("Bearer $accessToken", orderId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, null) // Authorization successful
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    callback(false, errorMessage) // Authorization failed
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                val errorMessage = t.message ?: "Unknown error"
                callback(false, errorMessage) // Authorization failed
            }
        })
    }

    // Function to capture the payment
    fun capturePayment(accessToken: String, orderId: String, callback: (Boolean, String?) -> Unit) {
        val call = apiService.captureOrder("Bearer $accessToken", orderId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, null) // Capture successful
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    callback(false, errorMessage) // Capture failed
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                val errorMessage = t.message ?: "Unknown error"
                callback(false, errorMessage) // Capture failed
            }
        })
    }
}

