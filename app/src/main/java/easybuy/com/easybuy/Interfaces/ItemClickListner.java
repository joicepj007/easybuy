package easybuy.com.easybuy.Interfaces;

import android.view.View;

public interface ItemClickListner {
    void onItemSelected(String item,int position, Object response);
    public void onClick(View view, int position);
    public void onLongClick(View view,int position,Object response);
}
