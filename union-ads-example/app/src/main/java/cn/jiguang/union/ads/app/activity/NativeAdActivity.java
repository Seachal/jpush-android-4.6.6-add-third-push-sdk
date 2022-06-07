package cn.jiguang.union.ads.app.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.union.ads.api.JUnionAdApi;
import cn.jiguang.union.ads.api.JUnionAdError;
import cn.jiguang.union.ads.app.R;
import cn.jiguang.union.ads.nativ.api.JNativeAd;
import cn.jiguang.union.ads.nativ.api.JNativeAdApi;
import cn.jiguang.union.ads.nativ.api.JNativeAdSlot;
import cn.jiguang.union.ads.nativ.callback.OnNativeAdEventListener;
import cn.jiguang.union.ads.nativ.callback.OnNativeAdLoadListener;

public class NativeAdActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "NativeAdActivity";

    private FrameLayout adContainer;
    private JNativeAd bannerNativeAd;
    private JNativeAd modalNativeAd;
    private JNativeAd floatNativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        adContainer = findViewById(R.id.fl_view);

        findViewById(R.id.btn_banner_load).setOnClickListener(this);
        findViewById(R.id.btn_modal_load).setOnClickListener(this);
        findViewById(R.id.btn_float_load).setOnClickListener(this);

        findViewById(R.id.btn_banner_show).setOnClickListener(this);
        findViewById(R.id.btn_modal_show).setOnClickListener(this);
        findViewById(R.id.btn_float_show).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.btn_banner_load) {
            loadAd(NativeAdActivity.this, JNativeAdSlot.STYLE_BANNER);
        } else if (id == R.id.btn_modal_load) {
            loadAd(NativeAdActivity.this, JNativeAdSlot.STYLE_MODAL);
        } else if (id == R.id.btn_float_load) {
            loadAd(NativeAdActivity.this, JNativeAdSlot.STYLE_FLOAT);
        } else if (id == R.id.btn_banner_show) {
            showAd(NativeAdActivity.this, JNativeAdSlot.STYLE_BANNER);
        } else if (id == R.id.btn_modal_show) {
            showAd(NativeAdActivity.this, JNativeAdSlot.STYLE_MODAL);
        } else if (id == R.id.btn_float_show) {
            showAd(NativeAdActivity.this, JNativeAdSlot.STYLE_FLOAT);
        } else if (id == R.id.iv_banner_cancel) {
            cancelAd(NativeAdActivity.this);
        } else if (id == R.id.iv_modal_cancel) {
            cancelAd(NativeAdActivity.this);
        } else if (id == R.id.iv_float_cancel) {
            cancelAd(NativeAdActivity.this);
        }
    }

    private void loadAd(final Context context, final int style) {
        JNativeAdApi nativeAdApi = JUnionAdApi.buildNativeAdApi(context);
        JNativeAdSlot nativeAdSlot = new JNativeAdSlot.Builder()
                .setAdCode("test1234")  // 广告代码位名称
                .setTimeout(3000)       // 加载广告超时时间，默认3秒
                .setAdStyle(style)      // 广告样式，可选择JNativeAdSlot.STYLE_BANNER/JNativeAdSlot.STYLE_MODAL/JNativeAdSlot.STYLE_FLOAT
                .build();
        nativeAdApi.loadNativeAd(context, nativeAdSlot, new OnNativeAdLoadListener() {

            @Override
            public void onError(JUnionAdError error) {
                Log.e(TAG, "onLoadError code:" + error.code + ", message:" + error.message);
                Toast.makeText(context, "onError code:" + error.code + ", message:" + error.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoaded(List<JNativeAd> ads) {
                View adView = LayoutInflater.from(context).inflate(R.layout.view_ad, adContainer, false);
                TextView adTitle = adView.findViewById(R.id.tv_title);
                TextView adContent = adView.findViewById(R.id.tv_content);
                TextView adImageUrl = adView.findViewById(R.id.tv_image_url);
                switch (style) {
                    case JNativeAdSlot.STYLE_BANNER:
                        // 目前只支持一次拉取一个广告
                        bannerNativeAd = ads.get(0);
                        modalNativeAd = null;
                        floatNativeAd = null;
                        Toast.makeText(context, "onAdLoaded " + bannerNativeAd.getTitle(), Toast.LENGTH_SHORT).show();
                        adTitle.setText("title：" + bannerNativeAd.getTitle() + "\n");
                        adContent.setText("content：" + bannerNativeAd.getContent() + "\n");
                        adImageUrl.setText("imageUrl：" + bannerNativeAd.getImageList().get(0).getUrl());
                        break;
                    case JNativeAdSlot.STYLE_MODAL:
                        // 目前只支持一次拉取一个广告
                        modalNativeAd = ads.get(0);
                        bannerNativeAd = null;
                        floatNativeAd = null;
                        Toast.makeText(context, "onAdLoaded " + modalNativeAd.getTitle(), Toast.LENGTH_SHORT).show();
                        adTitle.setText("title：" + modalNativeAd.getTitle() + "\n");
                        adContent.setText("content：" + modalNativeAd.getContent() + "\n");
                        adImageUrl.setText("imageUrl：" + modalNativeAd.getImageList().get(0).getUrl());
                        break;
                    case JNativeAdSlot.STYLE_FLOAT:
                        // 目前只支持一次拉取一个广告
                        floatNativeAd = ads.get(0);
                        bannerNativeAd = null;
                        modalNativeAd = null;
                        Toast.makeText(context, "onAdLoaded " + floatNativeAd.getTitle(), Toast.LENGTH_SHORT).show();
                        adTitle.setText("title：" + floatNativeAd.getTitle() + "\n");
                        adContent.setText("content：" + floatNativeAd.getContent() + "\n");
                        adImageUrl.setText("imageUrl：" + floatNativeAd.getImageList().get(0).getUrl());
                        break;
                    default:
                        break;
                }
                adContainer.removeAllViews();
                adContainer.addView(adView);
            }

        });
    }

    private void showAd(final Context context, int style) {
        View adView = null;
        List<View> clickViews = new ArrayList<>();
        JNativeAd nativeAd = null;
        switch (style) {
            case JNativeAdSlot.STYLE_BANNER:
                if (bannerNativeAd == null) {
                    Toast.makeText(context, "there are no banner ad，please load first", Toast.LENGTH_SHORT).show();
                    return;
                }
                nativeAd = bannerNativeAd;
                adView = LayoutInflater.from(context).inflate(R.layout.view_banner, adContainer, false);
                TextView bannerTitle = adView.findViewById(R.id.tv_banner_title);
                TextView bannerContent = adView.findViewById(R.id.tv_banner_content);
                ImageView bannerImage = adView.findViewById(R.id.iv_banner_image);
                bannerTitle.setText(bannerNativeAd.getTitle());
                bannerContent.setText(bannerNativeAd.getContent());
                Glide.with(context).load(bannerNativeAd.getImageList().get(0).getUrl()).into(bannerImage);
                adView.findViewById(R.id.iv_banner_cancel).setOnClickListener(this);
                break;
            case JNativeAdSlot.STYLE_MODAL:
                if (modalNativeAd == null) {
                    Toast.makeText(context, "there are no modal ad，please load first", Toast.LENGTH_SHORT).show();
                    return;
                }
                nativeAd = modalNativeAd;
                adView = LayoutInflater.from(context).inflate(R.layout.view_modal, adContainer, false);
                TextView modalTitle = adView.findViewById(R.id.tv_modal_title);
                TextView modalContent = adView.findViewById(R.id.tv_modal_content);
                ImageView modalImage = adView.findViewById(R.id.iv_modal_image);
                modalTitle.setText(modalNativeAd.getTitle());
                modalContent.setText(modalNativeAd.getContent());
                Glide.with(context).load(modalNativeAd.getImageList().get(0).getUrl()).into(modalImage);
                // 这里指定button为点击响应
                clickViews.add(adView.findViewById(R.id.btn_modal));
                adView.findViewById(R.id.iv_modal_cancel).setOnClickListener(this);
                break;
            case JNativeAdSlot.STYLE_FLOAT:
                if (floatNativeAd == null) {
                    Toast.makeText(context, "there are no float ad，please load first", Toast.LENGTH_SHORT).show();
                    return;
                }
                nativeAd = floatNativeAd;
                adView = LayoutInflater.from(context).inflate(R.layout.view_float, adContainer, false);
                ImageView floatImage = adView.findViewById(R.id.iv_float_image);
                Glide.with(context).load(floatNativeAd.getImageList().get(0).getUrl()).into(floatImage);
                adView.findViewById(R.id.iv_float_cancel).setOnClickListener(this);
                break;
            default:
                break;
        }
        if (adView == null) {
            return;
        }
        adContainer.removeAllViews();
        adContainer.addView(adView);
        if (nativeAd == null) {
            Toast.makeText(context, "there are no ad，please load first", Toast.LENGTH_SHORT).show();
            return;
        }

        // 如果ckickView为空列表，则默认使用adContainer作为点击响应区域
        nativeAd.setOnNativeAdEventListener(adContainer, clickViews, new OnNativeAdEventListener() {

            @Override
            public void onError(JUnionAdError error) {
                Log.e(TAG, "onEventError code:" + error.code + ", message:" + error.message);
                Toast.makeText(context, "onError code:" + error.code + ", message:" + error.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdExposed(JNativeAd nativeAd) {
                Toast.makeText(context, "onAdExposed " + nativeAd.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked(View view, JNativeAd nativeAd) {
                Toast.makeText(context, "onAdClicked " + nativeAd.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelAd(Context context) {
        bannerNativeAd = null;
        modalNativeAd = null;
        floatNativeAd = null;
        if (adContainer != null) {
            adContainer.removeAllViews();
        }
        Toast.makeText(context, "onAdCanceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAd(this);
    }

}
