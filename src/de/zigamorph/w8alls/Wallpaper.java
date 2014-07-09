package de.zigamorph.w8alls;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
public class Wallpaper {
    
    private int id; 
    private String title;
    private String author;
    private String time;
    private String image;
    private String url;
	
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
   
    public Wallpaper(){}
    
	/**
	 * fetch a wallpaper
	 * 
	 * @return Wallpaper
	 */
    public static Wallpaper fetch() {
        
        JSONArray json = null;
        try {
            json = Remote.getInstance().read();
        } catch (IOException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Random random = new Random();
        Integer index = random.nextInt(json.length());
            
        Wallpaper wp = new Wallpaper();
        try {
            wp.setId(json.getJSONObject(index).getInt("id"));
            wp.setTitle(json.getJSONObject(index).get("title").toString());
            wp.setAuthor(json.getJSONObject(index).get("author").toString());
            wp.setTime(json.getJSONObject(index).get("time").toString());
            wp.setImage(json.getJSONObject(index).get("image").toString());
            wp.setUrl(json.getJSONObject(index).get("url").toString());
        } catch (JSONException ex) {
            Logger.getLogger(Wallpaper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return wp;
    }
}
