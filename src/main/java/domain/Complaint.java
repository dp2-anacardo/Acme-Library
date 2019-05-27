package domain;

import datatype.Url;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Complaint extends DomainEntity{

    private String ticker;
    private Date moment;
    private String body;
    private Collection<Url> attachments;
    private Reader reader;
    private Referee referee;


    @NotBlank
    @Column(unique = true)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    @NotBlank
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    public Collection<Url> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<Url> attachments) {
        this.attachments = attachments;
    }

    @ManyToOne(optional = false)
    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @ManyToOne
    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }
}
