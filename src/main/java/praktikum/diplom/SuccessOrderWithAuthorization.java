package praktikum.diplom;

public class SuccessOrderWithAuthorization {
    private boolean success;
    private String name;
    private Order order;

    public SuccessOrderWithAuthorization(boolean success, String name, Order order) {
        this.success = success;
        this.name = name;
        this.order = order;
    }

    public SuccessOrderWithAuthorization() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
