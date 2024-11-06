package cf20.soft.shoes.timmi.admin.ui.product;

import cf20.soft.shoes.timmi.model.Product;

public interface OnClickProductListener {

    default void onClickItem(Product product) {
    }

    void onEditItem(Product product);

    void onDeleteItem(Product product);
}
