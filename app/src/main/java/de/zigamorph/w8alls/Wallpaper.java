package de.zigamorph.w8alls;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
public class Wallpaper extends Artwork.Builder {
    
    public Wallpaper(){}
    
    /**
     * fetch a wallpaper
     *
     * @param isRandom boolean
     * @return Artwork
     */
    public static Artwork fetch(boolean isRandom) {

        JSONArray json = null;
        try {
            json = Remote.getInstance().read();
        } catch (IOException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

        Integer index;
        if(isRandom){
            Random random = new Random();
            index = random.nextInt(json.length());
        } else {
            index = 0;
        }

        Wallpaper wallpaper = new Wallpaper();

        try {
            wallpaper.title(json.getJSONObject(index).get("title").toString());
            wallpaper.byline(json.getJSONObject(index).get("author").toString());
            wallpaper.imageUri(Uri.parse(json.getJSONObject(index).get("image").toString()));
            wallpaper.token(String.valueOf(json.getJSONObject(index).getInt("id")));
            wallpaper.viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(json.getJSONObject(index).get("url").toString())));
        } catch (JSONException ex) {
            Logger.getLogger(Wallpaper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wallpaper.build();
    }
}
