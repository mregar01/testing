public class StringAssert {
    
    private String s;

    public StringAssert(String passed) {
        s = passed;
    }

    public StringAssert isNotNull() {
        if (s == null) {
            throw new UnsupportedOperationException();
        } else {
            return this;
        }
    }

    public StringAssert isNull() {
        if (s != null) {
            throw new UnsupportedOperationException();
        } else {
            return this;
        }
    }

    public StringAssert isEqualTo(Object o2) {
        if (o2 instanceof String) {
            if (!s.equals(o2)) {
                throw new UnsupportedOperationException();   
            } else {
                return this;
            }
        } else {
            throw new UnsupportedOperationException();     
        }
    }

    public StringAssert isNotEqualTo(Object o2) {

        if (!(o2 instanceof String) && o2 == null && s == null) {
            throw new UnsupportedOperationException();
        }

        if (s == null && o2 == null) {
            return this;
        }
        
        if (s == null || o2 == null) {
            return this;
        }
    
        if (!(o2 instanceof String)) {
            return this;
        }
    
        if (s.equals(o2)) {
            throw new UnsupportedOperationException();
        } else {
            return this;
        }
    }

    public StringAssert startsWith(String s2) {
        if (s.startsWith(s2)) {
            return this;
        } else {
            throw new UnsupportedOperationException();   
        }
    }

    public StringAssert isEmpty() {
        if (!s.equals("")) {
            throw new UnsupportedOperationException(); 
        } else {
            return this;   
        }
    }

    public StringAssert contains(String s2) {
        if (!s.contains(s2)) {
            throw new UnsupportedOperationException(); 
        } else {
            return this;   
        }
    }

        
}