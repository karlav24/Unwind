package com.example.unwind.api
import com.example.unwind.model.CreateOrderRequest
import com.example.unwind.model.OrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PayPalApiService {
    @Headers(
        "Accept: application/json",
        "Accept-Language: en_US"
    )
    @POST("v2/checkout/orders")
    fun createOrder(
        @HeaderMap headers: Map<String, String>,
        @Body request: CreateOrderRequest
    ): Call<OrderResponse>

    @POST("v2/checkout/orders/{orderId}/authorize")
    fun authorizeOrder(
        @Path("orderId") orderId: String,
        @HeaderMap headers: Map<String, String>
    ): Call<Void>


    // Function to capture the payment
    @POST("v2/checkout/orders/{orderId}/capture")
    fun captureOrder(
        @Path("orderId") orderId: String,
        @HeaderMap headers: Map<String, String>
    ): Call<Void>
}
