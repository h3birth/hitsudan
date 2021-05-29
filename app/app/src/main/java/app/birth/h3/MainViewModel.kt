package app.birth.h3

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import app.birth.h3.repository.SharePreferenceRepository
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        val spf: SharePreferenceRepository
) : ViewModel() {



}
