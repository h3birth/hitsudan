package app.birth.h3.module

import app.birth.h3.repository.ColorRepository
import app.birth.h3.repository.ColorRepositoryImpl
import app.birth.h3.repository.SharePreferenceRepository
import app.birth.h3.repository.SharePreferenceRepositoryImpl
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
}
