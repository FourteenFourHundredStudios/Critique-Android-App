package critique.fourteenfourhundred.marc.critique;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



public class QueFragment extends Fragment {
    public QueFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_que, container, false);

        setHasOptionsMenu(true);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        //inflater.inflate(R.menu.vote_menu, menu);



        Toolbar toolbar2 = (Toolbar) getActivity().findViewById(R.id.bottom_toolbar);
        toolbar2.getMenu().clear();
        toolbar2.inflateMenu(R.menu.vote_menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_upvote:
                Util.showDialog(getActivity(),"Up vote!");
                // Do Activity menu item stuff here
                return true;
            case R.id.action_downvote:
                Util.showDialog(getActivity(),"down vote!");
                // Do Activity menu item stuff here
                return true;
            default:
                break;
        }

        return false;
    }

}
