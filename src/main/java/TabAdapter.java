package trainedge.htmleditor;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Abc on 06-01-2017.
 */

public class TabAdapter extends RecyclerView.Adapter<TabHolder> {

    private final Context context;
    private final AutoCompleteTextView autoCompleteTextView;
    private final ArrayList<String> tabs;
    private final ArrayList<String> tabsData;

    public TabAdapter(Context context, AutoCompleteTextView autoCompleteTextView) {
        this.context=context;
        this.autoCompleteTextView = autoCompleteTextView;
        tabs = TabDataList.getTabs();
        tabsData = TabDataList.getTabsData();
    }

    @Override
    public TabHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card= LayoutInflater.from(context).inflate(R.layout.tab_for_bottom_sheet,parent,false);
        TabHolder th=new TabHolder(card);
        return th;
    }

    @Override
    public void onBindViewHolder(TabHolder holder, final int position) {
        holder.btnTabs.setText(tabs.get(position));
        holder.btnTabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int start=Math.max(autoCompleteTextView.getSelectionStart(),0);
                int end=Math.max(autoCompleteTextView.getSelectionEnd(),0);
                autoCompleteTextView.getText().replace(Math.min(start,end),Math.max(start,end),tabsData.get(position),0,tabsData.get(position).length());
                /* try {
                    autoCompleteTextView.getText().insert(autoCompleteTextView.getSelectionStart(), tabs.get(position));
                }catch (Exception e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return tabs.size();
    }
}
