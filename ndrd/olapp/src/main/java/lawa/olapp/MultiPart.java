package lawa.olapp;

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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class MultiPart {
    private final static String TAG = "MulitiPart";

	public static String PostImage(Form form) 
	{
		String result = "resultat";
		try
		{
			Log.d(TAG, "images length=" + form.imgLarge.length + "," + form.imgThumbnail.length);
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost("http://vernerolapp.azurewebsites.net/new");
//		    HttpPost post = new HttpPost("http://vernerphoto.azurewebsites.net/upload");
//		    HttpPost post = new HttpPost("http://10.0.2.2:8080/formhandler");
			//String boundary = "-------------" + System.currentTimeMillis();
			//post.setHeader("Content-type", "multipart/form-data; boundary="+boundary);		
			
		    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		    entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		    //entityBuilder.setBoundary(boundary);
//boundary skapas automatiskt och det behöver man inte ange. gäller både header och parts		
//		    entityBuilder.addTextBody(USER_ID, userId);
		
//		    entityBuilder.addBinaryBody("fileUploaded", image);
//formidable ville inte känna till filen förrän contenttype och filnamnet fanns med i body (eller om det var något av dessa som gjorde skilnaden)
//		    entityBuilder.addBinaryBody("fileUploaded", image, ContentType.create("image/jpeg"), "myfilenme.jpg");
		    entityBuilder.addBinaryBody("img", form.imgLarge, ContentType.create("image/jpeg"), "form_imgLarge.jpg");
		    entityBuilder.addBinaryBody("thumb", form.imgThumbnail, ContentType.create("image/jpeg"), "form_imgThumbnail.jpg");
		
		    HttpEntity entity = entityBuilder.build();
		    post.setEntity(entity);
		    HttpResponse response = client.execute(post);
		    HttpEntity httpEntity = response.getEntity();
		    result = EntityUtils.toString(httpEntity);
           // Toast.makeText(context, "multipart result=" + result, Toast.LENGTH_SHORT).show();
		    Log.d(TAG, "result2bilder=" + result);
		}
		catch (Exception e)
		{
            //Toast.makeText(context, "exception=" + e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error uploading image", e);
            Log.e(TAG, "Error uploading image" + e.toString(), e);
		}
		return result;
	}//postImage
}
