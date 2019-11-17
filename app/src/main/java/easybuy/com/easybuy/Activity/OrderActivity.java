package easybuy.com.easybuy.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import easybuy.com.easybuy.Adapter.OrderHistoryAdapter;
import easybuy.com.easybuy.Adapter.OrderListAdapter;
import easybuy.com.easybuy.Adapter.ScheduledOrderAdapter;
import easybuy.com.easybuy.Controller.AppSession;
import easybuy.com.easybuy.Controller.Constants;
import easybuy.com.easybuy.Controller.Constants.API;
import easybuy.com.easybuy.Interfaces.ItemClickListner;
import easybuy.com.easybuy.Interfaces.onApiFinish;
import easybuy.com.easybuy.R;
import easybuy.com.easybuy.ResponseClass.OrderHistoryResponse;
import easybuy.com.easybuy.ResponseClass.OrderListResponse;
import easybuy.com.easybuy.WebServices.Backgroundtask.CallWebServiceTask;
import easybuy.com.easybuy.util.ConnectionDetector;
import easybuy.com.easybuy.util.Fonts;

public class OrderActivity extends RootActivity implements onApiFinish {

    private LinearLayout ll_back_arrow, linear_log_out, order_list, order_history, scheduled_list;
    private TextView grocery_name, grocery_address, tv_order_list, tv_order_history, tv_scheduled_list;
    private View order_view, history_view, scheduled_view;
    ListView listview, listview2, listview3;
    ImageView img_calender;

    OrderListAdapter mOrderListAdapter;
    OrderHistoryAdapter mOrderHistoryAdapter;
    ScheduledOrderAdapter mScheduledOrderAdapter;
    List<OrderListResponse.OrderData.orders> mOrderList;
    List<OrderListResponse.OrderData.orders.OrderInfo.OrderProducts> mOrderListPrice=new ArrayList<>();
    List<OrderHistoryResponse.OrderData.orders> mOrderHistory;
    private Context mContext;
    private final String FROM_ORDER_LIST = "order_list";
    private final String FROM_ORDER_HISTORY = "order_history";
    private final String FROM_SCHEDULED_LIST = "scheduled_list";
    public static final String MyPREFERENCES_Order = "MyPrefsOrder";
    private SharedPreferences sharedpreferenceOrder;

    public ItemClickListner getmItemClickListner() {
        return mItemClickListner;
    }

