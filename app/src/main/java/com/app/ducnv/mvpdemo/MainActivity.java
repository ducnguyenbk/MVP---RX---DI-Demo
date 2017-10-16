package com.app.ducnv.mvpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.app.ducnv.mvpdemo.dragger2.DIFragment;
import com.app.ducnv.mvpdemo.mvp.view.ImageViewFragment;
import com.app.ducnv.mvpdemo.rx.RxDemoFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initImageLoader();

        findViewById(R.id.tv_mvp).setOnClickListener(this);
        findViewById(R.id.tv_rx).setOnClickListener(this);
        findViewById(R.id.tv_dagger).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_mvp:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, ImageViewFragment.newInstance())
                        .addToBackStack(ImageViewFragment.class.getSimpleName())
                        .commit();
                break;
            case R.id.tv_rx:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, RxDemoFragment.newInstance())
                        .addToBackStack(RxDemoFragment.class.getSimpleName())
                        .commit();
                break;

            case R.id.tv_dagger:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, DIFragment.newInstance())
                        .addToBackStack(DIFragment.class.getSimpleName())
                        .commit();
                break;

        }

    }

    public void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(200))
                .cacheOnDisk(false)
                .cacheInMemory(false)
                .build();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(defaultOptions);
        ImageLoader.getInstance().init(config.build());
    }

}
