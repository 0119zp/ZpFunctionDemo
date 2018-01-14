package zp.com.zpfunctiondemo.custom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zp.com.zpfunctiondemo.R;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class ZpPagerFragment extends Fragment {

    private int mPageType;

    public static Fragment newInstance(int pageType) {
        Bundle args = new Bundle();
        args.putInt("page_tag", pageType);
        ZpPagerFragment fragment = new ZpPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        mPageType = getArguments().getInt("page_tag");
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView text = (TextView) view.findViewById(R.id.text);
        switch (mPageType) {
            case 0:
                text.setText("tab---1");
                break;
            case 1:
                text.setText("tab---2");
                break;
            case 2:
                text.setText("tab---3");
                break;
        }
    }

}
