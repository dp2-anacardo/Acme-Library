
package domain;

import com.google.gson.annotations.Expose;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

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
