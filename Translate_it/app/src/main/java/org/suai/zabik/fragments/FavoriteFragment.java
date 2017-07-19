
package org.suai.zabik.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import org.suai.zabik.models.DataBase;
import org.suai.zabik.models.History;
import org.suai.zabik.views.R;
import org.suai.zabik.views.RequestListAdapter;

import java.util.ArrayList;
import java.util.Vector;

/**
 *  fragment contain list of favorite translations
 */

public class FavoriteFragment extends Fragment implements View.OnClickListener{
    ChangeFavoriteItem callback;
    ArrayList<History> favoriteList;
    RequestListAdapter requestListAdapter;
    //view of fragment
    View fragmentView;
    //listView of History and Favorite requests
    ListView historyAndFavoriteListView;
    private ImageButton backButton;
    //database clear button
    private Button clearHistoryButton;
    private Vector<Integer> needToDeleteElements;

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    private void onAttachToContext(Context context) {
        callback = (ChangeFavoriteItem) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        needToDeleteElements = new Vector<>();
        fragmentView =  inflater.inflate(R.layout.activity_history_and_favorite,container,false);
        backButton = (ImageButton) fragmentView.findViewById(R.id.backButton);
        backButton.setImageResource(R.drawable.ic_back);
        historyAndFavoriteListView = (ListView)fragmentView.findViewById(R.id.historyAndFavoritelistView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //find view, get resource and set onClick listener
        try {
            backButton.setOnClickListener(this);
            clearHistoryButton = (Button) getView().findViewById(R.id.clearHistoryButton);
            clearHistoryButton.setVisibility(View.INVISIBLE);
        } catch (NullPointerException e) {
            Log.e("ViewFind", "cant find clearButton on favorite ", e);
        }
        showHistory();
    }

    private void showHistory(){
        favoriteList = DataBase.getAllFavorite(getContext());
        requestListAdapter = new RequestListAdapter(getActivity(),
                R.layout.list_item,
                favoriteList
        );
        historyAndFavoriteListView.setAdapter(requestListAdapter);
        historyAndFavoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = favoriteList.get(position).getId();
                ImageButton star = (ImageButton) view.findViewById(R.id.star);
                if (!needToDeleteElements.contains(itemId)) {
                    needToDeleteElements.add(itemId);
                    star.setImageResource(R.drawable.ic_notfavorite);
                }
                else {
                    needToDeleteElements.remove((Object) itemId);
                    star.setImageResource(R.drawable.ic_favorite);
                }
            }
        });
    }

    public Vector<Integer> getNeedToDeleteItems(){
        return needToDeleteElements;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                getActivity().finish();
                break;
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        notifyAllItems();
    }

    //notify all added item
    public void notifyAllItems(){
        for(int i = 0;i < needToDeleteElements.size();++i) {
            History tmp = new History();
            int idx = needToDeleteElements.get(i);
            DataBase.changeFavoriteState(getActivity(), idx);
            tmp.setId(idx);
            favoriteList.remove(tmp);
        }
        requestListAdapter.notifyDataSetChanged();
        needToDeleteElements.clear();
    }

    //add new item to list
    public void addNewItem(History item){
        if(!favoriteList.contains(item))
            favoriteList.add(0,item);
        notifyAllItems();
    }

    //delete item from list
    public void deleteElem(History item){
        if(favoriteList.contains(item)){
            favoriteList.remove(item);
        }
    }

    public void deleteAll() {
        favoriteList.clear();
        requestListAdapter.notifyDataSetChanged();
    }

    public interface ChangeFavoriteItem {
        void changeStateItem(int idx);
    }
}