package com.mindorks.bootcamp.instagram.utils.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mindorks.bootcamp.instagram.utils.Compressor.getCompressed;

public class ImageCompressTask implements Runnable {

    private Context mContext;
    private List<String> originalPaths = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private List<File> result = new ArrayList<>();
    private IImageCompressTaskListener mIImageCompressTaskListener;


    public ImageCompressTask(Context context, String path, IImageCompressTaskListener compressTaskListener) {

        originalPaths.add(path);
        mContext = context;

        mIImageCompressTaskListener = compressTaskListener;
    }
    public ImageCompressTask(Context context, List<String> paths, IImageCompressTaskListener compressTaskListener) {
        originalPaths = paths;
        mContext = context;
        mIImageCompressTaskListener = compressTaskListener;
    }
    @Override
    public void run() {

        try {

            //Loop through all the given paths and collect the compressed file from Util.getCompressed(Context, String)
            for (String path : originalPaths) {
                File file = getCompressed(mContext, path);
                //add it!
                result.add(file);
            }
            //use Handler to post the result back to the main Thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if(mIImageCompressTaskListener != null)
                        mIImageCompressTaskListener.onComplete(result);
                }
            });
        }catch (final IOException ex) {
            //There was an error, report the error back through the callback
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mIImageCompressTaskListener != null)
                        mIImageCompressTaskListener.onError(ex);
                }
            });
        }
    }
}
