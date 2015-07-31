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

	public static String PostForm(Form form) 
	{
		String errormsg = null;
		try
		{
			//Log.d(TAG, "images length=" + form.getImgLarge().length + "," + form.getImgThumbnail().length);
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost("http://vernerolapp.azurewebsites.net/newedit");
//		    HttpPost post = new HttpPost("http://vernerolapp.azurewebsites.net/new");
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
//lägg till hide-checkbox här
		
		    HttpEntity entity = entityBuilder.build();
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
            Log.e(TAG, "Error uploading image", e);
            Log.e(TAG, errormsg, e);
		}
		return errormsg;
	}//postImage
}
