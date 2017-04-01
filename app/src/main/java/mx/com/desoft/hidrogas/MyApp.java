package mx.com.desoft.hidrogas;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by David on 30/03/17.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
