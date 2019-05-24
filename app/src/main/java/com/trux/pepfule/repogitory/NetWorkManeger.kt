package com.trux.pepfule.repogitory

import android.app.ProgressDialog
import android.content.Context
import android.content.SyncRequest
import android.util.Log
import android.widget.Toast
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.trux.pepfule.activity.CommonBaseActivity
import com.trux.pepfule.fragments.CommonBaseFragment
import com.trux.pepfule.utills.AppPreference
import com.trux.pepfule.utills.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class NetworkManager<T> constructor(
    context: Context,
    hostUrl: String,
    responseType: Class<T>,
    pageUrls: String,
    mode: String,
    obj: JSONObject,
    isProgress: Boolean
) : CommonBaseActivity() {

    var messageae: String = "Loading..."
    var _context = context
    var hostUrl = hostUrl
    var _responseType = responseType
    var pageUrl = pageUrls
    var isToShowProgress = isProgress
    var mode = mode
    var objects = obj
    var isApiRequired: Boolean = true
    var isForFragmen: Boolean = true
    var apiKey: String? = null
    var _dialog: ProgressDialog? = null;
    var result: T? = null
    lateinit var mFragment: CommonBaseFragment
    var  prefsc  : AppPreference =  AppPreference(context)

    lateinit var mapper: ObjectMapper

    init {
        mapper =  ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        apiKey = prefsc .getStringValueForTag(Constants.USER_NAME)
        if (isToShowProgress) {
            _dialog = ProgressDialog.show(_context, "", messageae, true)
        }
        if (obj != null && obj.length()>0) {
            Log.d("object", objects.toString())
            someMethodName(objects, pageUrl)
        } else {
            CallWithNullBody(pageUrl)

        }

    }

    fun someMethodName(`object`: JSONObject, url: String) {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val ongoing = chain.request().newBuilder()
                ongoing.addHeader("Accept", "application/json;versions=1")
                if (isApiRequired) {
                    ongoing.addHeader("authkey", apiKey)
                }
                chain.proceed(ongoing.build())
            }
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.HOST_URL)
            .client(httpClient)
            .build()

        val fdtgh = retrofit.create(ApiCalls::class.java)
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), `object`.toString())
        val requestCall: Call<ResponseBody>
        if (mode.equals("1", ignoreCase = true)) {
            requestCall = fdtgh.someCallGet(url, body)
        } else {
            requestCall = fdtgh.someCallPost(url, body)
        }
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, rawResponse: retrofit2.Response<ResponseBody>) {
                try {
                    val response = rawResponse.body().string()
                    Log.d("Responce_data", response)
                    if (_dialog != null) {
                        _dialog!!.cancel()
                    }
                   result = Gson().fromJson(response, _responseType)

                  //  result = mapper.readValue(response.toString(), _responseType)

                    if (isFragment()) {
                        mFragment.processFragmentResponse(result)
                    } else {
                        (_context as CommonBaseActivity).processResponse(result)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                if (_dialog != null) {
                    _dialog!!.cancel()
                }
                Toast.makeText(_context, throwable.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun CallWithNullBody(url: String) {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val ongoing = chain.request().newBuilder()
                ongoing.addHeader("Accept", "application/json; charset=utf-8")
                if (isApiRequired) {
                    ongoing.addHeader("authKey", apiKey)
                    // ongoing.addHeader("authKey", "8tnCXuGDwSeWGmXbGDxssW8bcKaBvTBO18V5uXHjJoguwMVpZxHEW0o0ZA4VG3rR");
                }
                chain.proceed(ongoing.build())
            }
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.HOST_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiCalls = retrofit.create(ApiCalls::class.java!!)
        val requestCall: Call<*>
        if (mode.equals("1", ignoreCase = true)) {
            requestCall = apiCalls.someCallGetNoBody(url)
        } else {
            requestCall = apiCalls.someCallPostNoBody(url)
        }
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                try {
                    val dre = response.body().string().toString()
                    Log.d("Responce_data", dre)
                    if (_dialog != null) {
                        _dialog!!.cancel()
                    }
                    result = Gson().fromJson<T>(dre, _responseType)
                   // result = mapper.readValue(dre, _responseType)

                    if (isFragment()) {
                        mFragment.processFragmentResponse(result)
                    } else {
                        (_context as CommonBaseActivity).processResponse(result)
                    }

                } catch (e: Exception) {
                    if (_dialog != null) {
                        _dialog!!.cancel()
                    }
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                if (_dialog != null) {
                    _dialog!!.cancel()
                }
                val response = throwable.message
            }
        })

    }

    fun setIsApiKeyRequired(isReqoured: Boolean) {
        isApiRequired = isReqoured
    }

    fun isFragment(): Boolean {
        return isForFragmen
    }

    internal fun setFragment(fragment: CommonBaseFragment) {
        mFragment = fragment
    }

    fun setForFragment(isFragment: Boolean) {
        this.isForFragmen = isFragment
    }

    fun setMessage(loadingMessage: String) {
        messageae = loadingMessage
    }

    fun setShowProgress(isToShowProgress: Boolean) {
        this.isToShowProgress = isToShowProgress
    }


}
