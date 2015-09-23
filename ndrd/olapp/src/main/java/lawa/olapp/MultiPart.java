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
		entityBuilder.addTextBody("id", b.getId());		
		entityBuilder.addTextBody("beerName", b.getBeerName());		
		entityBuilder.addTextBody("beerStyle", b.getBeerStyle());		
		entityBuilder.addTextBody("og", b.getOg());		
		entityBuilder.addTextBody("fg", b.getFg());		
		entityBuilder.addTextBody("description", b.getDescription());		
		entityBuilder.addTextBody("recipe", b.getRecipe());		
		entityBuilder.addTextBody("comments", b.getComments());		
		entityBuilder.addTextBody("brewingDate", b.getBrewingDate());		
		entityBuilder.addTextBody("people", b.getPeople());		
		entityBuilder.addTextBody("place", b.getPlace());		
		entityBuilder.addTextBody("hide", b.getHide() ? "true" : "false");		
		
		HttpEntity entity = entityBuilder.build();
		return PostMultipart("http://vernerolapp.azurewebsites.net/newedit", entity);
	}//postForm
	
	public static String PostFormGallery(FormGallery form) 
	{						
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		entityBuilder.addBinaryBody("img", form.getImgLarge(), ContentType.create("image/jpeg"), "form_imgLarge.jpg");
		entityBuilder.addBinaryBody("thumb", form.getImgThumbnail(), ContentType.create("image/jpeg"), "form_imgThumbnail.jpg");
		entityBuilder.addTextBody("id", form.getBrygdId());		
		entityBuilder.addTextBody("text", form.getText());		
		
		HttpEntity entity = entityBuilder.build();
		return PostMultipart("http://vernerolapp.azurewebsites.net/gallerynew", entity);
	}

	public static String PostFormGalleryEdit(FormGalleryEdit form) 
	{						
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		entityBuilder.addTextBody("id", form.getBrygdId());		
		entityBuilder.addTextBody("text", form.getText());		
		entityBuilder.addTextBody("imgURL", form.getImgURL());		
		entityBuilder.addTextBody("hide", form.getHide() ? "true" : "false");		
		
		HttpEntity entity = entityBuilder.build();
		return PostMultipart("http://vernerolapp.azurewebsites.net/galleryedit", entity);
	}
}
