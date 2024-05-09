package com.example.unwind.utils

import android.util.Log
import net.openid.appauth.AuthorizationRequest

object AuthStateManager {
    private var authRequest: AuthorizationRequest? = null

    fun setAuthRequest(request: AuthorizationRequest) {
        authRequest = request
        Log.d("AuthStateManager", "AuthorizationRequest stored: $request")
    }

    fun getAuthRequest(): AuthorizationRequest? {
        Log.d("AuthStateManager", "Retrieving AuthorizationRequest: $authRequest")
        return authRequest
    }
}