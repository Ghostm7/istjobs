package com.example.istjobs.viewmodel // Adjust package name accordingly



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.istjobs.data.UserProfile
import com.example.istjobs.data.repository.UserProfileRepository
import kotlinx.coroutines.launch

class UserProfileViewModel(private val userProfileRepository: UserProfileRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    // Fetch user profile by userId
    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            _userProfile.value = userProfileRepository.getUserProfile(userId)
        }
    }
}

