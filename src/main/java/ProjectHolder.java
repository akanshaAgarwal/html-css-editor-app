package trainedge.htmleditor;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Abc on 26-12-2016.
 */

public class ProjectHolder extends RecyclerView.ViewHolder {

    CardView cvProject;
    TextView tvProjectName,tvFileNumber;
    ImageView ivDelete,ivProject;

    public ProjectHolder(View v) {
        super(v);
        tvProjectName= (TextView) v.findViewById(R.id.tvProjectName);
        tvFileNumber= (TextView) v.findViewById(R.id.tvFilesNumber);
        ivProject= (ImageView) v.findViewById(R.id.ivProject);
        ivDelete= (ImageView) v.findViewById(R.id.ivDelete);
        cvProject =(CardView)v.findViewById(R.id.cvCard);
    }
}

