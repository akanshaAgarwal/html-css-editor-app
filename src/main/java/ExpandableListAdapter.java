package trainedge.htmleditor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abc on 05-01-2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    ArrayList<String> projectData;
    HashMap<String, ArrayList<String>> projectName;
    Context context;
    String path;
    private ExpandableDataList dataList;

    public ExpandableListAdapter(Context context, String path) {
        this.context = context;
        this.path=path;
        dataList = new ExpandableDataList(path);
        projectName = ExpandableDataList.getProjectData();
        projectData = new ArrayList<>(projectName.keySet());
    }

    public ExpandableListAdapter() {
    }

    @Override
    public int getGroupCount() {
        return projectData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return projectName.get(this.projectData.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.projectData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return projectName.get(this.projectData.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String project = (String) getGroup(i);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.ex_list_view_project_name,null,false);
        }
        TextView tvProjectName = (TextView) view.findViewById(R.id.tvProjectName);
        tvProjectName.setText(project);
        ImageView ivProjectIcon= (ImageView) view.findViewById(R.id.ivProjectIcon);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String fileName = (String) getChild(i, i1);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ex_list_view_file_name, null,false);
        }
        TextView tvFileName = (TextView) view.findViewById(R.id.tvFileName);
        tvFileName.setText(fileName);
        ImageView ivFileIcon= (ImageView) view.findViewById(R.id.ivFileIcon);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.projectName=ExpandableDataList.getProjectData();
        this.projectData = new ArrayList<>(projectName.keySet());

    }

}