    public void setmItemClickListner(ItemClickListner mItemClickListner) {
        this.mItemClickListner = mItemClickListner;
    }
    private ItemClickListner mItemClickListner;
    String newdatee;
    private Calendar cal1;
    private Calendar today;
    private int day1;
    private int month1;
    private int year1;
    String day1string, month1string, year1string;
    private int birthYear;
    FloatingActionButton btn_report;
    Dialog myDialog;
    TextView tv_exit, tv_cancel, tv_ok;
    private ConnectionDetector objDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);

        mContext=this;
        myDialog = new Dialog(mContext);
        objDetector = new ConnectionDetector(getContext());
        sharedpreferenceOrder = getSharedPreferences(MyPREFERENCES_Order, Context.MODE_PRIVATE);

        if (!objDetector.isConnectingToInternet()) {

            Toast.makeText(getApplicationContext(),"No network connection",Toast.LENGTH_LONG).show();

        }

        ll_back_arrow =(LinearLayout)findViewById(R.id.ll_back_arrow);
        linear_log_out =(LinearLayout)findViewById(R.id.linear_log_out);
        order_list =(LinearLayout)findViewById(R.id.order_list);
        order_history =(LinearLayout)findViewById(R.id.order_history);
        scheduled_list =(LinearLayout)findViewById(R.id.scheduled_list);

        grocery_name = (TextView)findViewById(R.id.grocery_name);
        grocery_address = (TextView)findViewById(R.id.grocery_address);
        tv_order_list = (TextView)findViewById(R.id.tv_order_list);
        tv_order_history = (TextView)findViewById(R.id.tv_order_history);
        tv_scheduled_list = (TextView)findViewById(R.id.tv_scheduled_list);

        order_view =(View)findViewById(R.id.order_view);
        history_view =(View)findViewById(R.id.history_view);
        scheduled_view =(View)findViewById(R.id.scheduled_view);

        listview = (ListView)findViewById(R.id.listview);
        listview2 = (ListView)findViewById(R.id.listview2);
        listview3 = (ListView)findViewById(R.id.listview3);
        img_calender = (ImageView) findViewById(R.id.img_calender);
        btn_report= (FloatingActionButton)findViewById(R.id.btn_report);
        grocery_name.setTypeface(Fonts.getTypefaceTwo(OrderActivity.this, "Lato-Regular.ttf"));
        grocery_address.setTypeface(Fonts.getTypefaceTwo(OrderActivity.this, "Lato-Regular.ttf"));
        tv_order_list.setTypeface(Fonts.getTypefaceTwo(OrderActivity.this, "Lato-Regular.ttf"));
        tv_order_history.setTypeface(Fonts.getTypefaceTwo(OrderActivity.this, "Lato-Regular.ttf"));


        if (AppSession.getInstance().getOrderListFlag()){

            AppSession.getInstance().setOrderListFlag(false);
            getOrderList();

        }

        else if(AppSession.getInstance().getOrderHistoryFlag()){

            AppSession.getInstance().setOrderHistoryFlag(false);

            history_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorYellow));
            tv_order_history.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
            order_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            tv_order_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            scheduled_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            tv_scheduled_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            getOrderHistory();
        }

        else if(AppSession.getInstance().getOrderScheduledFlag()){

            AppSession.getInstance().setOrderScheduledFlag(false);

            history_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            tv_order_history.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            order_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            tv_order_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
            scheduled_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorYellow));
            tv_scheduled_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));

            getScheduledList();

        }

       grocery_address.setText(AppSession.getInstance().getCustomer_address());
        grocery_name.setText(AppSession.getInstance().getGrocery_name());

        cal1 = Calendar.getInstance();
        today = Calendar.getInstance();

        day1 = cal1.get(Calendar.DAY_OF_MONTH);
        month1 = cal1.get(Calendar.MONTH);
        year1 = cal1.get(Calendar.YEAR);
        cal1.set(Calendar.YEAR, year1 );
        cal1.set(Calendar.DAY_OF_MONTH,day1);
        cal1.set(Calendar.MONTH, month1);

        btn_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        order_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                order_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorYellow));
                tv_order_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
                history_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                tv_order_history.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                scheduled_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                tv_scheduled_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));

                getOrderList();

                /*if (mContext!=null) {
                    listview.setVisibility(View.VISIBLE);
                    listview2.setVisibility(View.GONE);
                    mOrderListAdapter = new OrderListAdapter(getApplicationContext(), mOrderList, mItemClickListner);
                    listview.setAdapter(mOrderListAdapter);
                }*/
            }
        });

        order_history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                history_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorYellow));
                tv_order_history.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
                order_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                tv_order_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                scheduled_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                tv_scheduled_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));

                getOrderHistory();

                /*listview2.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                mOrderHistoryAdapter = new OrderHistoryAdapter(getApplicationContext(),OrderHistory, mItemClickListner);
                listview2.setAdapter(mOrderHistoryAdapter);*/
            }
        });

        scheduled_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                history_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                tv_order_history.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                order_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                tv_order_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorAsh));
                scheduled_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorYellow));
                tv_scheduled_list.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));

                getScheduledList();

                /*listview2.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                mOrderHistoryAdapter = new OrderHistoryAdapter(getApplicationContext(),OrderHistory, mItemClickListner);
                listview2.setAdapter(mOrderHistoryAdapter);*/
            }
        });


        ll_back_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialog.setContentView(R.layout.exit_popup);

                tv_exit =(TextView) myDialog.findViewById(R.id.tv_exit);
                tv_cancel =(TextView) myDialog.findViewById(R.id.tv_cancel);
                tv_ok =(TextView) myDialog.findViewById(R.id.tv_ok);



                tv_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        AppSession.getInstance().setUserId(" ");
                        AppSession.getInstance().setLoggedInStatus(false);
                        Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

