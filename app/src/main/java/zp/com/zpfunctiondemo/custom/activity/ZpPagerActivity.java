package zp.com.zpfunctiondemo.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;

import zp.com.zpfunctiondemo.R;
import zp.com.zpfunctiondemo.arouter.ArouterPath;
import zp.com.zpfunctiondemo.custom.ZpViewPagerTab;
import zp.com.zpfunctiondemo.custom.fragment.ZpPagerFragment;

/**
 * Created by Administrator on 2018/1/14 0014.
 */
@Route(path = ArouterPath.CUSTOM_VIEWPAGE_PAGE, name = "viewpage tab")
public class ZpPagerActivity extends FragmentActivity {

    private ZpViewPagerTab viewPagerTab;
    private ViewPager viewPager;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private String[] title = {"TAB-1", "TAB-2", "TAB-3"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        initView();
    }

    private void initView() {
        viewPagerTab = (ZpViewPagerTab) findViewById(R.id.work_table_tabs);
        viewPager = (ViewPager) findViewById(R.id.work_table_viewpager);

        mFragmentList.add(ZpPagerFragment.newInstance(0));
        mFragmentList.add(ZpPagerFragment.newInstance(1));
        mFragmentList.add(ZpPagerFragment.newInstance(2));

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(2);
        ZpPagerActivity.MyViewPagerAdapter viewPagerAdapter = new ZpPagerActivity.MyViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);

        viewPagerTab.setViewPager(viewPager);
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }

}
