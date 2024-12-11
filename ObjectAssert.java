import java.util.NoSuchElementException;

public class ObjectAssert {
    private Object o;

    public ObjectAssert(Object O){
        o = O;
    }

    public ObjectAssert isNotNull() {
        if (o == null) {
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

    public ObjectAssert isNull() {
        if (o != null) {
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

    public ObjectAssert isEqualTo(Object o2) {
        if (!o.equals(o2)) {
             throw new NoSuchElementException();   
        } else {
            return this;
        }
    }

    public ObjectAssert isNotEqualTo(Object o2) {
        if (o.equals(o2)) {
             throw new NoSuchElementException();   
        } else {
            return this;
        }
    }

    public ObjectAssert isInstanceOf(Class c) {
        if (!c.isInstance(o)) {
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

}
