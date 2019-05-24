package com.trux.pepfule.repogitory

import android.content.Context
import com.trux.pepfule.response.BaseResponse
import com.trux.pepfule.response.GetCountryIsdResponse
import com.trux.pepfule.utills.Constants
import org.json.JSONObject

object WebcallMaegerClass{

     fun getCountry(context: Context,  mode: String,obj:JSONObject,isProgress:Boolean) {
               var networkManager =  NetworkManager<GetCountryIsdResponse>(context,Constants.HOST_URL,
                   GetCountryIsdResponse::class.java,Constants.GET_COUNTRY_URL,mode,obj,isProgress)
               networkManager.setIsApiKeyRequired(true)
               networkManager.setForFragment(false)
               networkManager.setShowProgress(isProgress)
      }

}