package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ViewItemActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        title = (TextView) findViewById(R.id.textView3);
        title.setText(url);
    }
}