package com.nateshoffner.seachemdoser.ui.listener;

import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;

public interface ProductSelectionListener {
    void onProductSelected(SeachemProduct product);
    void onProductTypeSelected(SeachemProductType type);
}
