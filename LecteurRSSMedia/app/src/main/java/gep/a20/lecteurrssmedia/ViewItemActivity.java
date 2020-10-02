package gep.a20.lecteurrssmedia;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ViewItemActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

//        title = findViewById(R.id.textViewTitle);
//        RssItemParcelable rssItemParcelable;
//
//        try{
//            rssItemParcelable = (RssItemParcelable)getIntent().getParcelableExtra(ItemsFeedActivity.PAR_KEY);
//            title.setText(rssItemParcelable.getTitle());
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}