package com.example.user_managment;

public class DataClasses {

    private User Name;
    private Paintings MostFamous;
    private Paintings Cart;
    private Paintings Bought;

    public DataClasses(User name,Paintings mostFamous,Paintings cart,Paintings bought) {
        Name = name;
        MostFamous = mostFamous;
        Cart = cart;
        Bought = bought;
    }

    public Paintings getBought() {
        return Bought;
    }

    public void setBought(Paintings bought) {
        Bought = bought;
    }

    public Paintings getCart() {
        return Cart;
    }

    public void setCart(Paintings cart) {
        Cart = cart;
    }

    public Paintings getMostFamous() {
        return MostFamous;
    }

    public void setMostFamous(Paintings mostFamous) {
        MostFamous = mostFamous;
    }

    public User getName() {
        return Name;
    }

    public void setName(User name) {
        Name = name;
    }
}
