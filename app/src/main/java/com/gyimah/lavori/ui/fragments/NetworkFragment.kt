package com.gyimah.lavori.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.gyimah.lavori.R
import com.gyimah.lavori.adapters.NetworkAdapter
import com.gyimah.lavori.databinding.FragmentNetworkBinding
import com.gyimah.lavori.viewmodels.DashboardViewModel
import com.gyimah.lavori.viewmodels.NetworkViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NetworkFragment : Fragment() {

    private var _binding: FragmentNetworkBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var networkAdapter: NetworkAdapter

    @Inject
    lateinit var firebaseUser: FirebaseUser

    private lateinit var recyclerView: RecyclerView

    private val networkViewModel: NetworkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = binding.recyclerViewUsers
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireView().context, 2)


        recyclerView.adapter = networkAdapter

        binding.refresh.isRefreshing = true

        networkViewModel.getUsers(authId = firebaseUser.uid)

        networkViewModel.usersState.observe(requireActivity()) {

            if (_binding != null)  {

                binding.refresh.isRefreshing = false

                networkAdapter.setUsers(it)


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

            networkAdapter.notifyDataSetChanged()

        }

        networkViewModel.errorMessageState.observe(requireActivity()) {

            if (_binding != null) {
                binding.refresh.isRefreshing = false

                if (it != null) {
                    Toast.makeText(requireView().context, it, Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.refresh.setOnRefreshListener {
            networkViewModel.getUsers(authId =  firebaseUser.uid)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}