package bsoft.com.lib_gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bsoft.com.lib_gallery.adapter.DrawerAdapter;
import bsoft.com.lib_gallery.adapter.GalleryAdapter;
import bsoft.com.lib_gallery.adapter.GalleryFooterAdapter;
import bsoft.com.lib_gallery.custom.MyTextView;
import bsoft.com.lib_gallery.helper.GalleryHelper;
import bsoft.com.lib_gallery.listener.OnGalleryListener;
import bsoft.com.lib_gallery.model.DrawerModel;
import bsoft.com.lib_gallery.model.PhotoModel;
import bsoft.com.lib_gallery.util.PlatformUtil;
import bsoft.com.lib_gallery.util.Statistic;

/**
 * Created by Adm on 8/10/2016.
 */
public class GalleryActivity extends AppCompatActivity implements DrawerAdapter.OnNavigationItemSelectedListener, OnGalleryListener, View.OnClickListener, GalleryFooterAdapter.OnClearImageChooseListener {
    private static final java.lang.String TAG = GalleryActivity.class.getSimpleName();
    private Toolbar mToolbar = null;
    private GalleryAdapter adapter;
    private GalleryFooterAdapter imgAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewFooter;
    private int numImag = 0;
    private AdView mAdView;
    private TextView myTextView;
    private int countImage = 0;
    private int itemCount;
    private int photoCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        initAdmob();
        initArrayLists();
        initToolbar();
        initDrawer();
        initGalleryFooterView();
        initGalleryMainContentView();
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                GalleryHelper.loadGallery(GalleryActivity.this, GalleryActivity.this);
            }
        });
    }

    private void initArrayLists() {
        if (GalleryHelper.photoList == null) {
            GalleryHelper.photoList = new ArrayList<>();
        }

        if (GalleryHelper.allPhotoList == null) {
            GalleryHelper.allPhotoList = new ArrayList<>();
        }

        if (GalleryHelper.photoMap == null) {
            GalleryHelper.photoMap = new HashMap<>();
        }

        if (GalleryHelper.sortedFolderList == null) {
            GalleryHelper.sortedFolderList = new ArrayList<>();
        }

    }

    private void initAdmob() {
        mAdView = (AdView) findViewById(R.id.gallery_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void initGalleryMainContentView() {
        adapter = new GalleryAdapter(this, GalleryHelper.photoList).setOnGalleryListener(this);
        myTextView = (TextView) findViewById(R.id.gallery_text_hint);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GalleryHelper.NUMBER_GRID_CNT);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        setTextChooseImage(itemCount, 0);

//        adapter.notifyDataSetChanged();
    }

    //    private int curIndex = 0;
    public GalleryFooterAdapter getImgAdapter() {
        return imgAdapter;
    }

    private void setTextChooseImage(int num, int countImage) {
        myTextView.setText(String.format("%d/%d", countImage, num));


    }

    public View getCurrentTargetView() {
        return recyclerViewFooter.getLayoutManager().findViewByPosition(imgAdapter.getCurIndex());
    }

    private void initGalleryFooterView() {
        itemCount = getIntent().getIntExtra(Statistic.EXTRA_NUM_OF_PHOTOS, 0);
        photoCount = getIntent().getIntExtra(Statistic.EXTRA_PHOTO_STYPE, 0);

        Log.d("initGalleryFooterView ", " " + itemCount);

        View nextButton = findViewById(R.id.next_btn);
        nextButton.setOnClickListener(this);

        final int paddingRight = getResources().getDimensionPixelOffset(R.dimen.xx_large);
        imgAdapter = new GalleryFooterAdapter(this, this);
        recyclerViewFooter = (RecyclerView) findViewById(R.id.footer_recycle_view);
        recyclerViewFooter.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.right = paddingRight;
            }
        });
        recyclerViewFooter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFooter.setAdapter(imgAdapter);
        scrollToCurPosition();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.next_btn) {
            if (photoCount == 0x1) {
                if (countImage == itemCount) {
                    pickImage();
                } else {
                    Toast.makeText(this, R.string.non_pick_imgae, Toast.LENGTH_SHORT).show();
                }
            } else {
                pickImage();
            }

        }
    }

    private void pickImage() {
        Intent data = new Intent();
        Log.d("xxxxxxxx ", "111 " + imgAdapter.getChoosedModelList().size());
        for (int i = 0; i < imgAdapter.getChoosedModelList().size(); i++) {
            PhotoModel photoModel = imgAdapter.getChoosedModelList().get(i);
            Log.d("photoModel ", " " + photoModel);
            if (photoModel != null) {
                data.putParcelableArrayListExtra(Statistic.EXTRA_CHANGED_PHOTOS, imgAdapter.getChoosedModelList());
                setResult(Activity.RESULT_OK, data);
                GalleryHelper.release();
                finish();
                break;
            } else {
             //   Toast.makeText(this, R.string.non_pick_imgae, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void scrollToCurPosition() {
        recyclerViewFooter.getLayoutManager().scrollToPosition(imgAdapter.getCurIndex());
    }


    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(GalleryActivity.this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mToolbar.setTitle(R.string.all);
    }


    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.collage);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    private void performAnimation(View source, View destination) {
        if (source == null || destination == null) return;
        final FrameLayout frameLayout = new FrameLayout(this);
        final ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(source.getWidth(), source.getHeight());
        imageView.setLayoutParams(params);
        ImageView sourceImage = (ImageView) source.findViewById(R.id.img_picked);
        imageView.setImageDrawable(sourceImage.getDrawable());

        frameLayout.addView(imageView);

        frameLayout.setX(source.getX());
        frameLayout.setY(source.getY());

        final ViewGroup root = (ViewGroup) findViewById(R.id.root_content_view);
        root.addView(frameLayout);

        int[] sourcePos = new int[2];
        int[] destPos = new int[2];
        source.getLocationInWindow(sourcePos);
        destination.getLocationInWindow(destPos);

        int xDelta = destPos[0] - sourcePos[0];
        int yDelta = destPos[1] - sourcePos[1];

        TranslateAnimation translate = new TranslateAnimation(0, xDelta, 0, yDelta);
        translate.setDuration(Statistic.GALLERY_SHARED_ITEM_INTERVAL);
        translate.setFillAfter(true);

        float scaleX = (float) destination.getWidth() / source.getWidth();
        float scaleY = (float) destination.getHeight() / source.getHeight();

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, scaleX, 1.0f, scaleY);
        scaleAnimation.setDuration(Statistic.GALLERY_SHARED_ITEM_INTERVAL);
        scaleAnimation.setFillAfter(true);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                root.post(new Runnable() {
                    @Override
                    public void run() {
                        root.removeView(frameLayout);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(scaleAnimation);
        frameLayout.startAnimation(translate);

    }

    @Override
    public void onGalleryLoaded(List<String> sortedFolderList) {
        initNavigationView();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPhotoPickListener(PhotoModel photoModel, View sourceView) {

        if (photoModel == null || sourceView == null) return;
        performAnimation(sourceView, getCurrentTargetView());
        getImgAdapter().pushPhoto(photoModel);
        scrollToCurPosition();
        countImage++;
        Log.d("onPhotoPickListener ", "11111 " + itemCount + "__" + countImage);
        if (countImage >= itemCount) {
            countImage = itemCount;
            setTextChooseImage(itemCount, countImage);
            return;
        }
        setTextChooseImage(itemCount, countImage);

    }


    private void initNavigationView() {
        List<DrawerModel> mDrawerItemList = new ArrayList<>();
        DrawerModel item = new DrawerModel();
        item.setTitle(getString(R.string.all));
        item.total = GalleryHelper.allPhotoList.size();
        mDrawerItemList.add(item);


        for (String key : GalleryHelper.sortedFolderList) {


            item = new DrawerModel();
            item.setTitle(key);
            if (key.equals("ALL")) {
//                item.total = GalleryHelper.allPhotoList.size();
//                item.iconPath = GalleryHelper.allPhotoList.get(0).imgPath;
            } else {
                item.total = GalleryHelper.photoMap.get(key).size();
                item.iconPath = GalleryHelper.photoMap.get(key).get(0).imgPath;
            }

            mDrawerItemList.add(item);
        }

        DrawerAdapter adapter = new DrawerAdapter(mDrawerItemList);
        RecyclerView navView = (RecyclerView) findViewById(R.id.nav_view);

        navView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        navView.addItemDecoration(new DrawerAdapter.VerticalSpaceItemDecoration(PlatformUtil.dpToPx(this, 1)));
        navView.setAdapter(adapter);

        adapter.setOnItemSelectedListener(this);

    }
//
//    private void initNavigationView() {
//        List<DrawerModel> mDrawerItemList = new ArrayList<>();
//        DrawerModel item = new DrawerModel();
//        item.setTitle(getString(R.string.all));
//        item.total = GalleryHelper.allPhotoList.size();
//        mDrawerItemList.add(item);
//
//
//        for (String key : GalleryHelper.sortedFolderList) {
//
//            item = new DrawerModel();
//            item.setTitle(key);
//            if (key.equals("ALL")) {
////                item.total = GalleryHelper.allPhotoList.size();
////                item.iconPath = GalleryHelper.allPhotoList.get(0).imgPath;
//            } else {
//                item.total = GalleryHelper.photoMap.get(key).size();
//                item.iconPath = GalleryHelper.photoMap.get(key).get(0).imgPath;
//            }
//
//            mDrawerItemList.add(item);
//        }
//
//        DrawerAdapter adapter = new DrawerAdapter(mDrawerItemList);
//        RecyclerView navView = (RecyclerView) findViewById(R.id.nav_view);
//
//        navView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        navView.addItemDecoration(new DrawerAdapter.VerticalSpaceItemDecoration(PlatformUtil.dpToPx(this, 1)));
//        navView.setAdapter(adapter);
//
//        adapter.setOnItemSelectedListener(this);
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNavigationItemSelected(int position) {
        if (position == 0) {
            reloadRecycleView(null);
            mToolbar.setTitle(R.string.all);


            GalleryHelper.currrentAlbum = getResources().getString(R.string.all);
        } else {

            //Log.d("xxx", "xxxx = " + GalleryHelper.sortedFolderList);

            String key = GalleryHelper.sortedFolderList.get(position - 1);

            GalleryHelper.currrentAlbum = key;

            reloadRecycleView(key);
            mToolbar.setTitle(key);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    public void reloadRecycleView(String key) {
        GalleryHelper.photoList.clear();
        if (key != null) {
            GalleryHelper.photoList.addAll(GalleryHelper.photoMap.get(key));
        } else {
            GalleryHelper.photoList.addAll(GalleryHelper.allPhotoList);
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d("onBackPress ", "11111");
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("xxx", "onDestroy");
//        GalleryHelper.release(); // Duc comment: release before called finish();, some device call onDestroy later

        super.onDestroy();

    }

    @Override
    public void onClearImageClick() {
        countImage--;
        if (countImage <= 0) {
            countImage = 0;
            setTextChooseImage(itemCount, countImage);
            return;
        }
        setTextChooseImage(itemCount, countImage);

//        if (itemCount == 1) {
//            countImage = 0;
//            setTextChooseImage(1, countImage);
//        } else if (itemCount == 9) {
//            countImage--;
//            if (countImage <= 0) {
//                setTextChooseImage(9, countImage);
//                return;
//            }
//            setTextChooseImage(9, countImage);
//        }
    }
}
