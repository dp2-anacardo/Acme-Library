package domain;

import datatype.CreditCard;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)

public class Sponsorship extends DomainEntity {

    private String banner;
    private Boolean status;
    private CreditCard creditCard;

    @URL
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getBanner() {
        return this.banner;
    }

    public void setBanner(final String banner) {
        this.banner = banner;
    }

    @NotNull
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Valid
    @NotNull
    public CreditCard getCreditCard() {
        return this.creditCard;
    }

    public void setCreditCard(final CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    // Relationships ----------------------------------------------------------
    private Sponsor sponsor;
    private Event event;

    @Valid
    @NotNull
    @ManyToOne(optional = false)
    public Sponsor getSponsor() {
        return this.sponsor;
    }

    public void setSponsor(final Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    @Valid
    @NotNull
    @ManyToOne(optional = false)
    public Event getEvent() {
        return this.event;
    }

    public void setEvent(final Event event) {
        this.event = event;
    }

}