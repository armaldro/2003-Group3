import enums.OrderStatus;

public class OrderStatusCheck {
    public static void main(String[] args) {
        for (OrderStatus s : OrderStatus.values()) {
            System.out.println(s.name() + " -> " + s.getLabel());
        }
    }
}
