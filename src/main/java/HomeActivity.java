package trainedge.htmleditor;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class HomeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 123;
    private RecyclerView rvProjectList;
    private FloatingActionButton fab;
    private String folder_main;
    private ProjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent login=new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(login);
            finish();
        }

        initializeApp();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProjectCreationDialog();

            }
        });
        rvProjectList = (RecyclerView) findViewById(R.id.rvProjectList);
        rvProjectList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjectAdapter(this);
        rvProjectList.setAdapter(adapter);

    }

    private void initializeApp() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
        folder_main = "HtmlCssEditor";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            boolean status = f.mkdirs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(REQUEST_CODE==requestCode){
            int flag=0;
            for (int result : grantResults) {
                if(result==PackageManager.PERMISSION_GRANTED){
                    flag=1;
                }else{
                    flag=0;
                    break;
                }
            }
            if(flag==1){
                initializeApp();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_invite:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Html Css Editor");
                    String sAux = "\nTry this great app..for all those who love to code ..!!\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=trainedge.htmleditor\n";
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Html Css Editor"));
                } catch(Exception e) {
                    Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
                    //e.toString();
                }
                return true;
            case R.id.action_rating:
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=trainedge.htmleditor")));
                return true;
            case R.id.action_feedback:
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "akansha08agarwal@gmail.com" });
                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
                return true;
            case R.id.action_signout:
                FirebaseAuth.getInstance().signOut();
                Intent login=new Intent(this,LoginActivity.class);
                startActivity(login);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showProjectCreationDialog() {
        final AlertDialog dialog= new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_project_create, null);
        dialog.setView(view);
        final EditText etProjectName= (EditText) view.findViewById(R.id.etProjectName);
        dialog.setTitle("Create new Project");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = etProjectName.getText().toString().trim();
                if (!name.isEmpty()){
                    createProject(name);
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
    }

    private void createProject(String projectName) {

        File FPath = new File( Environment.getExternalStorageDirectory(),"/"+folder_main+"/"+projectName);
        String absolutePath = FPath.getAbsolutePath();
        if (!FPath.exists()) {
            if (!FPath.mkdir()) {
                Snackbar.make(fab, "Error! Please try again.", Snackbar.LENGTH_INDEFINITE);
            }else{
                File fIndex=new File(absolutePath+"/index.html");
                if(!fIndex.exists()) {
                    try {
                        fIndex.createNewFile();
                       FileHandler.getTemplateForHtml(fIndex);
                    } catch (IOException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                adapter.refreshList();
                adapter.notifyDataSetChanged();
            }
        } else {
            Snackbar.make(fab, "Project by this name already exists.", Snackbar.LENGTH_INDEFINITE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.refreshList();
        adapter.notifyDataSetChanged();
    }
}
