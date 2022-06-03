package com.gyimah.lavori.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gyimah.lavori.R
import com.gyimah.lavori.adapters.JobAdapter.*
import com.gyimah.lavori.models.Job
import com.gyimah.lavori.models.User
import javax.inject.Inject

class JobAdapter @Inject constructor(
    private val application: Application
) : RecyclerView.Adapter<JobViewHolder>() {

    private val jobs: MutableList<Job> = mutableListOf();


    fun setJobs(jobs: List<Job>) {
        this.jobs.clear()
        this.jobs.addAll(jobs)
    }

    fun getJobs() = this.jobs


    class JobViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val company: TextView = itemView.findViewById(R.id.company)
        val role: TextView = itemView.findViewById(R.id.role)
        val location: TextView = itemView.findViewById(R.id.location)
        val postedAt: TextView = itemView.findViewById(R.id.posted_at)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.job_item_row, parent, false))
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]

        holder.company.text = job.company
        holder.role.text = application.getString(R.string.company_type, job.company, job.type)
        holder.location.text = job.location
        holder.postedAt.text = job.postedAt.toString()

    }

    override fun getItemCount(): Int {
        return jobs.size
    }
}