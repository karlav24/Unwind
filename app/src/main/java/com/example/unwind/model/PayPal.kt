package com.example.unwind.model

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("intent") val intent: String,
    @SerializedName("purchase_units") val purchaseUnits: List<PurchaseUnitRequest>,
    @SerializedName("application_context") val applicationContext: ApplicationContext
)

data class PurchaseUnitRequest(
    @SerializedName("amount") val amount: Amount
)

data class Amount(
    @SerializedName("currency_code") val currencyCode: String,
    @SerializedName("value") val value: String
)

data class ApplicationContext(
    @SerializedName("return_url") val returnUrl: String,
    @SerializedName("cancel_url") val cancelUrl: String
)

data class OrderResponse(
    val id: String,
    val links: List<Link>
) {
    data class Link(
        val href: String,
        val rel: String,
        val method: String
    )
}
