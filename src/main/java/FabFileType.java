package trainedge.htmleditor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gordonwong.materialsheetfab.AnimatedFab;
import  android.support.design.widget.FloatingActionButton;

/**
 * Created by Abc on 07-01-2017.
 */

public class FabFileType extends FloatingActionButton implements AnimatedFab {
    public FabFileType(Context context) {
        super(context);
    }

    public FabFileType(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FabFileType(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void show() {
        show(0,0);
    }

    @Override
    public void show(float translationX, float translationY) {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(View.INVISIBLE);
    }

}
