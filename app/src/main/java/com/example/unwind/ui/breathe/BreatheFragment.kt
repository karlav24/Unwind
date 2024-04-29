package com.example.unwind.ui.breathe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.unwind.databinding.FragmentBreatheBinding
import com.example.unwind.network.YogaApi // Import the YogaApi object
import com.example.unwind.model.YogaPose // Assuming you have a YogaPose data class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.example.unwind.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class BreatheFragment : Fragment() {

    private var _binding: FragmentBreatheBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreatheBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Call the fetchRandomYogaPoseAndDisplay function directly in onViewCreated
        fetchRandomYogaPoseAndDisplay { randomYogaPose ->
            binding.theBreath.text = randomYogaPose.english_name
            Picasso.get()
                .load(randomYogaPose.url_png)
                .error(R.drawable.ic_play)
                .into(binding.yogaPoseImage)
            binding.yogaPoseImage.bringToFront()
            binding.poseDescription.text = randomYogaPose.pose_description
            // You can access other properties of randomYogaPose as needed
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Function to fetch a random yoga pose
    private fun fetchRandomYogaPoseAndDisplay(callback: (YogaPose) -> Unit) {
        // Make a network call to fetch all yoga poses
        YogaApi.instance.getPoses().enqueue(object : Callback<List<YogaPose>> {
            override fun onResponse(
                call: Call<List<YogaPose>>,
                response: Response<List<YogaPose>>
            ) {
                if (response.isSuccessful) {
                    val yogaPoses = response.body()
                    if (yogaPoses != null && yogaPoses.isNotEmpty()) {
                        // Choose a random yoga pose from the list
                        val randomIndex = Random().nextInt(yogaPoses.size)
                        val randomYogaPose = yogaPoses[randomIndex]
                        // Now you can access the fields of the randomYogaPose object
                        Log.d("Yoga API", "Random Yoga Pose: $randomYogaPose")
                        if (randomYogaPose != null) {
                            Log.d(
                                "Yoga API",
                                "Random Yoga Pose English Name: ${randomYogaPose.english_name}"
                            )
                            Log.d("Yoga API", "Random Yoga Pose SVG URL: ${randomYogaPose.url_svg}")
                        } else {
                            Log.e("Yoga API", "Random Yoga Pose is null")
                        }
                        // Call the callback function to display the random yoga pose
                        callback(randomYogaPose)
                    } else {
                        Log.e("Yoga API", "Yoga poses list is null or empty")
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("Yoga API", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<YogaPose>>, t: Throwable) {
                // Handle network call failure
                Log.e("Yoga API", "Failed to fetch yoga poses: ${t.message}")
            }
        })
    }
}