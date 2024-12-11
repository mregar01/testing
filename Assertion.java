public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
    static ObjectAssert assertThat(Object o) {
	    ObjectAssert local_obj = new ObjectAssert(o);

        return local_obj;
    }

    static StringAssert assertThat(String s) {
	    StringAssert local_string = new StringAssert(s);

        return local_string;
    }

    static BoolAssert assertThat(boolean b) {
	    BoolAssert local_bool = new BoolAssert(b);

        return local_bool;
    }

    static IntAssert assertThat(int i) {
	    IntAssert local_int = new IntAssert(i);

        return local_int;
    }
}