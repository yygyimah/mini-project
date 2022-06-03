package com.gyimah.lavori.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gyimah.lavori.R
import com.gyimah.lavori.databinding.ActivityMainBinding
import com.gyimah.lavori.databinding.ActivityPostJobBinding
import com.gyimah.lavori.utils.AppUtils
import com.gyimah.lavori.viewmodels.JobViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostJobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostJobBinding

    private val jobViewModel: JobViewModel by viewModels()

    private var  workType: String? = null

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = getString(R.string.post_job)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        dialog = AppUtils.createLoadingDialog(this, "Loading, please wait...")

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.workplaceType.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                workType = p0!!.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

        }

        binding.addJobBtn.setOnClickListener {

            dialog.show()

            jobViewModel.storeJob(
                role = binding.jobTitleLayout.editText?.text.toString(),
                company = binding.companyLayout.editText?.text.toString(),
                location = binding.locationLayout.editText?.text.toString(),
                type = workType!!
            )
        }

        jobViewModel.addJobState.observe(this) {
            dialog.dismiss()

            if (it != null) {
                Toast.makeText(this@PostJobActivity, "Job posted", Toast.LENGTH_SHORT).show()

                jobViewModel.addJobState.value = null

                finish()
            }

        }

        jobViewModel.errorMessageState.observe(this) {
            dialog.dismiss()

            if(it != null) {
                Toast.makeText(this@PostJobActivity, it, Toast.LENGTH_SHORT).show()

                jobViewModel.errorMessageState.value = null
            }


        }




    }
}