package lawa.olapp;
         
import java.io.File;
    
public class ImgCacheParam {
	private String mUrl;
	private File mCacheDir;
	
	public ImgCacheParam(File cacheDir, String url) {
		mUrl = url;
		mCacheDir = cacheDir;
	}
	
	public File getCacheDir() {
		return mCacheDir;
	}

	public String getUrl() {
		return mUrl;
	}
}
