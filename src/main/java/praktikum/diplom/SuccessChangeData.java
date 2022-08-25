package praktikum.diplom;

public class SuccessChangeData {
    private boolean success;
    private User user;

    public SuccessChangeData(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public SuccessChangeData() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
