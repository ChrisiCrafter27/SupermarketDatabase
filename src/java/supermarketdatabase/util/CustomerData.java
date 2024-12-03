package supermarketdatabase.util;

public record CustomerData(String name, String city, String street, String dateOfJoining, double totaleExpenditure) {
    public SimpleCustomerData simlify() {
        return new SimpleCustomerData(name(), city(), street(), null);
    }
}
