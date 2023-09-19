package by.nowo.autoschoolapp.retrofit

import by.nowo.autoschoolapp.model.DriveSlots
import by.nowo.autoschoolapp.model.User
import by.nowo.autoschoolapp.model.users.ShortUsersInfo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


val baseUrl = "http://192.168.100.18:8080/"
//val baseUrl = "http://nowo.by:8080/"

class TokenInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val requestWithToken = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(requestWithToken)
    }
}



object RetrofitHelper {
    fun getInstance(): Retrofit {

        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbm9ueW0iLCJpYXQiOjE2OTUxNTMxMTUsImV4cCI6MTY5NTE1MzgzNX0.QcgV_7Md6GcI2-W2H1IH8yk4q_MEd5p99KjpkSmfamM"

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(token))
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface UserService {
    @GET("api/users")
    suspend fun getAllUsers(): Response<List<User>>
}

interface DriveSlotService {
    @GET("api/driveslots") // Путь к вашему REST-контроллеру на сервере Spring Boot
    suspend fun getAllDriveSlots(): Response<List<DriveSlots>>
}



interface ShortUserService {
    @GET("api/user/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<ShortUsersInfo>
}

