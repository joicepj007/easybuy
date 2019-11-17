package easybuy.com.easybuy.Controller;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSession {
    private static final String PREF_NAME = "easybuy";
    private static AppSession sInstance;
    private final SharedPreferences mPref;
    public static String USER_ID="user_id";
    private String StoreId = "storeId";
    private String LOGGED_IN_STATUS = "logged_in_status";
    private String Customer_address = "country_name";
    private String Grocery_name = "grocery_name";
    private String Order_Id = "order_id";
    private String Order_List = "OrderList";
    private String Order_History = "OrderHistory";
    private String Order_Scheduled = "OrderScheduled";


    public AppSession(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppSession(context);
        }
    }

    public static synchronized AppSession getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(
                    AppSession.class.getSimpleName()
                            + " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setUserId(String userId) {
        mPref.edit().putString(USER_ID, userId).commit();

    }
    public String getUserId() {
        return mPref.getString(USER_ID, "");
    }

    public String getStoreId() {
        return mPref.getString(this.StoreId, "");
    }

    public void setStoreId(String store_id) {
        mPref.edit().putString(this.StoreId, store_id).commit();
    }

    public boolean getLoggedInStatus() {
        return mPref.getBoolean(LOGGED_IN_STATUS, false);

    }

    public void setLoggedInStatus(boolean status) {
        mPref.edit().putBoolean(LOGGED_IN_STATUS, status).commit();
    }

    public void setCustomer_address(String customer_address) {
        mPref.edit().putString(this.Customer_address, customer_address).commit();
    }

    public String getCustomer_address() {
        return mPref.getString(this.Customer_address, "");
    }

    public void setGrocery_name(String grocery_name) {
        mPref.edit().putString(this.Grocery_name, grocery_name).commit();
    }

    public String getGrocery_name() {
        return mPref.getString(this.Grocery_name, "");
    }

    public void setOrder_Id(String order_id) {
        mPref.edit().putString(this.Order_Id, order_id).commit();
    }

    public String getOrder_Id() {
        return mPref.getString(this.Order_Id, "");
    }

    public void setOrderListFlag(boolean firstTimeUser) {
        mPref.edit().putBoolean(Order_List, firstTimeUser).commit();
    }


    public boolean getOrderListFlag() {
        return mPref.getBoolean(Order_List, false);
    }

    public void setOrderHistoryFlag(boolean firstTimeUser) {
        mPref.edit().putBoolean(Order_History, firstTimeUser).commit();
    }


    public boolean getOrderHistoryFlag() {
        return mPref.getBoolean(Order_History, false);
    }

    public void setOrderScheduledFlag(boolean firstTimeUser) {
        mPref.edit().putBoolean(Order_Scheduled, firstTimeUser).commit();
    }


    public boolean getOrderScheduledFlag() {
        return mPref.getBoolean(Order_Scheduled, false);
    }
}
