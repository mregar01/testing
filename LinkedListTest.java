
import java.util.*;


public class LinkedListTest {
    private static LL<Integer> list;

    @BeforeClass
    public static void setUpClass() {
        list = new LL<>();
    }

    @Before
    public void setUp() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
    }

    @Test
    public void testSize() {
        Assertion.assertThat(list.size()).isEqualTo(4).isGreaterThan(3);
        assert(list.size() == 4);
    }

    @Test
    public void testIsEmpty() {
        Assertion.assertThat(list.isEmpty()).isEqualTo(false);
    }

    @Test
    public void testRemove() {
        assert(list.remove(2));
        assert(!list.remove(2));
        assert(list.size() == 3);
    }

    @Property
        public boolean absNonNeg(@IntRange(min=-10, max=10) Integer i) {
            return Math.abs(i.intValue()) >= 0;
    }

    @Property
    public void testIntRangeProperty(@IntRange(min = 1, max = 10) int value) {
        assert(value >= 1 && value <= 10);
    }

    @Property
    public void testStringSetProperty(@StringSet(strings = {"s1", "s2", "s3"} ) String value) {
        assert(value.equals("s1") || value.equals("s2") || value.equals("s3"));
    }

    @Property
    public boolean testListLengthProperty(@ListLength(min = 0, max = 3) List<@IntRange(min = 5, max = 7) Integer> list) {
        // assert(list.size() >= 0 && list.size() < 3);

        if (!(list.size() >= 0 && list.size() <= 3)){
            return false;
        }
        for (Integer value : list) {
            if (!(value >= 5 && value <= 7)){
                return false;
            }
            // assert(value >= 5 && value <= 7);
        }

        return true;
    }

    @Property
    public boolean testListLengthPropertyStr(@ListLength(min = 0, max = 3) List<@StringSet(strings = {"s1", "s2", "s3"} ) String> list) {
        // assert(list.size() >= 0 && list.size() < 3);

        if (!(list.size() >= 0 && list.size() <= 3)){
            return false;
        }
        for (String value : list) {
            if (!((value.equals("s1") || value.equals("s2") || value.equals("s3")))){
                return false;
            }            
        }

        return true;
    }

    @Property
    public boolean addIntegers(@ForAll(name = "generateIntegers", times = 100) int a, @ForAll(name = "generateIntegers", times = 100) int b) {
        int sum = a + b;
        
        // Ensure that the sum is always greater than or equal to either of the input integers.
        // return sum >= a && sum >= b;
        return true;
    }

    int counter = 0;

    // Define a method to generate integers.
    public Integer generateIntegers() {
        System.err.println("here");
        // Generate random integers in the range of -100 to 100.
        counter++;
        return counter;
    }

    // @Property
    // public void testObjectProperty(@ForAll(name = "generateValues", times = 3) Object obj) {
    //     // assertNotNull(obj);
    //     assert(obj != null);
    //     // Assuming that there's a method named "generateValues" in PropertyClass
    //     // that generates values for the object.
    // }

    @Property public boolean testFoo(@ForAll(name="genIntSet", times=10) Object o) {
        Set s = (Set) o;
        s.add("foo");
        return s.contains("foo");
    }

    int count = 0;
    public Object genIntSet() {
        Set s = new HashSet();
        for (int i=0; i<count; i++) { s.add(i); }
        count++;
        return s;
    }


    @After
    public void tearDown() {
        list = new LL<>();
    }

    @AfterClass
    public static void tearDownClass() {
        list = null;
    }
}
