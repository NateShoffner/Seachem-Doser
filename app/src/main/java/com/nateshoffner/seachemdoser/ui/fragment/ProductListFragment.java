package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.ui.adapter.ProductAdapter;
import com.nateshoffner.seachemdoser.ui.listener.ProductSelectionListener;

import java.util.List;

public class ProductListFragment extends ListFragment {

    public static final String EXTRA_PRODUCT_TYPE = "PRODUCT_TYPE";

    private static final String TAG = "ProductListFragment";
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private ProductSelectionListener mListener;
    private List<SeachemProduct> mProducts;
    private boolean mActivateOnItemClick;
    private boolean mViewCreated;

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public ProductListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SeachemProductType productType = (SeachemProductType)getArguments().
                getSerializable(EXTRA_PRODUCT_TYPE);
        mProducts = SeachemManager.GetProducts(productType);
        setListAdapter(new ProductAdapter(getActivity(), mProducts));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewCreated = true;
        setActivateOnItemClick(mActivateOnItemClick);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mListener.onProductSelected(mProducts.get(position));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ProductSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ProductSelectionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        mActivateOnItemClick = activateOnItemClick;

        if (mViewCreated) {
            // When setting CHOICE_MODE_SINGLE, ListView will automatically
            // give items the 'activated' state when touched.
            getListView().setChoiceMode(activateOnItemClick
                    ? ListView.CHOICE_MODE_SINGLE
                    : ListView.CHOICE_MODE_NONE);
        }
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
