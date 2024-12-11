import java.util.*;

public class IntAssert {

    private int i;

    public IntAssert(int I){
        i = I;
    }


    public IntAssert isEqualTo(int i2){
        if (i != i2){
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

    public IntAssert isLessThan(int i2){
        if (i >= i2){
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

    public IntAssert isGreaterThan(int i2){
        if (i <= i2){
            throw new NoSuchElementException();
        } else {
            return this;
        }
    }

    
}