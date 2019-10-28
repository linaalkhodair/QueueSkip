package com.example.queueskip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import static com.example.queueskip.ui.dashboard.DashboardFragment.totalAmount;


public class CheckoutActivity extends AppCompatActivity {

    //Paypal  request code
    public static final int PAYPAL_REQUEST_CODE = 123;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration() // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID)
            .merchantName("TechNxt Code Labs")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"))
            ;  // or live (ENVIRONMENT_PRODUCTION)

    Button subscribe;
    private String paymentAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

//        subscribe = (Button)findViewById(R.id.subscribe);
//        subscribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                subscribe();
//            }
//        });
        getPayment();
    }

    /*private void subscribe() {

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View layout= null;
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.subscribe_popup, null);

        RadioGroup radioGroup = (RadioGroup)layout.findViewById(R.id.radioGroup);
        RadioButton radioButton1 = (RadioButton) layout.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = (RadioButton)layout.findViewById(R.id.radioButton2);
        RadioButton radioButton3 = (RadioButton)layout.findViewById(R.id.radioButton3);

        Button conti = (Button)layout.findViewById(R.id.conti);
        Button cancel = (Button)layout.findViewById(R.id.cancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });



        //Getting the amount from Radio Button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton1:
                        // do operations specific to this selection
                        paymentAmount = "1";
                        break;
                    case R.id.radioButton2:
                        // do operations specific to this selection
                        paymentAmount = "2";
                        break;
                    case R.id.radioButton3:
                        // do operations specific to this selection
                        paymentAmount = "3";
                        break;
                }
            }
        });



        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                getPayment();

            }
        });

        dialog.setTitle("Subscribe with Us");
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.show();
    }
*/
    private void getPayment() {



        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new
                BigDecimal(totalAmount /3.75),"USD","Total Amount:",PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //invoice number
        //payment.invoiceNumber("321"); //deleted because of duplicated...

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                Log.d("CONFIRM", String.valueOf(confirm));
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.d("paymentExample", paymentDetails);
                        Log.i("paymentExample", paymentDetails);
                        Log.d("Pay Confirm : ", String.valueOf(confirm.getPayment().toJSONObject()));
//                        Starting a new activity for the payment details and status to show

                        startActivity(new Intent(CheckoutActivity.this, SuccessActivity.class)
                                .putExtra("PaymentDetails", paymentDetails));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred : ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        totalAmount=0;
        super.onDestroy();
    }
}
