package com.raviteja.addictionary;

import android.content.Context;
import android.os.StrictMode;
import org.w3c.dom.Element;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ListAdapter extends BaseAdapter
{
    private ArrayList<String> items;
    private ArrayList<String> meanings;
    private Context appContext;
    private final String API_ENDPOINT = "http://www.dictionaryapi.com/api/v1/references/learners/xml/";
    private final String TEMP = "?key=";
    private final String API_KEY = "028cb880-d87e-4cd5-9f5a-d1efba4ef37b";
    private final String TAG = "addiction-ary";

    public ListAdapter(Context c)
    {
        this.items = new ArrayList<String>();
        this.meanings = new ArrayList<String>();
        this.appContext = c;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        TextView wordView,meaningView;
        if(position%2==0) // even
        {
            newView = inflater.inflate(R.layout.list_item_left_align,null);
            wordView = (TextView)newView.findViewById(R.id.textView);
            meaningView = (TextView)newView.findViewById(R.id.textView3);
        }
        else
        {
            newView = inflater.inflate(R.layout.list_item_right_align,null);
            wordView = (TextView)newView.findViewById(R.id.textView2);
            meaningView = (TextView) newView.findViewById(R.id.textView4);
        }

        wordView.setText(items.get(position));

        String all = meanings.get(position),_final = null;
        String[] tokens;
        if(all.contains(":")) {
            tokens = all.split(":");
            int i = 1;
            for (String t : tokens) {
                String token = t.trim();
                if(token.length() > 0) {
                    _final = token;
                    break;
                }
            }
            meaningView.setText(_final);
        }
        else
        {
            meaningView.setText(meanings.get(position));
        }
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
        String meaning = getMeaning(item);
        items.add(item);
        if(meaning == null) {
            meanings.add("Meaning cannot be found !");
        }
        else {
            meanings.add(meaning);
        }
        this.notifyDataSetChanged();
    }
    public void setItems(ArrayList<String> items){
        this.items = new ArrayList<String>(items);
        this.notifyDataSetChanged();
    }

    public String get_API_URL(String word) {
        String url = API_ENDPOINT + word + TEMP + API_KEY;
        Log.d(TAG, url);
        return url;
    }

    public void clear() {
        items.clear();
    }

    public String getMeaning(String word) {
        String api_url = get_API_URL(word);
        try {
            // getting the meaning of the word
            URL url = new URL(api_url);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("entry");
            Log.d(TAG,"Length of entry nodes : "+nodeList.getLength());
            Node node = nodeList.item(0);
            Element element = (Element) node;
            NodeList def_list = element.getElementsByTagName("def");
            Log.d(TAG,"Length of def nodes : "+def_list.getLength());
            node = def_list.item(0);
            element = (Element) node;
            NodeList dt_list = element.getElementsByTagName("dt");
            Log.d(TAG, "Length of dt nodes : " + dt_list.getLength());
            for(int i=0;i<dt_list.getLength();i++) {
                Node meaning = dt_list.item(i);
                String m = meaning.getTextContent();
                if(m != null) {
                    Log.d(TAG, "Node Value : " + m);
                    //displayToast(word + " : " + m);
                    return m;
                }
            }
        }
        catch(Exception e) {
            Log.d(TAG, e.toString());
            displayToast(e.toString());
        }
        return null;
    }

    public void displayToast(String message) {
        Toast.makeText(this.appContext,message,Toast.LENGTH_LONG).show();
    }
}
