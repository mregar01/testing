import java.util.*;


// import org.omg.Dynamic.Parameter;

import java.lang.reflect.Method;
import java.lang.annotation.*;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationTargetException;


public class Unit {

    public static Map<String, Throwable> testClass(String name) {
        HashMap<String, Throwable> tests_to_outputs = new HashMap<>();
        
        try {
            Method[] class_methods = Class.forName(name).getDeclaredMethods();



            ArrayList<Method> before_classes = new ArrayList<Method>();
            ArrayList<Method> befores = new ArrayList<Method>();
            ArrayList<Method> tests = new ArrayList<Method>();
            ArrayList<Method> afters = new ArrayList<Method>();
            ArrayList<Method> after_classes = new ArrayList<Method>();


            for (Method method : class_methods) {
                // Annotation[] annotations = method.getDeclaredAnnotations();
                // if (annotations.length != 1) {
                //     throw new NoSuchElementException();
                // }
                if (method.isAnnotationPresent(BeforeClass.class)) {
                    before_classes.add(method);
                } else if (method.isAnnotationPresent(Before.class)) {
                    befores.add(method);
                } else if (method.isAnnotationPresent(Test.class)) {
                    tests.add(method);
                } else if (method.isAnnotationPresent(After.class)) {
                    afters.add(method);
                } else if (method.isAnnotationPresent(AfterClass.class)) {
                    after_classes.add(method);
                } 
            }

            before_classes.sort(Comparator.comparing(Method::getName));
            befores.sort(Comparator.comparing(Method::getName));
            tests.sort(Comparator.comparing(Method::getName));
            afters.sort(Comparator.comparing(Method::getName));
            after_classes.sort(Comparator.comparing(Method::getName));   
            
            

            run_methods(before_classes, name);

            for (Method method : tests){

                run_methods(befores, name);

                try {
                    Class<?> targetClass = Class.forName(name);
                    Object targetObject = targetClass.getDeclaredConstructor().newInstance();                   
    
                    method.invoke(targetObject);

                    tests_to_outputs.put(method.getName(), null);
                } catch (InvocationTargetException ite) {
                    Throwable originalException = ite.getTargetException();
                    tests_to_outputs.put(method.getName(), originalException);
                } catch (Exception e) {}

                run_methods(afters, name);
            }

            run_methods(after_classes, name);
            

        } catch (Exception e) { 
            throw new UnsupportedOperationException(); 
            
        }

        return tests_to_outputs;


    }

    public static void run_methods(ArrayList<Method> method_list, String c_name) {
        for (Method method : method_list) {
            try {
                Class<?> targetClass = Class.forName(c_name);
                Object targetObject = targetClass.getDeclaredConstructor().newInstance();                     

                method.invoke(targetObject);
        
            } catch (Exception e) {                            
            }
        }

    }

    public static Map<String, Object[]> quickCheckClass(String name) {
	    Map<String, Object[]> properties_to_output = new HashMap<>();

        try {
            Method[] all_methods = Class.forName(name).getDeclaredMethods();
            ArrayList<Method> properties = new ArrayList<Method>();

            for (Method method : all_methods) {
                if (method.isAnnotationPresent(Property.class)) {
                    properties.add(method);
                }
            }
 
            properties.sort(Comparator.comparing(Method::getName));

            Annotation[][] annotations = null;


            for (Method m : properties){
                annotations = m.getParameterAnnotations();
                for (int i = 0; i < annotations.length; i++) {
                    for (int j = 0; j < annotations[i].length; j++) {
                        if (annotations[i][j] instanceof IntRange){
                            IntRange curr = (IntRange) annotations[i][j];
                            runIntRange(curr, properties_to_output, m, name);
                
                            
                        } else if (annotations[i][j] instanceof StringSet){
                            StringSet curr = (StringSet) annotations[i][j];
                            runStringSet(curr, properties_to_output, m, name);
    
                        } else if (annotations[i][j] instanceof ListLength) {
                            ListLength curr = (ListLength) annotations[i][j];
                            runListLength(curr, properties_to_output, m, name);
    
                        } else if (annotations[i][j] instanceof ForAll) {
                            System.err.println("in here" + annotations[i][j].toString());
                            ForAll curr = (ForAll) annotations[i][j];
                            runForAll(curr, properties_to_output, m, name);
    
                        }
                    }
                }
            }
        } catch (Exception e) {
            
            throw new UnsupportedOperationException();
        }
        return properties_to_output;

    }

