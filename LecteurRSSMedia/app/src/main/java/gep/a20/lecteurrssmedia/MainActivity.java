package gep.a20.lecteurrssmedia;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    EditText editText;
    Button btn;
    MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.urlList);
        editText = (EditText)findViewById(R.id.addUrl);
        btn = (Button)findViewById(R.id.addBtn);
        main = this;

        //Url de demo de depart
        arrayList.add("https://ici.radio-canada.ca/rss/4159");//radio canada grands titres
        arrayList.add("https://ici.radio-canada.ca/rss/1000524"); //radio canada feed continue
        arrayList.add("http://www.lapresse.ca/actualites/rss");// actualite la presse
        arrayList.add("https://www.developpez.com/index/rss");
        arrayList.add("https://feeds.twit.tv/sn.xml");
        arrayList.add("https://feeds.twit.tv/sn_video_hd.xml"); //RSS avec video
        arrayList.add("https://feeds.simplecast.com/gvtxUiIf");
        arrayList.add("https://visualstudiotalkshow.libsyn.com/rss"); //RSS avec video


        ArrayAdapter adapter = new ArrayAdapter(main, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text  = editText.getText().toString();
                if( text.length() > 0){
                    arrayList.add(text);
                    ArrayAdapter adapter = new ArrayAdapter(main, android.R.layout.simple_list_item_1, arrayList);
                    listView.setAdapter(adapter);
                    editText.setText("");
                }
            }
        });
    }
}