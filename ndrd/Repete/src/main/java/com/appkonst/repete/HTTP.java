package com.appkonst.repete;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by lars on 2017-02-16.
 */

//class för att  1. hämta json-struct som sparas till fil 2. posta image 3. posta comments (bl.a.?) för merge
public class HTTP {
    private final static String TAG = "HTTP";

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }//convertInputStreamToString

    public String GET(String url) {
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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            System.out.println("InputStream:" + e.getLocalizedMessage());
        }

        return result;
    }//GET

    //public för att den används av thumbnaildownloader och i asynctask. hör kanske hemma i annan klass.
    public static byte[] getUrlBytes(File cacheDir, String urlSpec) throws IOException {

        String fileName = urlSpec.substring(urlSpec.lastIndexOf("/"));
        File cachedImgFile = null;
        if (cacheDir != null) {
            cachedImgFile = new File(cacheDir, fileName);
            //om filen finns, returnera innehållet
            if (cachedImgFile.exists()) {
                return file2bytes(cachedImgFile);
            }
        }

//filen fanns inte cachad. hämta från molnet och spara i cachen
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

//spara bilden i cachen
            if (cachedImgFile != null) {
//                File file;
                FileOutputStream outputStream;
//                String fileName = urlSpec.substring(urlSpec.lastIndexOf("/"));
                try {
//                    file = new File(cacheDir, fileName);
                    outputStream = new FileOutputStream(cachedImgFile);
                    outputStream.write(out.toByteArray());
                    outputStream.close();

                } catch (IOException e) {
                    Log.e(TAG, "fel vid spara en fil", e);
                }
            }
//spara bilden i cachen SLUT

            //WriteToFile.writeToFile(TAG, "getUrlBytes, 5,  " + urlSpec);
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }//getUrlBytes


    private static byte[] file2bytes(File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }

        return buffer;
    }//file2bytes

    private static String PostMultipart(String url, HttpEntity entity) {
        String errormsg = null;
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

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
//id, imgtype (question|answer), image
    public static String postImage(String id, byte[] byteArr, boolean question) {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addBinaryBody("img", byteArr, ContentType.create("image/jpeg"), "form_imgLarge.jpg");

        //ContentType contentType = ContentType.create(org.apache.http.protocol.HTTP.PLAIN_TEXT_TYPE, org.apache.http.protocol.HTTP.UTF_8);
        entityBuilder.addTextBody("id", id);
        entityBuilder.addTextBody("imgtype", question ? "question" : "answer");

        HttpEntity entity = entityBuilder.build();
        return PostMultipart("http://repete.azurewebsites.net/image", entity);
    }
    //id, comments
    public static String postComments(String id, String comments) {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        ContentType contentType = ContentType.create(org.apache.http.protocol.HTTP.PLAIN_TEXT_TYPE, org.apache.http.protocol.HTTP.UTF_8);
        entityBuilder.addTextBody("id", id);
        entityBuilder.addTextBody("comments", comments, contentType);

        HttpEntity entity = entityBuilder.build();
        return PostMultipart("http://repete.azurewebsites.net/newedit", entity);
    }
}
