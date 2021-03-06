package ts.trainticket.fragement;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.CityChooseActivity;
import ts.trainticket.MainActivity;
import ts.trainticket.R;
import ts.trainticket.databean.ContactPSPageResponse;
import ts.trainticket.databean.ContactPathStation;
import ts.trainticket.databean.StationPageResponse;
import ts.trainticket.databean.Trip;
import ts.trainticket.domain.QueryInfo;
import ts.trainticket.domain.Station;
import ts.trainticket.domain.TravelAdvanceResult;
import ts.trainticket.domain.TravelAdvanceResultUnit;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;
import ts.trainticket.utils.CalendarUtil;

/**
 * 站站查询
 */
public class StationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swiper = null; // 下拉刷新控件
    private Button common_head_back_btn;

    private TextView headText;

    private ImageView animationIV;
    private AnimationDrawable animationDrawable;

    // 城市按钮
    private Button st_btn_city = null;
    private LinearLayout st_stationList = null;
    private TextView st_search_tips = null;

    // 右边三个控件
    private Button startTimeFirst_btn;
    private Button arriveTimeFirst_btn;
    private EditText searchCity_et;
    //  展示列车时刻表
    RecyclerView recyclerView = null;
    ContactPSPageResponse filterPath = null;


    ArrayList<Trip>  queryTravelAllList = new ArrayList<Trip>();;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_station, container, false);
        initHeads(view);
        initViews(view);
        initCityBtn(view); // 选择城市按钮
        initContactPath(view);
        initTimeTableFilter(); // time early
        return view;
    }

    private void initHeads(View view) {
        common_head_back_btn = (Button) view.findViewById(R.id.common_head_back_btn);
        common_head_back_btn.setVisibility(View.GONE);
    }

    private void initContactPath(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.view_timeStation_table);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initViews(View view) {
        swiper = (SwipeRefreshLayout) view.findViewById(R.id.refresh_time_table);
        swiper.setOnRefreshListener(this);
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        headText = (TextView) view.findViewById(R.id.title_head_tv);
        headText.setText("时刻表");
        animationIV = (ImageView) view.findViewById(R.id.animationIV);
        animationIV.setImageResource(R.drawable.pic_loading);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
        animationDrawable.start();

        startTimeFirst_btn = (Button) view.findViewById(R.id.startTimeFirst_btn);
        arriveTimeFirst_btn = (Button) view.findViewById(R.id.arriveTimeFirst_btn);
        searchCity_et = (EditText) view.findViewById(R.id.searchCity_et);
    }

    private void initCityBtn(View view) {
        st_btn_city = (Button) view.findViewById(R.id.st_btn_city);
        addToBtnController(st_btn_city);
        st_btn_city.setOnClickListener(new CityChooseListener(R.id.btn_start_city));

        st_stationList = (LinearLayout) view.findViewById(R.id.st_stationList);
        st_search_tips = (TextView) view.findViewById(R.id.st_search_tips);
    }

    private void initTimeTableFilter() {
        startTimeFirst_btn.setOnClickListener(new TimeTableFilterListener());
        arriveTimeFirst_btn.setOnClickListener(new TimeTableFilterListener());
//        searchCity_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String inputCity = s.toString();
//                if (inputCity.length() >= 2) {
//                    st_btn_city.setText(s.toString());
//
//                }
//            }
//        });
    }

    @Override
    public void onRefresh() {
        // 刷新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 一般会从网络获取数据
                swiper.setRefreshing(false);// 结束后停止刷新
            }
        }, 2300);
    }

    // 城市按按钮监听器
    private class CityChooseListener implements View.OnClickListener {
        int city;

        public CityChooseListener(int city) {
            this.city = city;
        }

        @Override
        public void onClick(View v) {
            // 跳转到新页面选择城市
            Intent cityIntent = new Intent(getActivity(), CityChooseActivity.class);
            startActivityForResult(cityIntent, CityChooseActivity.CITY_CHOOSE_REQUEST_CODE);
        }
    }

    // 从选择城市页面返回的城市名字
    // 根据城市名字得到车站名字
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CityChooseActivity.CITY_CHOOSE_REQUEST_CODE &&
                resultCode == MainActivity.CITY_CHOOSE_RESULT && null != data) {
            st_btn_city.setText(data.getStringExtra(CityChooseFragement.CITY_CHOOSED));
            getTimeTableFromServer(st_btn_city.getText().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getTimeTableFromServer(st_btn_city.getText().toString());
    }


    public void getTimeTableFromServer(String stationName) {
        System.out.println("stationName-----" +stationName);
        // 准备数据
        try {
            stationName = URLEncoder.encode(stationName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        QueryInfo queryInfo = new QueryInfo( "Nan Jing","Shang Hai", "2018-10-30" );
       // QueryInfo queryInfo = new QueryInfo("2018-10-26", "Shang Hai", "Nan Jing");
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(queryInfo));


        String loginId = ApplicationPreferences.getOneInfo(getContext(), "realIcard");
        String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");

        System.out.println("FDf----=323323322=" + new Gson().toJson(queryInfo));

        String listCityUri = UrlProperties.clientIstioIp + UrlProperties.getTravelAll ;
        subscription = RxHttpUtils.getDataByUrl(listCityUri, getContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        unlockClick();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String responseResult) {
                        System.out.println(responseResult + "===--0-9");

                        if (responseResult != null && !responseResult.equals("")) {
                            JSONArray jsonArray = JSON.parseArray(responseResult);
                            List<ContactPathStation> temp = new ArrayList<>();
                            System.out.println(jsonArray.size() +"-=====---------");
                            Iterator it = jsonArray.iterator();
                            while (it.hasNext()) {
                                ContactPathStation cps = new ContactPathStation();
                                JSONObject tripObj = (JSONObject) it.next();
                                JSONObject tripIdObj = tripObj.getJSONObject("tripId");
                                String tripId = tripIdObj.getString("type") + tripIdObj.getString("number");

                                cps.setStationNumber(tripObj.getString("trainTypeId"));
                                cps.setPathName(tripId);

                                cps.setArriveTime(CalendarUtil.getHM(tripObj.getString("endTime")));
                                cps.setStartTime(CalendarUtil.getHM(tripObj.getString("startingTime")));

                                String startPlace = tripObj.getString("startingStationId");
                                String endPlace = tripObj.getString("terminalStationId");
                                String selectedPlace = st_btn_city.getText().toString();

                                cps.setBeginStation(startPlace);
                                cps.setEndStation(endPlace);
                                cps.setStationName(selectedPlace);

                                if(startPlace.equals(selectedPlace.replaceAll(" ","").toLowerCase()) || endPlace.equals(selectedPlace.replaceAll(" ","").toLowerCase()))
                                   temp.add(cps);
                            }
                            setTimeTable(temp);
                        } else {
                            Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 展示路线时刻表TravelAdvanceResult
    private void setTimeTable(List<ContactPathStation> temp) {
        filterPath = new ContactPSPageResponse(true,"good",1,1,1,1,temp);
        st_search_tips.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        animationDrawable.stop();
        animationIV.setVisibility(View.GONE);

        // 通过adapter展示数据
        if(temp.size() >0) {
            ContactPathAdapter myAdapter = new ContactPathAdapter(temp);
            myAdapter.setHasStableIds(true);
            recyclerView.setAdapter(myAdapter);
        }else{
            animationDrawable.stop();
            animationIV.setVisibility(View.GONE);
            st_search_tips.setText("no data...");
            st_search_tips.setVisibility(View.VISIBLE);
        }
    }

    private void setTimeTable2(ContactPSPageResponse path) {
        st_search_tips.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        // 通过adapter展示数据
        ContactPathAdapter myAdapter = new ContactPathAdapter(path.getCntactPathStation());
        myAdapter.setHasStableIds(true);
        recyclerView.setAdapter(myAdapter);
    }

    //  过滤
    private class TimeTableFilterListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            recyclerView.stopScroll();
            if (startTimeFirst_btn.getId() == v.getId()) {  // 最早发站时刻表

                if (filterPath != null) {
                    Collections.sort(filterPath.getCntactPathStation(), new Comparator<ContactPathStation>() {
                        @Override
                        public int compare(ContactPathStation oL, ContactPathStation oR) {
                            return compareTime(oL.getStartTime(), oR.getStartTime());
                        }
                    });
                    // 通过adapter展示数据
                    setTimeTable2(filterPath);
                } else {
                    Toast.makeText(getActivity(), "好像没有请求到数据", Toast.LENGTH_SHORT).show();
                }
            } else if (arriveTimeFirst_btn.getId() == v.getId()) { // 最早到达时刻表
                if (filterPath != null) {
                    Collections.sort(filterPath.getCntactPathStation(), new Comparator<ContactPathStation>() {
                        @Override
                        public int compare(ContactPathStation oL, ContactPathStation oR) {
                            return compareTime(oL.getArriveTime(), oR.getArriveTime());
                        }
                    });
                    // 通过adapter展示数据
                    setTimeTable2(filterPath);
                } else {
                    Toast.makeText(getActivity(), "好像没有请求到数据", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private int compareTime(String l, String r) {
        Calendar ll = CalendarUtil.getCalendarByStr(l);
        Calendar rr = CalendarUtil.getCalendarByStr(r);
        return CalendarUtil.compareDate(ll, rr);
    }

    // 每个item 对应一个adapter , 一个adapter 对应一个viewholder
    class ContactPathAdapter extends RecyclerView.Adapter<ContactPathAdapter.ContactPathViewHolder> {

        private List<ContactPathStation> list;

        public ContactPathAdapter(List<ContactPathStation> list) {
            this.list = list;
        }

        @Override
        public ContactPathViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_station_table, viewGroup, false);
            return new ContactPathViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactPathViewHolder cpholder, int i) {
            cpholder.pathName.setText(list.get(i).getPathName());
            cpholder.stationName.setText(list.get(i).getStationName());
            if (list.get(i).getArriveTime().equals("00:00:00")) {
                cpholder.arriveTime.setText("终点站");
                cpholder.arriveTime.setTextColor(Color.parseColor("#FFD700"));
            } else {
                cpholder.arriveTime.setText(list.get(i).getArriveTime().substring(0, 5));
                cpholder.arriveTime.setTextColor(Color.parseColor("#B8B8B8"));
            }
            if (list.get(i).getStartTime().equals("00:00:00")) {
                cpholder.startTime.setText("始发站");
                cpholder.startTime.setTextColor(Color.parseColor("#FFD700"));
            } else {
                cpholder.startTime.setText(list.get(i).getStartTime().substring(0, 5));
                cpholder.startTime.setTextColor(Color.parseColor("#B8B8B8"));
            }

            cpholder.beginStation.setText(list.get(i).getBeginStation());
            cpholder.endStation.setText(list.get(i).getEndStation());
            final int position = i;
            cpholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击每一个路线进行跳转
//                    Intent intent = new Intent(getActivity(), BuyTicketActivity.class);
//                    Gson gson = new Gson();
//                    intent.putExtra("item_contact_path",gson.toJson(list.get(position)));
//                    Toast.makeText(getActivity(), gson.toJson(list.get(position)), Toast.LENGTH_LONG).show();
//                    startActivity(intent);
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        // 对应item 里面的每一个元素
        class ContactPathViewHolder extends RecyclerView.ViewHolder {
            private TextView pathName;
            private TextView stationName;
            private TextView arriveTime;
            private TextView startTime;
            private TextView beginStation;
            private TextView endStation;

            public ContactPathViewHolder(View itemView) {
                super(itemView);
                pathName = (TextView) itemView.findViewById(R.id.timetable_pathName);
                stationName = (TextView) itemView.findViewById(R.id.timetable_stationName);
                arriveTime = (TextView) itemView.findViewById(R.id.timetable_arriveTime);
                startTime = (TextView) itemView.findViewById(R.id.timetable_startTime);
                beginStation = (TextView) itemView.findViewById(R.id.timetable_startStation);
                endStation = (TextView) itemView.findViewById(R.id.timetable_arriveStation);
            }
        }
    }
}
