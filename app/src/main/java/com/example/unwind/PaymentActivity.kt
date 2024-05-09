package com.example.unwind

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.api.PayPalApiService
import com.example.unwind.api.PayPalAuthService
import com.example.unwind.api.PayPalService
import com.example.unwind.model.AccessTokenResponse
import com.example.unwind.model.Amount
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import com.paypal.android.paymentbuttons.PayPalButton
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutClient
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutFundingSource
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutListener
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutRequest
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.paypal.android.paypalwebpayments.*

class PaymentActivity : AppCompatActivity() {
    private val authService = PayPalAuthService()
    private val payPalService = PayPalService()
    private lateinit var payPalWebCheckoutClient: PayPalWebCheckoutClient
    private lateinit var returnUrl: String
    private lateinit var accessToken: String
    private lateinit var orderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        orderId = AppData.orderId
        // Initialize PayPalWebCheckoutClient
        val config = CoreConfig("ARUQBsQhd4ePCllg2Ri45l803pcaNczYuC7Q0CvVk8w4C_64OLO8I4lygON34wqIQH3hdTAO4LPb8F2_", environment = Environment.SANDBOX)
        returnUrl = "unwind://payment" // Customize the return URL as per your app's deep linking scheme
        payPalWebCheckoutClient = PayPalWebCheckoutClient(this@PaymentActivity, config, returnUrl)

        // Set listener for PayPalWebCheckoutClient
        payPalWebCheckoutClient.listener = object : PayPalWebCheckoutListener {
            override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
                // Handle PayPal web success
                orderId?.let { authorizeAndCapture(it, accessToken) }

                // Do not start another activity here, as this method will be called multiple times
                // Instead, handle redirection back to the app in onNewIntent
            }

            override fun onPayPalWebFailure(error: PayPalSDKError) {
                // Handle PayPal web failure
                Log.e("PayPalWebFailure", "PayPalWebFailure callback invoked: $error")
            }

            override fun onPayPalWebCanceled() {
                // Handle PayPal web cancellation
                Log.e("PayPalWebCancelled", "PayPalWebCancelled callback invoked")
            }
        }

        // Obtain access token and create PayPal order
        obtainAccessTokenAndCreateOrder()
    }

    private fun obtainAccessTokenAndCreateOrder() {
        val clientId = "ARUQBsQhd4ePCllg2Ri45l803pcaNczYuC7Q0CvVk8w4C_64OLO8I4lygON34wqIQH3hdTAO4LPb8F2_"
        val clientSecret = "EAmWYCIyyqQzy-wjfhrZN2vTQZ_1ARSM7JGLYB9nN40dmm3tp0ptMJ393KQ4-4cyR944Ni73f8d0sLwE"
        authService.getAccessToken(clientId, clientSecret) { token ->
            if (token != null) {
                accessToken = token
                payPalService.createOrder(token, this@PaymentActivity) { Id, errorMessage ->
                    if (Id != null) {
                        // Order created successfully, start PayPal checkout
                        orderId = Id
                        AppData.orderId = Id
                        Log.d("CreateOrder", "Order id is {AppData.orderId}")
                        val payPalWebCheckoutRequest = PayPalWebCheckoutRequest(Id)
                        payPalWebCheckoutClient.start(payPalWebCheckoutRequest)
                    } else {
                        // Handle error while creating order
                        Log.e("OrderCreationError", "Failed to create order: ${errorMessage ?: "Unknown error"}")
                    }
                }
            } else {
                // Handle error while obtaining access token
                Log.e("AccessTokenError", "Failed to obtain access token")
            }
        }
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        intent = newIntent

        // Handle the URI redirect here
        if (intent?.action == Intent.ACTION_VIEW) {
            // It might be good to reload or confirm orderId here if necessary
            orderId = AppData.orderId
            if (orderId != null) {
                Log.d("AccessToken", "The access token is {$accessToken}")
                authorizeAndCapture(orderId, accessToken)
            } else {
                // Handle error: orderId not found
                Log.e("PaymentActivity", "Order ID not found in URI")
            }
        }
    }


    private fun extractOrderIdFromReturnUrl(returnUrl: String): String? {
        val uri = Uri.parse(returnUrl)
        return uri.getQueryParameter("orderId")
    }

    private fun authorizeAndCapture(Id: String, Token: String) {
        Log.d("AuthorizePayment", "Order ID: $orderId")
        //payPalService.authorizePayment(accessToken, orderId) { isAuthorized, authorizeErrorMessage ->
          //  if (isAuthorized) {
                // Authorization successful, capture the payment
            //    Log.d("AuthorizePayment", "Authorization successful")
        payPalService.capturePayment(accessToken, orderId) { isCaptured, captureErrorMessage ->
            if (isCaptured) {
                // Payment captured successfully
                Log.d("PaymentCapture", "Payment captured successfully")
            } else {
                // Capture failed
                Log.e("PaymentCaptureError", "Failed to capture payment: ${captureErrorMessage ?: "Unknown error"}")
            }
        }
            //} else {
                // Authorization failed
            //    Log.e("PaymentAuthorizationError", "Failed to authorize payment: ${authorizeErrorMessage ?: "Unknown error"}")
            //}
        //}
    }
}