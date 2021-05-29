package app.birth.h3.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharePreferenceRepositoryImpl @Inject constructor(
        @ApplicationContext val context: Context
): SharePreferenceRepository {
}
