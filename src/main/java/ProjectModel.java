package trainedge.htmleditor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Abc on 26-12-2016.
 */

public class ProjectModel {
    String projectName,numOfFiles;
    File file;

    public ProjectModel(String projectName, String numOfFiles, String date) {
        this.projectName = projectName;
        this.numOfFiles = numOfFiles;
    }

    public ProjectModel(File f) {
        file =f;
        projectName=   f.getName();
        numOfFiles=f.listFiles().length + " files";
    }

}
