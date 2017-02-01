package lawa.olapp;

import java.io.IOException;
import java.io.File;
//import java.io;
import android.util.Log;

public class WriteToFile {
    private final static String TAG = "WriteToFile";

public static void writeToFile(String tg, String data)
{
    // Get the directory for the user's public pictures directory.
        Log.d(TAG, "enter WriteToFile: ");
    final File path =
        android.os.Environment.getExternalStoragePublicDirectory
        (
            //Environment.DIRECTORY_PICTURES
            android.os.Environment.DIRECTORY_DCIM + "/olapp/"
        );
        Log.d(TAG, "path: " + path);

    // Make sure the path directory exists.
    if(!path.exists())
    {
        Log.d(TAG, "path does not exists ");
        // Make it, if it doesn't exit
        path.mkdirs();
    }

    final File file = new File(path, "log.txt");

    // Save your stream, don't forget to flush() it before closing it.

    try
    {
        file.createNewFile();
        java.io.FileOutputStream fOut = new java.io.FileOutputStream(file, true);
        java.io.OutputStreamWriter myOutWriter = new java.io.OutputStreamWriter(fOut);
        myOutWriter.append(new java.util.Date().toString() + ", " + tg + ", "  + data + System.lineSeparator());

        myOutWriter.close();
/*
OutputStreamWriter writer = new OutputStreamWriter(
                  new FileOutputStream("x.txt", true), "UTF-8");
            BufferedWriter fbw = new BufferedWriter(writer);
            fbw.write("append txt...");
            fbw.newLine();
            fbw.close();
*/
        fOut.flush();
        fOut.close();
        Log.d(TAG, "klart!");
    }
    catch (IOException e)
    {
        Log.e("Exception", "File write failed: " + e.toString());
    } 
}
}//class