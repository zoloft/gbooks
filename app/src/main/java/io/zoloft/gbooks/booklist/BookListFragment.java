package io.zoloft.gbooks.booklist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import io.zoloft.gbooks.GBooksApplication;
import io.zoloft.gbooks.R;

public class BookListFragment extends Fragment {

  private static final String TAG = BookListFragment.class.getName();

  private static final String ARG_COLUMN_COUNT = "column-count";
  private int mColumnCount = 1;
  private OnListFragmentInteractionListener mListener;

  @Inject
  RequestQueue mQueue;

  @Inject
  String mApiKey;

  private BookListRecyclerViewAdapter mAdapter;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public BookListFragment() {
  }

  @SuppressWarnings("unused")
  public static BookListFragment newInstance(int columnCount) {
    BookListFragment fragment = new BookListFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_COLUMN_COUNT, columnCount);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_book_list, container, false);

    if (view instanceof RecyclerView) {
      Context context = view.getContext();
      RecyclerView recyclerView = (RecyclerView) view;
      if (mColumnCount <= 1) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
      } else {
        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
      }
      mAdapter = new BookListRecyclerViewAdapter(null, mListener);
      recyclerView.setAdapter(mAdapter);
    }
    return view;
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    ((GBooksApplication)context.getApplicationContext()).getComponent().inject(this);
    if (context instanceof OnListFragmentInteractionListener) {
      mListener = (OnListFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnListFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public void search(String query) {
    // TODO(aegizio): Show spinner while loading content
    String encodedQuery = "";
    try {
      encodedQuery = URLEncoder.encode(query, "UTF-8");
    } catch (UnsupportedEncodingException e) {}
    String url = "https://www.googleapis.com/books/v1/volumes?q=" +
         encodedQuery + "&key=" + mApiKey;
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            try {
              mAdapter.setItems(Book.getItems(response));
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Connection error", error);
            String details = "";
            try {
              details = new String(error.networkResponse.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {}
            Log.e(TAG, "Error details: " + details);
          }
        }
    );
    mQueue.add(request);
  }

  public interface OnListFragmentInteractionListener {
    void onListFragmentInteraction(Book item, TextView titleView, TextView authorView,
                                   ImageView coverView);
  }
}
