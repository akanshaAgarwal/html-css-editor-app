package trainedge.htmleditor;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.SourceFormatter;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditorFragment extends Fragment {

    private AutoCompleteTextView autoCompleteTextView;
    private String fileContent = "";
    private View rootView;
    private UpdateEditorFragmentData mListener;
    private File f;
    private SendFileToPreviewFragment secListener;
    private RecyclerView rvTabs;
    private ImageButton btnPreview;
    private ImageButton btnSave;
    private ImageButton btnUndo;
    private ImageButton btnRedo;
    private ImageButton btnKeyboard;
    private TextViewUndoRedo undoRedo;
    private View bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private ArrayAdapter<String> completeTextAdapter;
    boolean isCodeTidy = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        initViews();
        setupUi();
        return rootView;
    }

    public void initViews() {
        bottomSheetLayout = (View) rootView.findViewById(R.id.rlBottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        rvTabs = (RecyclerView) rootView.findViewById(R.id.rvTabs);
        btnPreview = (ImageButton) rootView.findViewById(R.id.btnPreview);
        btnSave = (ImageButton) rootView.findViewById(R.id.btnSave);
        btnUndo = (ImageButton) rootView.findViewById(R.id.btnUndo);
        btnRedo = (ImageButton) rootView.findViewById(R.id.btnRedo);
        btnKeyboard = (ImageButton) rootView.findViewById(R.id.btnKeyboard);
        undoRedo = new TextViewUndoRedo(autoCompleteTextView);
    }

    public void setupUi() {
        //imp..remember it..
        completeTextAdapter = new ArrayAdapter<String>(autoCompleteTextView.getContext(), android.R.layout.select_dialog_item, R.id.autoCompleteItem, TabDataList.getTabs());
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setDropDownAnchor(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(completeTextAdapter);
        autoCompleteTextView.setTextIsSelectable(true);

        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });
        bottomSheetBehavior.setPeekHeight(65);
        bottomSheetBehavior.isHideable();
        rvTabs.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        rvTabs.setAdapter(new TabAdapter(getActivity(), autoCompleteTextView));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCodeToFile();
                Toast.makeText(getActivity(), "File saved..", Toast.LENGTH_SHORT).show();
            }
        });
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCodeToFile();
                secListener.fileForPreview(f);

            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoRedo.undo();
            }
        });
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoRedo.redo();
            }
        });
        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayKeyboard();
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // nothing here
                //isCodeTidy=false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //isCodeTidy = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //FileHandler.writeIntoFile(f, userCode);
                //if (f != null) {
                try {
                    saveCodeToFile();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //}
                //
            }
        });

    }

    public String cleanCode(String userCode) {
        Source source = new Source(userCode);
        return new SourceFormatter(source).setIndentString("\t").setTidyTags(true).setCollapseWhiteSpace(true).toString();
    }

    public void displayKeyboard() {
        InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void saveCodeToFile() {
        String s = cleanCode(autoCompleteTextView.getText().toString());
        FileHandler.writeIntoFile(f, s);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateEditorFragmentData) {
            mListener = (UpdateEditorFragmentData) context;
        } else {
            throw new RuntimeException("Sorry... Some error occured");
        }
        if (context instanceof SendFileToPreviewFragment) {
            secListener = (SendFileToPreviewFragment) context;
        } else {
            throw new RuntimeException("Sorry... Some error occured");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            if (mListener != null) {
                f = new File(Environment.getExternalStorageDirectory().getPath(), "/HtmlCssEditor/" + mListener.updateData());
                try {
                    if (f.exists()) {
                        fileContent = FileHandler.readFile(f.getAbsolutePath());
                        autoCompleteTextView.setText(fileContent);

                        /*
                        completeText = new ArrayAdapter<String>(autoCompleteTextView.getContext(), android.R.layout.select_dialog_item, R.id.autoCompleteItem, TabDataList.getTabs());
                        autoCompleteTextView.setThreshold(1);
                        autoCompleteTextView.setDropDownAnchor(R.id.autoCompleteTextView);
                        autoCompleteTextView.setAdapter(completeText);*/
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public interface SendFileToPreviewFragment {
        void fileForPreview(File file);
    }

    public interface UpdateEditorFragmentData {
        String updateData();
    }


}
