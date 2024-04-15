package com.example.unwind.ui.breathe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.unwind.HomeActivity
import com.example.unwind.R
import com.example.unwind.databinding.FragmentBreatheBinding

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

        // Example of accessing a TextView and setting its text
//        binding.forToday.text = "For Today"
        binding.backButton.setOnClickListener {
            // Use NavController to navigate up in the fragment hierarchy
            findNavController().navigateUp()
        }
        // If using a ViewModel to observe data
        // Assume a BreatheViewModel provides a LiveData<String> for the text
        val breatheViewModel = ViewModelProvider(this)[BreatheViewModel::class.java]
//        breatheViewModel.text.observe(viewLifecycleOwner) {
//            binding.textBreathe.text = it
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Clear the binding when the view is destroyed
    }
}
