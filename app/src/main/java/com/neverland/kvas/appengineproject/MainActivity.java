package com.neverland.kvas.appengineproject;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    public static final int BOOK_LIST_LOADER_ID = 101;

    private ListView mLisView;

    private ArrayAdapter<String> mArrayAdapter;

    private List<String> mData;

    private int mCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLisView = (ListView) findViewById(R.id.listView);
        mData = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData);
        mLisView.setAdapter(mArrayAdapter);

        getSupportLoaderManager().initLoader(BOOK_LIST_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_action:
                startLoader(BOOK_LIST_LOADER_ID);
                break;
            case R.id.new_book_action:
                addNewBook();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewBook() {
        CreateNewBookAsyncTask bookAsyncTask = new CreateNewBookAsyncTask(this);
        Pair<String, String> data = new Pair<>("Title " + mCounter, "Author " + mCounter);
        mCounter++;
        bookAsyncTask.execute(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLoader(BOOK_LIST_LOADER_ID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLoader(BOOK_LIST_LOADER_ID);
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new BookListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        if (loader.getId() == BOOK_LIST_LOADER_ID) {
            mData = data;
            mArrayAdapter.clear();
            mArrayAdapter.addAll(mData);
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    private void startLoader(int loaderId) {
        Loader loader = getSupportLoaderManager().getLoader(loaderId);
        if (loader != null) {
            loader.forceLoad();
        }
    }

    private void stopLoader(int loaderId) {
        Loader loader = getSupportLoaderManager().getLoader(loaderId);
        if (loader != null) {
            loader.cancelLoad();
        }
    }
}
