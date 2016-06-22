package io.zoloft.gbooks.booklist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {

  private static final long serialVersionUID = -7341296545035882764L;

  public final String id;
  public final String title;
  public final String subtitle;
  public final String description;
  public final List<String> authors;
  public final String thumbUrl;

  public Book(JSONObject resource) throws JSONException {
    id = resource.getString("id");
    JSONObject info = resource.getJSONObject("volumeInfo");
    title = info.optString("title");
    subtitle = info.optString("subtitle");
    authors = new ArrayList<>();
    JSONArray authors = info.optJSONArray("authors");
    if (authors != null) {
      for (int i = 0; i < authors.length(); i++) {
        this.authors.add(authors.getString(i));
      }
    }
    description = info.optString("description");
    JSONObject thumbLinks = info.optJSONObject("imageLinks");
    // TODO(aegizio): Search which image size fit best with the current size.
    // considering that the available sizes are:
    // thumbnail - 128px
    // small - 300px
    // medium - 500px
    // large - 800px
    // smallThumbnail - 80px
    // extraLarge - 1280px
    thumbUrl = thumbLinks == null ? "" : thumbLinks.optString("thumbnail");
  }

  public String getMainAuthor() {
    return authors.size() > 0 ? authors.get(0) : "";
  }

  public static List<Book> getItems(JSONObject response) throws JSONException {
    ArrayList<Book> list = new ArrayList<>();
    JSONArray items = response.getJSONArray("items");
    for (int i=0; i<items.length(); i++) {
      list.add(new Book(items.getJSONObject(i)));
    }
    return list;
  }

}
