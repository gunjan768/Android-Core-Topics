package com.example.asynctask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    private ImageView imageView;
    private static final int LOADER_ID = 1;
    private TextView httpTextView;
    ProgressBar progressBar;

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args)
    {
        Toast.makeText(this, "Loading Started", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.VISIBLE);

        return new ExampleAsyncTaskLoader(this, "https://www.zappycode.com");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data)
    {
        Toast.makeText(this, "Loading finished", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.GONE);
        httpTextView.setText(HtmlCompat.fromHtml(data, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader)
    {
        progressBar.setVisibility(View.GONE);

        Toast.makeText(this, "Loading is reset", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        httpTextView = findViewById(R.id.httpTextView);
        progressBar = findViewById(R.id.progress_bar);

        useAsyncTaskLoader();

        // To do a task using deprecated asyncTaskLoader uncomment the below line.
        // useDeprecatedAsyncTask();
    }

    private void useAsyncTaskLoader()
    {
        // What is the appropriate replacement of deprecated getSupportLoaderManager() ? Answer is LoaderManager.
        // getSupportLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this).forceLoad();

        // initLoader() : Ensures a loader is initialized and active. Remember to forcibly the load the Loader using forceLoad().
        LoaderManager.getInstance(this).initLoader(LOADER_ID, Bundle.EMPTY, this).forceLoad();
    }

    private void useDeprecatedAsyncTask()
    {
        HTMLContentDownloader task = new HTMLContentDownloader();
        String result = "";

        try
        {
            result = task.execute("https://www.zappycode.com").get();
        }
        catch(Exception error)
        {
            error.printStackTrace();
        }

        TextView httpTextView = findViewById(R.id.httpTextView);

        httpTextView.setText(HtmlCompat.fromHtml(result, HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Use above or below one to display the html code in mobile.
        // httpTextView.setText(Html.fromHtml(result, 1));
    }


    // ***************************************************** Using deprecated AsyncTask starts ***********************************************************


    public static class HTMLContentDownloader extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            URL url;
            StringBuilder result = new StringBuilder();
            HttpsURLConnection urlConnection = null;

            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.connect();

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
            catch(IOException e)
            {
                e.printStackTrace();

                return "Failed";
            }
        }
    }

    public static class ImageDownloader extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... urls)
        {
            try
            {
                URL url = new URL(urls[0]);

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStreamReader = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStreamReader);

                return bitmap;
            }
            catch (IOException e)
            {
                e.printStackTrace();

                return null;
            }
        }
    }

    public void downloadImageFromWeb(View view)
    {
        ImageDownloader task = new ImageDownloader();
        Bitmap myImage = null;

        try
        {
            myImage = task.execute("https://upload.wikimedia.org/wikipedia/en/a/aa/Bart_Simpson_200px.png").get();

            imageView.setImageBitmap(myImage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // *********************************************************** Deprecated AsyncTask finishes **********************************************************
}