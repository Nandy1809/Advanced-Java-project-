package common;

import java.util.prefs.Preferences;

public class SessionDetails {
    private Preferences prefs;
    String current;
    public void setPreferences(String loginId){
        prefs = Preferences.userRoot().node(this.getClass().getName());
        prefs.put("currentUser", loginId);
    }

    public String getPreferences(){
        return prefs.get("currentUser","");
    }
}
