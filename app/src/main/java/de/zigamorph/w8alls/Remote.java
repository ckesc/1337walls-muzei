package de.zigamorph.w8alls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
public class Remote {
    
    private static Remote remote = null;
    
    public static Remote getInstance(){
        if(null==remote){
            remote = new Remote();
        }
        return remote;
    }
    
    private Remote(){}
    
    public JSONArray read() throws IOException, JSONException {
        InputStream is = new URL(Config.APIURL).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String txt = readAll(rd);
            JSONArray json = new JSONArray(txt);
            return json;
        } finally {
            is.close();
        }
    }
    
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }  
}