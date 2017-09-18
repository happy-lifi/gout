package com.example.yang.gout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.yang.gout.Adapter.MyFragmentAdapter;
import com.example.yang.gout.Adapter.TitleFragmentPagerAdapter;
import com.example.yang.gout.fragment.DiscoveryFragment;
import com.example.yang.gout.fragment.NearFragment;
import com.example.yang.gout.fragment.fragment_center;
import com.example.yang.gout.fragment.fragment_run;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class MainActivity extends FragmentActivity {
    BottomNavigationBar bottomNavigationBar;
    private NoScrollViewPager viewpager;
    private TabLayout tabLayout;
    private MyFragmentAdapter adapter;
    private List<Fragment> list_fragment = new ArrayList<>();
    private static final String[] CHANNELS = new String[]{"KITKAT", "NOUGAT", "DONUT"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
//     MyFragmentAdapter mExamplePagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(),list_fragment);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        click();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
//        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
//        magicIndicator.setBackgroundColor(Color.WHITE);
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            @Override
//            public int getCount() {
//                return mDataList == null ? 0 : mDataList.size();
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
//                simplePagerTitleView.setText(mDataList.get(index));
//                simplePagerTitleView.setTextSize(18);
//                simplePagerTitleView.setNormalColor(Color.parseColor("#616161"));
//                simplePagerTitleView.setSelectedColor(Color.parseColor("#f57c00"));
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewpager.setCurrentItem(index);
//                    }
//                });
//                return simplePagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
//                indicator.setYOffset(UIUtil.dip2px(context, 39));
//                indicator.setLineHeight(UIUtil.dip2px(context, 1));
//                indicator.setColors(Color.parseColor("#f57c00"));
//                return indicator;
//            }
//
//            @Override
//            public float getTitleWeight(Context context, int index) {
//                if (index == 0) {
//                    return 2.0f;
//                } else if (index == 1) {
//                    return 1.2f;
//                } else {
//                    return 1.0f;
//                }
//            }
//        });
//        magicIndicator.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(magicIndicator, viewpager);
//    }

        list_fragment.add(new NearFragment());
        list_fragment.add(new fragment_center());
        list_fragment.add(new DiscoveryFragment());
        adapter = new MyFragmentAdapter(getSupportFragmentManager(),list_fragment);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(),list_fragment, new String[]{"near", "center", "discover"});
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }
    private void click() {
        initData();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottomNavigationBar
                .setInActiveColor(R.color.colorInActive)//设置未选中的Item的颜色，包括图片和文字
                .setActiveColor(R.color.colorPrimary)
                .setBarBackgroundColor(R.color.colorBarBg);//设置整个控件的背景色
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {//未选中 -> 选中
            }

            @Override
            public void onTabUnselected(int position) {//选中 -> 未选中
            }

            @Override
            public void onTabReselected(int position) {//选中 -> 选中
            }
        });
    }

    private void init() {
        viewpager = (NoScrollViewPager) findViewById(R.id.viewPager);
       tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.message, "消息"))
                .addItem(new BottomNavigationItem(R.mipmap.sum, "发动态"))
                .addItem(new BottomNavigationItem(R.mipmap.friend, "关注"))
                .initialise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
