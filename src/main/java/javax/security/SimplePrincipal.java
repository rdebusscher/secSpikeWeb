package javax.security;

import java.security.Principal;

/**
 *
 */
public class SimplePrincipal implements Principal {

    private String name;

    public SimplePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimplePrincipal)) {
            return false;
        }

        SimplePrincipal that = (SimplePrincipal) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
