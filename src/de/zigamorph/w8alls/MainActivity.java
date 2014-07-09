/**
 * MuzeiRemoteArtSource for 1337walls.w8l.org
 * 
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
package de.zigamorph.w8alls;

import android.content.Intent;
import android.net.Uri;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

public class MainActivity extends RemoteMuzeiArtSource {
    
    private static final String TAG = "1337walls";
    private static final String SOURCE_NAME = "OneThousandThreeHundredThirtySevenWallsArtSource";
    
    public MainActivity(){
        super(TAG);
    }
    
    @Override
    public void onCreate(){
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }
    
    @Override 
    protected void onTryUpdate(int reason) throws RetryException {
        
        Wallpaper wallpaper = Wallpaper.fetch();
        
        publishArtwork(new Artwork.Builder()
            .title(wallpaper.getTitle())
            .byline(wallpaper.getAuthor())
            .imageUri(Uri.parse(wallpaper.getImage()))
            .token(String.valueOf(wallpaper.getId()))
            .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(wallpaper.getUrl())))
            .build());

        this.reschedule();
    }

    private void reschedule() {
        scheduleUpdate(System.currentTimeMillis() + Config.ROTATE_TIME_MILLIS);
    }   
}