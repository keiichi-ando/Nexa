package com.ex.nexa.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import com.ex.nexa.R
import androidx.navigation.fragment.findNavController as navigationFragmentFindNavController


class SplashFragment : Fragment() {
    companion object {
        fun newInstance() = SplashFragment()
    }

    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().actionBar?.hide()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(
            {
                navigationFragmentFindNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            },
            1500
        )
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }
}