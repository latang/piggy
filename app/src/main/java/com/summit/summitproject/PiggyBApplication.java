package com.summit.summitproject;

import android.app.Application;
import android.content.Context;

public class PiggyBApplication extends Application {

    public static ApplicationState applicationState = new ApplicationState();

    public static class ApplicationState{
        String merchant=null;
        String phoneNumber=null;
        String password = null;
        Context context = null;
    }

}
