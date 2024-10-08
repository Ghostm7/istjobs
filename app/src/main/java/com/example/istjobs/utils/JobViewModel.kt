package com.example.istjobs.utils

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.istjobs.data.Job



class JobViewModel : ViewModel() {
    // List of jobs to be observed
    private val _jobs = mutableStateListOf<Job>()
    val jobs: List<Job> get() = _jobs

    // Function to add a job
    fun addJob(job: Job) {
        _jobs.add(job)
    }

    // Function to get a job by ID
    fun getJobById(jobId: String): Job? {
        return _jobs.find { it.id == jobId }
    }

    // Function to update an existing job
    fun updateJob(updatedJob: Job) {
        val index = _jobs.indexOfFirst { it.id == updatedJob.id }
        if (index != -1) {
            _jobs[index] = updatedJob
        }
    }

    // Function to remove a job
    fun removeJob(jobId: String) {
        _jobs.removeAll { it.id == jobId }
    }
}
