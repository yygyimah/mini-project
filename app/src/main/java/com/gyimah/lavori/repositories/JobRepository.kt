package com.gyimah.lavori.repositories

import android.app.Application
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.gyimah.lavori.constants.Constants
import com.gyimah.lavori.listeners.JobListener
import com.gyimah.lavori.models.Job
import com.gyimah.lavori.models.Post
import com.gyimah.lavori.utils.Session
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import javax.inject.Inject


class JobRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val application: Application,
    private val simpleDateFormat: SimpleDateFormat
) {

    private lateinit var jobListener: JobListener

    private var session: Session = Session.getInstance(application.applicationContext)

    fun setJobListener(jobListener: JobListener) {
        this.jobListener = jobListener
    }

    fun getJobs() {
        val jobs = mutableListOf<Job>()
        firebaseFirestore.collection(Constants.JOBS)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    jobListener.onJobsRetrievedFailure(e.localizedMessage!!)
                }

                if (snapshot != null) {

                    jobs.clear()

                    for (item in snapshot.documents) {

                        val job = item.toObject<Job>()

                        Log.i("JOB", job.toString())

                        if (job != null) {
                            jobs.add(job)
                        }

                    }
                }
                jobListener.onJobsRetrievedSuccess(jobs)

            }
    }

    fun storeJob(
        role: String,
        company: String,
        location: String,
        type: String,
    ) {
        val key = firebaseFirestore.collection(Constants.JOBS).document().id

        val user = session.getUser()

        val job = Job(
            id = key,
            role = role,
            company = company,
            location = location,
            type = type,
            user = user!!,
            postedAt = simpleDateFormat.format(System.currentTimeMillis())
        )

        firebaseFirestore.collection(Constants.JOBS)
            .document(key)
            .set(job)
            .addOnSuccessListener {
                jobListener.onJobAddedSuccess()
            }
            .addOnFailureListener {
                jobListener.onJobAddedFailure(it.localizedMessage!!)
            }
    }
}