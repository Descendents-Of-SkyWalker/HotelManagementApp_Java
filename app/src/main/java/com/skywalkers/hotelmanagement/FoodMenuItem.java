package com.skywalkers.hotelmanagement;

public class FoodMenuItem {
    private String itemName;
    private int VegNonVeg;
    private int Cost;
    private String IngredientsList;


    FoodMenuItem(String itemName, String val, int Cost, String IngredientsList){

        this.itemName=itemName;
        if(val=="veg")
            this.VegNonVeg=R.drawable.veg_icon;
        else
            this.VegNonVeg=R.drawable.non_veg_icon;
        this.Cost=Cost;

        this.IngredientsList=IngredientsList;
    }

    public String getItemName() {
        return itemName;
    }


    public int getVegNonVeg() {
        return VegNonVeg;
    }

    public int getCost() {
        return Cost;
    }

    public String getIngredientsList() {
        return IngredientsList;
    }
}
