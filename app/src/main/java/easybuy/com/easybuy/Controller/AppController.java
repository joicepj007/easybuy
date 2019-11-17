package easybuy.com.easybuy.Controller;



import android.app.Application;
import android.content.Context;
import android.os.StrictMode;


public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();



    private static AppController mInstance;
    public static volatile Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        mInstance = this;

	/*	if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);*/
        // Normal app init code...
        AppSession.initializeInstance(this);
        //appEnvironment = AppEnvironment.SANDBOX;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());




    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }




}

