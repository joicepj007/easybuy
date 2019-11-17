package easybuy.com.easybuy.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import easybuy.com.easybuy.R;
import easybuy.com.easybuy.util.ConnectionDetector;

public class RootActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private int screenHeightInPixels = 0, screenWidhtInPixels = 0;

    private static RootActivity instance;
    private ConnectionDetector objDetector;
    private ProgressDialog progress;
    private ProgressDialog mProgressDialog;
    // SweetAlertDialog mDialog;

    public RootActivity() {

        instance = this;
        // TODO Auto-generated constructor stub
    }

    public static Context getContext() {
        return instance;
    }

    //ProgressHUD mProgressHUD;


    public void showToastMessage(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeightInPixels = displaymetrics.heightPixels;
        screenWidhtInPixels = displaymetrics.widthPixels;
        //  initializeProgressDialog();

    }

 /*   private void initializeProgressDialog() {
        try {
            if (android.os.Build.VERSION.SDK_INT > 10) {
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
            } else {
                mProgressDialog = new ProgressDialog(this);
            }
            mProgressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* public void showProgressDialog(String message) {
        try {
            mProgressDialog.setMessage(message);
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public void showProgressDialog(String message) {
        try {
            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            mDialog.setTitleText("Loading");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

   /* public void hideProgressDialog() {
        try {
            //noinspection ConstantConditions,ConstantConditions
            if (mProgressDialog.isShowing() && mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public void hideProgressDialog() {

        if (mDialog != null)
            if (mDialog.isShowing())
                mDialog.cancel();

    }*/

    public int getScreenHeightInPixels() {
        return screenHeightInPixels;
    }

    public int getScreenWidhtInPixels() {
        return screenWidhtInPixels;
    }


    public void showBusyAnimation() {

        progress = new ProgressDialog(RootActivity.this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        // progress.setProgress(0);
        progress.show();


    }

    public void hideBusyAnimation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the thing that takes a long time

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
            }
        }).start();


    }

    public boolean CheckInternetStatus(){

        if (!objDetector.isConnectingToInternet()) {
            showSnackBar("No Internet Connection");
            return false;
        }
        else
        {
            return true;
        }


    }

    public void showSnackBar(String message) {
        try {
            final Snackbar snack = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            TextView snackbarActionTextView = (TextView) snack.getView().findViewById( android.support.design.R.id.snackbar_action );
            snackbarActionTextView.setTextSize( 18 );
            View view = snack.getView();
            snack.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snack.dismiss();
                }
            });
            snack.setActionTextColor(getResources().getColor(R.color.colorWhite));
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(getResources().getColor(R.color.colorWhite));
            //tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.result_font));
            snack.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }


    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSoftKeyboard(View focusView) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(focusView,
                    inputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }
}



