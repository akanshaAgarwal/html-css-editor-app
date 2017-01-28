package trainedge.htmleditor;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewFragment extends Fragment {


    private SwipeRefreshLayout swipeRefresh;
    private WebView webView;
    private File file;
    private GetFileFromEditor secListener;
    private String fileContent;
    private boolean flag=false;
    private String parentPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        webView = (WebView) view.findViewById(R.id.webView);

        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView.HitTestResult result=webView.getHitTestResult();
                if (result.getType() == WebView.HitTestResult.ANCHOR_TYPE
                        || result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE){
                    webView.loadDataWithBaseURL(parentPath+"/"+result.getExtra(),fileContent,"text/html","utf-8",null);
                }
            }
        });



        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);

                return true;

            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

                //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                if (url.contains("#") && flag == false) {
                    webView.loadUrl(url);
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });

        onSwipeRefresh();

        return view;
    }

    private void onSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (file.exists()) {
                        fileContent = FileHandler.readFile(secListener.getFile().getAbsolutePath());
                        webView.loadData(fileContent,"text/html; charset=UTF-8",null);

                        swipeRefresh.setRefreshing(false);
                    }

                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Sorry...No Preview.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(secListener!=null){
            try {
                file=secListener.getFile();
                if (file.exists()) {
                    fileContent = FileHandler.readFile(secListener.getFile().getAbsolutePath());
                    webView.loadData(fileContent,"text/html; charset=UTF-8",null);
                    parentPath = file.getParent();
                }

            } catch (Exception e) {
                //Toast.makeText(getActivity(), "Sorry...No Preview.", Toast.LENGTH_SHORT).show();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GetFileFromEditor) {
            secListener=(GetFileFromEditor)context;
        }
    }

    interface GetFileFromEditor{
        File getFile();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        secListener=null;
    }
}
