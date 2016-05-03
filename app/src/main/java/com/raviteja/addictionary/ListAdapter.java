package com.raviteja.addictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.addictionary.addictionary.R;

import java.util.ArrayList;


public class ListAdapter extends BaseAdapter
{
    private ArrayList<String> items;
    private Context appContext;

    public ListAdapter(Context c)
    {
        this.items = new ArrayList<String>();
        this.appContext = c;
    }

    @Override
    public int getCount() {
        if(items == null)
            items = new ArrayList<String>();
        return items.size();
    }

    @Override
    public Object getItem(int position) {

        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView;
        TextView wordView;
        if(position%2==0) // even
        {
            newView = inflater.inflate(R.layout.list_item_left_align,null);
            wordView = (TextView)newView.findViewById(R.id.textView);
        }
        else
        {
            newView = inflater.inflate(R.layout.list_item_right_align,null);
            wordView = (TextView)newView.findViewById(R.id.textView2);
        }
        wordView.setText(items.get(position));
        return newView;
    }

    public ArrayList<String> getItems()
    {
        return new ArrayList<String>(items);
    }

    public boolean containsItem(String item)
    {
        return items.contains(item);
    }
    public void addItem(String item)
    {
        items.add(item);
        this.notifyDataSetChanged();
    }
    public void setItems(ArrayList<String> items){
        this.items = new ArrayList<String>(items);
        this.notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
    }
}
