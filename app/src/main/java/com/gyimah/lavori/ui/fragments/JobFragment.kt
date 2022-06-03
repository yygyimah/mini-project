package com.gyimah.lavori.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gyimah.lavori.databinding.FragmentJobsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobFragment : Fragment() {

    private var _binding: FragmentJobsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobsBinding.inflate(inflater, container, false);

        return binding.root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}