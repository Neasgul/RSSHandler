package com.ensi.pierre.rsshandler;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyRSSsaxHandler handler = new MyRSSsaxHandler(this);
        handler.setUrl("http://www.nasa.gov/rss/dyn/image_of_the_day.rss");
        Toast.makeText(this,"chargement image :"+handler.getNumItem(), Toast.LENGTH_LONG).show();

        new DownloadRSSTask(this, 0).execute(handler);

    }

    private void setRSSField(String title, String date, String description, Bitmap image) {
        ((TextView)findViewById(R.id.imageTitle)).setText(title);
        ((TextView)findViewById(R.id.imageDate)).setText(date);
        ((TextView)findViewById(R.id.imageDescription)).setText(description);
        ((ImageView)findViewById(R.id.imageDisplay)).setImageBitmap(image);

    }

    public class DownloadRSSTask extends AsyncTask<MyRSSsaxHandler,Void,MyRSSsaxHandler> {

        private MainActivity activity;
        private int item;
        public DownloadRSSTask(MainActivity activity, int item) {
            this.activity = activity;
            this.item = item;
        }

        @Override
        protected MyRSSsaxHandler doInBackground(MyRSSsaxHandler... myRSSsaxHandlers) {
            myRSSsaxHandlers[0].processFeed(item);
            return myRSSsaxHandlers[0];
        }

        @Override
        protected void onPostExecute(MyRSSsaxHandler myRSSsaxHandler) {
            Bitmap image = myRSSsaxHandler.getImage();
            RssItem item = myRSSsaxHandler.itemList.get(this.item);
            activity.setRSSField(item.getTitle().toString(),item.getDate().toString(),item.getDescription().toString(),image);
        }
    }
}
