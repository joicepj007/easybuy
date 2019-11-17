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
import easybuy.com.easybuy.ResponseClass.OrderListResponse;
import easybuy.com.easybuy.util.Fonts;

public class OrderListAdapter extends BaseAdapter {
    private Context mActivity;
    private OrderListAdapter.ViewHolder holder = null;
    private static LayoutInflater inflater = null;
    List<OrderListResponse.OrderData.orders> morderList;

    private ItemClickListner ItemClickListner;
    int row_index=-1;

    public OrderListAdapter(Context applicationContext, List<OrderListResponse.OrderData.orders> orderList, ItemClickListner mItemClickListner) {

        mActivity = applicationContext;
        morderList = orderList;
        inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemClickListner = mItemClickListner;


    }


    @Override
    public int getCount() {

        return morderList.size();
        //return 6;
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

        ImageView location_icon;
        TextView tv_address, tv_status, tv_orderId, tv_time;
        LinearLayout linear_list;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View vi = view;

        if (view == null) {
            vi = inflater.inflate(R.layout.order_list_row_activity, null);

            holder = new OrderListAdapter.ViewHolder();

            holder.tv_address = (TextView) vi.findViewById(R.id.tv_address);
            holder.tv_status = (TextView) vi.findViewById(R.id.tv_status);
            holder.tv_orderId = (TextView) vi.findViewById(R.id.tv_orderId);
            holder.tv_time = (TextView) vi.findViewById(R.id.tv_time);
            holder.location_icon = (ImageView) vi.findViewById(R.id.location_icon);
            holder.linear_list = (LinearLayout) vi.findViewById(R.id.linear_list);

            holder.tv_address.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_status.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_orderId.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_time.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));

            vi.setTag(holder);
        } else {
            holder = (OrderListAdapter.ViewHolder) vi.getTag();
        }

        final OrderListResponse.OrderData.orders item = morderList.get(position);
       // AppSession.getInstance().setCustomer_address(item.getOrderInfo().getPayment_address_1()+ " "+item.getOrderInfo().getPayment_city());
       // AppSession.getInstance().setGrocery_name(item.getOrderInfo().getStore_name());
        holder.tv_address.setText(item.getOrderInfo().getPayment_address_1()+" " +item.getOrderInfo().getPayment_city());
        holder.tv_orderId.setText("Order Number:"+" "+item.getOrder_id());
        holder.tv_status.setText(item.getOrder_status());
        holder.tv_time.setText(item.getDate_added());

        holder.linear_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index=position;
                ItemClickListner.onItemSelected("select_list_item", position, item);
                notifyDataSetChanged();
            }
        });

        holder.location_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index=position;
                ItemClickListner.onItemSelected("select_location", position, item);
                notifyDataSetChanged();
                /* holder.l_layout_table_item.setBackgroundColor(Color.parseColor("#567845"));*/
            }
        });


        return vi;

    }
}
