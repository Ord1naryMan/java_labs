package reflectionAPILearn.Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Sandwich implements Nutritious {

    private final String f1Name, f2Name;

    private final static Map<String, Integer> calloriesMap = new HashMap<>(){{
            put("хлеб", 10);
            put("сыр", 20);
            put("помидор", 5);
            put("джем", 50);
    }};

    public Sandwich(String f1, String f2) throws IllegalArgumentException {
        f1Name = f1;
        f2Name = f2;
        if(!calloriesMap.containsKey(f1)) {
            throw new IllegalArgumentException();
        }
        if(!calloriesMap.containsKey(f2)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int calculateCalories(){
        return calloriesMap.get(f1Name) + calloriesMap.get(f2Name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sandwich sandwich = (Sandwich) o;
        return Objects.equals(f1Name, sandwich.f1Name) && Objects.equals(f2Name, sandwich.f2Name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(f1Name, f2Name);
    }

    @Override
    public String toString(){
        return "Sandwich with " + f1Name + " and " + f2Name;
    }

    public String getF1Name() {
        return f1Name;
    }

    public String getF2Name() {
        return f2Name;
    }
}
