package easybuy.com.easybuy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import easybuy.com.easybuy.Interfaces.ItemClickListner;
import easybuy.com.easybuy.R;
import easybuy.com.easybuy.ResponseClass.OrderHistoryResponse;
import easybuy.com.easybuy.ResponseClass.OrderHistoryResponse.OrderData.orders;
import easybuy.com.easybuy.util.Fonts;

public class ReportAdapter extends BaseAdapter {
    private Context mActivity;
    private static LayoutInflater inflater = null;
    private ReportAdapter.ViewHolder holder = null;
    private List<orders> mOrderHistory;
    private ItemClickListner ItemClickListner;
    int row_index=-1;

    public ReportAdapter(Context applicationContext, List<OrderHistoryResponse.OrderData.orders> OrderHistory, ItemClickListner mItemClickListner) {

        mActivity = applicationContext;
        mOrderHistory = OrderHistory;
        inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemClickListner = mItemClickListner;


    }

    @Override
    public int getCount() {

        return mOrderHistory.size();
        //  return 6;
    }

    @Override
    public Object getItem(int i) {

        return i;
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    class ViewHolder {

       TextView tv_name, tv_date, tv_total;
       LinearLayout linear_history_row;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View vi = view;

        if (view == null) {
            vi = inflater.inflate(R.layout.report_row_activity, null);


            holder = new ReportAdapter.ViewHolder();

            holder.tv_name =(TextView)vi.findViewById(R.id.tv_name);
            holder.tv_date =(TextView)vi.findViewById(R.id.tv_date);
            holder.tv_total =(TextView)vi.findViewById(R.id.tv_total);
            holder.linear_history_row =(LinearLayout) vi.findViewById(R.id.linear_history_row);

            holder.tv_name.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_date.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_total.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));


            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final OrderHistoryResponse.OrderData.orders item = mOrderHistory.get(position);
       // AppSession.getInstance().setCustomer_address(item.getOrderInfo().getPayment_address_1()+ " "+item.getOrderInfo().getPayment_city());
       // AppSession.getInstance().setGrocery_name(item.getOrderInfo().getStore_name());
        holder.tv_name.setText(item.getCustomer());
       // holder.tv_date.setText(dateTime(item.getDate_added()));

        String sDate1 = item.getDate_modified();
        Date date1 = null;
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yy");
        try {
             date1=formatter1.parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

      //  holder.tv_date.setText((CharSequence) date1);

        holder.tv_date.setText((item.getDate_modified()));

        holder.tv_total.setText(item.getTotal());


holder.linear_history_row.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View view) {
        row_index=position;
        ItemClickListner.onItemSelected("select_item", position, item);
        notifyDataSetChanged();

    }
});


        return vi;

    }

    public String dateTime(String time)
    {
        String dateString = null;
        try
        {

            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
            try {
                Date date1 = simpleDateFormat.parse(time);
                Date currentTime = Calendar.getInstance().getTime();
                //Date date2 = simpleDateFormat.parse("18:00:00");

                dateString=  printDifference(date1, currentTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return dateString;
    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;



        System.out.printf(
                "%02d days, %02d hours, %02d minutes, %02d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        if(elapsedHours>0)
        {
            String aaaa=String.format("%d hrs", elapsedHours)+" "+String.format("%02d +mins", elapsedMinutes);
            return aaaa;
        }
        else {
            String aaaa=String.format("%02d +mins", elapsedMinutes);
            return aaaa;
        }


        //String aaaa=String.format("%02d", elapsedHours)+":"+String.format("%02d", elapsedMinutes)+":"+String.format("%02d", elapsedSeconds);
        //return aaaa;
    }
}

