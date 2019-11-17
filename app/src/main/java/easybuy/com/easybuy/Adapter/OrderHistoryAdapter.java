package easybuy.com.easybuy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import easybuy.com.easybuy.Interfaces.ItemClickListner;
import easybuy.com.easybuy.R;
import easybuy.com.easybuy.ResponseClass.OrderHistoryResponse;
import easybuy.com.easybuy.util.Fonts;

public class OrderHistoryAdapter extends BaseAdapter {
    private Context mActivity;
    private static LayoutInflater inflater = null;
    private OrderHistoryAdapter.ViewHolder holder = null;
    private List<OrderHistoryResponse.OrderData.orders> mOrderHistory;
    private ItemClickListner ItemClickListner;
    int row_index=-1;

    public OrderHistoryAdapter(Context applicationContext, List<OrderHistoryResponse.OrderData.orders> OrderHistory, ItemClickListner mItemClickListner) {

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

        ImageView img_calender, img_location;
        TextView tv_address, tv_status, tv_orderId, tv_dateTime;
        LinearLayout linear_history_row;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View vi = view;

        if (view == null) {
            vi = inflater.inflate(R.layout.order_history_row_activity, null);


            holder = new OrderHistoryAdapter.ViewHolder();

            holder.tv_address = (TextView) vi.findViewById(R.id.tv_address);
            holder.tv_status = (TextView) vi.findViewById(R.id.tv_status);
            holder.tv_orderId = (TextView) vi.findViewById(R.id.tv_orderId);
            holder.tv_dateTime = (TextView) vi.findViewById(R.id.tv_dateTime);
            holder.img_location = (ImageView) vi.findViewById(R.id.img_location);
            holder.img_calender = (ImageView) vi.findViewById(R.id.img_calender);
            holder.linear_history_row = (LinearLayout) vi.findViewById(R.id.linear_history_row);

            holder.tv_address.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_status.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_orderId.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_dateTime.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final OrderHistoryResponse.OrderData.orders item = mOrderHistory.get(position);
       // AppSession.getInstance().setCustomer_address(item.getOrderInfo().getPayment_address_1()+ " "+item.getOrderInfo().getPayment_city());
       // AppSession.getInstance().setGrocery_name(item.getOrderInfo().getStore_name());
        holder.tv_address.setText(item.getOrderInfo().getPayment_address_1()+" " +item.getOrderInfo().getPayment_city());
        holder.tv_orderId.setText("Order Number:"+" "+item.getOrder_id());
        holder.tv_status.setText(item.getOrder_status());
        holder.tv_dateTime.setText(item.getDate_modified());

       holder.linear_history_row.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View view) {

               row_index=position;
               ItemClickListner.onItemSelected("select_item", position, item);
               notifyDataSetChanged();
           }
       });

        holder.img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index=position;
                ItemClickListner.onItemSelected("select_locations", position, item);
                notifyDataSetChanged();
                /* holder.l_layout_table_item.setBackgroundColor(Color.parseColor("#567845"));*/
            }
        });

        holder.img_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index=position;
                ItemClickListner.onItemSelected("select_calender", position, item);
                notifyDataSetChanged();
                /* holder.l_layout_table_item.setBackgroundColor(Color.parseColor("#567845"));*/
            }
        });

        return vi;

    }
}
