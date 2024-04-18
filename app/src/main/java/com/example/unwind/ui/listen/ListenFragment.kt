// ListenFragment.kt
package com.example.unwind.ui.listen

import ListenAdapter
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.MeditationTracks.MeditationTrack
import com.example.unwind.MeditationTracks.MeditationViewModel
import com.example.unwind.R
import com.example.unwind.databinding.FragmentListenBinding

data class ListenItem(val title: String, val resourceId: Int)
class ListenFragment : Fragment() {

    private var _binding: FragmentListenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MeditationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MeditationViewModel::class.java)
        insertSampleTracks()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

//
//
//        val paddingTopPx = (58 * resources.displayMetrics.density).toInt()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListenAdapter(getDummyData())
           // addItemDecoration(TopSpacingDecoration(paddingTopPx))
        }

    }

    private fun getDummyData(): List<ListenItem> {
        // This will return a dummy list of items, replace this with real data
        return listOf(
            ListenItem("Affirmation Audio", R.raw.ethereal),
            ListenItem("Calm Music", R.raw.sleep),
            ListenItem("Nature Sounds", R.raw.bird_singing),
            ListenItem("Color Noise", R.raw.rain_thunder)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun insertSampleTracks() {
        val sampleTracks = listOf(
            MeditationTrack(
                title = "Birds Singing",
                artist = "Nature Sounds",
                duration = 112, // duration in seconds.
                resourceId = R.raw.bird_singing
            ),
            MeditationTrack(
                title = "Ethereal",
                artist = "432 Hz Music",
                duration = 141, // duration in seconds.
                resourceId = R.raw.ethereal
            ),
            MeditationTrack(
                title = "Rain and Thunder",
                artist = "Nature Sounds",
                duration = 58, // duration in seconds.
                resourceId = R.raw.rain_thunder
            ),
            MeditationTrack(
                title = "Sleep",
                artist = "Sleeping Sounds",
                duration = 173, // duration in seconds.
                resourceId = R.raw.sleep
            )
        )

        for (track in sampleTracks) {
            viewModel.insert(track)
        }
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


