package lawa.camera;
//http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/
//http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app?rq=1 läs siamii

//klassen har funktioner som returnerar antingen Bitmap (laddas i imgViews) eller byte[] (skickas till servern)
public static class ImageLibrary {
	
	//returnerar en bytearray likadan som den som skickas i ett HTML-formulär om man laddar upp en jpg-fil (<input type="file")
	public static byte[] Bmp2Jpg(Bitmap bm) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, os);
		byte[] imgArray = os.toByteArray();
        os.close();
		return imgArray;
	}
	
	//hämtar, beskär och skalar en bild så att den fyller width och height (ScalingLogic.CROP). ELLER för ScalingLogic.FIT   
	//hämtar, beskär och skalar en bild så att antingen dstWidth eller dstHeight blir width eller height. den andra blir mindre eller lika.  
	public static Bitmap Uri2BmpCrop(ContentResolver cr, Uri imageUri, int dstWidth, int dstHeight, ScalingLogic) {		
	}
	
	//beskär och skalar en bitmap. används för att göra thumbnails. (undrar om det inte finns en sådan?)
	public static Bitmap Crop(Bitmap src, int dstWitdh, int dstHeight) {
		
	}
	
}