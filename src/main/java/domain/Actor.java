
package domain;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.*;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import security.UserAccount;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
        @Index(columnList = "isSuspicious, isBanned")
})
public abstract class Actor extends DomainEntity {

    @Expose
    private String name;
    @Expose
    private String middleName;
    @Expose
    private String surname;
    @Expose
    private String photo;
    @Expose
    private String email;
    @Expose
    private String phoneNumber;
    @Expose
    private String address;
    @Expose
    private Boolean isSuspicious;
    @Expose
    private Boolean isBanned;
    @Expose
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

    public Boolean getIsSuspicious() {
        return this.isSuspicious;
    }

    @Range(min = -1, max = 1)
    public Double getScore() {
        return this.score;
    }

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

    @Expose
    private UserAccount userAccount;
    @Expose
    private Collection<SocialProfile> socialProfiles;
    @Expose
    private Collection<MessageBox> boxes;

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

    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    public Collection<MessageBox> getBoxes() {
        return boxes;
    }

    public void setUserAccount(final UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public void setSocialProfiles(final Collection<SocialProfile> socialProfiles) {
        this.socialProfiles = socialProfiles;
    }

    public void setBoxes(Collection<MessageBox> boxes) {
        this.boxes = boxes;
    }

    public MessageBox getMessageBox(final String name) {
        final MessageBox result = null;
        for (final MessageBox box : this.getBoxes())
            if (box.getName().equals(name))
                return box;
        return result;
    }


}
