package io.zoloft.gbooks.booklist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.zoloft.gbooks.R;
import io.zoloft.gbooks.booklist.BookListFragment.OnListFragmentInteractionListener;

public class BookListRecyclerViewAdapter extends RecyclerView.Adapter<BookListRecyclerViewAdapter.ViewHolder> {

  private static final String TAG = BookListRecyclerViewAdapter.class.getName();
  private List<Book> mValues;
  private final OnListFragmentInteractionListener mListener;

  public BookListRecyclerViewAdapter(List<Book> items, OnListFragmentInteractionListener listener) {
    mValues = items;
    mListener = listener;
  }

  public void setItems(List<Book> items) {
    mValues = items;
    notifyDataSetChanged();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.book_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.item = mValues.get(position);
    holder.titleView.setText(holder.item.title);
    holder.authorView.setText(holder.item.getMainAuthor());
    holder.view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != mListener) {
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          mListener.onListFragmentInteraction(holder.item, holder.titleView, holder.authorView,
              holder.coverView);
        }
      }
    });
    Glide
        .with(holder.view.getContext())
        .load(holder.item.thumbUrl)
        .centerCrop()
        .crossFade()
        .into(holder.coverView);
  }

  @Override
  public int getItemCount() {
    if (mValues == null) {
      return 0;
    }
    return mValues.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View view;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.author) TextView authorView;
    @BindView(R.id.cover) ImageView coverView;
    public Book item;

    public ViewHolder(View view) {
      super(view);
      this.view = view;
      ButterKnife.bind(this, view);
    }

  }
}
