package com.example.xmn_android;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ShoppingCartHelper {

    public static void saveBadgeCount() {
        int badgeCount = 0;
        ArrayList<ShoppingCartEntry> shoppingList = Paper.book().read("shopping_line", new ArrayList<ShoppingCartEntry>());
        if (shoppingList.isEmpty() == false) {
            for (ShoppingCartEntry item: shoppingList) {
                badgeCount += item.getQuantity();
            };
        };
        Paper.book().write("badge_count", badgeCount);
    };
}
