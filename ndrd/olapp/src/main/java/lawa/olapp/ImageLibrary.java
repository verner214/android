package lawa.olapp;
//http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/
//http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app?rq=1 läs siamii

import java.io.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import android.util.Log;
import android.net.Uri;
import android.content.Context;


//klassen har funktioner som returnerar antingen Bitmap (laddas i imgViews) eller byte[] (skickas till servern)
public class ImageLibrary {
	private static String TAG = "ImageLibrary";
	
	private static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, boolean crop) {
	    if (!crop) {
	        final float srcAspect = (float)srcWidth / (float)srcHeight;
	        final float dstAspect = (float)dstWidth / (float)dstHeight;
	 
	        if (srcAspect > dstAspect) {
	            return srcWidth / dstWidth;
	        } else {
	            return srcHeight / dstHeight;
	        }
	    } else {
	        final float srcAspect = (float)srcWidth / (float)srcHeight;
	        final float dstAspect = (float)dstWidth / (float)dstHeight;
	 
	        if (srcAspect > dstAspect) {
	            return srcHeight / dstHeight;
	        } else {
	            return srcWidth / dstWidth;
	        }
	    }
	}	
	
	//returnerar nedladdad bitmap, ev. skalad till viss del. ej beskuren.
	private static Bitmap decodeFile(Context ctxt, Uri imgUri, int dstWidth, int dstHeight, boolean crop) {
		try {
			//hämta width och height
		    Options options = new Options();
		    options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(ctxt.getContentResolver().openInputStream(imgUri), null, options);
			
			//hämta bilden, skalad på vägen.
		    options.inJustDecodeBounds = false;
		    options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, crop);
			return BitmapFactory.decodeStream(ctxt.getContentResolver().openInputStream(imgUri), null, options);
		} catch (Exception e) {
            Log.e(TAG, "kunde inte göra en bitmap av en URI", e);
			return null;						
		}
	}
	 
	//beskär en bitmap så att proportionerna stämmer med dstWidth och dstHeight
	private static Bitmap Crop(Bitmap src, int dstWidth, int dstHeight) {
		double srcRatio = (double) src.getWidth() / (double) src.getHeight();
		double dstRatio = (double) dstWidth / (double) dstHeight;
		
		//on src-bilden är bredare än önskat, skär bort sidokanterna. annars botten och toppen
		if (srcRatio > dstRatio) {
			int newWidth = (int) ((src.getWidth() * dstRatio / srcRatio) + 0.5);
			return Bitmap.createBitmap(src, (int) ((src.getWidth() - newWidth) / 2), 0, newWidth, src.getHeight());
		}  else {
			int newHeight = (int) ((src.getHeight() * srcRatio / dstRatio) + 0.5);
			return Bitmap.createBitmap(src, 0, (int) ((src.getHeight() - newHeight) / 2), src.getWidth(), newHeight);
		}
	}
	
	//hämtar, beskär och skalar en bild så att den fyller width och height (crop == true). ELLER för crop == false:   
	//hämtar, beskär och skalar en bild så att antingen dstWidth eller dstHeight blir width eller height. den andra blir mindre eller lika.  
	//topp-bilden ska vara 640*360.
	public static Bitmap Uri2Bmp(Context ctxt, Uri imgUri, int dstWidth, int dstHeight, boolean crop) {		
		//ta reda på width, height och beräkna scaling faktor. ladda ner.
		Bitmap unscaledBitmap = decodeFile(ctxt, imgUri, dstWidth, dstHeight, crop);
		
		//bekär bilden vid crop: public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height)
		if (crop) {
			unscaledBitmap = Crop(unscaledBitmap, dstWidth, dstHeight);
		}
		
		//skala: public static Bitmap createScaledBitmap (Bitmap src, int dstWidth, int dstHeight, boolean filter)
		return Bitmap.createScaledBitmap(unscaledBitmap, dstWidth, dstHeight, true);
	}
	
	//beskär om så behövs
	public static Bitmap createThumbnail(Bitmap src, int dstWidth, int dstHeight) {
		return Bitmap.createScaledBitmap(ImageLibrary.Crop(src, dstWidth, dstHeight), dstWidth, dstHeight, true);
	}

	//returnerar en bytearray likadan som den som skickas i ett HTML-formulär om man laddar upp en jpg-fil (<input type="file")
	public static byte[] Bmp2Jpg(Bitmap bm, int quality) {
		try {
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.JPEG, quality, os);
			byte[] imgArray = os.toByteArray();
	        os.close();
			return imgArray;
		} catch (Exception e) {
			Log.e(TAG, "kunde inte omvandla en bitmap till en jpg", e);
			return null;
		}
	}
		
}