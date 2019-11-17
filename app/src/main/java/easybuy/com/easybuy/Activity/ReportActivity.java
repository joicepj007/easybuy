




package easybuy.com.easybuy.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import easybuy.com.easybuy.Adapter.ReportAdapter;
import easybuy.com.easybuy.Controller.AppSession;
import easybuy.com.easybuy.Controller.Constants;
import easybuy.com.easybuy.Interfaces.ItemClickListner;
import easybuy.com.easybuy.Interfaces.onApiFinish;
import easybuy.com.easybuy.R;
import easybuy.com.easybuy.ResponseClass.OrderHistoryResponse;
import easybuy.com.easybuy.ResponseClass.OrderReportResponse;
import easybuy.com.easybuy.WebServices.Backgroundtask.CallWebServiceTask;
import easybuy.com.easybuy.util.Fonts;
import pub.devrel.easypermissions.EasyPermissions;

public class ReportActivity extends RootActivity implements onApiFinish, EasyPermissions.PermissionCallbacks {

    private Context mContext;
    ListView listview;
    ImageView img_calender, img_calender_to;
    private Calendar cal1;
    private Calendar today;
    private int day1;
    private int month1;
    private int year1;
    String day1string, month1string, year1string;
    private int birthYear;
    Date newdatee, newdateeTo;
    String fromDate, toDate;
    private final String FROM_ORDER_HISTORY = "order_history";
    private final String FROM_ORDER_REPORT_MAIL = "order_report_mail";
    List<OrderHistoryResponse.OrderData.orders> mOrderHistory;
    ReportAdapter mOrderHistoryAdapter;
    Dialog myDialog;
    public static final String MyPREFERENCES_DATE = "MyPrefsDate";
    private SharedPreferences sharedpreferencesDate;
    boolean mFromFlag=false;
    boolean mToFlag=false;

    public ItemClickListner getmItemClickListner() {
        return mItemClickListner;
    }

    public void setmItemClickListner(ItemClickListner mItemClickListner) {
        this.mItemClickListner = mItemClickListner;
    }
    private ItemClickListner mItemClickListner;
    private LinearLayout ll_back_arrow;
    private TextView tv_head_login;
    public static final String MyPREFERENCES_Order = "MyPrefsOrder";
    private SharedPreferences sharedpreferenceOrder;
    private TextView tv_from, tv_from_date, tv_to, tv_to_date, tv_name, tv_date, tv_price, tv_order_id,
            tv_order_id_value, tv_ok, total, tv_exit, tv_okk;

    FloatingActionButton btn_report;

    String fileName = "myFile.txt";
    String textToWrite = "This is some text!";
    Date date1, date2;
    ArrayList<String> gfg = new ArrayList<String>();
    ArrayList<Integer> newList = new ArrayList<Integer>();

    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    private static final int WRITE_REQUEST_CODE = 300;
    String html="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
        mContext = this;

        myDialog = new Dialog(mContext);

        AppSession.getInstance().setOrderListFlag(true);
        sharedpreferenceOrder = getSharedPreferences(MyPREFERENCES_Order, Context.MODE_PRIVATE);
        sharedpreferencesDate = getSharedPreferences(MyPREFERENCES_DATE, Context.MODE_PRIVATE);

        listview = (ListView)findViewById(R.id.listview);
        img_calender = (ImageView)findViewById(R.id.img_calender);
        img_calender_to = (ImageView)findViewById(R.id.img_calender_to);
        tv_from = (TextView) findViewById(R.id.tv_from);
        tv_from_date = (TextView) findViewById(R.id.tv_from_date);
        tv_to = (TextView) findViewById(R.id.tv_to);
        tv_to_date = (TextView) findViewById(R.id.tv_to_date);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_price = (TextView) findViewById(R.id.tv_price);
        total = (TextView) findViewById(R.id.total);
        ll_back_arrow = (LinearLayout) findViewById(R.id.ll_back_arrow);

