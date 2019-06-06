package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
@Entity
@Access(AccessType.PROPERTY)
public class Offer extends DomainEntity{

    //Attributes----------------------------------------------------------------------
    private Date moment;
    private String comment;
    private String status;

    @NotBlank
    @Pattern(regexp = "^ACCEPTED|PENDING|REJECTED$")
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    //Relationships----------------------------------------------------------------------------------------------------

    private Reader reader;
    private Book book;
    private Transaction transaction;

    @NotNull
    @ManyToOne(optional = false)
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @NotNull
    @OneToOne(optional = false)
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @NotNull
    @ManyToOne(optional = false)
    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }




}
