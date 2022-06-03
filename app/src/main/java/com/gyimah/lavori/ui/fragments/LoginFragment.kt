package com.gyimah.lavori.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.gyimah.lavori.R
import com.gyimah.lavori.databinding.FragmentLoginBinding
import com.gyimah.lavori.ui.activities.MainActivity
import com.gyimah.lavori.utils.AppUtils
import com.gyimah.lavori.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var dialog: AlertDialog

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = AppUtils.createLoadingDialog(requireActivity(), "Loading, please wait..")

        binding.registerTxt.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.registerFragment)
        }

        binding.continueBtn.setOnClickListener {

            dialog.show()

            loginViewModel.loginWithEmail(
                email = binding.emailLayout.editText?.text.toString(),
                password = binding.passwordLayout.editText?.text.toString()
            )
        }

        binding.loginWithGoogle.setOnClickListener {

            val intent = googleSignInClient.signInIntent
            googleLauncher.launch(intent)

        }

        loginViewModel.accountState.observe(requireActivity()) {
            dialog.dismiss()
            if (it != null && it == 1) {
                //redirect to account page

                NavHostFragment.findNavController(this).navigate(R.id.accountFragment)

                loginViewModel.accountState.value = null
            }
        }

        loginViewModel.errorState.observe(requireActivity()) {

            dialog.dismiss()

            if (it != null) {
                Toast.makeText(requireView().context, it, Toast.LENGTH_SHORT).show()

                loginViewModel.errorState.value = null
            }

        }

        loginViewModel.successState.observe(requireActivity()) {

            dialog.dismiss()

            if (it != null && it) {

                startActivity(Intent(requireActivity(), MainActivity::class.java))

                requireActivity().finish()

                loginViewModel.successState.value = null

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val googleLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        Log.i("RESULT", result.resultCode.toString())
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    dialog.show()
                    Log.i("ID TOKEN", account.idToken!!)
                    loginViewModel.loginWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(
                    requireView().context, "Error signing in with google", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}