
package domain;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
public class Reader extends Actor {

    @Expose
    private Finder finder;
    @Expose
    private Collection<Book> book;

    @Valid
    @OneToOne(optional = false)
    public Finder getFinder() {
        return finder;
    }

    public void setFinder(Finder finder) {
        this.finder = finder;
    }

    @Valid
    @ElementCollection
    @OneToMany
    public Collection<Book> getBook() {
        return book;
    }

    public void setBook(Collection<Book> book) {
        this.book = book;
    }
}
