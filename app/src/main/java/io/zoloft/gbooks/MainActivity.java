package io.zoloft.gbooks;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.zoloft.gbooks.booklist.Book;
import io.zoloft.gbooks.booklist.BookListFragment;

public class MainActivity extends AppCompatActivity implements
    BookListFragment.OnListFragmentInteractionListener {

  @BindView(R.id.toolbar) Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getWindow().setSharedElementsUseOverlay(false);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      performSearch(query);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setIconifiedByDefault(false);

    return super.onCreateOptionsMenu(menu);
  }

  private void performSearch(String query) {
    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    BookListFragment bookList = (BookListFragment) getSupportFragmentManager()
        .findFragmentById(R.id.book_list);
    bookList.search(query);
  }

  @Override
  public void onListFragmentInteraction(Book item, TextView titleView, TextView authorView,
                                        ImageView coverView) {
    Intent bookIntent = new Intent(this, BookActivity.class);
    bookIntent.putExtra("book", item);
    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
        Pair.create((View)mToolbar, "toolbar"),
        Pair.create((View)coverView, "cover"),
        Pair.create((View)titleView, "title")
    );
    Bundle bundle = options.toBundle();
    startActivity(bookIntent, bundle);
  }
}
