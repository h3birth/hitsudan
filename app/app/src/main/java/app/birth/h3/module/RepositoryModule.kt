package app.birth.h3.module

import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.AnalyticsRepositoryImpl
import app.birth.h3.repository.AuthRepository
import app.birth.h3.repository.AuthRepositoryImpl
import app.birth.h3.repository.ColorRepository
import app.birth.h3.repository.ColorRepositoryImpl
import app.birth.h3.repository.FunctionsRepository
import app.birth.h3.repository.FunctionsRepositoryImpl
import app.birth.h3.repository.SharePreferenceRepository
import app.birth.h3.repository.SharePreferenceRepositoryImpl
import app.birth.h3.repository.TextToSpeechRepository
import app.birth.h3.repository.TextToSpeechRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindSharePreferenceRepository(impl: SharePreferenceRepositoryImpl): SharePreferenceRepository

    @Binds
    @ViewModelScoped
    abstract fun bindDataRepository(impl: ColorRepositoryImpl): ColorRepository

    @Binds
    @ViewModelScoped
    abstract fun bindAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository

    @Binds
    @ViewModelScoped
    abstract fun bindTextToSpeechRepository(impl: TextToSpeechRepositoryImpl): TextToSpeechRepository

    @Binds
    @ViewModelScoped
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @ViewModelScoped
    abstract fun bindFunctionsRepository(impl: FunctionsRepositoryImpl): FunctionsRepository
}
