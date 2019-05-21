package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.util.Collection;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Event extends DomainEntity {
    private String title;
    private String description;
    private Date date;
    private String address;
    private int maximumCapacity;
    // private int actualCapacity;

    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Future
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    // Relationships...................................................................................................
    private Organizer organizer;
    private Collection<Sponsorship> sponsorships;
    private Collection<Register> registers;

    @ManyToOne(optional = false)
    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    @ManyToMany
    public Collection<Sponsorship> getSponsorships() {
        return sponsorships;
    }

    public void setSponsorships(Collection<Sponsorship> sponsorships) {
        this.sponsorships = sponsorships;
    }

    @OneToMany
    public Collection<Register> getRegisters() {
        return registers;
    }

    public void setRegisters(Collection<Register> registers) {
        this.registers = registers;
    }
}
