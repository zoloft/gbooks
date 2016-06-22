package io.zoloft.gbooks;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module
public class MainModule {

  Application mApplication;

  public MainModule(Application application) {
    mApplication = application;
  }

  @Provides
  String provideApiKey() {
    return "AIzaSyC_UAuPcVIwhF7f6iYN7KIIYFK5AnVRgx4";
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return mApplication;
  }

  @Provides
  Context provideContext(Application application) {
    return application;
  }

  @Provides
  @NonNull
  File provideCacheDirectory(Application application) {
    return application.getCacheDir();
  }

  @Provides
  @NonNull
  Cache provideResponseCache(File cacheDirectory) {
    int cacheSize = 50 * 1024 * 1024; // 50 MB
    return new Cache(cacheDirectory, cacheSize);
  }

  @Provides
  @Singleton
  @NonNull
  OkHttpClient provideOkHttpClient(Cache cache) {
    OkHttpClient client = new OkHttpClient.Builder()
        .cache(cache)
        .build();
    return client;
  }

  @Provides
  @Singleton
  @NonNull
  RequestQueue provideRequestQueue(Context context, File cacheDirectory) {
    DiskBasedCache cache = new DiskBasedCache(cacheDirectory, 10 * 1024 * 1024);
    Network network = new BasicNetwork(new OkHttpStack());
    RequestQueue queue = new RequestQueue(cache, network);
    queue.start();
    return queue;
  }

}