tv_cancel.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View view) {

        myDialog.dismiss();
    }
});

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                Window window = myDialog.getWindow();
                window.setLayout(400, LayoutParams.WRAP_CONTENT);
                myDialog.show();

                // Toast.makeText(getApplicationContext(),mOrderHistory.get(position).getOrder_id(),Toast.LENGTH_LONG).show();

            }


               /* Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();*/
        });

        linear_log_out.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialog.setContentView(R.layout.exit_popup);

                tv_exit =(TextView) myDialog.findViewById(R.id.tv_exit);
                tv_cancel =(TextView) myDialog.findViewById(R.id.tv_cancel);
                tv_ok =(TextView) myDialog.findViewById(R.id.tv_ok);



                tv_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        AppSession.getInstance().setUserId(" ");
                        AppSession.getInstance().setLoggedInStatus(false);
                        Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                tv_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        myDialog.dismiss();
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                Window window = myDialog.getWindow();
                window.setLayout(400, LayoutParams.WRAP_CONTENT);
                myDialog.show();


            }
        });



        mItemClickListner = new ItemClickListner() {
            @Override
            public void onItemSelected(String tag, int position, Object response) {

                if (tag.equals("select_location")){

                    SharedPreferences.Editor editor1 = sharedpreferenceOrder.edit();
                    editor1.putString("CustomerAddress",mOrderList.get(position).getOrderInfo().getPayment_address_1());
                    editor1.putString("CustomerCity",mOrderList.get(position).getOrderInfo().getPayment_city());
                    //editor1.putString("CustomerCity",mOrderList.get(position).getPayment_city());
                    editor1.commit();

                Intent intent = new Intent(OrderActivity.this,LocationMapActivity.class);
                startActivity(intent);

                }

                if (tag.equals("select_locations")){

                    SharedPreferences.Editor editor1 = sharedpreferenceOrder.edit();
                    editor1.putString("CustomerAddress",mOrderHistory.get(position).getOrderInfo().getPayment_address_1());
                    editor1.putString("CustomerCity",mOrderHistory.get(position).getOrderInfo().getPayment_city());
                    //editor1.putString("CustomerCity",mOrderList.get(position).getPayment_city());
                    editor1.commit();

                    Intent intent = new Intent(OrderActivity.this,LocationMapHistoryActivity.class);
                    startActivity(intent);

                }

                if (tag.equals("select_location_schedule")){

                    SharedPreferences.Editor editor1 = sharedpreferenceOrder.edit();
                    editor1.putString("CustomerAddress",mOrderList.get(position).getOrderInfo().getPayment_address_1());
                    editor1.putString("CustomerCity",mOrderList.get(position).getOrderInfo().getPayment_city());
                    //editor1.putString("CustomerCity",mOrderList.get(position).getPayment_city());
                    editor1.commit();

                    Intent intent = new Intent(OrderActivity.this,LocationMapScheduledActivity.class);
                    startActivity(intent);

                }


                if (tag.equals("select_calender")){

                    showdatePicker();

                }

               if (tag.equals("select_list_item")){
                   SharedPreferences.Editor editor1 = sharedpreferenceOrder.edit();
                   editor1.putString("CustomerName",mOrderList.get(position).getCustomer());
                   editor1.putString("CustomerEmail",mOrderList.get(position).getEmail());
                   editor1.putString("CustomerPhone",mOrderList.get(position).getTelephone());
                   editor1.putString("OrderId",mOrderList.get(position).getOrder_id());
                   editor1.putString("OrderStatus",mOrderList.get(position).getOrder_status());
                   editor1.putString("OrderDate",mOrderList.get(position).getDate_added());
                   editor1.putString("OrderTotal",mOrderList.get(position).getTotal());
                   editor1.putString("OrderVat",mOrderList.get(position).getVat_charge());
                   editor1.putString("OrderDelivery",mOrderList.get(position).getDelivery_charge());
                   editor1.putString("OrderProductName",mOrderList.get(position).getOrderInfo().getStore_name());


                   double sum = 0;
                   DecimalFormat precision = new DecimalFormat("0.00");
                   //double sumOne =0.00;
                   for ( OrderListResponse.OrderData.orders.OrderInfo.OrderProducts item : mOrderListPrice) {
                       sum = (double) (sum + (Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity())));
                       //sumOne = (double)sum;
                       editor1.putString("OrderSubTotal",precision.format(sum )+ "");
                   }
                   //tv_subtotal_value.setText(precision.format(sum )+ "");

                  // editor1.putString("OrderProductName",mOrderList.get(position).getOrderInfo().getOrder_products().get(position).getName());
                   // editor1.putString("OrderProductPrice",mOrderHistory.get(position).getOrderProducts().get(position).getPrice());
                   // editor1.putString("OrderProductQuantity",mOrderHistory.get(position).getOrderProducts().get(position).getQuantity());
                   editor1.commit();

                   Intent intent = new Intent(OrderActivity.this,OrderDetailActivity.class);
                   startActivity(intent);
               }

                if (tag.equals("select_scheduled_list_item")){
                    SharedPreferences.Editor editor1 = sharedpreferenceOrder.edit();
                    editor1.putString("CustomerName",mOrderList.get(position).getCustomer());
                    editor1.putString("CustomerEmail",mOrderList.get(position).getEmail());
                    editor1.putString("CustomerPhone",mOrderList.get(position).getTelephone());
                    editor1.putString("OrderId",mOrderList.get(position).getOrder_id());
                    editor1.putString("OrderStatus",mOrderList.get(position).getOrder_status());
                    editor1.putString("OrderDate",mOrderList.get(position).getDate_added());
                    editor1.putString("OrderTotal",mOrderList.get(position).getTotal());
                    editor1.putString("OrderVat",mOrderList.get(position).getVat_charge());
                    editor1.putString("OrderDelivery",mOrderList.get(position).getDelivery_charge());
                    editor1.putString("OrderProductName",mOrderList.get(position).getOrderInfo().getStore_name());

                    double sum = 0;
                    DecimalFormat precision = new DecimalFormat("0.00");
                    //double sumOne =0.00;
                    for ( OrderListResponse.OrderData.orders.OrderInfo.OrderProducts item : mOrderListPrice) {
                        sum = (double) (sum + (Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity())));
                        //sumOne = (double)sum;

                        editor1.putString("OrderSubTotal",precision.format(sum )+ "");
                    }
                    //tv_subtotal_value.setText(precision.format(sum )+ "");

                    // editor1.putString("OrderProductName",mOrderList.get(position).getOrderInfo().getOrder_products().get(position).getName());
                    // editor1.putString("OrderProductPrice",mOrderHistory.get(position).getOrderProducts().get(position).getPrice());
                    // editor1.putString("OrderProductQuantity",mOrderHistory.get(position).getOrderProducts().get(position).getQuantity());
                    editor1.commit();

                    Intent intent = new Intent(OrderActivity.this,OrderDetailScheduledActivity.class);
                    startActivity(intent);
                }
               if (tag.equals("select_item")){

                   SharedPreferences.Editor editor1 = sharedpreferenceOrder.edit();
                   editor1.putString("CustomerName",mOrderHistory.get(position).getCustomer());
                   editor1.putString("CustomerEmail",mOrderHistory.get(position).getEmail());
                   editor1.putString("CustomerPhone",mOrderHistory.get(position).getTelephone());
                   editor1.putString("OrderId",mOrderHistory.get(position).getOrder_id());
                   editor1.putString("OrderStatus",mOrderHistory.get(position).getOrder_status());
                   editor1.putString("OrderDate",mOrderHistory.get(position).getDate_added());
                   editor1.putString("OrderTotal",mOrderHistory.get(position).getTotal());
                   editor1.putString("OrderVat",mOrderHistory.get(position).getVat_charge());
                   editor1.putString("OrderDelivery",mOrderHistory.get(position).getDelivery_charge());
                   editor1.putString("OrderProductName",mOrderHistory.get(position).getOrderInfo().getStore_name());

                   double sum = 0;
                   DecimalFormat precision = new DecimalFormat("0.00");
                   //double sumOne =0.00;
                   for ( OrderListResponse.OrderData.orders.OrderInfo.OrderProducts item : mOrderListPrice) {
                       sum = (double) (sum + (Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity())));
                       //sumOne = (double)sum;

                       editor1.putString("OrderSubTotal",precision.format(sum )+ "");
                   }
                   //tv_subtotal_value.setText(precision.format(sum )+ "");

                  // editor1.putString("OrderProductPrice",mOrderHistory.get(position).getOrderProducts().get(position).getPrice());
                  // editor1.putString("OrderProductQuantity",mOrderHistory.get(position).getOrderProducts().get(position).getQuantity());
                   editor1.commit();

                   Intent intent = new Intent(OrderActivity.this,OrderDetailHistoryActivity.class);
                   startActivity(intent);

               }



            }

            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position, Object response) {

            }
        };

    }


    private void showdatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year1string, int month1string, int day1string) {

                myCalendar.set(Calendar.YEAR, year1string);
                myCalendar.set(Calendar.MONTH, month1string);
                myCalendar.set(Calendar.DAY_OF_MONTH, day1string);

               /* int day = day1string;
                int month = month1string + 1;
                int year = year1string;*/

                //edt_Date_birth.setText(day + "-" + month + "-" + year);
                /*birthYear= Integer.parseInt(String.valueOf(year));
                newdatee=(year+"-"+month+"-"+day);*/
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                newdatee=sdf.format(myCalendar.getTime()).toString();
                orderFilter();
                //Toast.makeText(getApplicationContext(),newdatee,Toast.LENGTH_LONG).show();

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year1, month1, day1);
        dpDialog.getDatePicker().setMaxDate(cal1.getTimeInMillis());
        dpDialog.show();


    }

    private void orderFilter() {

        String URL = API.ORDERHISTORY;
        URL = URL + "&filter_date_added=" + newdatee;
        HashMap<String, String> values = new HashMap<>();
        values.put("store_id", AppSession.getInstance().getStoreId());

        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(OrderActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_ORDER_HISTORY, "POST");
    }

    private void getOrderHistory() {

        String URL = API.ORDERHISTORY;
        URL = URL + "&filter_order_status=" + "15,17,7";
        HashMap<String, String> values = new HashMap<>();
        values.put("store_id", AppSession.getInstance().getStoreId());

        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(OrderActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_ORDER_HISTORY, "POST");

    }

    private void getOrderList() {

        String URL = Constants.API.ORDERLIST;
        HashMap<String, String> values = new HashMap<>();
        values.put("store_id", AppSession.getInstance().getStoreId());
        values.put("filter_scheduled", "0");

        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(OrderActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_ORDER_LIST, "POST");
/*
        listview.setVisibility(View.VISIBLE);
        listview2.setVisibility(View.GONE);
        mOrderListAdapter = new OrderListAdapter(getApplicationContext(),OrderList, mItemClickListner);
        listview.setAdapter(mOrderListAdapter);*/
       // mOrderListAdapter.notifyDataSetChanged();
    }

    private void getScheduledList() {

        String URL = Constants.API.ORDERLIST;
        HashMap<String, String> values = new HashMap<>();
        values.put("store_id", AppSession.getInstance().getStoreId());
        values.put("filter_scheduled", "1");

        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(OrderActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_SCHEDULED_LIST, "POST");

    }


    @Override
    public void onSuccess(String tag, String response) {

        if (tag.equals(FROM_ORDER_LIST)) {


            Gson gson = new Gson();
            OrderListResponse orderListResponse = gson.fromJson(response, OrderListResponse.class);
            if (orderListResponse.getStatus().equals("success")) {

                mOrderList = orderListResponse.getOrderData().getOrders();



                if (mContext!=null) {
                    listview.setVisibility(View.VISIBLE);
                    listview2.setVisibility(View.GONE);
                    listview3.setVisibility(View.GONE);
                    img_calender.setVisibility(View.GONE);
                   // AppSession.getInstance().setCustomer_address(orderListResponse.getOrders().get(0).getPayment_address_1());
                    mOrderListAdapter = new OrderListAdapter(getApplicationContext(), mOrderList, mItemClickListner);
                    listview.setAdapter(mOrderListAdapter);
                    mOrderListAdapter.notifyDataSetChanged();
                }
            }

            else {
                showSnackBar(orderListResponse.getMessage());
            }
        }

        else if (tag.equals(FROM_ORDER_HISTORY)){

            Gson gson = new Gson();
            OrderHistoryResponse orderHistoryResponse = gson.fromJson(response, OrderHistoryResponse.class);
            if (orderHistoryResponse.getStatus().equals("success")) {

                mOrderHistory = orderHistoryResponse.getOrderData().getOrders();

                if (mContext != null) {
                    listview.setVisibility(View.GONE);
                    listview2.setVisibility(View.VISIBLE);
                    listview3.setVisibility(View.GONE);
                    img_calender.setVisibility(View.VISIBLE);

                   // AppSession.getInstance().setCustomer_address(orderHistoryResponse.getOrderData().getOrders().get(0).getOrderInfo().getPayment_city());
                    mOrderHistoryAdapter = new OrderHistoryAdapter(getApplicationContext(), mOrderHistory, mItemClickListner);
                    listview2.setAdapter(mOrderHistoryAdapter);
                    mOrderHistoryAdapter.notifyDataSetChanged();
                }

                img_calender.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showdatePicker();

                    }
                });

            }
        }

        if (tag.equals(FROM_SCHEDULED_LIST)) {

            Gson gson = new Gson();
            OrderListResponse orderListResponse = gson.fromJson(response, OrderListResponse.class);
            if (orderListResponse.getStatus().equals("success")) {

                mOrderList = orderListResponse.getOrderData().getOrders();



                if (mContext!=null) {
                    listview.setVisibility(View.GONE);
                    listview2.setVisibility(View.GONE);
                    listview3.setVisibility(View.VISIBLE);
                    img_calender.setVisibility(View.GONE);
                    // AppSession.getInstance().setCustomer_address(orderListResponse.getOrders().get(0).getPayment_address_1());
                    mScheduledOrderAdapter = new ScheduledOrderAdapter(getApplicationContext(), mOrderList, mItemClickListner);
                    listview3.setAdapter(mScheduledOrderAdapter);
                    mScheduledOrderAdapter.notifyDataSetChanged();
                }
            }

            else {
                showSnackBar(orderListResponse.getMessage());
            }

        }
    }

    @Override
    public void onFailed(String tag, String response) {

    }

    @Override
    public void onJsonError(String tag, String response) {

    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onBackPressed()

    {

        myDialog.setContentView(R.layout.exit_popup);

        tv_exit =(TextView) myDialog.findViewById(R.id.tv_exit);
        tv_cancel =(TextView) myDialog.findViewById(R.id.tv_cancel);
        tv_ok =(TextView) myDialog.findViewById(R.id.tv_ok);



        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        Window window = myDialog.getWindow();
        window.setLayout(400, LayoutParams.WRAP_CONTENT);
        myDialog.show();

        // Toast.makeText(getApplicationContext(),mOrderHistory.get(position).getOrder_id(),Toast.LENGTH_LONG).show();



        /*Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();*/
    }

}
