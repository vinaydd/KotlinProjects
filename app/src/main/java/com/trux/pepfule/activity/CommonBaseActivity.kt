package com.trux.pepfule.activity

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.trux.pepfule.utills.AppPreference

open class CommonBaseActivity  : AppCompatActivity() {
    var   prefs :AppPreference? =null;
    var isErrorResponse :Boolean? =false
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = AppPreference(this);
        isErrorResponse =  false
    }

   open  fun <T> processResponse(result: T?) {
        isErrorResponse = false
        if (result == null) {
            isErrorResponse = true
            return
        }
    }


    fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    fun noInternetConnection(context: Context): Dialog {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("No Internet Connection")
        builder.setMessage("Please connect to Network")
        builder.setNegativeButton("OK") { dialog, which ->
            val intent = Intent(Settings.ACTION_SETTINGS)
            context.startActivity(intent)
        }
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
        return alertDialog
    }


    fun noGpsEnabledDialog(context: Context): Dialog {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Location Services Not Active")
        builder.setMessage("Please enable Location Services and GPS")
        builder.setNegativeButton("OK") { dialogInterface, i ->
            // Show location settings when the user acknowledges the alert dialog
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        return alertDialog
    }


    fun getUniqueIMEIId(context: Context): String {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling

                return ""
            }
            val imei = telephonyManager.deviceId
            Log.e("imei", "=" + imei!!)
            return if (imei != null && !imei.isEmpty()) {
                imei
            } else {
                android.os.Build.SERIAL
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "not_found"
    }

    fun isOnline(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }
}