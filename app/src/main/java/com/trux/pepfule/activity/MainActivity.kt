package com.trux.pepfule.activity

import android.os.Bundle
import android.widget.Button
import com.trux.pepfule.R
import com.trux.pepfule.model.DataModel
import com.trux.pepfule.response.BaseResponse
import com.trux.pepfule.repogitory.WebcallMaegerClass
import com.trux.pepfule.response.GetCountryIsdResponse
import com.trux.pepfule.utills.Constants
import org.json.JSONObject

class MainActivity : CommonBaseActivity() {
       private  lateinit var   text : Button;
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs!!.setStringValueForTag(Constants.USER_NAME,"nn");
        var  name :String  =  prefs!!.getStringValueForTag(Constants.USER_NAME)
        text =  findViewById(R.id.text)
        text.setOnClickListener({
          var   pbj : JSONObject  = JSONObject()
            WebcallMaegerClass.getCountry(this, Constants.GET_REQUEST, pbj, true);
        })
    }
   override fun  <T> processResponse(result: T?) {
        super.processResponse(result)
          if (result is GetCountryIsdResponse) {
              val res = result as GetCountryIsdResponse
              var list  :  ArrayList<DataModel> =  res.data
          }
      }
}
