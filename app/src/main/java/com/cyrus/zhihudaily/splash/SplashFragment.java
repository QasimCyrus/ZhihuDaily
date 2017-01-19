package com.cyrus.zhihudaily.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动页的UI
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
public class SplashFragment extends Fragment implements SplashContract.View {

    private SplashContract.Presenter mSplashPresenter;

    @BindView(R.id.iv_splash)
    ImageView mIvSplash;
    @BindView(R.id.tv_splash)
    TextView mTvSplash;

    public static SplashFragment newInstance() {
        Bundle args = new Bundle();
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSplashPresenter.start();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mSplashPresenter = presenter;
    }

    @Override
    public ImageView getSplashView() {
        return mIvSplash;
    }

    @Override
    public void setSplashText(String splashText) {
        mTvSplash.setText(splashText);
    }

    @Override
    public void jump2MainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }
}
