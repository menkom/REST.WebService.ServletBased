package info.mastera.model;

import javax.validation.constraints.Size;

/**
 * Simple class with only one field
 */
public class Foo {

    private long id;

    @Size(min = 5, max = 14)
    private String name;

    public Foo(final String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}