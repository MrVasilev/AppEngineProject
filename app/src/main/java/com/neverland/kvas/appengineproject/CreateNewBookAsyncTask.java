package com.neverland.kvas.appengineproject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.neverland.kvas.backend.bookApi.BookApi;
import com.neverland.kvas.backend.bookApi.model.Book;

import java.io.IOException;

/**
 * Created by kvas on 19/04/2016.
 */
public class CreateNewBookAsyncTask extends AsyncTask<Pair<String, String>, Void, Boolean> {

    private static BookApi mMyApiService = null;
    private Context mContext;

    public CreateNewBookAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Pair<String, String>... params) {
        if (mMyApiService == null) {
            BookApi.Builder builder = new BookApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            mMyApiService = builder.build();
        }

        try {
            Pair<String, String> bookData = params[0];
            Book book = new Book();
            book.setTitle(bookData.first);
            book.setAuthor(bookData.second);
            mMyApiService.insert(book).execute();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        String message = (result) ? "Successfully add new book" : "Failed to add new book";
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
