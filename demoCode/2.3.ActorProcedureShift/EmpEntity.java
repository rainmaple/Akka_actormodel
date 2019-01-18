package Entity;

public class EmpEntity {
    private String name;
    private double salary;
    private int level;
    public EmpEntity(String name,double salary){
        this.name=name;
        this.salary =salary;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
