package yahoo.arjunhande.com.yahooweather.listener;

import java.util.ArrayList;

import yahoo.arjunhande.com.yahooweather.data.Channel;

/**
 * Created by ArjunHandeMac on 2/21/16.
 */
public interface YahooWeatherListener {

    void onSuccess(ArrayList<Channel> channel);

    void onFailure();
}
