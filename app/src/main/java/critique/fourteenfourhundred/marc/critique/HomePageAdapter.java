package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Marc on 9/3/18.
 */

public class HomePageAdapter extends FragmentStatePagerAdapter {
    private Context mContext;



    public HomePageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
      //  ProfileFragment tab1 = new ProfileFragment();

        switch (position) {
            case 0:
                return new QueHolderFragment();
            case 1:
                return new FriendsFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return 3;
    }

    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Que";
            case 1:
                return "Friends";
            case 2:
                return "Profile";
            default:
                return null;
        }
    }

}