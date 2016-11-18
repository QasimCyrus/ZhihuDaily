package com.cyrus.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.activity.NewsDetailActivity;
import com.cyrus.zhihudaily.constants.DataConstant;
import com.cyrus.zhihudaily.models.SimpleStory;
import com.cyrus.zhihudaily.models.TopStory;
import com.cyrus.zhihudaily.utils.ImageUtils;
import com.cyrus.zhihudaily.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 头条图片适配器
 * <p>
 * Created by Cyrus on 2016/10/14.
 */

public class HeaderImageAdapter extends PagerAdapter {

    private List<TopStory> mTopStories;
    private Context mContext;

    public HeaderImageAdapter(Context context, List<TopStory> topStories) {
        mTopStories = topStories;
        mContext = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(UiUtils.getContext(), R.layout.item_image, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStory simpleStory = new SimpleStory();
                TopStory topStory = mTopStories.get(position % 5);

                simpleStory.setId(topStory.getId());
                simpleStory.setTitle(topStory.getTitle());
                ArrayList<String> images = new ArrayList<>();
                images.add(topStory.getImage());
                simpleStory.setImages(images);

                Intent intent = new Intent(UiUtils.getContext(), NewsDetailActivity.class);
                intent .putExtra(DataConstant.INTENT_NEWS, simpleStory);
                mContext.startActivity(intent);
            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_show);
        TextView textView = (TextView) view.findViewById(R.id.tv_show);
        TopStory topStory = mTopStories.get(position % 5);

        textView.setText(topStory.getTitle());
        ImageUtils.loadImage(topStory.getImage(), imageView);
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
