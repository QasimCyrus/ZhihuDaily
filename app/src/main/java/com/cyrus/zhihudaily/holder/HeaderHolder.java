package com.cyrus.zhihudaily.holder;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cyrus.zhihudaily.R;


/**
 * 显示大图轮播的holder
 * <p>
 * Created by Cyrus on 2016/5/1.
 */
public class HeaderHolder extends RecyclerView.ViewHolder {

    private ViewPager mViewPager;
    private ImageView mGuideSpots[];
    private static int mSpotsIds[] = {R.id.iv_guide_spot_1, R.id.iv_guide_spot_2,
            R.id.iv_guide_spot_3, R.id.iv_guide_spot_4, R.id.iv_guide_spot_5};

    public ImageView[] getGuideSpots() {
        return mGuideSpots;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }


    public HeaderHolder(View itemView) {
        super(itemView);

        mViewPager = (ViewPager) itemView.findViewById(R.id.vp_picture);
        mGuideSpots = new ImageView[mSpotsIds.length];
        for (int i = 0; i < mSpotsIds.length; i++) {
            mGuideSpots[i] = (ImageView) itemView.findViewById(mSpotsIds[i]);
        }
    }

}
