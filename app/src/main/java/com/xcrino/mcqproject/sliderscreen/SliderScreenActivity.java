package com.xcrino.mcqproject.sliderscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.activity.LoginActivity;
import com.xcrino.mcqproject.adapter.IntroScreenViewPagerAdapter;
import com.xcrino.mcqproject.utils.AppPreferences;

public class SliderScreenActivity extends AppCompatActivity {

    AppPreferences mAppPreferences;
    Context mContext;
    TextView[] introBullets;
    int[] introSliderLayouts;
    ViewPager mSlider_viewpager;
    LinearLayout mSlider_dots;
    IntroScreenViewPagerAdapter mIntroScreenViewPagerAdapter;
    Button mSkip_button, mNext_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_screen);

        mContext = this;
        mAppPreferences = new AppPreferences(mContext);

        if (!mAppPreferences.getIsFirstTimeLaunch()){
            applicationStartup();
            finish();
        }

        mSlider_viewpager = findViewById(R.id.slider_viewpager);
        mSlider_dots = findViewById(R.id.slider_dots);
        mSkip_button = findViewById(R.id.skip_button);
        mNext_button = findViewById(R.id.next_button);
        mSlider_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                makeIntroBullets(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        introSliderLayouts = new int[]{R.layout.slider_one, R.layout.slider_two, R.layout.slider_three, R.layout.slider_four};
        makeIntroBullets(0);

        mIntroScreenViewPagerAdapter = new IntroScreenViewPagerAdapter(mContext, introSliderLayouts);
        mSlider_viewpager.setAdapter(mIntroScreenViewPagerAdapter);

        mSkip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applicationStartup();
            }
        });

        mNext_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < introSliderLayouts.length){
                    mSlider_viewpager.setCurrentItem(current);
                }
                else {
                    applicationStartup();
                }
            }
        });
    }

    private void makeIntroBullets(int currentPage){
        int arraySize = introSliderLayouts.length;
        introBullets = new TextView[arraySize];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        mSlider_dots.removeAllViews();
        for (int i=0; i< arraySize; i++) {
            introBullets[i] = new TextView(this);
            introBullets[i].setText(HtmlCompat.fromHtml("   &#9679;   ", HtmlCompat.FROM_HTML_MODE_LEGACY));
            introBullets[i].setTextSize(17F);
            introBullets[i].setTextColor(colorsInactive[currentPage]);
            mSlider_dots.addView(introBullets[i]);
        }
        if (introBullets.length > 0){
            introBullets[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return mSlider_viewpager.getCurrentItem() + i;
    }

    private void applicationStartup(){
        mAppPreferences.setIsFirstTimeLaunch(false);
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }
}
