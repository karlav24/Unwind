package com.example.unwind.ui.listen

import android.graphics.Rect
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unwind.databinding.FragmentListenBinding
import com.example.unwind.network.SpotifyApi
import com.example.unwind.network.PlaylistsResponse
import net.openid.appauth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.RecyclerView
import android.net.Uri
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationException
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.AuthorizationServiceConfiguration
import com.example.unwind.BuildConfig

data class ListenItem(val text: String)

class ListenFragment : Fragment() {

    private var _binding: FragmentListenBinding? = null
    private val binding get() = _binding!!

    // Spotify Auth Variables
    private lateinit var authService: AuthorizationService
    private lateinit var authState: AuthState

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authService = AuthorizationService(requireContext())
        binding.spotifyImage.setOnClickListener {
            startSpotifyAuthentication()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListenAdapter(getDummyData())
        }
    }

    private fun getDummyData(): List<ListenItem> {
        return listOf(
            ListenItem("Affirmation Audio"),
            ListenItem("Calm Music"),
            ListenItem("Nature Sounds"),
            ListenItem("Color Noise")
        )
    }

    private fun startSpotifyAuthentication() {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.spotify.com/authorize"), // Spotify authorization endpoint
            Uri.parse("https://accounts.spotify.com/api/token") // Spotify token endpoint
        )

        // Use BuildConfig to pull client ID and redirect URI
        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            BuildConfig.SPOTIFY_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(BuildConfig.SPOTIFY_REDIRECT_URI)
        ).setScopes(*arrayOf("playlist-read-private", "user-library-read"))
            .build() // add more scopes as needed

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        startActivityForResult(authIntent, AUTH_REQUEST_CODE)
    }

    private fun fetchSpotifyData(accessToken: String) {
        SpotifyApi.service.getUserPlaylists("Bearer $accessToken").enqueue(object : Callback<PlaylistsResponse> {
            override fun onResponse(call: Call<PlaylistsResponse>, response: Response<PlaylistsResponse>) {
                if (response.isSuccessful) {
                    // Convert the response into a data model or directly update UI
                    val playlists = response.body()?.items ?: emptyList()
                    // Update the UI with the playlists data, e.g. by setting it to RecyclerView adapter
                } else {
                    // Handle API errors here
                }
            }

            override fun onFailure(call: Call<PlaylistsResponse>, t: Throwable) {
                // Handle network errors here
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTH_REQUEST_CODE && data != null) {
            val authResponse = AuthorizationResponse.fromIntent(data)
            val authException = AuthorizationException.fromIntent(data)

            if (authResponse != null && authException == null) {
                authService.performTokenRequest(
                    authResponse.createTokenExchangeRequest()
                ) { tokenResponse, exception ->
                    if (tokenResponse != null && exception == null) {
                        // Use the access token to make API calls
                        val accessToken = tokenResponse.accessToken.orEmpty()
                        fetchSpotifyData(accessToken)
                    } else {
                        // Handle error
                    }
                }
            } else {
                // Handle other cases such as errors
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        authService.dispose()
    }

    companion object {
        const val AUTH_REQUEST_CODE = 1001
        // Other constants as needed
    }
}
class TopSpacingDecoration(private val paddingTop: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.top = paddingTop
        }
    }
}