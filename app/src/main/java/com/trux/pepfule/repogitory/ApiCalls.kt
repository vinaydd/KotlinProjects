package com.trux.pepfule.repogitory

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiCalls {
    @POST
    abstract fun someCallPost(@Url url: String, @Body params: RequestBody): Call<ResponseBody>
    @GET
    abstract fun someCallGet(@Url url: String, @Body params: RequestBody): Call<ResponseBody>
    @POST
    abstract fun someCallPostNoBody(@Url url: String): Call<ResponseBody>
    @GET
    abstract fun someCallGetNoBody(@Url url: String): Call<ResponseBody>
}