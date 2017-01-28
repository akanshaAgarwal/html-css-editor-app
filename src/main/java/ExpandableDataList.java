package trainedge.htmleditor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abc on 05-01-2017.
 */

public class ExpandableDataList {
    public static HashMap<String, ArrayList<String>> projectName;
    public static ArrayList<String> stringArrayList;
    static String path;

    public ExpandableDataList(String path) {
        this.path = path;
        stringArrayList = new ArrayList<>();
        projectName = new HashMap<>();
    }

    public static HashMap<String, ArrayList<String>> getProjectData() {
        File file = new File(path);
        stringArrayList.clear();
        for (File f : file.listFiles()) {
            if (f.exists()) {
                stringArrayList.add(f.getName());
            }
        }
        projectName.put(file.getName(), stringArrayList);
        return projectName;
    }

}
