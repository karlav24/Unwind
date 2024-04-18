// ListenFragment.kt
package com.example.unwind.ui.listen

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.databinding.FragmentListenBinding

data class ListenItem(val text: String)
class ListenFragment : Fragment() {

    private var _binding: FragmentListenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListenBinding.inflate(inflater, container, false)
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
            ListenItem("Affirmation Audio"),
            ListenItem("Calm Music"),
            ListenItem("Nature Sounds"),
            ListenItem("Color Noise")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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


