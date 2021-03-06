import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//nu försök med json som tidiagare hämtats från azure

public class GsonTest4 {

	private class Item {
		private String thumbURL;
		private String mediumURL;
		private String description;
		private String textarea;
		
		private Item() {}
		
		public Item(String thumbURL, String mediumURL, String description, String textarea) {
			this.thumbURL = thumbURL;
			this.mediumURL = mediumURL;
            this.description = description;
            this.textarea = textarea;
		}
		
		@Override
		public String toString() {
			return thumbURL + "\n" + mediumURL + "\n" + description + "\n" + textarea + "\n";
		}
	}

	private class PClass {
	    private Item[] value;
	 
	    private PClass() {
	    }
	 
	    public PClass(Item[] value) {
	        this.value = value;
	    }
	 
	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
			for (Item i : value) {
				sb.append(i.toString() + "\n");
			}
						
	        //sb.append("Name: " + name + " ");
	        return sb.toString();
	    }
	}
	
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpGet get = new HttpGet(url);
            get.setHeader("Accept", "application/json;odata=nometadata");
            HttpResponse httpResponse = httpclient.execute(get);
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            System.out.println("InputStream:" + e.getLocalizedMessage());
        }
 
        return result;
    }
 
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
 
	public static void main(String[] args) {
//        System.setProperty("javax.net.ssl.trustStore","C:/own/java/javax86/jdk1.7.0_79/jre/lib/security/cacerts");
		String json = GET("https://portalvhdsgfh152bhy290k.table.core.windows.net/photos?st=2015-04-02T09%3A13%3A00Z&se=2017-02-24T21%3A33%3A00Z&sp=r&sv=2014-02-14&tn=photos&sig=f8Eo%2FmE3SxQE1TstvG5memvKfmTxyMszMTOa27AQ0WQ%3D");
					
		Gson gson = new Gson();
        /*
		String json = "{ \"value\": [{ \"PartitionKey\": \"hometasks\", \"RowKey\": \"0\", \"Timestamp\": \"2015-03-23T21:47:34.3221026Z\", \"description\": \"take out the trash\", \"dueDate\": \"2015-07-20T00:00:00Z\" },"+
		"{ \"PartitionKey\": \"photos\",\"RowKey\":\"079d9bce-8542-42ee-bb55-644eeed26e78\", \"Timestamp\": \"2015-03-24T21:42:57.0755037Z\", \"description\": \"take out the trash\","+
		" \"imgURL\": \"https://portalvhdsgfh152bhy290k.blob.core.windows.net/photos/IMG_1003.JPG\",\"thumbURL\":\"https://portalvhdsgfh152bhy290k.blob.core.windows.net/photos/t_IMG_1003.JPG\" },{ \"PartitionKey\": \"photos\", "+
		"\"RowKey\": \"0fae2d59-0b98-470b-b3ee-c60f15c9416a\", \"Timestamp\": \"2015-03-24T18:19:18.3864847Z\", \"description\": \"take out the trash\", \"imgURL\":\"https://portalvhdsgfh152bhy290k.blob.core.windows.net/photos/IMG_1002.JPG\", "+
		"\"thumbURL\": \"https://portalvhdsgfh152bhy290k.blob.core.windows.net/photos/t_IMG_1002.JPG\" }]}";
		*/	

		PClass johnDoe = gson.fromJson(json, PClass.class);
		System.out.println(johnDoe.toString());
		
		
	}
}
