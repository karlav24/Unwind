package com.example.unwind.ui.listen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unwind.AppData
import com.example.unwind.BuildConfig
import com.example.unwind.databinding.FragmentListenBinding
import com.example.unwind.network.PlaylistsResponse
import com.example.unwind.network.SpotifyApi
import com.example.unwind.utils.AuthStateManager
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ListenItem(val text: String)

class ListenFragment : Fragment() {
    private var _binding: FragmentListenBinding? = null
    private val binding get() = _binding!!
    private lateinit var authService: AuthorizationService
    private lateinit var authActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authService = AuthorizationService(requireContext())
        setupActivityResultLauncher()

        binding.spotifyImage.setOnClickListener {
            if(AppData.orderId != ""){
                startSpotifyAuthentication()
            }
            else{
                Toast.makeText(context, "This feature is only for premium members", Toast.LENGTH_LONG).show()
            }

        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListenAdapter(getDummyData())
        }
    }

    private fun setupActivityResultLauncher() {
        authActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
                handleSpotifyResponse(result.data)
            }
        }
    }

    private fun handleSpotifyResponse(data: Intent?) {
        Log.d("SpotifyAuth", "Received intent data: ${data?.dataString}")  // Log the complete URI for debugging
        data?.let {
            val authResponse = AuthorizationResponse.fromIntent(it)
            val authException = AuthorizationException.fromIntent(it)

            if (authException != null) {
                Log.e("SpotifyAuth", "Authorization exception occurred", authException)
            }

            if (authResponse != null) {
                authService.performTokenRequest(authResponse.createTokenExchangeRequest()) { tokenResponse, exception ->
                    if (tokenResponse != null && exception == null) {
                        val accessToken = tokenResponse.accessToken.orEmpty()
                        fetchSpotifyData(accessToken)
                    } else {
                        Log.e("SpotifyAuth", "Token exchange error", exception)
                    }
                }
            } else {
                Log.e("SpotifyAuth", "No Authorization response received")
            }
        } ?: run {
            Log.e("SpotifyAuth", "Intent data is null")
        }
    }

    private fun fetchSpotifyData(accessToken: String) {
        SpotifyApi.service.getUserPlaylists("Bearer $accessToken").enqueue(object : Callback<PlaylistsResponse> {
            override fun onResponse(call: Call<PlaylistsResponse>, response: Response<PlaylistsResponse>) {
                if (response.isSuccessful) {
                    Log.d("SpotifyDataFetch", "Successfully fetched playlists")
                } else {
                    Log.e("SpotifyDataFetch", "Error fetching playlists: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PlaylistsResponse>, t: Throwable) {
                Log.e("SpotifyDataFetch", "Failed to fetch playlists", t)
            }
        })
    }

    private fun startSpotifyAuthentication() {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.spotify.com/authorize"),
            Uri.parse("https://accounts.spotify.com/api/token")
        )
        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            BuildConfig.SPOTIFY_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(BuildConfig.SPOTIFY_REDIRECT_URI)
        ).setScopes("playlist-read-private", "user-library-read").build()

        // Store the AuthorizationRequest for later retrieval in the callback activity
        AuthStateManager.setAuthRequest(authRequest)

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        authActivityResultLauncher.launch(authIntent)
    }

    private fun getDummyData(): List<ListenItem> = listOf(
        ListenItem("Affirmation Audio"),
        ListenItem("Calm Music"),
        ListenItem("Nature Sounds"),
        ListenItem("Color Noise")
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        authService.dispose()
    }
}
