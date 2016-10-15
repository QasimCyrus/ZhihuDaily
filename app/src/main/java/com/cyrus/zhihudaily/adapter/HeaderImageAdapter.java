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
import com.cyrus.zhihudaily.constants.IntentConstant;
import com.cyrus.zhihudaily.models.IntentStory;
import com.cyrus.zhihudaily.models.TopStory;
import com.cyrus.zhihudaily.utils.LoadImageUtils;
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
        return mTopStories.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(UiUtils.getContext(), R.layout.item_image, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentStory intentStory = new IntentStory();
                TopStory topStory = mTopStories.get(position);

                intentStory.setId(topStory.getId());
                intentStory.setTitle(topStory.getTitle());
                ArrayList<String> images = new ArrayList<>();
                images.add(topStory.getImage());
                intentStory.setImages(images);

                Intent intent = new Intent(UiUtils.getContext(), NewsDetailActivity.class);
                intent .putExtra(IntentConstant.INTENT_NEWS, intentStory);
                mContext.startActivity(intent);
            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_show);
        TextView textView = (TextView) view.findViewById(R.id.tv_show);
        TopStory topStory = mTopStories.get(position);

        textView.setText(topStory.getTitle());
        LoadImageUtils.loadImage(topStory.getImage(), imageView);
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
