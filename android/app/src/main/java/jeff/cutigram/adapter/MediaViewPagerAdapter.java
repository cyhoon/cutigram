package jeff.cutigram.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import jeff.cutigram.ui.BitmapImagePage;

public class MediaViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mediaFragment = new ArrayList<>();
    private List<Bitmap> bitmapList;

    public MediaViewPagerAdapter(FragmentManager fm, List<Bitmap> bitmapList) {
        super(fm);
        this.bitmapList = bitmapList;

        for (Bitmap bitmap : bitmapList) {
            mediaFragment.add(new BitmapImagePage(bitmap));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mediaFragment.get(position);
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }
}
