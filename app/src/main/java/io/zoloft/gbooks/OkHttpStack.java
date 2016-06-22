package io.zoloft.gbooks;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;

@SuppressWarnings("deprecation")
public class OkHttpStack extends HurlStack {

  private final OkUrlFactory mFactory;

  public OkHttpStack() {
    this(new OkUrlFactory(new OkHttpClient()));
  }

  public OkHttpStack(OkUrlFactory factory) {
    mFactory = Objects.requireNonNull(factory);
  }

  @Override
  protected HttpURLConnection createConnection(URL url) throws IOException {
    return mFactory.open(url);
  }
}
