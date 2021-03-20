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

    public void addUser(ID user) { users.add(user); }

    public void removeUser(ID user) { users.remove(user); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return group.getGroupName().equals(getGroupName());
    }
}
