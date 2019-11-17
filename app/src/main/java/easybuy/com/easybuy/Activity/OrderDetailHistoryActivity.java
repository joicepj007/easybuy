package easybuy.com.easybuy.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import easybuy.com.easybuy.Adapter.OrderDetailAdapter;
import easybuy.com.easybuy.Controller.AppSession;
import easybuy.com.easybuy.Controller.Constants;
import easybuy.com.easybuy.Interfaces.onApiFinish;
import easybuy.com.easybuy.R;
import easybuy.com.easybuy.ResponseClass.OrderListResponse;
import easybuy.com.easybuy.WebServices.Backgroundtask.CallWebServiceTask;
import easybuy.com.easybuy.util.Fonts;

public class OrderDetailHistoryActivity extends RootActivity implements onApiFinish {

    LinearLayout ll_back_arrow, linear_log_out;
    private TextView grocery_name, grocery_address, tv_status, tv_time, tv_cust_name, tv_cust_email, tv_cust_phone,
            tv_subtotal_value, tv_delivery_value, tv_vat_value, tv_total_value, tv_accept, tv_reject;
    ListView listview;
    private ImageView img_status;
    public static final String MyPREFERENCES_Order = "MyPrefsOrder";
    private SharedPreferences sharedpreferenceOrder;
    private final String FROM_ORDER_ACCEPT = "order_accept";
    private final String FROM_ORDER_REJECT = "order_reject";
    private final String FROM_ORDER_HISTORY = "order_list";
    private String Order_id="";
    private Context mContext;
    OrderDetailAdapter mOrderDetailAdapter;
    List<OrderListResponse.OrderData.orders.OrderInfo.OrderProducts> mOrderProduct;
    Double totalVAlue=0.00;
    Dialog myDialog;
    TextView tv_exit, tv_cancel, tv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);
        mContext = this;
        myDialog = new Dialog(mContext);

        AppSession.getInstance().setOrderHistoryFlag(true);

        ll_back_arrow = (LinearLayout) findViewById(R.id.ll_back_arrow);
        linear_log_out = (LinearLayout) findViewById(R.id.linear_log_out);

        grocery_name = (TextView) findViewById(R.id.grocery_name);
        grocery_address = (TextView) findViewById(R.id.grocery_address);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_cust_name = (TextView) findViewById(R.id.tv_cust_name);
        tv_cust_email = (TextView) findViewById(R.id.tv_cust_email);
        tv_cust_phone = (TextView) findViewById(R.id.tv_cust_phone);
        tv_subtotal_value = (TextView) findViewById(R.id.tv_subtotal_value);
        tv_delivery_value = (TextView) findViewById(R.id.tv_delivery_value);
        tv_vat_value = (TextView) findViewById(R.id.tv_vat_value);
        tv_total_value = (TextView) findViewById(R.id.tv_total_value);
        tv_accept = (TextView) findViewById(R.id.tv_accept);
        tv_reject = (TextView) findViewById(R.id.tv_reject);
        listview = (ListView) findViewById(R.id.listview);

        tv_accept.setVisibility(View.INVISIBLE);
        tv_reject.setVisibility(View.INVISIBLE);

        sharedpreferenceOrder = getSharedPreferences(MyPREFERENCES_Order, Context.MODE_PRIVATE);

        AppSession.getInstance().setOrder_Id(sharedpreferenceOrder.getString("OrderId", ""));

        grocery_address.setText(AppSession.getInstance().getCustomer_address());
        grocery_name.setText(AppSession.getInstance().getGrocery_name());

        tv_status.setText(sharedpreferenceOrder.getString("OrderStatus", ""));
        tv_time.setText(sharedpreferenceOrder.getString("OrderDate", ""));
        tv_cust_name.setText(sharedpreferenceOrder.getString("CustomerName", ""));
        tv_cust_email.setText(sharedpreferenceOrder.getString("CustomerEmail", ""));
        tv_cust_phone.setText(sharedpreferenceOrder.getString("CustomerPhone", ""));
        tv_subtotal_value.setText(sharedpreferenceOrder.getString("OrderSubTotal", ""));
        tv_total_value.setText(sharedpreferenceOrder.getString("OrderTotal", ""));
        tv_vat_value.setText(sharedpreferenceOrder.getString("OrderVat",""));
        tv_delivery_value.setText(sharedpreferenceOrder.getString("OrderDelivery",""));
       // tv_delivery_value.setText("5");

        grocery_name.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        grocery_address.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_status.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_time.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_cust_name.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_cust_email.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_cust_phone.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_subtotal_value.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_delivery_value.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_vat_value.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));
        tv_total_value.setTypeface(Fonts.getTypefaceTwo(OrderDetailHistoryActivity.this, "Lato-Regular.ttf"));

       /* String sub_total = tv_subtotal_value.getText().toString();
        double subTotal = Double.parseDouble(sub_total);

        String delivery_value = tv_delivery_value.getText().toString();
        double deliveryValue = Double.parseDouble(delivery_value);

        totalVAlue = totalVAlue + Double.parseDouble(String.valueOf(subTotal + deliveryValue));
        tv_total_value.setText(String.valueOf(totalVAlue));*/


        getOrderHistory();

        ll_back_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderDetailHistoryActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();
            }
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
                        Intent intent = new Intent(OrderDetailHistoryActivity.this, LoginActivity.class);
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

    }

    private void getOrderHistory() {

        String URL = Constants.API.ORDERHISTORY;
        HashMap<String, String> values = new HashMap<>();
        values.put("store_id", AppSession.getInstance().getStoreId());

        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(OrderDetailHistoryActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_ORDER_HISTORY, "POST");

    }

    @Override
    public void onBackPressed()

    {
        Intent intent = new Intent(OrderDetailHistoryActivity.this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccess(String tag, String response) {
        if (tag.equals(FROM_ORDER_HISTORY)){

            Gson gson = new Gson();
            OrderListResponse orderListResponse = gson.fromJson(response, OrderListResponse.class);
            if (orderListResponse.getStatus().equals("success")) {

                mOrderProduct = orderListResponse.getOrderData().getOrders().get(0).getOrderInfo().getOrder_products();

                if (mContext!=null){
                    mOrderDetailAdapter = new OrderDetailAdapter(getApplicationContext(), mOrderProduct);
                    listview.setAdapter(mOrderDetailAdapter);
                    mOrderDetailAdapter.notifyDataSetChanged();
                }
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
}
