package lv.romanpipchenko.restexample.beans;

public class Cat {

    String name;
    int age;
    String color;
    Cat[] parents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Cat[] getParents() {
        return parents;
    }

    public void setParents(Cat[] parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "Cat [name=" + name + ", age=" + age + ", color="+color+"]";
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    public void update(Cat cat){
        this.setName(cat.getName());
        this.setAge(cat.getAge());
        this.setColor(cat.getColor());
    }
}