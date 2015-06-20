package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaintListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaintListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaintListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView mPaintList;
    private final List<String> mPaintNames;

    private Spinner mBrandSpinner;
    private final List<String> mBrandNames;
    private Spinner mRangeSpinner;
    private final List<String> mRangeNames;
    private Spinner mStatusSpinner;
    private final List<String> mStatusNames;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PaintListFragment.
     */
    public static PaintListFragment newInstance(int sectionNumber) {
        PaintListFragment fragment = new PaintListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PaintListFragment() {
        mPaintNames = new ArrayList<>();
        mBrandNames = new ArrayList<>();
        mRangeNames = new ArrayList<>();
        mStatusNames = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paint_list, container, false);

        mPaintList = (ListView) rootView.findViewById(R.id.paint_list);
        mPaintList.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mPaintNames
        ));

        mBrandSpinner = (Spinner) rootView.findViewById(R.id.brands_spinner);
        mBrandSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1,
                mBrandNames
        ));

        mRangeSpinner = (Spinner) rootView.findViewById(R.id.ranges_spinner);
        mRangeSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1,
                mRangeNames
        ));

        mStatusSpinner = (Spinner) rootView.findViewById(R.id.statuses_spinner);
        mStatusSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1,
                mStatusNames
        ));

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((PaintListActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

        mPaintNames.clear();
        List<Paint> paints = Select.from(Paint.class).orderBy("name").list();
        for (Paint p : paints) {
            mPaintNames.add(p.name);
        }

        mBrandNames.clear();
        List<Brand> brands = Select.from(Brand.class).orderBy("name").list();
        mBrandNames.add(getString(R.string.brand));
        for (Brand b : brands) {
            mBrandNames.add(b.name);
        }

        mRangeNames.clear();
        List<Range> ranges = Select.from(Range.class).orderBy("name").list();
        mRangeNames.add(getString(R.string.range));
        for (Range r : ranges) {
            mRangeNames.add(r.name);
        }

        mStatusNames.clear();
        List<PaintStatus> statuses = Select.from(PaintStatus.class).orderBy("name").list();
        mStatusNames.add(getString(R.string.status));
        for (PaintStatus s : statuses) {
            mStatusNames.add(s.name);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
