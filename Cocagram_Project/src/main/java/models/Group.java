package models;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private List<ID> users;
    public Group(String groupName) {
        this.groupName = groupName;
        users = new ArrayList<>();
    }
    public String getGroupName() { return groupName; }

    public List<ID> getUsers() { return users; }
}
