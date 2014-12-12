package de.zigamorph.w8alls;

/**
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
public class Config {
    
    /**
     * rotate every 3 hours
     */
   public static final int ROTATE_TIME_MILLIS = 3*60*60*1000; 

   /**
    * API URL
    */
   public static final String APIURL = "http://1337walls.w8l.org/api/";

   public static final String TAG = "1337walls";
   public static final String SOURCE_NAME = "OneThousandThreeHundredThirtySevenWallsArtSource";

   public static final int COMMAND_ID_SHARE = 1000;
   public static final int COMMAND_ID_SAVE = 1001;
}
