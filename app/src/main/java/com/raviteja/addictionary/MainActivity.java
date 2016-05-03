package com.raviteja.addictionary;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.addictionary.addictionary.R;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ListView listView;
    private ListAdapter adapter;
    private EditText textField;
    private ArrayList<String> items;
    String words[] = {"cool","fool","tool","pool","toll","doll","told","mold","fold","sold"},compWord="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        if(adapter == null)
            adapter = new ListAdapter(this.getApplicationContext());

        //setting adapter
        listView.setDivider(null);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleClick(View view) {
        String enteredWord = textField.getText().toString().trim();
        if (enteredWord.length() != 4) {
            Context context = getApplicationContext();
            CharSequence text = "Enter 4 letter words only!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            if(adapter.containsItem(enteredWord)) {
                Context context = getApplicationContext();
                CharSequence text = "Word already Used!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                textField.setText("");
                adapter.addItem(enteredWord);
                compWord = think(enteredWord); // Computer: generate word
                if(!compWord.equals("kaboom")) {
                    adapter.addItem(compWord);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "You Win!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    textField.setFocusable(false);
                }
                listView.setSelection(adapter.getCount() - 1);
            }
        }
    }

    public String think(String enteredWord)
    {
        return generateWord(enteredWord);
    }

    public String generateWord(String enteredWord)
    {
        for(int i=0;i<4;i++) {
            char alpha = 'a';
            for(int j=0;j<26;j++) {
                String enteredWord1 = replaceOnce(enteredWord,i,alpha);
                for(int k=0;k<words.length;k++) {
                    if(enteredWord1.equals(words[k])&&!enteredWord1.equals(enteredWord)) {
                        //adapter.addItem(enteredWord1.toUpperCase());
                        if(!adapter.containsItem(enteredWord1)) {
                            return enteredWord1;
                        }
                    }
                }
                alpha++;
            }
        }
        return "kaboom";
    }

    public String replaceOnce(String word, int index, char newChar) {
        String newWord = word.substring(0,index)+newChar+word.substring(index+1,word.length());
        return newWord;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (items == null)
            items = new ArrayList<String>();
        items = adapter.getItems();
        adapter.clear();
    }

    public void onResume(){
        super.onResume();
        if(items == null)
            items = new ArrayList<String>();
        adapter.setItems(new ArrayList(items));
        items.clear();
    }

}
