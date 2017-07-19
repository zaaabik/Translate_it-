package org.suai.zabik.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import org.suai.zabik.fragments.FavoriteFragment;
import org.suai.zabik.fragments.HistoryFragment;
import org.suai.zabik.models.History;
import org.suai.zabik.views.R;

import java.util.List;
import java.util.Vector;

public class HistoryAndFavoriteActivity extends FragmentActivity implements HistoryFragment.ResultsIsChanged,FavoriteFragment.ChangeFavoriteItem {
    final List<Fragment> fragments = new Vector<>();
    private final int FAVORITE_FRAGMENT = 1;
    private final int HISTORY_FRAGMENT = 0;
    TabLayout tabLayout;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    ScreenSlidePagerAdapter adapter;
    private ImageButton backButton;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_history_favorite);

        backButton = (ImageButton)findViewById(R.id.backButton);



        mPager = (ViewPager) findViewById(R.id.pagerViewPager);


        tabLayout = (TabLayout)findViewById(R.id.currentFragmentNameTabLayout);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        //create list with all favorite and history fragment
        fragments.add(Fragment.instantiate(this,HistoryFragment.class.getName()));
        fragments.add(Fragment.instantiate(this,FavoriteFragment.class.getName()));

        // Instantiate a ViewPager and a PagerAdapter.
        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),fragments);
        mPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mPager);




        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == FAVORITE_FRAGMENT) {
                    //update favorite fragment when favorite fragment is chosen
                    FavoriteFragment favoriteFragment = (FavoriteFragment) fragments.get(FAVORITE_FRAGMENT);
                    favoriteFragment.notifyAllItems();

                }
                if (position == HISTORY_FRAGMENT) {
                    //update favorite and history fragment when
                    //first page is chosen
                    FavoriteFragment favoriteFragment = (FavoriteFragment) fragments.get(FAVORITE_FRAGMENT);
                    Vector<Integer> needToUpdate = favoriteFragment.getNeedToDeleteItems();
                    int size = needToUpdate.size();
                    for(int i = 0; i < size;++i){
                        changeStateItem(needToUpdate.get(i));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user press back button go to main page of app
                FavoriteFragment favoriteFragment = (FavoriteFragment) fragments.get(FAVORITE_FRAGMENT);
                Vector<Integer> needToUpdate = favoriteFragment.getNeedToDeleteItems();
                int size = needToUpdate.size();
                for (int i = 0; i < size; ++i) {
                    changeStateItem(needToUpdate.get(i));
                }

                favoriteFragment.notifyAllItems();

                finish();
            }
        });

    }

    @Override
    public void newItem(History item) {
        FavoriteFragment favoriteFragment = (FavoriteFragment) fragments.get(FAVORITE_FRAGMENT);
        favoriteFragment.addNewItem(item);
    }

    @Override
    public void deleteItemFromFavorite(History item) {
        FavoriteFragment favoriteFragment = (FavoriteFragment) fragments.get(FAVORITE_FRAGMENT);
        favoriteFragment.deleteElem(item);
    }

    @Override
    public void deleteAll() {
        FavoriteFragment favoriteFragment = (FavoriteFragment) fragments.get(FAVORITE_FRAGMENT);
        favoriteFragment.deleteAll();
    }

    @Override
    public void changeStateItem(int id) {
        HistoryFragment historyFragment = (HistoryFragment) fragments.get(HISTORY_FRAGMENT);
        historyFragment.changeItemsState(id);


    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragments; //contain all fragments

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case HISTORY_FRAGMENT:
                    return "History";
                case FAVORITE_FRAGMENT:
                default:
                    return "Favorite";
            }
        }
    }
}
