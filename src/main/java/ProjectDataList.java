package trainedge.htmleditor;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Abc on 26-12-2016.
 */

public class ProjectDataList {

    private final Context context;
    public ArrayList<ProjectModel> projectModels;

    public ProjectDataList(Context context) {
        this.context = context;

    }

    public ArrayList<ProjectModel> getProjectData() {

        File projectObject = new File( Environment.getExternalStorageDirectory().getPath().toString(),"/HtmlCssEditor");
        File[] allProjects = projectObject.listFiles();
        projectModels = new ArrayList<>();
        if(allProjects!=null) {
            for (File f : allProjects) {
                if (f.isDirectory()) {
                    projectModels.add(new ProjectModel(f));
                }
            }
        }
        return projectModels;
    }

}
