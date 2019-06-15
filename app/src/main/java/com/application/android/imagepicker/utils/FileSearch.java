package com.application.android.imagepicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nirali on 7/28/2017.
 */

public class FileSearch {

    /**
     * Search a directory and return a list of all **directories** contained inside
     *
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        if (listfiles != null) {

            for (int i = 0; i < listfiles.length; i++) {
                if (listfiles[i].isDirectory()) {
                    pathArray.add(listfiles[i].getAbsolutePath());
                }
            }

        }
        return pathArray;
    }

    /**
     * Search a directory and return a list of all **files** contained inside
     *
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        if (listfiles != null) {
            for (int i = 0; i < listfiles.length; i++) {
                if (listfiles[i].isFile()) {
                    pathArray.add(listfiles[i].getAbsolutePath());
                }
            }
        }
        return pathArray;
    }

    public static ArrayList<String> getImageBuckets(Context mContext){
        ArrayList<String> buckets = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]));
                String fisrtImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(fisrtImage);
                if (file.exists() && !buckets.contains(bucketPath)) {
                    buckets.add(bucketPath);
                }
            }
            cursor.close();
        }
        getVideoBuckets(mContext,buckets);
        return buckets;
    }



    public static ArrayList<String> getVideoBuckets(Context mContext,ArrayList<String> buckets){

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]));
                String fisrtImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(fisrtImage);
                if (file.exists() && !buckets.contains(bucketPath)) {
                    buckets.add(bucketPath);
                }
            }
            cursor.close();
        }
        return buckets;
    }


    public static ArrayList<File> getVideosByBucket(Context mContext , @NonNull String bucketPath){

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?";
        String orderBy = MediaStore.Images.Media.DATE_ADDED+" DESC";

        ArrayList<File> images = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection,new String[]{bucketPath}, orderBy);

        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                file = new File(path);
                if (file.exists() && !images.contains(path)) {
                    images.add(file);
                }
            }
            cursor.close();
        }

        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String [] videoProjection = {MediaStore.Video.Media.DATA};
        String videoSelection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME+" =?";
        String videoOrderBy = MediaStore.Video.Media.DATE_ADDED+" DESC";

        Cursor videoCursor = mContext.getContentResolver().query(videoUri, videoProjection, videoSelection,new String[]{bucketPath}, videoOrderBy);

        if(videoCursor != null){
            File file;
            while (videoCursor.moveToNext()){
                String path = videoCursor.getString(videoCursor.getColumnIndex(projection[0]));
                file = new File(path);
                if (file.exists() && !images.contains(path)) {
                    images.add(file);
                }
            }
            videoCursor.close();
        }
        return images;
    }


    public static String getVideoTimeDuration(Context context,File file){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.fromFile(file));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time );

        retriever.release();
        return convertMillieToHMmSs(timeInMillisec);
    }

    public static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        else {
            return String.format("%02d:%02d" , minute, second);
        }

    }

}
