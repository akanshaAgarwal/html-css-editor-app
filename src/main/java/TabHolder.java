package trainedge.htmleditor;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Abc on 06-01-2017.
 */

public class TabHolder extends RecyclerView.ViewHolder {
    Button btnTabs;
    public TabHolder(View v) {
        super(v);
        btnTabs= (Button) v.findViewById(R.id.btnTabs);
    }
}
