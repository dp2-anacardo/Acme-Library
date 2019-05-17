
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsor extends Actor {

    // Relationships ----------------------------------------------------------
    private Collection<Sponsorship>	sponsorships;


    @Valid
    @OneToMany(mappedBy = "sponsor")
    public Collection<Sponsorship> getSponsorships() {
        return this.sponsorships;
    }

    public void setSponsorships(final Collection<Sponsorship> sponsorships) {
        this.sponsorships = sponsorships;
    }

}
