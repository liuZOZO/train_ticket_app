package ts.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ts.trainticket.fragement.TicketReserve_Fragement;


/**
 * Created by liuZOZO on 2018/1/22.
 * 车票预订
 */
public class TicketReserveActivity extends AppCompatActivity {

    private TicketReserve_Fragement goReserve_fragement;
    // head
    private Button head_back_btn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_reserve);
        initHeads();
        initFragment();
        showFragment();
    }

    private void initHeads(){
        head_back_btn  = (Button) findViewById(R.id.common_head_back_btn);
        head_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
                Intent cityIntent = new Intent(getApplication(), BuyTicketActivity.class);
                startActivity(cityIntent);
            }
        });
    }
    private void initFragment(){
        goReserve_fragement = new TicketReserve_Fragement();
        // 初始化数据
        Bundle bundle = new Bundle();
        bundle.putString("ticket_detail",getIntent().getStringExtra("ticket_detail"));
        bundle.putString("seatType",getIntent().getStringExtra("seatType"));
        bundle.putString("seatPrice",getIntent().getStringExtra("seatPrice"));
        goReserve_fragement.setArguments(bundle);
    }
    private void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_goreserve_container,goReserve_fragement,"")
                .commit();
    }
}
