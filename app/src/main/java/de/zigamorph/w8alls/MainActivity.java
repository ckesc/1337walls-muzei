/**
 * MuzeiRemoteArtSource for 1337walls.w8l.org
 * 
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
package de.zigamorph.w8alls;

import android.content.Intent;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RemoteMuzeiArtSource {
    
    public MainActivity(){
        super(Config.TAG);
    }
    
    @Override
    public void onCreate(){
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
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
        publishArtwork(Wallpaper.fetch());
        this.reschedule();
    }

    private void reschedule() {
        scheduleUpdate(System.currentTimeMillis() + Config.ROTATE_TIME_MILLIS);
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
    private void shareArtwork(){
        Artwork currentArtwork = getCurrentArtwork();

        if (currentArtwork == null) {
            return;
        }

        String detailUrl    = currentArtwork.getViewIntent().getDataString();
        String artist       = currentArtwork.getByline().replaceFirst("\\.\\s*($|\\n).*", "").trim();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "My wallpaper today is '" + currentArtwork.getTitle().trim() + "' by " + artist + " from @1337walls (" + detailUrl + ")");
        shareIntent = Intent.createChooser(shareIntent, getString(R.string.share_artwork));
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);
    }
}