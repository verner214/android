package lawa.camera;

import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.*;
import org.apache.http.util.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class MultiPart {
    private final static String TAG = "MulitiPart";

	public static void PostImage(Context context, byte[] image) 
	{
		try
		{
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost("http://vernerphoto.azurewebsites.net/upload");
		
		    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		    entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
//		    entityBuilder.addTextBody(USER_ID, userId);
		
		    if (image != null)
		    {
		        entityBuilder.addBinaryBody("fileUploaded", image);
		    }
		
		    HttpEntity entity = entityBuilder.build();
		    post.setEntity(entity);
		    HttpResponse response = client.execute(post);
		    HttpEntity httpEntity = response.getEntity();
		    String result = EntityUtils.toString(httpEntity);
            Toast.makeText(context, "multipart result=" + result, Toast.LENGTH_SHORT).show();
//		    Log.v("result", result);
		}
		catch (Exception e)
		{
            Toast.makeText(context, "exception=" + e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error uploading image", e);
		}
	}
}
