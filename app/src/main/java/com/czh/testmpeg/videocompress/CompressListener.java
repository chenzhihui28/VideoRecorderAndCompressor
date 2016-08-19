package com.czh.testmpeg.videocompress;

/**
 * Created by karan on 13/2/15.
 */
public interface CompressListener {
    public void onExecSuccess(String message);
    public void onExecFail(String reason);
    public void onExecProgress(String message);
}
