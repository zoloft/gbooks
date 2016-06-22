package io.zoloft.gbooks;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.zoloft.gbooks.booklist.Book;

public class BookActivity extends AppCompatActivity {

  private Book mItem;
  @BindView(R.id.hero) ImageView mImageView;
  @BindView(R.id.author) TextView mAuthorView;
  @BindView(R.id.description) TextView mDescriptionView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book);
    getWindow().setSharedElementsUseOverlay(false);
    ButterKnife.bind(this);
    CollapsingToolbarLayout toolbarLayout =
        (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

    mItem = (Book) getIntent().getExtras().getSerializable("book");
    toolbarLayout.setTitle(mItem.title);
    mAuthorView.setText(mItem.getMainAuthor());
    mDescriptionView.setText(mItem.description);
    Glide.with(this)
        .load(mItem.thumbUrl)
        .centerCrop()
        .into(mImageView);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
