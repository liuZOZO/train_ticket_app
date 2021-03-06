package ts.trainticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by liuZOZO on 2018/3/29.
 */
public class AboutActivity extends AppCompatActivity {

    public static final String SERVICE_CLASSNAME = "ts.trainticket.messagepush.MQTTService";

    // head
    private Button head_back_btn = null;
    private TextView title_head_tv= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abouts);

        head_back_btn  = (Button) findViewById(R.id.common_head_back_btn);
        title_head_tv = (TextView) findViewById(R.id.title_head_tv);
        title_head_tv.setText("about us");
        head_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
                finish();
            }
        });
    }

}
