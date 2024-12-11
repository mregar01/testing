import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String className = "LinkedListTest";

        Map<String, Throwable> testResults = Unit.testClass(className);

        System.err.println("-------------------------");
        System.err.println("Part 1 Testing");


        for (Map.Entry<String, Throwable> entry : testResults.entrySet()) {
            String methodName = entry.getKey();
            Throwable exception = entry.getValue();
            if (exception == null) {
                System.out.println(methodName + ": Passed");
            } else {
                System.err.println(methodName + ": Failed");
                exception.printStackTrace();
            }
        }

        System.err.println("-------------------------");

        System.err.println("Part 2 Testing");

        Map<String, Object[]> props = Unit.quickCheckClass(className);

        for (Map.Entry<String, Object[]> entry : props.entrySet()) {
            String methodName = entry.getKey();
            Object[] exception = entry.getValue();
            if (exception == null) {
                System.out.println(methodName + ": Passed");
            } else {
                System.err.println(methodName + ": Failed");
                // exception.printStackTrace();
                System.err.println("error: " + exception.length);
                System.err.println("error: " + exception[0]);
                // System.err.println("error: " + exception[3]);
            }
        }

        System.err.println("-------------------------");
    }
}
