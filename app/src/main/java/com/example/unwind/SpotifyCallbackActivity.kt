package com.example.unwind

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import android.content.Intent
import com.example.unwind.ui.listen.AudioPlayerActivity
import android.util.Log

class SpotifyCallbackActivity : AppCompatActivity() {
    private lateinit var authService: AuthorizationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CallbackActivity", "Intent Data URI: " + intent.dataString)  // Log the entire URI

        authService = AuthorizationService(this)
        val authResponse = AuthorizationResponse.fromIntent(intent)
        val authException = AuthorizationException.fromIntent(intent)

        if (authResponse != null) {
            Log.d("CallbackActivity", "AuthorizationResponse obtained successfully")
            exchangeAuthorizationCode(authResponse)
        } else {
            Log.e("CallbackActivity", "No AuthorizationResponse, checking manually")
            if (intent.data != null && intent.data?.getQueryParameter("code") != null) {
                val code = intent.data?.getQueryParameter("code")
                Log.d("CallbackActivity", "Manual code extraction: $code")
                // Manually handle the authorization code if needed
            }
            if (authException != null) {
                authException.printStackTrace()
            } else {
                Log.e("CallbackActivity", "No Auth data received")
            }
        }
    }

    private fun exchangeAuthorizationCode(authResponse: AuthorizationResponse) {
        val tokenExchangeRequest = authResponse.createTokenExchangeRequest()
        authService.performTokenRequest(tokenExchangeRequest) { tokenResponse, exception ->
            if (tokenResponse != null && exception == null) {
                // Securely store the access token and refresh token
                storeAccessToken(tokenResponse.accessToken.orEmpty())
                storeRefreshToken(tokenResponse.refreshToken.orEmpty())

                // Redirect to AudioPlayerActivity
                redirectToAudioPlayer(tokenResponse.accessToken.orEmpty())
            } else {
                // Handle error
                exception?.printStackTrace()
            }
        }
    }
    private fun redirectToAudioPlayer(accessToken: String) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("SPOTIFY_ACCESS_TOKEN", accessToken)
        startActivity(intent)
        finish()  // Finish this activity so it's removed from the back stack
    }
    private fun storeAccessToken(accessToken: String) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "spotify_credentials",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit().putString("access_token", accessToken).apply()
    }

    private fun storeRefreshToken(refreshToken: String) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "spotify_credentials",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        authService.dispose()
    }
}