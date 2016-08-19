package com.czh.testmpeg.videocompress;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.czh.testmpeg.R;
import com.czh.testmpeg.databinding.ActivityMainBinding;
import com.czh.testmpeg.permission.PermissionsActivity;
import com.czh.testmpeg.permission.PermissionsChecker;
import com.czh.testmpeg.videorecord.CameraActivity;


/**
 * http://androidwarzone.blogspot.hk/2011/12/ffmpeg4android.html
 * Video Compress:
 // simple regular commad
 ffmpeg -y -i /sdcard/videokit/in.mp4 -strict experimental -s 160x120 -r 25 -vcodec mpeg4 -b 150k -ab 48000 -ac 2 -ar 22050 /sdcard/videokit/out.mp4

 // compress with h264 (to support chrome)
 ffmpeg -y -i /sdcard/videokit/in.mp4 -strict experimental -vcodec libx264 -preset ultrafast -crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 320x240 -aspect 4:3 /sdcard/videokit/out3.mp4
 // As complex command, don't forget to use setCommandComplex(complexCommand)
 // Use this format to support files that contains spaces and special characters
 String[] complexCommand = {"ffmpeg","-y" ,"-i", "/sdcard/video kit/in.mp4","-strict","experimental","-s", "160x120","-r","25", "-vcodec", "mpeg4", "-b", "150k", "-ab","48000", "-ac", "2", "-ar", "22050", "/sdcard/video kit/out.mp4"};
 The parameters that control the quality are the -s (resolution, currently set on 160x120) and the -b (the bitrate, currently set on 150k).
 Increase them, e.g -s 480x320
 And -b 900k
 To improve quality (and get less compression).﻿

 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Compressor mCompressor;
    ActivityMainBinding mBinding;
    private String currentInputVideoPath = "/mnt/sdcard/videokit/in.mp4";
    private String currentOutputVideoPath = "/mnt/sdcard/videokit/out.mp4";
    String cmd = "-y -i "+currentInputVideoPath+" -strict -2 -vcodec libx264 -preset ultrafast " +
            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 480x320 -aspect 16:9 "+currentOutputVideoPath;
    //相机权限,录制音频权限,读写sd卡的权限,都为必须,缺一不可
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_FOR_PERMISSIONS = 0;//
    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 1;//录制视频请求码
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED = 2;//视频录制成功
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_CANCEL = 4;//取消录制
    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径
    private static final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.startActivityForResult(MainActivity.this, REQUEST_CODE_FOR_RECORD_VIDEO);
            }
        });


        mBinding.etCommand.setText(cmd);
        mBinding.btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String command = mBinding.etCommand.getText().toString();
                if (TextUtils.isEmpty(command)) {
                    Toast.makeText(MainActivity.this, getString(R.string.compree_please_input_command)
                            , Toast.LENGTH_SHORT).show();
                } else {
                    execCommand(command);
                }
            }
        });
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.v(TAG, "load library succeed");
                textAppend(getString(R.string.compress_load_library_succeed));
            }

            @Override
            public void onLoadFail(String reason) {
                Log.i(TAG,"load library fail:"+reason);
                textAppend(getString(R.string.compress_load_library_failed,reason));
            }
        });


        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE_FOR_PERMISSIONS, PERMISSIONS);
        }

    }


    private void execCommand(String cmd) {
        mCompressor.execCommand(cmd,new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                Log.i(TAG,"success "+message);
                textAppend(getString(R.string.compress_succeed));
                Toast.makeText(getApplicationContext(), R.string.compress_succeed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExecFail(String reason) {
                Log.i(TAG,"fail "+reason);
                textAppend(getString(R.string.compress_failed,reason));
            }

            @Override
            public void onExecProgress(String message) {
                Log.i(TAG,"progress "+message);
              textAppend(getString(R.string.compress_progress,message));

            }
        });
    }

    private void textAppend(String text) {
        if (!TextUtils.isEmpty(text)) {
            mBinding.tvLog.append(text+"\n");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_PERMISSIONS) {
            //权限申请
            if (PermissionsActivity.PERMISSIONS_DENIED == resultCode) {
                //权限未被授予，退出应用
                finish();
            } else if (PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
                //权限被授予
                //do nothing
            }
        } else if (requestCode == REQUEST_CODE_FOR_RECORD_VIDEO) {
            //录制视频
            if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED) {
                //录制成功
                String videoPath = data.getStringExtra(INTENT_EXTRA_VIDEO_PATH);
                if (!TextUtils.isEmpty(videoPath)) {
                    currentInputVideoPath = videoPath;
                }
            } else if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {
                //录制失败
            }
        }

    }




}
