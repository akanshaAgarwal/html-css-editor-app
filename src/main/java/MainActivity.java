package trainedge.htmleditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class MainActivity extends AppCompatActivity implements StructureFragment.OnFragmentListListener,PreviewFragment.GetFileFromEditor, EditorFragment.UpdateEditorFragmentData,EditorFragment.SendFileToPreviewFragment{

    private ScrollerViewPager scrollerViewPager;
    private MainFragmentAdapter mainFragmentAdapter;
    private SpringIndicator springIndicator;
    private String filePath;
    private String listFileName;
    private String listProjectName;
    private String fileName;
    private String projectName;
    private File file;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        if (getIntent() != null && getIntent().hasExtra("trainedge.htmleditor.EXTRA_PATH")) {
            filePath = getIntent().getStringExtra("trainedge.htmleditor.EXTRA_PATH");
        }

        scrollerViewPager = (ScrollerViewPager) findViewById(R.id.scrollerViewPager);
        mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), filePath);
        scrollerViewPager.setAdapter(mainFragmentAdapter);
        springIndicator = (SpringIndicator) findViewById(R.id.springIndicator);
        springIndicator.setViewPager(scrollerViewPager);
        //scrollerViewPager.setOffscreenPageLimit(2);


    }

    @Override
    public void getFileName(String fileName,String projectName) {
        this.fileName = fileName;
        this.projectName = projectName;
        scrollerViewPager.setCurrentItem(1,true);
    }

    @Override
    public void onBackPressed() {

        ++count;
        if(count==1){
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        if(count==2){
            super.onBackPressed();
        }

    }

    @Override
    public String updateData() {
        return projectName+"/"+fileName;
    }

    @Override
    public void fileForPreview(File file) {
        this.file = file;
        scrollerViewPager.setCurrentItem(2,true);
    }

    @Override
    public File getFile() {
        return file;
    }
}
