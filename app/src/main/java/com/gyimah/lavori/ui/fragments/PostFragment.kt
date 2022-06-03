package com.gyimah.lavori.ui.fragments

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment;
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gyimah.lavori.R
import com.gyimah.lavori.databinding.FragmentPostBinding
import com.gyimah.lavori.utils.AppUtils
import com.gyimah.lavori.viewmodels.PostViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null

    private val binding get() = _binding!!

    private var fileUri: Uri? = null

    private val postViewModel: PostViewModel by viewModels()

    private lateinit var dialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = AppUtils.createLoadingDialog(requireActivity(), "Loading, please wait")


        binding.postBtn.isEnabled = binding.content.text.toString().trim().isNotEmpty()

        binding.closeBtn.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }


        binding.photoBtn.setOnClickListener {

            Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {

                        ImagePicker.with(requireActivity())
                            .galleryOnly()
                            .crop()
                            .compress(1024)         //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(
                                1080,
                                1080
                            )  //Final image resolution will be less than 1080 x 1080(Optional)
                            .createIntent { intent ->
                                startForProfileImageResult.launch(intent)
                            }
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }


                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token!!.continuePermissionRequest()
                    }
                }).check()
        }

        binding.cameraBtn.setOnClickListener {
            Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {

                        ImagePicker.with(requireActivity())
                            .cameraOnly()
                            .crop()
                            .compress(1024)         //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(
                                1080,
                                1080
                            )  //Final image resolution will be less than 1080 x 1080(Optional)
                            .createIntent { intent ->
                                startForProfileImageResult.launch(intent)
                            }

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }


                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token!!.continuePermissionRequest()
                    }
                }).check()
        }



        binding.content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.postBtn.isEnabled = p0.toString().trim().isNotEmpty()

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.postBtn.setOnClickListener {

            dialog.show()

            postViewModel.storePost(
                content =  binding.content.text.toString(),
                fileUri = fileUri
            )
        }

        postViewModel.addPostState.observe(viewLifecycleOwner) {

            dialog.dismiss()

            if (it != null && it == 1) {
                Toast.makeText(requireContext().applicationContext, "Post Added", Toast.LENGTH_SHORT).show()

                NavHostFragment.findNavController(this).navigate(R.id.navigation_home)

                postViewModel.addPostState.value = null
            }
        }

        postViewModel.errorMessageState.observe(viewLifecycleOwner) {

            dialog.dismiss()

            if (it != null) {
                Toast.makeText(requireContext().applicationContext, it, Toast.LENGTH_SHORT).show()

                postViewModel.errorMessageState.value = null

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val startForProfileImageResult =
        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                     fileUri = data?.data!!

                    binding.postImage.setImageURI(fileUri)
                    binding.postImage.visibility = View.VISIBLE
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireView().context,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        requireView().context,
                        "Image task cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


}