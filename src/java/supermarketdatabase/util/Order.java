package supermarketdatabase.util;

public record Order(int id, NameRef customer, GoodRef good, int amount, FormattedDate orderDate, FormattedDate pickupDate, double price) {}