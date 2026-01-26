package co.id.rezha.mycrud.views.payungin.data.networks

import android.content.Context
import android.os.Build
import co.id.rezha.core.helpers.Consts
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object RetrofitPayungin {

    fun gson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setLenient()
        .create()

    private fun headerInterceptor(context: Context) = Interceptor { chain ->
        val req = chain.request()
        val newReq = req.newBuilder()
            .header("Content-Type", "application/json")
            .header("X-Custom-Response", "mobile")
            .header("App-Build-Number", "68")
            .header("App-Version", "2.0.0-dev")
            .header("OS", "Android")
            .header("OS-Version", Build.VERSION.RELEASE)
            .header("Device-Model", "${Build.MANUFACTURER} ${Build.MODEL}")
            .header("Device-Time", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).format(
                Date()
            ))
            .header("Device-Timezone", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
            .header("Provider", "wifi") // atau ambil dari connectivity manager
            // .addHeader("Authorization", "Bearer ${TokenStore.token}") // kalau nanti pakai token
            .build()
        chain.proceed(newReq)
    }

    fun getRetrofit(context: Context): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor(context))
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Consts.BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .client(okHttp)
            .build()
    }
}