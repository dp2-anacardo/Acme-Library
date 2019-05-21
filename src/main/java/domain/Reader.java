
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


    @Valid
    @OneToOne(optional = false)
    public Finder getFinder() {
        return finder;
    }

    public void setFinder(Finder finder) {
        this.finder = finder;
    }


}
