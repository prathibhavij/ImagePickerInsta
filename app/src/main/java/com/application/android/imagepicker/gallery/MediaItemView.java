package com.application.android.imagepicker.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.android.imagepicker.R;
import com.application.android.imagepicker.modules.ReboundModule;
import com.application.android.imagepicker.modules.ReboundModuleDelegate;
import com.application.android.imagepicker.utils.FileSearch;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaItemView extends RelativeLayout implements ReboundModuleDelegate {

    @BindView(R.id.mMediaThumb)
    ImageView mMediaThumb;
    @BindView(R.id.tvDuration)
    TextView tvDuration;

    private File mCurrentFile;
    private ReboundModule mReboundModule = ReboundModule.getInstance(this);
    private WeakReference<MediaItemViewListener> mWrListener;


    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_JPEG = ".jpeg";
    private static final String EXTENSION_PNG = ".png";

    void setListener(MediaItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    public MediaItemView(Context context) {
        super(context);
        View v = View.inflate(context, R.layout.media_item_view, this);
        ButterKnife.bind(this, v);
    }

    public void bind(File file) {
        mCurrentFile = file;
        mReboundModule.init(mMediaThumb);

        if (file.getName().toLowerCase().endsWith(EXTENSION_JPG)
                || file.getName().toLowerCase().endsWith(EXTENSION_JPEG)
                || file.getName().toLowerCase().endsWith(EXTENSION_PNG)) {

            Picasso.with(getContext())
                    .load(Uri.fromFile(file))
                    .resize(350, 350)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_media)
                    .error(R.drawable.placeholder_error_media)
                    .noFade()
                    .into(mMediaThumb);
            tvDuration.setVisibility(View.GONE);

        }else{

            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mCurrentFile.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
            mMediaThumb.setImageBitmap(bmThumbnail);

            tvDuration.setVisibility(View.VISIBLE);
            tvDuration.setText(FileSearch.getVideoTimeDuration(getContext(),mCurrentFile));

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onTouchActionUp() {
        mWrListener.get().onClickItem(mCurrentFile);
    }
}
