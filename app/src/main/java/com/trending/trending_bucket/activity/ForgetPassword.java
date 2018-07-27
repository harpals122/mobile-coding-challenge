package com.trending.trending_bucket.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.trending.trending.R;
import com.trending.trending_bucket.model.User;
import com.trending.trending_bucket.util.FloatLabel;
import com.trending.trending_bucket.util.GetUserCallBack;
import com.trending.trending_bucket.util.ServerRequest2;
import com.trending.trending_bucket.util.utilMethods;
import com.trending.trending_bucket.util.utilMethods.InternetConnectionListener;
import java.util.Random;
import static com.trending.trending_bucket.util.constants.JF_CONTACT_NUMBER;
import static com.trending.trending_bucket.util.utilMethods.isConnectedToInternet;
import static com.trending.trending_bucket.util.Validator.isInputted;
import static com.trending.trending_bucket.util.utilMethods.savePreference;
import static com.trending.trending_bucket.util.Validator.isMobileNumberValid;
import static com.trending.trending_bucket.util.Validator.setPhoneCodeListener;

/**
 * @author prabh kaur
 * @class ForgetPassword
 * @brief Activity for sending the password to the user in case of forget
 */

public class ForgetPassword extends Activity implements View.OnClickListener, InternetConnectionListener {

    private final int FORGET_PASSWORD_ACTION = 1;
    private FloatLabel etMobileNumber;
    private AlertDialog resetDialog = null;
    private boolean isUserCanceled = false;
    private int code;
    private InternetConnectionListener internetConnectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        findViewById(R.id.btnReset).setOnClickListener(this);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        etMobileNumber = (FloatLabel) findViewById(R.id.etMobileNumber);
        etMobileNumber.getEditText().setTextColor(getResources().getColor(R.color.post_business_edit_text_color));
        etMobileNumber.getEditText().setOnFocusChangeListener(setPhoneCodeListener(this));
        etMobileNumber.getEditText().setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    if (etMobileNumber.getEditText().getText().length() <=
                            getResources().getText(R.string.mobile_country_code).length()) {
                        return true;
                    }
                }
                return false;
            }
        });


        ActivityCompat.requestPermissions(ForgetPassword.this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                1);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnReset:
                if (isInputValid())
                {
                    if (isConnectedToInternet(this))
                    {
                        doConfirmNumber(etMobileNumber.getEditText().getText().toString());

                    }
                    else
                        {
                        internetConnectionListener = ForgetPassword.this;
                        utilMethods.showNoInternetDialog(ForgetPassword.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                            getResources().getString(R.string.no_internet_text),
                            getResources().getString(R.string.retry_string),
                            getResources().getString(R.string.cancel_string), FORGET_PASSWORD_ACTION);
                    }
                }
            break;

            case R.id.crossImgView:
                isUserCanceled = true;
                onPause();
            break;
        }
    }

    private boolean isInputValid()
    {
        if (!isInputted(this, etMobileNumber)) {
            return false;
        }
        if (!isMobileNumberValid(this, etMobileNumber)) {
            return false;
        }
         return true;
    }

    public void showVerificationConfirmDialog(final Context context, String headline, String body,
                                              String positiveString)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_dialog, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(headline);
        ((TextView) view.findViewById(R.id.bodyTV)).setText(body);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton(positiveString, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which) {
                    Random rnd = new Random();
                    code = 100000 + rnd.nextInt(900000);
                    doRequestForPassword(code);
                    dialog.dismiss();
                    isUserCanceled = true;
                    Intent intent = new Intent(ForgetPassword.this, VerifySMSCode.class);
                    intent.putExtra("verify", Integer.toString(code));
                    startActivity(intent);
                    onPause();
                }
            }).setView(view).setCancelable(false);
        resetDialog = builder.create();
        resetDialog.show();
    }

    private void doRequestForPassword(int code)
    {
        String phone =  etMobileNumber.getEditText().getText().toString();
        savePreference(ForgetPassword.this, JF_CONTACT_NUMBER, phone);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, "Your Trending Bucket verification code is : " + Integer.toString(code), null, null);

    }

    private void doConfirmNumber(String phone) {
        User user = new User();
        user.setPhoneNumber(phone);
        authenticate(user);
    }

    private void authenticate(User user){
        ServerRequest2 serverRequest = new ServerRequest2(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallBack()
        {
            @Override
            public void done(User returnedUser)
            {
                 try
                 {
                    showVerificationConfirmDialog(ForgetPassword.this,
                    getResources().getString(R.string.password_reset_heading),
                    getResources().getString(R.string.password_reset_body2),
                    getResources().getString(R.string.continue_string));
                 }
                 catch (NullPointerException ex)
                 {
                     showErrorMessage();
                 }
            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(ForgetPassword.this);
        dialogbuilder.setMessage("This number is not registered");
        dialogbuilder.setPositiveButton("ok", null);
        dialogbuilder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isUserCanceled) {
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
        }
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == FORGET_PASSWORD_ACTION) {
            showVerificationConfirmDialog(ForgetPassword.this,
                    getResources().getString(R.string.password_reset_heading),
                    getResources().getString(R.string.password_reset_body),
                    getResources().getString(R.string.continue_string));
        }
    }

    @Override
    public void onUserCanceled(int code) {
        isUserCanceled = true;
        onPause();
    }
}
