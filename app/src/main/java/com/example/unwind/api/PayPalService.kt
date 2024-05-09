package com.example.unwind.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.unwind.AppData
import com.example.unwind.model.AccessTokenResponse
import com.example.unwind.model.Amount
import com.example.unwind.model.ApplicationContext
import com.example.unwind.model.CreateOrderRequest
import com.example.unwind.model.OrderResponse
import com.example.unwind.model.PurchaseUnitRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PayPalService {

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api-m.sandbox.paypal.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(PayPalApiService::class.java)

    fun createOrder(accessToken: String, context: Context, callback: (String?, String?) -> Unit) {
        val request = CreateOrderRequest(
            intent = "CAPTURE",
            purchaseUnits = listOf(
                PurchaseUnitRequest(
                    amount = Amount("USD", "30.00")
                )
            ),
            applicationContext = ApplicationContext(
                returnUrl = "unwind://payment", // Replace with your return URL
                cancelUrl = "unwind://payment"  // Replace with your cancel URL
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

                    // Extract the Order ID and set it in AppData
                    orderResponse?.id?.let { orderId ->
                        AppData.orderId = orderId
                        Log.d("AppData", "Order ID set in AppData: $orderId")

                        // Retrieve the approve link
                        val approveLink = orderResponse.links.find { it.rel == "approve" }?.href
                        if (!approveLink.isNullOrEmpty()) {
                            // Redirect the user to the approve link
                            // Implement redirection logic here, such as opening a web browser with the approve link
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(approveLink))
                            context.startActivity(intent)
                        } else {
                            // If the approve link is not found, handle the error
                            callback(null, "Approve link not found")
                        }
                    } ?: run {
                        Log.e("OrderCreationError", "Failed to extract Order ID")
                        callback(null, "Failed to extract Order ID")
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
        if (orderId.isEmpty()) {
            Log.e("AuthorizePaymentError", "Order ID is empty or invalid.")
            callback(false, "Order ID is empty or invalid")
            return
        }

        val headers = mapOf(
            "Authorization" to "Bearer $accessToken",
            "Content-Type" to "application/json"
        )

        val call = apiService.authorizeOrder(orderId, headers)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthorizePaymentError", "Failed to authorize payment: $error")
                    callback(false, error)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AuthorizePaymentFailure", "Failed to process the authorization: ${t.message}")
                callback(false, t.message ?: "Unknown error")
            }
        })
    }

    // Function to capture the payment
    fun capturePayment(accessToken: String, orderId: String, callback: (Boolean, String?) -> Unit) {
        val headers = mapOf(
            "Authorization" to "Bearer $accessToken",
            "Content-Type" to "application/json"
        )

        val call = apiService.captureOrder(orderId, headers)
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

