package com.pkasemer.worshipcenterdowntown.HelperClasses;

import com.pkasemer.worshipcenterdowntown.Models.FoodDBModel;

public interface CartItemHandlerListener {
    void increment(int qty, FoodDBModel foodDBModel);
    void decrement(int qty, FoodDBModel foodDBModel);
    void deletemenuitem(String foodMenu_id, FoodDBModel foodDBModel);
}
