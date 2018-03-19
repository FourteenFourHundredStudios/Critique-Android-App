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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class QueFragment extends Fragment implements View.OnClickListener {
        public QueFragment(){


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_que, container, false);



            /*
            Button button = (Button) getActivity().findViewById(R.id.voteGood);



            button.setOnClickListener(this);
*/





            ImageButton voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
            voteBad.setOnClickListener(this);

            ImageButton voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
            voteGood.setOnClickListener(this);


          //  ImageView profilePic = (ImageView) rootView.findViewById(R.id.profilePic);
           // profilePic.setClipToOutline(true);

            /*
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Util.showDialog(getActivity(),"GOOD!");
                }
            }
             */

            return rootView;
        }


        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.voteGood:
                    Util.showDialog(getActivity(),"GOOD!");
                    return;
                case R.id.voteBad:
                    Util.showDialog(getActivity(),"BAD!");
                    return;
            }
        }


        @Override
        public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);


            //inflater.inflate(R.menu.vote_menu, menu);



            //Toolbar toolbar2 = (Toolbar) getActivity().findViewById(R.id.bottom_toolbar);
            //toolbar2.getMenu().clear();
            //toolbar2.inflateMenu(R.menu.vote_menu);


        }

    public void myClickMethod(View v) {

    }



}
