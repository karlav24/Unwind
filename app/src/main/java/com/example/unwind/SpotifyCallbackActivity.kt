package com.example.unwind

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import com.example.unwind.BuildConfig
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

class SpotifyCallbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the intent that was used to launch this activity
        if (intent?.action == Intent.ACTION_VIEW) {
            // Extract the authorization code or access token from the callback URI
            val uri = intent.data
            if (uri != null && uri.toString().startsWith(BuildConfig.SPOTIFY_REDIRECT_URI)) {
                // Use the AppAuth library to extract the authorization code or access token
                val authResponse = AuthorizationResponse.fromIntent(intent)
                val authException = AuthorizationException.fromIntent(intent)

                if (authResponse != null) {
                    // Authorization was successful, exchange the code for a token
                    // and then use it to access the Spotify API
                } else if (authException != null) {
                    // Handle error - the authorization flow failed
                }
            }
        }
        finish() // Close this activity after handling the redirect
    }
}

