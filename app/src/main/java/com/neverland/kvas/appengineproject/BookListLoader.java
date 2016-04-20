package com.neverland.kvas.appengineproject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.neverland.kvas.backend.bookApi.BookApi;
import com.neverland.kvas.backend.bookApi.model.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kvas on 20/04/2016.
 */
public class BookListLoader extends AsyncTaskLoader<List<String>> {

    private static BookApi mMyApiService = null;

    public BookListLoader(Context context) {
        super(context);
    }

    @Override
    public List<String> loadInBackground() {
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
            List<Book> bookList = mMyApiService.list().execute().getItems();
            List<String> result = new ArrayList<>();
            for (Book book : bookList) {
                result.add("\"" + book.getTitle() + "\"" + " by " + book.getAuthor());
            }
            return result;
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }
}