    public static void runIntRange(IntRange curr, Map<String, Object[]> map, Method m, String name){
        Object[] arr = new Object[1];
        int counter = 0;
        try {
            int min = curr.min();
            int max = curr.max();

            Class<?> targetClass = Class.forName(name);
            Object targetObject = targetClass.getDeclaredConstructor().newInstance();                   
        
            

            
            for (int i = min; i < max + 1; i++){
                arr[0] = i;
                
                if (Boolean.FALSE.equals(m.invoke(targetObject, i))){
                    map.put(m.getName(), arr);
                    break;
                }
                counter++;
                if (counter == 100 || i == max){
                    map.put(m.getName(), null);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            map.put(m.getName(), arr);
        }
    }

    public static void runStringSet(StringSet curr, Map<String, Object[]> map, Method m, String name){
        Object[] arr = new Object[1];
        int counter = 0;
        try {
            String[] str_set = curr.strings();

            Class<?> targetClass = Class.forName(name);
            Object targetObject = targetClass.getDeclaredConstructor().newInstance(); 

            
            for (String s : str_set){
                arr[0] = s;
                if (Boolean.FALSE.equals(m.invoke(targetObject, s))){
                    map.put(m.getName(), arr);
                    break;
                }
                counter++;
                if (counter == 100 || s.equals(str_set[str_set.length - 1])){
                    map.put(m.getName(), null);
                    break;
                }
                
            }

            
            
        } catch (Exception e) {
            map.put(m.getName(), arr);
        }   
    }

    public static void runForAll(ForAll curr, Map<String, Object[]> map, Method m, String name){
        Object[] arr = new Object[1];
        String method_name = curr.name();
        int times = curr.times(); 
        int counter = 0;

        try {
            Class<?> targetClass = Class.forName(name);
            Object targetObject = targetClass.getDeclaredConstructor().newInstance();
            
            Method inner_Method = (Class.forName(name).getMethod(method_name));

            

            
            for (int i = 0; i < times; i++){
                System.err.println("invokoing");
                Object o = inner_Method.invoke(targetObject);
                arr[0] = o;
                if (Boolean.FALSE.equals(m.invoke(targetObject, o))){
                    System.err.println("putting");
                    map.put(m.getName(), arr);
                    break;
                }
                counter++;
                if (counter == 100 || i == times - 1){
                    map.put(m.getName(), null);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("invokoing wejkbfk");
            map.put(m.getName(), arr);
        }
    }

    public static void runListLength(ListLength curr, Map<String, Object[]> map, Method m, String name) {
        int max = curr.max();
        int min = curr.min();
        // Object[] arr = new Object[max];
        Object[] arr = null;
        Annotation inner = null;
    
        try {
            Class<?> targetClass = Class.forName(name);
            Object targetObject = targetClass.getDeclaredConstructor().newInstance();
                
            java.lang.reflect.Parameter parameter = m.getParameters()[0];
            AnnotatedType annotatedType = parameter.getAnnotatedType();

            if (annotatedType instanceof AnnotatedParameterizedType) {
                AnnotatedParameterizedType annotatedParameterizedType = (AnnotatedParameterizedType) annotatedType;
                AnnotatedType[] actualTypeArguments = annotatedParameterizedType.getAnnotatedActualTypeArguments();

                if (actualTypeArguments.length > 0) {
                    Annotation[] annotations = actualTypeArguments[0].getDeclaredAnnotations();

                    if (annotations.length > 0) {
                        inner = annotations[0];
                    }
                }
            }

            if (inner instanceof IntRange){
                IntRangeHelper(inner, arr, m, min, max, map, targetObject);
            } else if (inner instanceof StringSet) {
                StringSetHelper(inner, arr, m, min, max, map, targetObject);
            } else if (inner instanceof ListLength) {
                map.put(m.getName(), null);
            } else {
                throw new UnsupportedOperationException();
            }

    
            
        } catch (Exception e) {
            if (e instanceof UnsupportedOperationException){
                throw new UnsupportedOperationException();
            }  else {
                map.put(m.getName(), arr);
            }
            e.printStackTrace();
            
        }
    }

    public static void generatePermutations(int min, int max, int length, ArrayList<Integer> current, ArrayList<List<Integer>> all_options) {
        if (length == 0) {
            all_options.add(new ArrayList<>(current));
            return;
        }
    
        for (int i = min; i <= max; i++) {
            ArrayList<Integer> newCurrent = new ArrayList<>(current);
            newCurrent.add(i);
            generatePermutations(min, max, length - 1, newCurrent, all_options);
        }
    }

    public static void generateStringPermutations(String[] elements, int length, ArrayList<String> current, ArrayList<List<String>> all_options) {
        if (length == 0) {
            all_options.add(new ArrayList<>(current));
            return;
        }
    
        for (String element : elements) {
            ArrayList<String> newCurrent = new ArrayList<>(current);
            newCurrent.add(element);
            generateStringPermutations(elements, length - 1, newCurrent, all_options);
        }
    }

    public static void IntRangeHelper(Annotation inner, Object [] arr, Method m, int min, int max, Map<String, Object[]> map, Object targetObject){
        int counter = 0;
        IntRange local = (IntRange) inner;
        int local_min = local.min();
        int local_max = local.max();
        ArrayList<List<Integer>> all_options = new ArrayList<List<Integer>>();
        ArrayList<Integer> new_one = new ArrayList<Integer>();
        

        for (int i = min; i <= max; i++) {
            generatePermutations(local_min, local_max, i, new ArrayList<>(), all_options);
        } 
        
        try {
            for (int i = 0; i < all_options.size(); i++){
                // System.err.println();
                // System.err.println("size" + all_options.get(i).size());
                arr = new Object[1];
                
                // if (all_options.get(i).size() == 0) {
                    
                //     arr[0] = null;
                // } else {
                //     arr = new Object[all_options.get(i).size()];
                // }
                
                for (int x = 0; x < all_options.get(i).size(); x++){
                    // System.err.println("adding: " + all_options.get(i).get(x));
                    // arr[x] = all_options.get(i).get(x);
                    new_one.add(all_options.get(i).get(x));
                }

                arr[0] = new_one;
                if (Boolean.FALSE.equals(m.invoke(targetObject, all_options.get(i)))){
                    // System.err.println("in this case:");

                    map.put(m.getName(), arr);
                    break;
                }
                counter++;
                if (counter == 100 || i == all_options.size() - 1){
                    map.put(m.getName(), null);
                    break;
                }

                new_one.clear();
    
                arr = null;
                
            }
        } catch (Exception e) {
            map.put(m.getName(), arr);
        }

        
    }

    public static void StringSetHelper(Annotation inner, Object [] arr, Method m, int min, int max, Map<String, Object[]> map, Object targetObject){
        int counter = 0;
        StringSet local = (StringSet) inner;
        String[] str_set = local.strings();
        ArrayList<List<String>> all_options = new ArrayList<List<String>>();

        for (int i = min; i <= max; i++) {
            generateStringPermutations(str_set, i, new ArrayList<>(), all_options);
        } 
        
        try {
            for (int i = 0; i < all_options.size(); i++){
                // System.err.println();
                // System.err.println("size" + all_options.get(i).size());
                
                if (all_options.get(i).size() == 0) {
                    arr = new Object[1];
                    arr[0] = null;
                } else {
                    arr = new Object[all_options.get(i).size()];
                }
                
                for (int x = 0; x < all_options.get(i).size(); x++){
                    // System.err.println("adding: " + all_options.get(i).get(x));
                    arr[x] = all_options.get(i).get(x);
                }
                if (Boolean.FALSE.equals(m.invoke(targetObject, all_options.get(i)))){
                    // System.err.println("in this case:");
                    map.put(m.getName(), arr);
                    break;
                }
                counter++;
                if (counter == 100 || i == all_options.size() - 1){
                    map.put(m.getName(), null);
                    break;
                }
    
                arr = null;
                
            }
        } catch (Exception e) {
            map.put(m.getName(), arr);
        }

        
    }  

    public static void ListLengthHelper(Annotation inner, Object [] arr, Method m, int min, int max, Map<String, Object[]> map, Object targetObject){

    }
    
}




