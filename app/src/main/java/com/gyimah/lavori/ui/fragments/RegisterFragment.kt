package com.gyimah.lavori.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.gyimah.lavori.R
import com.gyimah.lavori.databinding.FragmentRegisterBinding
import com.gyimah.lavori.ui.activities.MainActivity
import com.gyimah.lavori.utils.AppUtils
import com.gyimah.lavori.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private lateinit var dialog: AlertDialog

    private val registerViewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = AppUtils.createLoadingDialog(requireActivity(), "Loading, please wait.")

        binding.loginTxt.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }

        binding.continueBtn.setOnClickListener {
            dialog.show()

            registerViewModel.registerWithEmail(
                email = binding.emailLayout.editText?.text.toString(),
                password = binding.passwordLayout.editText?.text.toString()
            )

        }

        binding.registerWithGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            googleLauncher.launch(intent)
        }

        registerViewModel.errorState.observe(viewLifecycleOwner) {

            dialog.dismiss()

            if (it != null) {
                Toast.makeText(requireView().context, it, Toast.LENGTH_SHORT).show()

                registerViewModel.errorState.value = null
            }
        }

        registerViewModel.successState.observe(viewLifecycleOwner) {

            dialog.dismiss()

            if (it != null) {

                NavHostFragment.findNavController(this).navigate(R.id.accountFragment)

                registerViewModel.successState.value = null
            }
        }

    }

    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    dialog.show()
                    registerViewModel.registerWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(
                    requireView().context, "Error signing in with google", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}