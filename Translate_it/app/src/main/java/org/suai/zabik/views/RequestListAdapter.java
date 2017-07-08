package org.suai.zabik.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.suai.zabik.models.History;

import java.util.ArrayList;


/**
 * Adapter for history and favorite list
 */

public class RequestListAdapter extends ArrayAdapter<History>{
    private ArrayList<History> listData;

    private Context context;
    public RequestListAdapter(Context context,int resource,ArrayList<History> listData){
        super(context,resource,listData);
        this.context = context;
        this.listData = listData;

    }
    public int getCount() {
        return listData.size();
    }

    @Override
    public History getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listData.get(position).getId();
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        holder.request = (TextView)convertView.findViewById(R.id.request);
        holder.response = (TextView)convertView.findViewById(R.id.answer);
        holder.isFavorite = (ImageButton)convertView.findViewById(R.id.star);
        holder.langs = (TextView)convertView.findViewById(R.id.tvLangs);


        holder.request.setText(listData.get(position).getRequest());
        holder.response.setText(listData.get(position).getResponse());
        holder.langs.setText(listData.get(position).getLanguage());
        convertView.setTag(listData.get(position).getId());

        if(listData.get(position).getIsFavlorite()){
            holder.isFavorite.setImageResource(R.drawable.ic_favorite);
        }
        else {
            holder.isFavorite.setImageResource(R.drawable.ic_notfavorite);
        }

        return convertView;
    }

    public ArrayList<History> getListData(){
        return listData;
    }

    final private class ViewHolder {
        private TextView request;
        private TextView response;
        private ImageButton isFavorite;
        private TextView langs;

    }
}