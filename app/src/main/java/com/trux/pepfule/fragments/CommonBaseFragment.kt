package com.trux.pepfule.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.trux.pepfule.utills.AppPreference

class CommonBaseFragment : Fragment() {
    var   isErrorResponse : Boolean= false
    var  prefs: AppPreference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isErrorResponse = false
        prefs =  AppPreference(this!!.activity!!)
    }
    fun <T> processFragmentResponse(result: T?) {
        isErrorResponse = false
        if (result == null) {
            try {
                isErrorResponse = true
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}