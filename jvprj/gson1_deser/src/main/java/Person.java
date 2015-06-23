public class Person {
    private String name;
    private String surname;
    private Car[] cars;
    private int phone;
    private transient int age;
 
    private Person() {
    }
 
    public Person(String name, String surname, int phone, int age, Car[] cars) {
        this.name = name;
        this.surname = surname;
        this.cars = cars;
        this.phone = phone;
        this.age = age;
    }
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
 
        sb.append("Name: " + name + " " + surname + "\n");
        sb.append("Phone: " + phone + "\n");
        sb.append("Age: " + age + "\n");
 
        int i = 0;
        for (Car item : cars) {
            i++;
            sb.append("Car " + i + ": " + item + "\n");
        }
 
        return sb.toString();
    }
}
