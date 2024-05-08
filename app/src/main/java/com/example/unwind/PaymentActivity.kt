package com.example.unwind

import android.content.Intent
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

class PaymentActivity : AppCompatActivity() {
    private val authService = PayPalAuthService()
    private val payPalService = PayPalService()
    private lateinit var paypalButton: PayPalButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        paypalButton = findViewById(R.id.paypal_button)
        val config = CoreConfig("CLIENT_ID", environment = Environment.SANDBOX)
        val returnUrl = "unwind://"
        val payPalWebCheckoutClient = PayPalWebCheckoutClient(this@PaymentActivity, config, returnUrl)
        payPalWebCheckoutClient.listener = object : PayPalWebCheckoutListener {
            override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
                // order was approved and is ready to be captured/authorized (see step 7)
            }
            override fun onPayPalWebFailure(error: PayPalSDKError) {
                // handle the error
            }
            override fun onPayPalWebCanceled() {
                // the user canceled the flow
            }
        }
        val clientId = "ARUQBsQhd4ePCllg2Ri45l803pcaNczYuC7Q0CvVk8w4C_64OLO8I4lygON34wqIQH3hdTAO4LPb8F2_"
        val clientSecret = "EAmWYCIyyqQzy-wjfhrZN2vTQZ_1ARSM7JGLYB9nN40dmm3tp0ptMJ393KQ4-4cyR944Ni73f8d0sLwE"
        authService.getAccessToken(clientId, clientSecret) { accessToken ->
            if (accessToken != null) {
                payPalService.createOrder(accessToken, this) { orderId, errorMessage ->
                    if (orderId != null) {
                        // Order created successfully, use orderId
                        Log.d("OrderID", orderId)
                        // Start the PayPal checkout with the obtained order ID
                        val payPalWebCheckoutRequest = PayPalWebCheckoutRequest(orderId, fundingSource = PayPalWebCheckoutFundingSource.PAYPAL)
                        payPalWebCheckoutClient.start(payPalWebCheckoutRequest)
                        authorizeAndCapture(accessToken, orderId)
                    } else {
                        // Handle error
                        Log.e("OrderCreationError", errorMessage ?: "Unknown error")
                    }
                }
            } else {
                // Handle error while obtaining access token
                Log.e("AccessTokenError", "Failed to obtain access token")
            }
        }
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(intent)
        intent = newIntent
    }

    private fun authorizeAndCapture(accessToken: String, orderId: String) {
        payPalService.authorizePayment(accessToken, orderId) { isAuthorized, authorizeErrorMessage ->
            if (isAuthorized) {
                // Authorization successful, capture the payment
                payPalService.capturePayment(accessToken, orderId) { isCaptured, captureErrorMessage ->
                    if (isCaptured) {
                        // Payment captured successfully
                        Log.d("PaymentCapture", "Payment captured successfully")
                    } else {
                        // Capture failed
                        Log.e("PaymentCaptureError", captureErrorMessage ?: "Unknown error")
                    }
                }
            } else {
                // Authorization failed
                Log.e("PaymentAuthorizationError", authorizeErrorMessage ?: "Unknown error")
            }
        }
    }


}