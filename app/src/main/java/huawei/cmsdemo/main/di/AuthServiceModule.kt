package huawei.cmsdemo.main.di

import android.content.Context
import com.hms.lib.commonmobileservices.auth.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthServiceModule {
    @Singleton
    @Provides
    fun injectAuthService(@ApplicationContext context: Context) = AuthService.Factory.create(context)
}
