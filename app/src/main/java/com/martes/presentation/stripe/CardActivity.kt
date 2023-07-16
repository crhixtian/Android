package com.martes.presentation.stripe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martes.databinding.ActivityCardBinding
import com.stripe.android.Stripe

class CardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardBinding
    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //stripe = Stripe(this, PaymentConfiguration
          //  .getInstance(applicationContext).publishableKey)
        //startCheckout()
    }
  /*  private fun displayAlert(title: String, message: String){
            runOnUiThread {
                val builder = AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                builder.setPositiveButton("Ok", null)
                builder.create().show()
            }
    }

    private fun startCheckout(){
        ApiClient().createPaymentIntent("card", "usd", completion = {
            paymentIntentClientSecret, error ->
            run{
                paymentIntentClientSecret?.let {
                    this.paymentIntentClientSecret = it
                }
                error?.let {
                    displayAlert("Failed", "Error:$error")
                }
            }
        })
        binding.payButton.setOnClickListener {
            binding.cardInputWidget.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params,paymentIntentClientSecret)
                stripe.confirmPayment(this, confirmParams)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult>{
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                if(paymentIntent.status == StripeIntent.Status.Succeeded){
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    displayAlert("Payment", gson.toJson(paymentIntent))
                } else if(paymentIntent.status == StripeIntent.Status.RequiresPaymentMethod){
                    displayAlert("Failed", paymentIntent.lastPaymentError?.message.orEmpty())
                }
            }

            override fun onError(e: Exception) {
                displayAlert("Error",e.toString())
            }
        })
    }*/
}