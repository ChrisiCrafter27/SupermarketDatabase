package supermarketdatabase.util;

public record Good(int id, String name, Price price, int stock, Temperature temperature, double requiredSpace) {
    public EditableGoodData editable() {
        return new EditableGoodData(name, price, stock);
    }

    @Override
    public String toString() {
        return name() + " (ID: " + id() + ")";
    }
}
