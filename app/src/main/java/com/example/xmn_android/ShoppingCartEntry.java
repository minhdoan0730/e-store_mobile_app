package com.example.xmn_android;

import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.paperdb.Paper;

public class ShoppingCartEntry {
    private Product mProduct;
    private int mQuantity;

    public ShoppingCartEntry(Product product, int quantity) {
        mProduct = product;
        mQuantity = quantity;
    }

    public Product getProduct() {
        return mProduct;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public boolean addToShoppingCart() {
        ArrayList<ShoppingCartEntry> shoppingList = new ArrayList<ShoppingCartEntry>();
        ArrayList<ShoppingCartEntry> storedList = Paper.book().read("shopping_line", new ArrayList<ShoppingCartEntry>());

        if (storedList.isEmpty() == false) {
            for (ShoppingCartEntry item: storedList) {
                if (item.getProduct().getID() == this.getProduct().getID()) {
                    item.setQuantity(item.getQuantity() + 1);
                    Paper.book().write("shopping_line", storedList);
                    ShoppingCartHelper.saveBadgeCount();
                    return true;
                }
            }
        }
        ShoppingCartEntry shoppingItem = new ShoppingCartEntry(this.mProduct, this.mQuantity);
        shoppingList.addAll(storedList);
        shoppingList.add(shoppingItem);
        Paper.book().write("shopping_line", shoppingList);
        ShoppingCartHelper.saveBadgeCount();
        return true;
    };
}