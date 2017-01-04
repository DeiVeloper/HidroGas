package mx.com.desoft.hidrogas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mx.com.desoft.hidrogas.R;

/**
 * Created by David on 03/12/16.
 */

public class TopRatedFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_liquidacion, container, false);

        return rootView;
    }
}
