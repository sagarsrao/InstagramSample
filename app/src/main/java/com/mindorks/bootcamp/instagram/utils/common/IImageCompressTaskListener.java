package com.mindorks.bootcamp.instagram.utils.common;

import java.io.File;
import java.util.List;

public interface  IImageCompressTaskListener {

    public void onComplete(List<File> compressed);
    public void onError(Throwable error);
}
