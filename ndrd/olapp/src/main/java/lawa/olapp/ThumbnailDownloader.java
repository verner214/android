package lawa.olapp;

import java.io.IOException;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class ThumbnailDownloader<Handle> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    
    Handler mHandler;
    Map<Handle,String> requestMap = 
            Collections.synchronizedMap(new HashMap<Handle,String>());
    Handler mResponseHandler;
    Listener<Handle> mListener;
    File cacheDir;
    
    public interface Listener<Handle> {
        void onThumbnailDownloaded(Handle handle, Bitmap thumbnail);
    }
    
    public void setListener(Listener<Handle> listener) {
        mListener = listener;
    }

    public ThumbnailDownloader(File cacheDir, Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
        this.cacheDir = cacheDir; 
    }
    
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Handle handle = (Handle)msg.obj;
                    Log.i(TAG, "Got a request for url: " + requestMap.get(handle));
                    handleRequest(handle);
                }
            }
        };
    }

    private void handleRequest(final Handle handle) {
        try {
            final String url = requestMap.get(handle);
            if (url == null) 
                return;
            
            byte[] bitmapBytes = GetGson.getUrlBytes(cacheDir, url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            
            mResponseHandler.post(new Runnable() {
                public void run() {
                    if (requestMap.get(handle) != url)
                        return;
                    
                    requestMap.remove(handle);
                    mListener.onThumbnailDownloaded(handle, bitmap);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }
    
    public void queueThumbnail(Handle handle, String url) {
        requestMap.put(handle, url);
        
        mHandler
            .obtainMessage(MESSAGE_DOWNLOAD, handle)
            .sendToTarget();
    }
    
    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}
