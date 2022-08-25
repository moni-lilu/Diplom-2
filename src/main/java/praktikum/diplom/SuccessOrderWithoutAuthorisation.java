package praktikum.diplom;

public class SuccessOrderWithoutAuthorisation {
    private boolean success;
    private String name;
    private OrderWithoutAuth order;

    public SuccessOrderWithoutAuthorisation(boolean success, String name, OrderWithoutAuth order) {
        this.success = success;
        this.name = name;
        this.order = order;
    }

    public SuccessOrderWithoutAuthorisation() {}

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

    public OrderWithoutAuth getOrder() {
        return order;
    }

    public void setOrder(OrderWithoutAuth order) {
        this.order = order;
    }
}
