
package domain;

import org.hibernate.validator.constraints.*;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import security.UserAccount;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
        @Index(columnList = "isSuspicious, isBanned")
})
public abstract class Actor extends DomainEntity {

    private String name;
    private String middleName;
    private String surname;
    private String photo;
    private String email;
    private String phoneNumber;
    private String address;
    private Boolean isSuspicious;
    private Boolean isBanned;
    private Double score;


    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getName() {
        return this.name;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getMiddleName() {
        return this.middleName;
    }

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getSurname() {
        return this.surname;
    }

    @URL
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getPhoto() {
        return this.photo;
    }

    @NotBlank
    @Email
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getEmail() {
        return this.email;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getAddress() {
        return this.address;
    }

    @NotNull
    public Boolean getIsSuspicious() {
        return this.isSuspicious;
    }

    @NotNull
    @Range(min = -1, max = 1)
    public Double getScore() {
        return this.score;
    }

    @NotNull
    public Boolean getIsBanned() {
        return this.isBanned;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public void setPhoto(final String photo) {
        this.photo = photo;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setIsSuspicious(final Boolean isSuspicious) {
        this.isSuspicious = isSuspicious;
    }

    public void setIsBanned(final Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public void setScore(final Double score) {
        this.score = score;
    }

    //Relationships

    private UserAccount userAccount;
    private Collection<SocialProfile> socialProfiles;

    @Valid
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    public UserAccount getUserAccount() {
        return this.userAccount;
    }

    @Valid
    @OneToMany
    public Collection<SocialProfile> getSocialProfiles() {
        return this.socialProfiles;
    }

    public void setUserAccount(final UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public void setSocialProfiles(final Collection<SocialProfile> socialProfiles) {
        this.socialProfiles = socialProfiles;
    }

}
