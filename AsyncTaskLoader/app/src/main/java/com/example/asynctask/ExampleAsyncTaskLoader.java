package com.example.asynctask;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ExampleAsyncTaskLoader extends AsyncTaskLoader<String>
{
    // AsyncTaskLoader is the loader equivalent of AsyncTask. AsyncTaskLoader provides a method, loadInBackground(), that runs on a separate thread. The
    // results of loadInBackground() are automatically delivered to the UI thread, by way of the onLoadFinished() LoaderManager callback. AsyncTaskLoader is
    // subclass of Loader. This class performs the same function as the AsyncTask, but a bit better, it can also be useful in handling configuration changes
    // (screen orientation).

    private String httpUrl = "";
    private static final String TAG = "ExampleAsyncTaskLoader";

    public ExampleAsyncTaskLoader(@NonNull Context context, String httpUrl)
    {
        super(context);

        this.httpUrl = httpUrl;
    }

    // Called on a worker thread to perform the actual load and to return the result of the load operation.
    @Nullable
    @Override
    public String loadInBackground()
    {
        URL url;
        StringBuilder result = new StringBuilder();
        HttpsURLConnection urlConnection = null;

        try
        {
            url = new URL(httpUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            int data = reader.read();

            while(data != -1)
            {
                char current = (char) data;
                result.append(current);

                data = reader.read();
            }

            return result.toString();
        }
        catch (IOException e)
        {
            // Will call the onCancelLoad() method. Plus as it abort the loadInBackground() method so it will also trigger cancelLoadInBackground().
            cancelLoad();

            return httpUrl;
        }
    }

    // onForceLoad() : Subclasses must implement this to take care of requests to forceLoad(). This will always be called from the process's main thread
    @Override
    protected void onForceLoad()
    {
        // Log.d(TAG, "onForceLoad ");

        super.onForceLoad();
    }

    // onLoadInBackground() calls loadInBackground(). This method is reserved for use by the loader framework. Subclasses should override loadInBackground()
    // instead of this method.
    @Nullable
    @Override
    protected String onLoadInBackground()
    {
        // Log.d(TAG, "onLoadInBackground");

        return super.onLoadInBackground();
    }

    // onCancelLoad() : Subclasses must implement this to take care of requests to cancelLoad(). This will always be called from the process's main thread.
    @Override
    protected boolean onCancelLoad()
    {
        // Log.d(TAG, "onCancelLoad");

        return super.onCancelLoad();
    }

    // Called on the main thread to abort a load in progress. Override this method to abort the current invocation of loadInBackground() that is running in
    // the background on a worker thread. This method should do nothing if loadInBackground() has not started running or if it has already finished.
    @Override
    public void cancelLoadInBackground()
    {
        // Log.d(TAG, "cancelLoadInBackground is triggered");

        super.cancelLoadInBackground();
    }

    // onCanceled() : Called if the task was canceled before it was completed. Gives the class a chance to clean up post-cancellation and to properly dispose
    // of the result. data ( parameter ) : The value that was returned by loadInBackground(), or null if the task threw OperationCanceledException.
    @Override
    public void onCanceled(@Nullable String data)
    {
        super.onCanceled(data);
    }
}