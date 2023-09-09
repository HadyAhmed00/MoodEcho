package io.github.moodEcho.ServerHandler
import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
var TAG = "RESPON1"
class ApiClient {
    private val BASE_URL = "https://ourlegitimateadmin.haniasu.repl.co/" // Replace with your server's URL

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    // Define the Retrofit interface for your API
    interface ApiService {
        @Multipart
        @POST("Check") // Replace with the appropriate endpoint
        fun uploadWavFile(@Part file: MultipartBody.Part): Call<ResponseBody>
    }

    // Function to upload a .wav file to the server
    fun uploadWavFile(wavFileByteArray: ByteArray, callback: (Boolean, String?) -> Unit) {
        Log.i(TAG, "uploadWavFile: start up lode")
        val requestBody = RequestBody.create(MediaType.parse("audio/wav"), wavFileByteArray)
        val filePart = MultipartBody.Part.createFormData("voice_message", "voice_message.wav", requestBody)

        val call = apiService.uploadWavFile(filePart)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val serverResponse = response.body()?.string()
                    // Handle the successful response here

                    Log.i(TAG, "onResponse: ${serverResponse}")
                    callback(true, serverResponse)
                } else {
                    // Handle the error

                    Log.i(TAG, "onResponse: ${response.code()}")
                    callback(false, "Server returned error: ${response.code()}")

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle the network error
                callback(false, "Network error: ${t.message}")
            }
        })
    }

}

