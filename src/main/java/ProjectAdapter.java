package trainedge.htmleditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Abc on 26-12-2016.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectHolder> {

    Context context;
    ArrayList<ProjectModel> projectModels;
    private final ProjectDataList data;

    public ProjectAdapter(Context context) {
        data = new ProjectDataList(context);
        projectModels = data.getProjectData();
        this.context = context;
    }

    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(R.layout.card_layout_for_recyclerview, parent, false);
        ProjectHolder h = new ProjectHolder(card);
        return h;
    }

    @Override
    public void onBindViewHolder(ProjectHolder holder, int position) {

        final ProjectModel model = projectModels.get(position);
        holder.tvProjectName.setText(model.projectName);
        holder.tvFileNumber.setText(model.numOfFiles);

        holder.cvProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(context,MainActivity.class);
                mainIntent.putExtra("trainedge.htmleditor.EXTRA_PATH",model.file.getAbsolutePath());
                context.startActivity(mainIntent);
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog deleteDialog=new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete project..?");
                deleteDialog.setCancelable(false);
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FileHandler.deleteFileRecusively(model.file);
                        notifyDataSetChanged();
                        refreshList();
                    }
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDialog.cancel();
                    }
                });
                deleteDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return projectModels.size();
    }

    public void refreshList() {
        projectModels = data.getProjectData();
    }

}
