package com.gyimah.lavori.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gyimah.lavori.adapters.JobAdapter
import com.gyimah.lavori.databinding.FragmentJobsBinding
import com.gyimah.lavori.ui.activities.PostJobActivity
import com.gyimah.lavori.viewmodels.JobViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JobFragment : Fragment() {

    private var _binding: FragmentJobsBinding? = null

    private val binding get() = _binding!!

    private val jobViewModel: JobViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var jobAdapter: JobAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobsBinding.inflate(inflater, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewJobs
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(requireView().context, LinearLayoutManager.VERTICAL, false)


        recyclerView.adapter = jobAdapter

        binding.refresh.isRefreshing = true

        jobViewModel.getJobs()

        jobViewModel.jobsState.observe(requireActivity()) {

            if (_binding != null)  {

                binding.refresh.isRefreshing = false

                jobAdapter.setJobs(it)

                jobAdapter.notifyDataSetChanged()

                when (it.size) {
                    0 -> {
                        recyclerView.visibility = View.GONE
                        binding.txtMessage.visibility = View.VISIBLE
                    }
                    else -> {
                        recyclerView.visibility = View.VISIBLE
                        binding.txtMessage.visibility = View.GONE
                    }
                }
            }

        }

        jobViewModel.errorMessageState.observe(requireActivity()) {

            if (_binding != null) {
                binding.refresh.isRefreshing = false

                if (it != null) {
                    Toast.makeText(requireView().context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.refresh.setOnRefreshListener {
            jobViewModel.getJobs()
        }

        binding.addJobBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), PostJobActivity::class.java))
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}