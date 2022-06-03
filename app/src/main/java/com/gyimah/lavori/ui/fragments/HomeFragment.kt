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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gyimah.lavori.adapters.PostAdapter
import com.gyimah.lavori.databinding.FragmentHomeBinding
import com.gyimah.lavori.databinding.FragmentPostBinding
import com.gyimah.lavori.listeners.ItemClickListener
import com.gyimah.lavori.listeners.PostLikeShareListener
import com.gyimah.lavori.viewmodels.HomeViewModel
import com.gyimah.lavori.viewmodels.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemClickListener, PostLikeShareListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var postAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView

    private val postViewModel: PostViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewPosts
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(requireView().context, LinearLayoutManager.VERTICAL, false)

        postAdapter.setItemClickListener(this)
        postAdapter.setPostLikeShareListener(this)

        recyclerView.adapter = postAdapter

        binding.refresh.isRefreshing = true

        postViewModel.getPosts()

        postViewModel.postState.observe(requireActivity()) {

            if(_binding != null) {
                binding.refresh.isRefreshing = false
            }


            postAdapter.setPosts(it)

            postAdapter.notifyDataSetChanged()
        }

        postViewModel.errorMessageState.observe(requireActivity()) {

           if (_binding != null) {
               binding.refresh.isRefreshing = false

               if (it != null) {
                   Toast.makeText(requireView().context, it, Toast.LENGTH_SHORT).show()
               }
           }
        }

        binding.refresh.setOnRefreshListener {
            postViewModel.getPosts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(view: View, position: Int) {
        Toast.makeText(requireContext().applicationContext, "Item clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onShareClicked(position: Int) {
        Toast.makeText(requireContext().applicationContext, "share clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onLikedClicked(position: Int) {
        Toast.makeText(requireContext().applicationContext, "liked clicked", Toast.LENGTH_SHORT).show()
    }
}