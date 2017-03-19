package lawa.olapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import android.os.AsyncTask;
import android.os.Build;

public class MultiPart {
	public static boolean isOnline(Context ctx) {
		ConnectivityManager cm =
				(ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private static String getDeviceInfo() {
        final StringBuffer report = new StringBuffer();
        final String lineSeperator = "-------------------------------\n\n";
        report.append("--------- Device ---------\n\n");
        report.append("Brand: ");
        report.append(Build.BRAND);
        report.append("\n");
        report.append("Device: ");
        report.append(Build.DEVICE);
        report.append("\n");
        report.append("Model: ");
        report.append(Build.MODEL);
        report.append("\n");
        report.append("Id: ");
        report.append(Build.ID);
        report.append("\n");
        report.append("Product: ");
        report.append(Build.PRODUCT);
        report.append("\n");
        report.append(lineSeperator);
        report.append("--------- Firmware ---------\n\n");
        report.append("SDK: ");
        report.append(Build.VERSION.SDK);
        report.append("\n");
        report.append("Release: ");
        report.append(Build.VERSION.RELEASE);
        report.append("\n");
        report.append("Incremental: ");
        report.append(Build.VERSION.INCREMENTAL);
        report.append("\n");
        report.append(lineSeperator);
        return report.toString();		
	}
	
    private static class AsyncPostLog extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... errors) {
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	
			ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
			entityBuilder.addTextBody("error", errors[0]);		
			entityBuilder.addTextBody("device", getDeviceInfo());		
			
			HttpEntity entity = entityBuilder.build();
			PostMultipart("http://vernerolapp.azurewebsites.net/lognew", entity);
			return null;
			//return new Void();
        }
    }//AsyncPostLog
	
	public static void sendLogAsync(String error) {
		new MultiPart.AsyncPostLog().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, error);
	}
	
    private final static String TAG = "MulitiPart";

	private static String PostMultipart(String url, HttpEntity entity) {
		String errormsg = null;
		try
		{
			//Log.d(TAG, "images length=" + form.getImgLarge().length + "," + form.getImgThumbnail().length);
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(url);
//		    HttpPost post = new HttpPost("http://vernerolapp.azurewebsites.net/new");
//		    HttpPost post = new HttpPost("http://vernerphoto.azurewebsites.net/upload");
//		    HttpPost post = new HttpPost("http://10.0.2.2:8080/formhandler");
			//String boundary = "-------------" + System.currentTimeMillis();
			//post.setHeader("Content-type", "multipart/form-data; boundary="+boundary);		
			
		    post.setEntity(entity);
		    HttpResponse response = client.execute(post);
		    HttpEntity httpEntity = response.getEntity();
		    String postResult = EntityUtils.toString(httpEntity);
           // Toast.makeText(context, "multipart result=" + result, Toast.LENGTH_SHORT).show();
		    Log.d(TAG, "result from post:" + postResult);
		}
		catch (Exception e)
		{
            //Toast.makeText(context, "exception=" + e.toString(), Toast.LENGTH_LONG).show();
			errormsg = "Error uploading form, " + e.toString();
            Log.e(TAG, "Error uploading multipartform ", e);
            Log.e(TAG, errormsg, e);
		}
		return errormsg;
	}

	public static String PostForm(Form form) 
	{
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		if (form.getImgLarge() != null) {
			entityBuilder.addBinaryBody("img", form.getImgLarge(), ContentType.create("image/jpeg"), "form_imgLarge.jpg");
			entityBuilder.addBinaryBody("thumb", form.getImgThumbnail(), ContentType.create("image/jpeg"), "form_imgThumbnail.jpg");
		}
		Brygd b = form.getBrygd();
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		entityBuilder.addTextBody("id", b.getId());		
		entityBuilder.addTextBody("beerName", b.getBeerName(), contentType);		
		entityBuilder.addTextBody("beerStyle", b.getBeerStyle(), contentType);		
		entityBuilder.addTextBody("og", b.getOg());		
		entityBuilder.addTextBody("fg", b.getFg());		
		entityBuilder.addTextBody("description", b.getDescription(), contentType);		
		entityBuilder.addTextBody("recipe", b.getRecipe(), contentType);		
		entityBuilder.addTextBody("comments", b.getComments(), contentType);		
		entityBuilder.addTextBody("brewingDate", b.getBrewingDate(), contentType);		
		entityBuilder.addTextBody("people", b.getPeople(), contentType);		
		entityBuilder.addTextBody("place", b.getPlace(), contentType);		
		entityBuilder.addTextBody("hide", b.getHide() ? "true" : "false");		
		entityBuilder.addTextBody("demo", BrygdLab.getSourceIsDemo() ? "true" : "false");				
		
		HttpEntity entity = entityBuilder.build();
		return PostMultipart("http://vernerolapp.azurewebsites.net/newedit", entity);
	}//postForm
	
	public static String PostFormGallery(FormGallery form) 
	{						
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		entityBuilder.addBinaryBody("img", form.getImgLarge(), ContentType.create("image/jpeg"), "form_imgLarge.jpg");
		entityBuilder.addBinaryBody("thumb", form.getImgThumbnail(), ContentType.create("image/jpeg"), "form_imgThumbnail.jpg");
		entityBuilder.addTextBody("id", form.getBrygdId());		
		entityBuilder.addTextBody("text", form.getText(), contentType);		
		entityBuilder.addTextBody("demo", BrygdLab.getSourceIsDemo() ? "true" : "false");				
		
		HttpEntity entity = entityBuilder.build();
		return PostMultipart("http://vernerolapp.azurewebsites.net/gallerynew", entity);
	}

	public static String PostFormGalleryEdit(FormGalleryEdit form) 
	{						
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		entityBuilder.addTextBody("id", form.getBrygdId());		
		entityBuilder.addTextBody("text", form.getText(), contentType);		
		entityBuilder.addTextBody("imgURL", form.getImgURL());		
		entityBuilder.addTextBody("hide", form.getHide() ? "true" : "false");		
		entityBuilder.addTextBody("demo", BrygdLab.getSourceIsDemo() ? "true" : "false");				
		
		HttpEntity entity = entityBuilder.build();
		return PostMultipart("http://vernerolapp.azurewebsites.net/galleryedit", entity);
	}
}
