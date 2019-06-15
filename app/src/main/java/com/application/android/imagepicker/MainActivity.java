package com.application.android.imagepicker;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.application.android.imagepicker.adapters.ViewPagerAdapter;
import com.application.android.imagepicker.gallery.GalleryPickerFragment;
import com.application.android.imagepicker.models.Session;
import com.application.android.imagepicker.models.enums.SourceType;
import com.application.android.imagepicker.modules.PermissionModule;
import com.application.android.imagepicker.photo.CapturePhotoFragment;
import com.application.android.imagepicker.ui.ToolbarView;
import com.application.android.imagepicker.video.CaptureVideoFragment;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ToolbarView.OnClickTitleListener,
        ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener {

    @BindView(R.id.mMainTabLayout)
    TabLayout mMainTabLayout;
    @BindView(R.id.mMainViewPager)
    ViewPager mMainViewPager;
    @BindView(R.id.mToolbar)
    ToolbarView mToolbar;

    @BindString(R.string.tab_gallery)
    String _tabGallery;
    @BindString(R.string.tab_photo)
    String _tabPhoto;
    @BindString(R.string.tab_video)
    String _tabVideo;

    private Session mSession = Session.getInstance();
    private ViewPagerAdapter pagerAdapter ;
    private HashSet<SourceType> mSourceTypeSet = new HashSet<>();

    private void initViews() {
        PermissionModule permissionModule = new PermissionModule(this);
        permissionModule.checkPermissions();

        mToolbar.setOnClickBackMenuListener(this)
                .setOnClickTitleListener(this)
                .setOnClickNextListener(this);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
        mMainViewPager.setAdapter(pagerAdapter);

        mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTabLayout));
        mMainViewPager.setOffscreenPageLimit(0);
        mMainViewPager.setCurrentItem(0);
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                displayTitleByTab(tab);
                initNextButtonByTab(tab.getPosition());
            }
        };
    }

    private void displayTitleByTab(TabLayout.Tab tab) {
        if (tab.getText() != null) {
            String title = tab.getText().toString();
            mToolbar.setTitle(title);
            mToolbar.setVisibility(View.GONE);
        }
    }

    private void initNextButtonByTab(int position) {
        switch (position) {
            case 0:
                mToolbar.showNext();
                break;
            case 1:
                mToolbar.hideNext();
                break;
            case 2:
                mToolbar.hideNext();
                break;
            default:
                mToolbar.hideNext();
                break;
        }
    }

    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (mSourceTypeSet.contains(SourceType.Gallery)) {
            //fragments.add(GalleryPickerFragment.newInstance());
            fragments.add(GalleryPickerFragment.newInstance());
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(_tabGallery));
        }

        if (mSourceTypeSet.contains(SourceType.Video)) {
            fragments.add(CaptureVideoFragment.newInstance());
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(_tabVideo));
        }

        if (mSourceTypeSet.contains(SourceType.Photo)) {
            fragments.add(CapturePhotoFragment.newInstance());
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(_tabPhoto));
        }

        return fragments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        ButterKnife.bind(this);

        // If you want to start activity with custom Tab
        mSourceTypeSet.add(SourceType.Gallery);
        mSourceTypeSet.add(SourceType.Video);
        mSourceTypeSet.add(SourceType.Photo);


        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                finish();
            }
        }
        /*if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length == VIDEO_PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        initViews();
                        break;
                    }
                }
            } else {

            }*/
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onClickBack() {

    }

    @Override
    public void onClickNext() {
        // Fetch file to upload
      //  mSession.getFileToUpload();
    }

    @Override
    public void onClickTitle() {

    }
}
