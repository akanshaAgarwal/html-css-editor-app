package trainedge.htmleditor;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static trainedge.htmleditor.R.attr.colorAccent;


/**
 * A simple {@link Fragment} subclass.
 */
public class StructureFragment extends Fragment {


    public static final String PATH = "path";
    private String path;
    private File pathFile;
    ExpandableListView elvProjectStructure;
    private HashMap<String, ArrayList<String>> projectName;
    private ExpandableListAdapter adapter;
    private FloatingActionButton fabAddFile;
    private MaterialSheetFab<FabFileType> materialSheetFab;
    private TextView tvHtml;
    private TextView tvCss;
    private OnFragmentListListener mListener;
    private int groupPosition;
    private int childPosition;

    public StructureFragment() {

        // Required empty public constructor
    }

    public static StructureFragment newInstance(String path) {
        StructureFragment sf = new StructureFragment();
        Bundle b = new Bundle();
        b.putString(PATH, path);
        sf.setArguments(b);
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getString(PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_structure, container, false);
        elvProjectStructure = (ExpandableListView) view.findViewById(R.id.elvProjectStructure);
        if (path != null) {
            adapter = new ExpandableListAdapter(getActivity(), path);
        }
        elvProjectStructure.setAdapter(adapter);
        elvProjectStructure.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String fileName = (String) adapter.getChild(i, i1);
                String projectName = (String) adapter.getGroup(i);
                mListener.getFileName(fileName, projectName);
                return true;
            }
        });
        elvProjectStructure.expandGroup(0);
        FabFileType fab = (FabFileType) view.findViewById(R.id.fabFileType);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.material_fab);
        int fabColor = getResources().getColor(R.color.indicator_1);
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        tvHtml = (TextView) view.findViewById(R.id.tvHtml);
        tvHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialSheetFab.hideSheet();
                AlertDialogWithEditText.createFileAlertDialog(getActivity(), "Create new HTML File", path, ".html");
                elvProjectStructure.setAdapter(adapter);

            }
        });
        tvCss = (TextView) view.findViewById(R.id.tvCss);
        tvCss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialSheetFab.hideSheet();
                AlertDialogWithEditText.createFileAlertDialog(getActivity(), "Create new CSS File", path, ".css");
                elvProjectStructure.setAdapter(adapter);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListListener) {
            mListener = (OnFragmentListListener) context;
        } else {
            throw new RuntimeException("Sorry... Some error occured");
        }
    }

    public interface OnFragmentListListener {
        void getFileName(String fileName, String projectName);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);

        this.getActivity().getMenuInflater().inflate(R.menu.context_menu_for_expandable_list_files,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final String fileName = (String) adapter.getChild(groupPosition, childPosition);
        final String projectName = (String) adapter.getGroup(groupPosition);
        switch (item.getItemId()) {
            case R.id.miOpen:
                mListener.getFileName(fileName, projectName);
                break;
            case R.id.miClear:
                final AlertDialog dialog=new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle("Clear file..?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File f=new File(Environment.getExternalStorageDirectory(),"/HtmlCssEditor/"+projectName+"/"+fileName);
                        if(f.exists()){
                            FileHandler.writeIntoFile(f,"");
                        }
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.miDelete:
                final AlertDialog dialogD=new AlertDialog.Builder(getActivity()).create();
                dialogD.setTitle("Delete file..?");
                dialogD.setButton(DialogInterface.BUTTON_POSITIVE, "delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File f=new File(Environment.getExternalStorageDirectory(),"/HtmlCssEditor/"+projectName+"/"+fileName);
                        if(f.exists()){
                            f.delete();
                            adapter.notifyDataSetChanged();
                            elvProjectStructure.setAdapter(adapter);

                        }
                    }
                });
                dialogD.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogD.cancel();
                    }
                });
                dialogD.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerForContextMenu(elvProjectStructure);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterForContextMenu(elvProjectStructure);
    }
}
