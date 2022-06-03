package com.gyimah.lavori.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gyimah.lavori.listeners.JobListener
import com.gyimah.lavori.models.Job
import com.gyimah.lavori.repositories.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel(), JobListener {

    init {
        jobRepository.setJobListener(this)
    }

    val jobsState = MutableLiveData<MutableList<Job>>()
    val errorMessageState = MutableLiveData<String>()
    val addJobState = MutableLiveData<Int>()


    fun getJobs() {
        jobRepository.getJobs()
    }

    fun storeJob(
        role: String,
        company: String,
        location: String,
        type: String,
    ) {
        when {
            role.trim().isEmpty() -> {
                errorMessageState.postValue("Job title is required")
            }
            company.trim().isEmpty() -> {
                errorMessageState.postValue("Company is required")
            }
            location.trim().isEmpty() -> {
                errorMessageState.postValue("Location is required")
            }
            type.trim().isEmpty() -> {
                errorMessageState.postValue("Workplace type is required")
            }else -> {
            jobRepository.storeJob(
                role = role,
                company = company,
                location = location,
                type = type
            )
            }
        }


    }

    override fun onJobsRetrievedSuccess(jobs: MutableList<Job>) {
        jobsState.postValue(jobs)
    }

    override fun onJobsRetrievedFailure(message: String) {
        errorMessageState.postValue(message)
    }

    override fun onJobAddedSuccess() {
        addJobState.postValue(1)
    }

    override fun onJobAddedFailure(message: String) {
        addJobState.postValue(0)
        errorMessageState.postValue(message)
    }


}