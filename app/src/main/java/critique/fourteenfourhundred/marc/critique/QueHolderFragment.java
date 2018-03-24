package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class QueHolderFragment extends Fragment {

    View rootView;
    DeactivatedViewPager viewPager;


    public QueHolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_que_holder, container, false);


        viewPager = (DeactivatedViewPager) rootView.findViewById(R.id.pageHolder);





        // Create an adapter that knows which fragment should be shown on each page
        QueHolderAdapter adapter = new QueHolderAdapter(getContext(), getFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setPagingEnabled(false);

        return rootView;
    }

}

class QueHolderAdapter extends FragmentStatePagerAdapter {
    private Context mContext;



    public QueHolderAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        //  ProfileFragment tab1 = new ProfileFragment();

        switch (position) {
            case 0:
                return new QueFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return 1;
    }

    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Que";
            default:
                return null;
        }
    }

}