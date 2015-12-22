package com.nateshoffner.seachemdoser.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<SeachemProduct> {

    public ProductAdapter(Context context, List<SeachemProduct> products) {
        super(context, R.layout.product_row_layout, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.product_row_layout, parent, false);

        SeachemProduct product = getItem(position);

        TextView tvText = (TextView) theView.findViewById(R.id.textView1);
        tvText.setText(product.getName());

        return theView;

    }
}
