package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.xcrino.mcqproject.BuildConfig;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.models.Data;
import com.xcrino.mcqproject.utils.AppPreferences;

import java.util.List;

public class ReferAndEarnActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView toolbar_title;
    private ImageView back_arrow;
    private CardView facebookLayout, whatsappLinearLayout, msgLinearLayout, emailLinearLayout, shareMoreLayout;
    private TextView referral_code, dollers_ruppees;
    private Button copy_referral_code;

    private char it;
    private String msg, referralUinqueCode;
    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private Context mContext;

    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        getUiLayoutInit();

        toolbar_title.setText("Refer and Earn");
        mContext = this;

        appPreferences = new AppPreferences(this);
        referral_code.setText(appPreferences.getReferCode());
        referralUinqueCode = appPreferences.getReferCode();

        back_arrow.setOnClickListener(this);
        facebookLayout.setOnClickListener(this);
        whatsappLinearLayout.setOnClickListener(this);
        msgLinearLayout.setOnClickListener(this);
        emailLinearLayout.setOnClickListener(this);
        shareMoreLayout.setOnClickListener(this);
        copy_referral_code.setOnClickListener(this);

        if (appPreferences.getCountry().equals("India")) {
            dollers_ruppees.setText("Get your free ₹70 Credits");
        } else {
            dollers_ruppees.setText("Get your free $1 Credits");
        }

        referral_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied successfully", referralUinqueCode);
                clipboard.setPrimaryClip(clip);
//                Toast.makeText(mContext, "copied successfully", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(v, "copied successfully", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return true;
            }
        });
    }

    private void getUiLayoutInit() {
        dollers_ruppees = (TextView) findViewById(R.id.dollers_ruppees);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        facebookLayout = (CardView) findViewById(R.id.facebookLayout);
        whatsappLinearLayout = (CardView) findViewById(R.id.whatsappLinearLayout);
        msgLinearLayout = (CardView) findViewById(R.id.msgLinearLayout);
        emailLinearLayout = (CardView) findViewById(R.id.emailLinearLayout);
        shareMoreLayout = (CardView) findViewById(R.id.shareMoreLayout);
        referral_code = (TextView) findViewById(R.id.referral_code);
        copy_referral_code = (Button) findViewById(R.id.copy_referral_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow:
                onBackPressed();
                break;

            case R.id.facebookLayout:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                if (appPreferences.getCountry().equals("India")) {
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello Dear Friends.\n" +
                            "I have earned Rs. 15000 by QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                            "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free ₹70 Credits \n" +
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);

                } else {
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello Dear Friends.\n" +
                            "I have earned $200 By QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                            "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free $1 Credits \n" +
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                }

                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "Please install facebook messenger.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.whatsappLinearLayout:
                try {
                    Intent whatsAppIntent = new Intent(Intent.ACTION_SEND);
                    whatsAppIntent.setType("text/plain");
                    whatsAppIntent.setPackage("com.whatsapp");
                    if (appPreferences.getCountry().equals("India")) {
                        whatsAppIntent.putExtra(Intent.EXTRA_TEXT, "Hello Dear Friends.\n" +
                                "I have earned Rs. 15000 by QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                                        "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free ₹70 Credits \n" +
                                        "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);

                    } else {
                        whatsAppIntent.putExtra(Intent.EXTRA_TEXT, "Hello Dear Friends.\n" +
                                "I have earned $200 By QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                                "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free $1 Credits \n" +
                                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                    }
                    startActivity(whatsAppIntent);
                } catch (ActivityNotFoundException anfe) {
                    anfe.getMessage();
                    Toast.makeText(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.msgLinearLayout:
                try {
                    String m;
                    if (appPreferences.getCountry().equals("India")) {
                        m ="Hello Dear Friends.\n" +
                                "I have earned Rs. 15000 by QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                                "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free ₹70 Credits \n" +
                                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

                    } else {
                        m = "Hello Dear Friends.\n" +
                                "I have earned $200 By QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                                "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free $1 Credits \n" +
                                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    }
                    Intent sendIntent1 = new Intent(Intent.ACTION_VIEW);
                    sendIntent1.setData(Uri.parse("sms:"));
                    sendIntent1.putExtra("sms_body", m);
                    startActivity(sendIntent1);
                } catch (ActivityNotFoundException ant) {
                    ant.getMessage();

                }
                break;

            case R.id.emailLinearLayout:
                if (appPreferences.getCountry().equals("India")) {
                    msg = "Hello Dear Friends.\n" +
                            "I have earned Rs. 15000 by QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                            "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free ₹70 Credits \n" +
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

                } else {
                    msg = "Hello Dear Friends.\n" +
                            "I have earned $200 By QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                            "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free $1 Credits \n" +
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Refer And Earn");
                    intent.putExtra(Intent.EXTRA_TEXT, msg);
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));

                } catch (ActivityNotFoundException ant) {
                    ant.getMessage();
                }
                break;

            case R.id.shareMoreLayout:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage;
                    if (appPreferences.getCountry().equals("India")) {
                        shareMessage = "Hello Dear Friends.\n" +
                                "I have earned Rs. 15000 by QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                                "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free ₹70 Credits \n" +
                                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

                    } else {
                        shareMessage = "Hello Dear Friends.\n" +
                                "I have earned $200 By QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                                "Quick Download from below link and enter Referral code " + appPreferences.getReferCode() + " to get free $1 Credits \n" +
                                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    }

//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n" + "Use the Referral Code: " + appPreferences.getReferCode();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.getMessage();
                }
                break;

            case R.id.copy_referral_code:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Successfully", referralUinqueCode);
                clipboard.setPrimaryClip(clip);
//                Toast.makeText(mContext, "copied successfully", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(v, "Copied Successfully", Snackbar.LENGTH_SHORT);
                snackbar.show();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}