package com.gyimah.lavori.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gyimah.lavori.databinding.FragmentAccountBinding
import com.gyimah.lavori.ui.activities.MainActivity
import com.gyimah.lavori.utils.AppUtils
import com.gyimah.lavori.viewmodels.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    private val accountViewModel: AccountViewModel by viewModels()

    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = AppUtils.createLoadingDialog(requireActivity(), "Loading, please wait")

        binding.continueBtn.setOnClickListener {

            dialog.show()

            accountViewModel.saveUserInfo(
                firstname = binding.firstnameLayout.editText?.text.toString(),
                lastname = binding.lastnameLayout.editText?.text.toString(),
                summary = binding.headlineLayout.editText?.text.toString(),
            )

        }

        accountViewModel.successState.observe(viewLifecycleOwner) {

            dialog.dismiss()

            if (it != null) {

                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()

                accountViewModel.successState.value = null
            }
        }

        accountViewModel.errorState.observe(viewLifecycleOwner) {
            dialog.dismiss()

            if (it != null) {

                Toast.makeText(requireView().context, it, Toast.LENGTH_SHORT).show()

                accountViewModel.errorState.value = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}