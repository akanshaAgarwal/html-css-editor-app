package trainedge.htmleditor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Abc on 07-01-2017.
 */

public class AlertDialogWithEditText {
    public static void createFileAlertDialog(final Context context, String title, final String path, final String extention) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_project_create, null);
        dialog.setView(v);
        final EditText etProjectName = (EditText) v.findViewById(R.id.etProjectName);
        dialog.setTitle(title);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = etProjectName.getText().toString().trim();
                if (!name.isEmpty()) {
                    File f = new File(path, "/" + name + extention);
                    if (!f.exists()) {
                        try {
                            f.createNewFile();
                            if (extention.equals(".html"))
                                FileHandler.getTemplateForHtml(f);
                            else if (extention.equals(".css"))
                                FileHandler.getTemplateForCss(f);
                            ExpandableListAdapter elvAdapter=new ExpandableListAdapter();
                            elvAdapter.notifyDataSetChanged();

                        } catch (IOException e) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Already Exists..!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etProjectName.setError("Enter text");
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
}
