/**
 * MuzeiRemoteArtSource for 1337walls.w8l.org
 * 
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
package de.zigamorph.w8alls;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends RemoteMuzeiArtSource {

    /* preferences */
    private SharedPreferences prefs;
    private boolean isRandom;
    private boolean isRefreshOnWifiOnly;

    public static final String ACTION_NEW_SETTINGS = "de.zigamorph.w8alls.action.NEW_SETTINGS";
    public static final String EXTRA_SHOULD_REFRESH = "de.zigamorph.w8alls.extra.SHOULD_REFRESH";

    public MainActivity() {
        super(Config.TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        isRandom = prefs.getBoolean(getString(R.string.pref_cyclemode_key), true);
        Log.d(Config.TAG, "isRandom: " + this.isRandom);

        isRefreshOnWifiOnly = prefs.getBoolean(getString(R.string.pref_wifiswitch_key), false);
        Log.d(Config.TAG, "isRefreshOnWifiOnly: " + this.isRefreshOnWifiOnly);
    }

    @Override
    protected void onUpdate(int reason) {
        super.onUpdate(reason);

        List<UserCommand> commands = new ArrayList<UserCommand>();
        commands.add(new UserCommand(Config.COMMAND_ID_SHARE, getString(R.string.share_artwork)));
        commands.add(new UserCommand(BUILTIN_COMMAND_ID_NEXT_ARTWORK));
        setUserCommands(commands);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        if (isRefreshOnWifiOnly && !isConnectedWifi()) {
            throw new RetryException();
        }
        publishArtwork(Wallpaper.fetch(this.isRandom));
        this.reschedule();
    }

    private void reschedule() {

        int intervalTimeMilis;
        if(isRandom) {
            intervalTimeMilis = Integer.parseInt(
                    prefs.getString(getString(R.string.pref_intervalpicker_key), getString(R.string.pref_intervalpicker_defaultvalue))
            ) * 60 * 1000;
        } else {
            intervalTimeMilis = Config.ROTATE_TIME_MILLIS;
        }

        scheduleUpdate(System.currentTimeMillis() + intervalTimeMilis);

        //scheduleUpdate(System.currentTimeMillis() + Config.ROTATE_TIME_MILLIS);
    }

    @Override
    protected void onCustomCommand(int id) {
        super.onCustomCommand(id);
        if (id == Config.COMMAND_ID_SHARE) {
            this.shareArtwork();
        }
    }

    /**
     * Share artwork with other apps (e.g. Twitter)
     */
    private void shareArtwork() {
        Artwork currentArtwork = getCurrentArtwork();

        if (currentArtwork == null) {
            return;
        }

        String detailUrl = currentArtwork.getViewIntent().getDataString();
        String artist = currentArtwork.getByline().replaceFirst("\\.\\s*($|\\n).*", "").trim();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "My wallpaper today is '" + currentArtwork.getTitle().trim() + "' by " + artist + " from @1337walls (" + detailUrl + ")");
        shareIntent = Intent.createChooser(shareIntent, getString(R.string.share_artwork));
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);
    }


    private boolean isConnectedWifi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }
}