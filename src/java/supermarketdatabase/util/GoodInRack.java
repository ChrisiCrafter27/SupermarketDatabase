package supermarketdatabase.util;

public record GoodInRack(int id, int amount, GoodRef ref, Price price, int stock, Temperature temperature, double requiredSpace) {}

