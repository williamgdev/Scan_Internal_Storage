package com.example.yendry.scaninternalstoage.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lisetpupo on 4/29/18.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().name("myDatabase").build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