        btn_report= (FloatingActionButton) findViewById(R.id.btn_report);

        tv_from.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Regular.ttf"));
        tv_from_date.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Regular.ttf"));
        tv_to.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Regular.ttf"));
        tv_to_date.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Regular.ttf"));
        tv_name.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Bold.ttf"));
        tv_date.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Bold.ttf"));
        tv_price.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Bold.ttf"));
        total.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Bold.ttf"));

        total.setText("SALES TOTAL :");

        cal1 = Calendar.getInstance();
        today = Calendar.getInstance();

        day1 = cal1.get(Calendar.DAY_OF_MONTH);
        month1 = cal1.get(Calendar.MONTH);
        year1 = cal1.get(Calendar.YEAR);
        cal1.set(Calendar.YEAR, year1 );
        cal1.set(Calendar.DAY_OF_MONTH,day1);
        cal1.set(Calendar.MONTH, month1);

        img_calender.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                showdatePicker();
            }
        });

        img_calender_to.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                showdatePickerTo();
            }
        });

        ll_back_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ReportActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                mailReport();
                //mailMethod();




            }
        });

        mItemClickListner=new ItemClickListner() {
            @Override
            public void onItemSelected(String item, int position, Object response) {

                if (item.equals("select_item")){

                    myDialog.setContentView(R.layout.report_popup);

                    tv_order_id =(TextView) myDialog.findViewById(R.id.tv_order_id);
                    tv_order_id_value =(TextView) myDialog.findViewById(R.id.tv_order_id_value);
                    tv_ok =(TextView) myDialog.findViewById(R.id.tv_ok);

                    tv_order_id_value.setText(mOrderHistory.get(position).getOrder_id());
                    tv_order_id.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Semibold.ttf"));
                    tv_order_id_value.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Semibold.ttf"));
                    tv_ok.setTypeface(Fonts.getTypefaceTwo(ReportActivity.this, "Lato-Semibold.ttf"));

                    tv_ok.setOnClickListener(new OnClickListener() {
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


            }

            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position, Object response) {

            }
        };

    }

    private void mailReport() {

        String URL = Constants.API.ORDERREPORTMAIL;
        URL = URL + "&filter_date_from=" + fromDate+ "&filter_date_to=" + toDate +"&limit=" + "500"+ "&store_id=" +AppSession.getInstance().getStoreId();
        HashMap<String, String> values = new HashMap<>();
       // values.put("store_id", AppSession.getInstance().getStoreId());

        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(ReportActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_ORDER_REPORT_MAIL, "GET");

    }

    private void mailMethod() {

        String filename="myFile.txt";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"madhuri.xtapps@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    private void showdatePickerTo() {

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year1string, int month1string, int day1string) {

                try {

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


                    newdateeTo = sdf.parse(year1string + "-" + month1string + "-" + day1string);


                    toDate = sdf.format(myCalendar.getTime()).toString();
                    String DayTwo, MonthTwo, YearTwo;

                    DayTwo = String.valueOf(day1string);
                    MonthTwo = String.valueOf(month1string + 1);
                    YearTwo = String.valueOf(year1string);

                    // date2 = sdf.parse(day2string+ "/" + month2string + "/" + year2string);

                    /*Toast.makeText(getActivity(), "date1"+date1.toString(),Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "date2"+date2.toString(),Toast.LENGTH_LONG).show();*/

                    sdf = new SimpleDateFormat("yy-MM-dd");

                    String formatedDate = sdf.format(newdateeTo);

                    date2 =sdf.parse(year1string+ "-" +MonthTwo + "-" +day1string);

                    /*SharedPreferences.Editor editor1 = sharedpreferencesDate.edit();
                    editor1.putString("DateFlag", "2");
                    editor1.putString("Day2", DayTwo);
                    editor1.putString("month2", MonthTwo);
                    editor1.putString("year2", YearTwo);
                    editor1.putString("date1", sharedpreferencesDate.getString("date1", ""));
                    editor1.putString("date2", formatedDate);

                    editor1.commit();


                    if (!sharedpreferencesDate.getString("date1", "").equals("")) {


                        // String formatedDate = sdf.format(date);

                        //  Date firstDate = sdf.parse(sharedpreferencesDate.getString("date1",""));

                        Date firstDate = null;

                        firstDate = sdf.parse(sharedpreferencesDate.getString("Day1", "") + "/" + sharedpreferencesDate.getString("month1", "") + "/" + sharedpreferencesDate.getString("year1", ""));


                        Date secDate = null;

                        secDate = sdf.parse(sharedpreferencesDate.getString("Day2", "") + "/" + sharedpreferencesDate.getString("month2", "") + "/" + sharedpreferencesDate.getString("year2", ""));


                        //   Date secDate = sdf.parse(day2string+ "/" + month2string+1+ "/" + year2string);
                        //  Date secDate = date2;

                        if (secDate.compareTo(firstDate) < 0) {


                            Toast.makeText(getApplicationContext(), " To date should be greater then From date", Toast.LENGTH_LONG).show();
                        } else {
                            mFromFlag = false;
                            mToFlag = false;

                        }
                    } else {
                        mToFlag = false;
                    }*/

                }
                catch (ParseException e){

                }

                int month= month1string+1;
                String fm=""+month;
                String fd=""+day1string;
                if(month<10){
                    fm ="0"+month;
                }
                if (day1string<10){
                    fd="0"+day1string;
                }
                String myFormat = "dd-MM-yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                String date= sdf.format(date2);


                //Collection.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                //  Collection.setText(fm+"/"+fd+"/"+year1string);



                //availableFrom.setText(day1string+"/"+month1string+"/"+year1string);
                tv_to_date.setText(date);


                // tv_to_date.setText(newdateeTo);
                orderFilter();
                //Toast.makeText(getApplicationContext(),newdatee,Toast.LENGTH_LONG).show();

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year1, month1, day1);
        dpDialog.getDatePicker().setMaxDate(cal1.getTimeInMillis());
        dpDialog.show();


    }

    private void showdatePicker() {

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year1string, int month1string, int day1string) {

                try {

                    myCalendar.set(Calendar.YEAR, year1string);
                    myCalendar.set(Calendar.MONTH, month1string);
                    myCalendar.set(Calendar.DAY_OF_MONTH, day1string);


                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    newdatee = sdf.parse(year1string + "-" + month1string + "-" + day1string);

                    fromDate = sdf.format(myCalendar.getTime()).toString();

                    String DayOne, MonthOne, YearOne;

                    DayOne = String.valueOf(day1string);
                    MonthOne = String.valueOf(month1string + 1);
                    YearOne = String.valueOf(year1string);

                    sdf = new SimpleDateFormat("yy-MM-dd");
                    String formatedDate = sdf.format(newdatee);

                    date1 =sdf.parse(year1string+ "-" +MonthOne + "-" +day1string);

                    /*SharedPreferences.Editor editor1 = sharedpreferencesDate.edit();
                    editor1.putString("DateFlag", "2");
                    editor1.putString("Day1", DayOne);
                    editor1.putString("month1", MonthOne);
                    editor1.putString("year1", YearOne);
                    editor1.putString("date1", formatedDate);
                    editor1.putString("date2", sharedpreferencesDate.getString("date2", ""));


                    editor1.commit();


                    if (!sharedpreferencesDate.getString("date2", "").equals("")) {


                        Date secDate = null;

                        secDate = sdf.parse(sharedpreferencesDate.getString("year1", "") + "-" + sharedpreferencesDate.getString("month1", "") + "-" + sharedpreferencesDate.getString("Day1", ""));


                        Date firstDate = null;

                        firstDate = sdf.parse(sharedpreferencesDate.getString("year2", "") + "-" + sharedpreferencesDate.getString("month2", "") + "-" + sharedpreferencesDate.getString("Day2", ""));


                        //  Date firstDate = date1;
                        // date2 = sdf.parse(sharedpreferencesDate.getString("Day2","")+ "/" + sharedpreferencesDate.getString("month2","") + "/" + sharedpreferencesDate.getString("year2",""));
                        // Date secDate = sdf.parse(sharedpreferencesDate.getString("date2",""));


                        if (firstDate.compareTo(secDate) < 0) {
                            mFromFlag = true;
                            Toast.makeText(getApplicationContext(), " From date should be less than To date", Toast.LENGTH_LONG).show();

                        } else {
                            mFromFlag = false;
                            mToFlag = false;
                        }
                    } else {
                        mFromFlag = false;
                    }
*/
                }
                catch (ParseException e){

                }

                int month= month1string+1;
                String fm=""+month;
                String fd=""+day1string;
                if(month<10){
                    fm ="0"+month;
                }
                if (day1string<10){
                    fd="0"+day1string;
                }

                String myFormat = "dd-MM-yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                String date= sdf.format(date1);


                //Collection.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                //  Collection.setText(fm+"/"+fd+"/"+year1string);



                //availableFrom.setText(day1string+"/"+month1string+"/"+year1string);
                tv_from_date.setText(date);

                // tv_from_date.setText(newdatee);
                orderFilter();
                //Toast.makeText(getApplicationContext(),newdatee,Toast.LENGTH_LONG).show();

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year1, month1, day1);
        dpDialog.getDatePicker().setMaxDate(cal1.getTimeInMillis());
        dpDialog.show();

    }


    private void orderFilter() {


        String URL = Constants.API.ORDERHISTORY;
        URL = URL + "&filter_date_from=" + fromDate+ "&filter_date_to=" + toDate;
        HashMap<String, String> values = new HashMap<>();
        values.put("store_id", AppSession.getInstance().getStoreId());

        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(ReportActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_ORDER_HISTORY, "POST");
    }


    @Override
    public void onSuccess(String tag, String response) {

        if (tag.equals(FROM_ORDER_HISTORY)){

            Gson gson = new Gson();
            OrderHistoryResponse orderHistoryResponse = gson.fromJson(response, OrderHistoryResponse.class);
            if (orderHistoryResponse.getStatus().equals("success")) {

                if (date2==null){

                    mOrderHistory = orderHistoryResponse.getOrderData().getOrders();

                   /* for (int i=0; i<orderHistoryResponse.getOrderData().getOrders().get(i).getTotal().length(); i++){

                        gfg.add(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal());
                    }
                    Log.v(tag, "index=" + gfg);
*/

                    if (mContext != null) {
                        listview.setVisibility(View.VISIBLE);
                       /* btn_report.setVisibility(View.VISIBLE);*/
                        // AppSession.getInstance().setCustomer_address(orderHistoryResponse.getOrderData().getOrders().get(0).getOrderInfo().getPayment_city());
                        mOrderHistoryAdapter = new ReportAdapter(getApplicationContext(), mOrderHistory, mItemClickListner);
                        listview.setAdapter(mOrderHistoryAdapter);
                        mOrderHistoryAdapter.notifyDataSetChanged();
                    }

                    if (mOrderHistoryAdapter.isEmpty()){
                        total.setText("SALES TOTAL :");
                    }

                    else {

                        btn_report.setVisibility(View.VISIBLE);

                        double sum=0.0;

                        for (int i=0; i<mOrderHistory.size(); i++){

                            // gfg.add(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal());
                            sum += Double.parseDouble(((String.valueOf(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal()))));
                            // newList = new ArrayList<Integer>(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal().length()) ;

                        }
                        Log.v(tag, "index=" + gfg);

                    /*for (double i: newList) {
                        sum += i;

                    }*/

                        DecimalFormat df = new DecimalFormat(".##");

                        total.setText("SALES TOTAL :"+" "+"AED"+" "+df.format(sum));
                    }



                }

               else if (date1==null){

                    mOrderHistory = orderHistoryResponse.getOrderData().getOrders();

                   /* for (int i=0; i<orderHistoryResponse.getOrderData().getOrders().get(i).getTotal().length(); i++){

                        gfg.add(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal());
                    }
                    Log.v(tag, "index=" + gfg);
*/

                    if (mContext != null) {
                        listview.setVisibility(View.VISIBLE);
                        /* btn_report.setVisibility(View.VISIBLE);*/
                        // AppSession.getInstance().setCustomer_address(orderHistoryResponse.getOrderData().getOrders().get(0).getOrderInfo().getPayment_city());
                        mOrderHistoryAdapter = new ReportAdapter(getApplicationContext(), mOrderHistory, mItemClickListner);
                        listview.setAdapter(mOrderHistoryAdapter);
                        mOrderHistoryAdapter.notifyDataSetChanged();
                    }

                    if (mOrderHistoryAdapter.isEmpty()){
                        total.setText("SALES TOTAL :");
                    }

                    else {

                        btn_report.setVisibility(View.VISIBLE);

                        double sum=0.0;

                        for (int i=0; i<mOrderHistory.size(); i++){

                            // gfg.add(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal());
                            sum += Double.parseDouble(((String.valueOf(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal()))));
                            // newList = new ArrayList<Integer>(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal().length()) ;

                        }
                        Log.v(tag, "index=" + gfg);

                    /*for (double i: newList) {
                        sum += i;

                    }*/

                        DecimalFormat df = new DecimalFormat(".##");

                        total.setText("SALES TOTAL :"+" "+"AED"+" "+df.format(sum));
                    }



                }


                else if (date1.after(date2))
                {
                    //mFromFlag = false;

                    mOrderHistory = orderHistoryResponse.getOrderData().getOrders();


                    if (mContext != null) {
                        listview.setVisibility(View.VISIBLE);
                        /*btn_report.setVisibility(View.VISIBLE);*/
                        // AppSession.getInstance().setCustomer_address(orderHistoryResponse.getOrderData().getOrders().get(0).getOrderInfo().getPayment_city());
                        mOrderHistoryAdapter = new ReportAdapter(getApplicationContext(), mOrderHistory, mItemClickListner);
                        listview.setAdapter(mOrderHistoryAdapter);
                        mOrderHistoryAdapter.notifyDataSetChanged();
                    }

                    myDialog.setContentView(R.layout.date_popup);

                    tv_exit =(TextView) myDialog.findViewById(R.id.tv_exit);
                   // tv_cancel =(TextView) myDialog.findViewById(R.id.tv_cancel);
                    tv_okk =(TextView) myDialog.findViewById(R.id.tv_ok);



                    tv_okk.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            myDialog.dismiss();
                        }
                    });



                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    Window window = myDialog.getWindow();
                    window.setLayout(400, LayoutParams.WRAP_CONTENT);
                    myDialog.show();


                   // Toast.makeText(getApplicationContext(), " From date should be less than To date", Toast.LENGTH_LONG).show();
                    tv_from_date.setText("");
                    total.setText("SALES TOTAL :");
                }

                else if (date1.equals(date2)) {
                    //mFromFlag = false;

                    mOrderHistory = orderHistoryResponse.getOrderData().getOrders();


                    if (mContext != null) {

                        listview.setVisibility(View.VISIBLE);
                        /*btn_report.setVisibility(View.VISIBLE);*/
                        // AppSession.getInstance().setCustomer_address(orderHistoryResponse.getOrderData().getOrders().get(0).getOrderInfo().getPayment_city());
                        mOrderHistoryAdapter = new ReportAdapter(getApplicationContext(), mOrderHistory, mItemClickListner);
                        listview.setAdapter(mOrderHistoryAdapter);
                        mOrderHistoryAdapter.notifyDataSetChanged();
                    }

                    if (mOrderHistoryAdapter.isEmpty()){
                        total.setText("SALES TOTAL :");
                    }

                    else {

                        btn_report.setVisibility(View.VISIBLE);

                        double sum=0.0;

                        for (int i=0; i<mOrderHistory.size(); i++){

                            // gfg.add(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal());
                            sum += Double.parseDouble(((String.valueOf(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal()))));
                            // newList = new ArrayList<Integer>(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal().length()) ;

                        }
                        Log.v(tag, "index=" + gfg);

                    /*for (double i: newList) {
                        sum += i;

                    }*/

                        DecimalFormat df = new DecimalFormat(".##");

                        total.setText("SALES TOTAL :"+" "+"AED"+" "+df.format(sum));
                    }


                }




                else {

                    mOrderHistory = orderHistoryResponse.getOrderData().getOrders();

                    if (mContext != null) {
                        listview.setVisibility(View.VISIBLE);
                       // btn_report.setVisibility(View.VISIBLE);
                        // AppSession.getInstance().setCustomer_address(orderHistoryResponse.getOrderData().getOrders().get(0).getOrderInfo().getPayment_city());
                        mOrderHistoryAdapter = new ReportAdapter(getApplicationContext(), mOrderHistory, mItemClickListner);
                        listview.setAdapter(mOrderHistoryAdapter);
                        mOrderHistoryAdapter.notifyDataSetChanged();
                    }

                    if (mOrderHistoryAdapter.isEmpty()){
                        btn_report.setVisibility(View.INVISIBLE);
                        total.setText("SALES TOTAL :");
                    }

                    else {

                        btn_report.setVisibility(View.VISIBLE);

                        double sum=0.0;

                        for (int i=0; i<mOrderHistory.size(); i++){

                            // gfg.add(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal());
                            sum += Double.parseDouble(((String.valueOf(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal()))));
                            // newList = new ArrayList<Integer>(orderHistoryResponse.getOrderData().getOrders().get(i).getTotal().length()) ;

                        }
                        Log.v(tag, "index=" + gfg);

                    /*for (double i: newList) {
                        sum += i;

                    }*/

                        DecimalFormat df = new DecimalFormat(".##");

                        total.setText("SALES TOTAL :"+" "+"AED"+" "+df.format(sum));
                    }




                }

            }

            else {

            }
        }

        if (tag.equals(FROM_ORDER_REPORT_MAIL)){
                Gson gson = new Gson();
                OrderReportResponse orderReportResponse = gson.fromJson(response, OrderReportResponse.class);

            if (orderReportResponse.getStatus().equals("success")) {

                 html = orderReportResponse.getPath();

                if (CheckForSDCard.isSDCardPresent()) {

                    //check if app has permission to write to the external storage.
                    if (EasyPermissions.hasPermissions(ReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Get the URL entered
                        new DownloadFile().execute(html);

                    } else {
                        //If permission is not present request for the same.
                        EasyPermissions.requestPermissions(ReportActivity.this, "", WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "SD Card not found", Toast.LENGTH_LONG).show();

                }
            }

               /* String filename =  "/home/download/";
                String senden = html;

                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(senden.toString().getBytes());
                    outputStream.close();
                } catch (Exception e) {

                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filename));
               // intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(fileout));
                intent.setData(Uri.parse("mailto:"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

               startActivity(intent);
                finish();*/

                /*Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, html);

// this is where I want to create attachment
                intent.putExtra(Intent.EXTRA_STREAM, html);

                startActivity(Intent.createChooser(intent, "Send Email"));*/
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
        Intent intent = new Intent(ReportActivity.this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, ReportActivity.this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        new DownloadFile().execute(html);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
       // Log.d(TAG, "Permission has been denied");
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(ReportActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                //fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                   // Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }
}
