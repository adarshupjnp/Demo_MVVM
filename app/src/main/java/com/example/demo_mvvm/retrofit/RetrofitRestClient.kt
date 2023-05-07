package com.example.demo_mvvm.retrofit

import com.example.demo_mvvm.interfaces.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.crypto.NoSuchPaddingException

object RetrofitRestClient {
    private const val TIME = 80

    private var httpClient: OkHttpClient? = null
        get() {

            //Encryption Interceptor
            var encryptionInterceptor: EncryptionInterceptor? = null

            //Decryption Interceptor
            var decryptionInterceptor: DecryptionInterceptor? = null

            try {
                encryptionInterceptor = EncryptionInterceptor()
                decryptionInterceptor = DecryptionInterceptor()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            }

            field = OkHttpClient().newBuilder()
                .connectTimeout(TIME.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME.toLong(), TimeUnit.SECONDS)

                /* .addNetworkInterceptor(Interceptor { chain ->
                     val original = chain.request()
                     val requestBuilder = original.newBuilder()
                     val request = requestBuilder.build()
                     chain.proceed(request)
                 })*/

                .addInterceptor(encryptionInterceptor!!)
                .addInterceptor(decryptionInterceptor!!)
                .build()
            return field
        }

    private val retrofit = Retrofit.Builder()
        .baseUrl(RestConstant.BASE_URLS)
        .addConverterFactory(ToStringConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient!!)
        .build()
        .create(ApiService::class.java)

    fun getInstance(): ApiService {
        return retrofit
    }
}