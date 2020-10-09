package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Attributes
    public static final String TAG = "MainActivity";
    ProgressDialog progressDialog;
    ListView listViewMain;
    EditText addUrlEditText;
    private MainMenuAdapter mainMenuAdapter;

    /**
     * CTOR
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewMain = (ListView) findViewById(R.id.list_view);
        addUrlEditText = (EditText) findViewById(R.id.addUrl);
        // Set the ListView in MainActivity
        mainMenuAdapter = new MainMenuAdapter(getApplicationContext(), R.layout.listview_item);
        listViewMain.setAdapter(mainMenuAdapter);

        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appInfo = new Intent(MainActivity.this, ItemsFeedActivity.class);
                appInfo.putExtra("url", mainMenuAdapter.sites.get(i));
                startActivity(appInfo);
            }
        });

        // Set the add url button
        Button addUrlButton = (Button) findViewById(R.id.addBtn);
        addUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // À faire ici pour ajouter de nouveaux URL.
                    new MainProcessInBackground(MainActivity.this, mainMenuAdapter, true, mainMenuAdapter.sites, addUrlEditText).execute();
                } catch (Exception e) {
                    Log.e(TAG + "-addUrlButton", e.getMessage());
                }
            }
        });

        new MainProcessInBackground(this, mainMenuAdapter, false, mainMenuAdapter.sites, addUrlEditText).execute();
    }
}