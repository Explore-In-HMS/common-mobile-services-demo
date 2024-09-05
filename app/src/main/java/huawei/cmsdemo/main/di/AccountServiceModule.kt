package huawei.cmsdemo.main.di

import android.content.Context
import com.hms.lib.commonmobileservices.account.AccountService
import com.hms.lib.commonmobileservices.account.SignInParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountServiceModule {
    @Singleton
    @Provides
    fun injectAccountService(@ApplicationContext context: Context) = AccountService.Factory.create(
        context = context,
        signInParams = SignInParams.Builder()
            .requestEmail()
            .create()
    )
}