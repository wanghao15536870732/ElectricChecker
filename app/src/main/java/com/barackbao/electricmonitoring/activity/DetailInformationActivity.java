package com.barackbao.electricmonitoring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barackbao.electricmonitoring.R;
import com.barackbao.electricmonitoring.adapter.DetailPagerAdapter;
import com.barackbao.electricmonitoring.ui.NoScrollViewPager;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailInformationActivity extends AppCompatActivity {

    @BindView(R.id.navigation_layout)
    RelativeLayout navigationLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout slidingTabs;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.title)
    TextView toolbarTitle;
    @BindView( R.id.ic_back )
    ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_information);
        ButterKnife.bind(this);
        initViewPager();
        Intent intent = getIntent();
        String title = intent.getStringExtra("roomName");
        toolbarTitle.setText(title);
        ic_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
    }

    /**
     * 初始化pager
     */
    private void initViewPager() {
        final DetailPagerAdapter mHomeAdapter = new DetailPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mHomeAdapter);
        slidingTabs.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mHomeAdapter.changePage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
