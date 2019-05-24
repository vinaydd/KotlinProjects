package com.trux.pepfule.response

import com.trux.pepfule.model.DataModel

data  class GetCountryIsdResponse(var responseCode: String, var responseMessag : String, var data :ArrayList<DataModel>) {
}

