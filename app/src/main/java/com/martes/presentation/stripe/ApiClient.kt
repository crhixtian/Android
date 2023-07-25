package com.martes.presentation.stripe

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ApiClient {
    private val httpClient = OkHttpClient()

    fun createPaymentIntent(
        paymentMethodType: String,
        currency: String,
        completion: (paymentIntentClientSecret: String?, error: String?) -> Unit
    ){
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestJson = """
        {
            "currency""$currency"
            "paymentMethodType":"$paymentMethodType"
        }
        """.trimIndent()
        val body = requestJson.toRequestBody(mediaType)
        val request = Request.Builder()
            .url("localhost:4242" + "create-payment-intent")
            .post(body)
            .build()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                completion(null, "$e")
            }

            override fun onResponse(call: Call, response: Response) {
                if(!response.isSuccessful){
                    completion(null, "$response")
                } else {
                    val responseData = response.body?.string()
                    val responseJson = responseData?.let{
                        JSONObject(it)
                    }?: JSONObject()
                    val paymentIntentClientSecret: String = responseJson.getString("clientSecret")
                    completion(paymentIntentClientSecret, null)
                }
            }
        })

    }
}