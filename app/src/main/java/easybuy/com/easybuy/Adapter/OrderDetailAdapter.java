package easybuy.com.easybuy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import easybuy.com.easybuy.R;
import easybuy.com.easybuy.ResponseClass.OrderListResponse;
import easybuy.com.easybuy.util.Fonts;

public class OrderDetailAdapter extends BaseAdapter {
    private Context mActivity;
    private OrderDetailAdapter.ViewHolder holder = null;
    private static LayoutInflater inflater = null;
    List<OrderListResponse.OrderData.orders.OrderInfo.OrderProducts> mOrderProduct;

    public OrderDetailAdapter(Context applicationContext, List<OrderListResponse.OrderData.orders.OrderInfo.OrderProducts> orderList) {

        mActivity = applicationContext;
        mOrderProduct = orderList;
        inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return mOrderProduct.size();
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

        TextView tv_item, tv_number, tv_price, tv_quantity, tv_size;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View vi = view;

        if (view == null) {
            vi = inflater.inflate(R.layout.order_detail_row, null);

            holder = new OrderDetailAdapter.ViewHolder();

            holder.tv_number = (TextView) vi.findViewById(R.id.tv_number);
            holder.tv_item = (TextView) vi.findViewById(R.id.tv_item);
            holder.tv_price = (TextView) vi.findViewById(R.id.tv_price);
            holder.tv_quantity = (TextView) vi.findViewById(R.id.tv_quantity);
            holder.tv_size = (TextView) vi.findViewById(R.id.tv_size);

            holder.tv_number.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_item.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_price.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_quantity.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));
            holder.tv_size.setTypeface(Fonts.getTypefaceTwo(mActivity, "Lato-Regular.ttf"));

            vi.setTag(holder);
        } else {
            holder = (OrderDetailAdapter.ViewHolder) vi.getTag();
        }

        final OrderListResponse.OrderData.orders.OrderInfo.OrderProducts item = mOrderProduct.get(position);
        holder.tv_number.setText(""+(position + 1));
        holder.tv_item.setText(item.getName());
        holder.tv_quantity.setText("Quantity:"+" "+item.getQuantity());
        holder.tv_price.setText("AED"+" "+item.getPrice());
        holder.tv_size.setText("Size:"+" "+item.getOption().get(0).getValue());


        return vi;
    }
}
