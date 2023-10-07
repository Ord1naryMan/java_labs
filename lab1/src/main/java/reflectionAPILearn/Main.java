package reflectionAPILearn;

import reflectionAPILearn.Classes.Nutritious;
import reflectionAPILearn.Classes.Sandwich;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        List<String[]> parts = new ArrayList<>();

        List<String[]> classInstances = new ArrayList<>();

        boolean isSorting = false;
        boolean isCalories = false;

        for (String arg : args) {
            if (arg.equals("-sort")) {
                isSorting = true;
            } else if (arg.equals("-calories")) {
                isCalories = true;
            } else {
                parts.add(arg.split("/"));
            }
        }

        for (String[] part : parts) {
            String[] fillings = part[1].split(" ");
            if (fillings.length != 4) {
                fillings = new String[4];
                fillings[1] = "";
                fillings[3] = "";
            }
            if (fillings[1].endsWith("ом")) {
                fillings[1] = fillings[1]
                        .substring(0, fillings[1].length() - 2);
            }
            if (fillings[1].endsWith("ми")) {
                fillings[1] = fillings[1]
                        .substring(0, fillings[1].length() - 2) + "и";
            }
            if (fillings[3].endsWith("ом")) {
                fillings[3] = fillings[3]
                        .substring(0, fillings[3].length() - 2);
            }
            if (fillings[3].endsWith("ми")) {
                fillings[3] = fillings[3]
                        .substring(0, fillings[3].length() - 2) + "и";
            }

            classInstances.add(new String[]{part[0], fillings[1], fillings[3]});
        }

        List<Sandwich> breakfast = new ArrayList<>();
        //list of sandwiches because I should sort it by name
        for (String[] instance : classInstances) {
            Class<?> food;
            try {
                food = Class.forName("reflectionAPILearn.Classes." + instance[0]);
            } catch (ClassNotFoundException e) {
                System.out.println("Sorry but food " + instance[0] + " not found so you won't eat it.");
                continue;
            }
            try {
                Constructor<?> endFood = food.getDeclaredConstructor(String.class, String.class);
                breakfast.add((Sandwich) endFood.newInstance(instance[1], instance[2]));
            } catch (IllegalArgumentException e) {
                System.out.println("Sorry but you don't have such ingredients sorry :(");
            } catch (NoSuchMethodException | InstantiationException |
                     IllegalAccessException | InvocationTargetException ignored) {
            }

        }

        if (isSorting) {
            breakfast.sort((s1, s2) -> {
                if (s1.getF1Name().length() != s2.getF1Name().length()) {
                    return Integer.compare(
                                s1.getF1Name().length(),
                                s2.getF1Name().length()
                            );
                }
                return Integer.compare(
                            s1.getF1Name().length(),
                            s2.getF1Name().length()
                        );
            });
        }

        for (Nutritious sandwich : breakfast) {
            System.out.print(sandwich);
            if (isCalories) {
                System.out.println(" has " + sandwich.calculateCalories() + " calories");
            } else {
                System.out.println();
            }
        }

        Map<Sandwich, Integer> mealsCounter = new HashMap<>();

        for (Sandwich sandwich : breakfast) {
            mealsCounter.put(
                    sandwich,
                    mealsCounter.getOrDefault(sandwich, 0) + 1
            );
        }
        for (Map.Entry<Sandwich, Integer> entry : mealsCounter.entrySet()) {
            System.out.printf(
                    "In your breakfast %s found %s times!" +
                            System.lineSeparator(),
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }
}
