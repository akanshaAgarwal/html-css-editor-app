package trainedge.htmleditor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Abc on 01-01-2017.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private final String path;

    public MainFragmentAdapter(FragmentManager fm, String path) {
        super(fm);
        this.path = path;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Tree";
            case 1:
                return "Editor";
            case 2:
                return "View";
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return StructureFragment.newInstance(path);
            case 1:
                return new EditorFragment();
            case 2:
                return new PreviewFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
