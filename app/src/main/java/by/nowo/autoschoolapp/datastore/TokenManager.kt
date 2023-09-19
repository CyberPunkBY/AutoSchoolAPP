package by.nowo.autoschoolapp.datastore

import android.content.Context
import by.nowo.autoschoolapp.model.auth.LoginRequest
import by.nowo.autoschoolapp.model.auth.LoginResponse
import com.auth0.android.jwt.JWT
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class TokenManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)


    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun clearToken() {
        sharedPreferences.edit().remove("token").apply()
    }

    fun isTokenExpired(): Boolean {
        val token = getToken() ?: return true
        val jwt = JWT(token)
        val expirationTime = jwt.expiresAt?.time ?: return true
        return System.currentTimeMillis() >= expirationTime
    }
}



class ApiService(private val tokenManager: TokenManager) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.100.18:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: YourApi = retrofit.create(YourApi::class.java)

    suspend fun login(username: String, password: String): Response<LoginResponse> {
        val response = api.login(LoginRequest(username, password))
        if (response.isSuccessful) {
            val token = response.body()?.token
            if (token != null) {
                tokenManager.saveToken(token)
            }
        }
        return response
    }



    // Другие методы для выполнения запросов к серверу
}

interface YourApi {

    @POST("auth/authenticate") // Замените "login" на путь к вашему эндпоинту на сервере
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Другие методы для выполнения запросов к серверу
}