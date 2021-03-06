package ts.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ts.trainticket.fragement.ContactPath_Fragment;

/**
 * Created by liuZOZO on 2018/1/20.
 */
public class ContactPathActivity extends AppCompatActivity {

    private Button common_head_back_btn = null;
    private TextView title_head_tv = null;

    private ContactPath_Fragment contactPathFragment;
    private LinearLayout back_btnLyt= null;
    public static final int  FILTE_RPATH_REQUEST_CODE = 0;
    public static final int PATH_CHOOSE_RESULT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactpath);
        initFragment();
        initBackBtn();
        showFragment();
    }


    private void initFragment() {
        contactPathFragment = new ContactPath_Fragment();

        // 初始化数据
        Bundle bundle = new Bundle();
        bundle.putString("start_city", getIntent().getStringExtra("start_city"));
        bundle.putString("end_city", getIntent().getStringExtra("end_city"));
        bundle.putString("pathStartDate",getIntent().getStringExtra("pathStartDate"));

        bundle.putString("start_time",getIntent().getStringExtra("start_time"));
        bundle.putString("arrive_time",getIntent().getStringExtra("arrive_time"));
        bundle.putString("seat_type",getIntent().getStringExtra("seat_type"));
        bundle.putString("car_typegd",getIntent().getStringExtra("car_typegd"));
        bundle.putString("car_typez",getIntent().getStringExtra("car_typez"));
        bundle.putString("car_typet",getIntent().getStringExtra("car_typet"));
        bundle.putString("car_typek",getIntent().getStringExtra("car_typek"));
        contactPathFragment.setArguments(bundle);
    }

    private void initBackBtn() {
        common_head_back_btn = (Button) findViewById(R.id.common_head_back_btn);

        title_head_tv = (TextView) findViewById(R.id.title_head_tv);
        title_head_tv.setText("ContactPath");


        back_btnLyt = (LinearLayout) findViewById(R.id.back_btn_lid);
        back_btnLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
                Intent cityIntent = new Intent(getApplication(), MainActivity.class);
                startActivity(cityIntent);
            }
        });
    }

    private void showFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_contactpath_container, contactPathFragment, "")
                .commit();
    }
}
