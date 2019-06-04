package domain;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
        @Index(columnList = "isSpam")
})
public class Message extends DomainEntity {

    // Attributes -------------------------------------------------------------
    @Expose
    private Date				moment;
    @Expose
    private String				subject;
    @Expose
    private String				body;
    @Expose
    private String			priority;
    @Expose
    private Collection<String>	tags;
    @Expose
    private Boolean				isSpam;


    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Past
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getMoment() {
        return this.moment;
    }

    public void setMoment(final Date moment) {
        this.moment = moment;
    }

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getBody() {
        return this.body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    @NotBlank
    @Pattern(regexp = "^LOW|MEDIUM|HIGH$")
    public String getPriority() {
        return this.priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    @ElementCollection
    public Collection<String> getTags() {
        return this.tags;
    }

    public void setTags(final Collection<String> tags) {
        this.tags = tags;
    }

    public Boolean getIsSpam() {
        return this.isSpam;
    }

    public void setIsSpam(final Boolean isSpam) {
        this.isSpam = isSpam;
    }


    // Relationships ----------------------------------------------------------
    private Actor					sender;
    private Collection<Actor>		recipients;
    private Collection<MessageBox>	messageBoxes;


    @Valid
    @OneToOne(optional = true)
    public Actor getSender() {
        return this.sender;
    }

    public void setSender(final Actor sender) {
        this.sender = sender;
    }

    @Valid
    @NotNull
    @ManyToMany
    public Collection<Actor> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(final Collection<Actor> recipients) {
        this.recipients = recipients;
    }

    @NotNull
    @Valid
    @ManyToMany
    public Collection<MessageBox> getMessageBoxes() {
        return this.messageBoxes;
    }

    public void setMessageBoxes(final Collection<MessageBox> messageBoxes) {
        this.messageBoxes = messageBoxes;
    }

}
