package nz.co.test.transactions.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.co.test.transactions.data.repository.TransactionsRepository
import nz.co.test.transactions.data.repository.TransactionsRepositoryImpl
import nz.co.test.transactions.data.services.TransactionsService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkModule.NetworkModuleAbstract::class])
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/Josh-Ng/500f2716604dc1e8e2a3c6d31ad01830/raw/4d73acaa7caa1167676445c922835554c5572e82/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesOkhttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val newRequest = chain.request().newBuilder()
                    .build()
                chain.proceed(newRequest)
            }
        return builder.build()
    }

    @Singleton
    @Provides
    fun providesTransactionsService(retrofit: Retrofit): TransactionsService = retrofit.create(TransactionsService::class.java)


    @Module
    @InstallIn(SingletonComponent::class)
    interface NetworkModuleAbstract {
        @Singleton
        @Binds
        fun bindTransactionsRepository(repo: TransactionsRepositoryImpl): TransactionsRepository
    }
}