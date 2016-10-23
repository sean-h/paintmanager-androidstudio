package com.github.sean_h.paintmanager;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class PaintListFragment extends Fragment implements OnTaskCompleted {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView mPaintListView;
    private List<Paint> mPaints;
    private PaintListAdapter mPaintListAdapter;

    private final List<String> mBrandNames;
    private final List<String> mRangeNames;
    private final List<String> mStatusNames;
    private OnFragmentInteractionListener mListener;

    private Select<Paint> mPaintQuery;

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
        mBrandNames = new ArrayList<>();
        mRangeNames = new ArrayList<>();
        mStatusNames = new ArrayList<>();
        mPaints = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paint_list, container, false);

        mPaintListView = (ListView) rootView.findViewById(R.id.paint_list);
        mPaintListView.setAdapter(mPaintListAdapter);
        mPaintListView.setOnScrollListener(new EndlessScrollListener(10) {
            @Override
            public void onLoadMore(int itemOffset) {
                loadPaints(itemOffset, 25);
            }
        });

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

        mPaintListAdapter = new PaintListAdapter(getActivity(), mPaints);
        loadPaintList();

        loadSpinners();
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

    private void loadPaintList() {
        mPaints.clear();
        loadPaints(0, 25);
    }

    private void loadPaints(int offset, int count) {
        /*Select<Paint> query = Select.from(Paint.class);
        if (mRangeFilter != null) {
            query = query.where(Condition.prop("range").eq(mRangeFilter.getGuid()));
        }
        if (mStatusFilter != null) {
            query = query.where(Condition.prop("status").eq(mStatusFilter));
        }*/

        if (mPaintQuery == null) {
            mPaintQuery = Select.from(Paint.class);
        }

        String limit = Integer.toString(offset) + "," + Integer.toString(count);

        //List<Paint> paints = query.orderBy("name").limit(limit).list();
        List<Paint> paints = mPaintQuery.orderBy("name").limit(limit).list();
        if (paints.size() > 0) {
            mPaints.addAll(paints);
            mPaintListAdapter.notifyDataSetChanged();
        }
    }

    public void setPaintQuery(Select<Paint> query) {
        mPaintQuery = query;
        mPaints.clear();
        mPaintListAdapter.notifyDataSetChanged();
        loadPaints(0, 25);
    }

    private void loadSpinners() {
        mBrandNames.clear();
        List<Brand> brands = Select.from(Brand.class).orderBy("name").list();
        mBrandNames.add(getString(R.string.brand_spinner_default));
        for (Brand b : brands) {
            mBrandNames.add(b.name);
        }

        mRangeNames.clear();
        List<Range> ranges = Select.from(Range.class).orderBy("name").list();
        mRangeNames.add(getString(R.string.range_spinner_default));
        for (Range r : ranges) {
            mRangeNames.add(r.name);
        }

        mStatusNames.clear();
        mStatusNames.add(getString(R.string.status_spinner_default));
        mStatusNames.add(getString(R.string.dont_have));
        mStatusNames.add(getString(R.string.have));
        mStatusNames.add(getString(R.string.need));
    }

    @Override
    public void onTaskCompleted() {
        loadSpinners();
        loadPaintList();
    }
}
