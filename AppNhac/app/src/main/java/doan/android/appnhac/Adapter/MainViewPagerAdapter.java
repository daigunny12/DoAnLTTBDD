package doan.android.appnhac.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> arrayFragment = new ArrayList<>();
    private ArrayList<String> arraytile = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayFragment.size();
    }

    public void addFragment(Fragment fragment, String tile){
        arrayFragment.add(fragment);
        arraytile.add(tile);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arraytile.get(position);
    }
}
