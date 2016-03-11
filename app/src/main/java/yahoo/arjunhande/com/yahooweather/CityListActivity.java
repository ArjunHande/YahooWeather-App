package yahoo.arjunhande.com.yahooweather;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import yahoo.arjunhande.com.yahooweather.data.Channel;
import yahoo.arjunhande.com.yahooweather.data.Item;
import yahoo.arjunhande.com.yahooweather.listener.YahooWeatherListener;
import yahoo.arjunhande.com.yahooweather.service.YahooWeatherService;

public class CityListActivity extends AppCompatActivity implements YahooWeatherListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private YahooWeatherService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherService = new YahooWeatherService(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherService.refreshWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(ArrayList<Channel> channel) {
        // specify an adapter (see also next example)

        String[] myDataset = null;
        if(channel == null) {
            onFailure();
        }
        else
        {
            myDataset = new String[channel.size()];
            for(int i = 0; i < channel.size(); i++) {
                Item item = channel.get(i).getItem();
                String temperatureLabel = "" + item.getCondition().getTemperature();
                String unitLabel = "" + channel.get(i).getUnits().getTemperature();
                myDataset[i] = temperatureLabel + unitLabel;
                mAdapter = new CityListAdapter(myDataset);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

//        temperatureTextView.setText(temperatureLabel);


    }

    @Override
    public void onFailure() {

    }
}
