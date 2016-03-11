package yahoo.arjunhande.com.yahooweather.service;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import yahoo.arjunhande.com.yahooweather.data.Channel;
import yahoo.arjunhande.com.yahooweather.listener.YahooWeatherListener;

/**
 * Created by ArjunHandeMac on 2/21/16.
 */
public class YahooWeatherService {

    private final YahooWeatherListener listener;

    String locations = "\"tokyo\", \"new york\", \"sao paulo\", \"seoul\", \"mumbai\", \"delhi\", \"jakarta\"";

    public YahooWeatherService(YahooWeatherListener listener) {
        this.listener = listener;
    }

    public void refreshWeather() {

        new AsyncTask<String, Void, ArrayList<Channel>>() {
            @Override
            protected ArrayList<Channel> doInBackground(String[] l) {


                ArrayList<Channel> chList = new ArrayList<Channel>();
                Channel channel = new Channel();

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text in (%s))", locations);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject data = new JSONObject(result.toString());

                    JSONObject queryResults = data.optJSONObject("query");

                    int count = queryResults.optInt("count");

                    if (count == 0) {
//                        error = new LocationWeatherException("No weather information found for " + location);
//                        return null;
                    }

                    while (count != 0) {
                        JSONObject channelJSON = queryResults.optJSONObject("results").optJSONObject("channel");
                        if(channelJSON != null) {
                            channel.populate(channelJSON);
                            chList.add(channel);
                        }
                        count--;
                    }

                    return chList;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Channel> channel) {

//                if (channel == null && error != null) {
//                    listener.onFailure(error);
//                } else {
                    listener.onSuccess(channel);
//                }

            }

        }.execute();
    }
}
