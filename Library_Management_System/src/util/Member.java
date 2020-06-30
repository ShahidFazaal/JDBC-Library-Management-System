package util;

public class Member {
    private String nic;
    private String name;
    private int phone;
    private String email;

    public Member(String nic, String name, int phone, String email) {
        this.nic = nic;
        this.name = name;
        this.phone = phone;
        this.email=email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member{" +
                "nic='" + nic + '\'' +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                '}';
    }
}
