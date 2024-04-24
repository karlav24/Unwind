package com.example.unwind.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unwind.ui.QuotesFragment
import com.example.unwind.R
import com.example.unwind.ui.SettingsActivity
import com.example.unwind.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = activity?.intent?.getStringExtra("userName")
        binding.welcomeBac.text = if (userName != null) "Welcome back, $userName!" else "Welcome back!"

        // Initialize and attach the QuotesFragment
        val quotesFragment = QuotesFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentQuoteContainer, quotesFragment)
            .commit()

        // Setting button click to open SettingsActivity
        binding.menuVector.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding when the view is destroyed
    }
}

