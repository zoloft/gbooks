package io.zoloft.gbooks;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import io.zoloft.gbooks.booklist.BookListFragment;

public class GBooksApplication extends Application {

  @Singleton @Component(modules = {MainModule.class})
  public interface MainComponent {
    void inject(BookListFragment fragment);
  }

  private static MainComponent mComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    initDependencyTree();
  }

  private void initDependencyTree() {
    mComponent = DaggerGBooksApplication_MainComponent.builder()
        .mainModule(new MainModule(this))
        .build();
  }

  public MainComponent getComponent() {
    return mComponent;
  }
}
