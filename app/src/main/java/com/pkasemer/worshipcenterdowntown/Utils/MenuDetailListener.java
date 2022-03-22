package com.pkasemer.worshipcenterdowntown.Utils;

import com.pkasemer.worshipcenterdowntown.Models.FoodDBModel;
import com.pkasemer.worshipcenterdowntown.Models.SelectedCategoryMenuItemResult;

public interface MenuDetailListener {
    void retryPageLoad();
    void incrementqtn(int qty, FoodDBModel foodDBModel);
    void decrementqtn(int qty, FoodDBModel foodDBModel);

    void addToCartbtn(SelectedCategoryMenuItemResult selectedCategoryMenuItemResult);
    void orderNowMenuBtn(SelectedCategoryMenuItemResult selectedCategoryMenuItemResult);

}
