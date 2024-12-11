import java.util.*;

public class BoolAssert {
    private boolean b;

    public BoolAssert(boolean B){
        b = B;
    }

    public BoolAssert isEqualTo(boolean b2) {
        if (b != b2){
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

    public BoolAssert isTrue() {
        if (b == false){
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

    public BoolAssert isFalse() {
        if (b == true){
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }



}
