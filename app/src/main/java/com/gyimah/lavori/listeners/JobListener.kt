package com.gyimah.lavori.listeners

import com.gyimah.lavori.models.Job

interface JobListener {

    fun onJobsRetrievedSuccess(jobs: MutableList<Job>)

    fun onJobsRetrievedFailure(message: String)

    fun onJobAddedSuccess()

    fun onJobAddedFailure(message: String)
}