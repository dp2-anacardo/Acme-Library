package domain;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

    private String					keyWord;
    private String                  categoryName;
    private String                  status;
    private Date                    lastUpdate;

    //Relationships

    private Collection<Transaction>	transactions;


    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(final String keyWord) {
        if (keyWord == null)
            this.keyWord = keyWord;
        else
            this.keyWord = keyWord.trim();
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yy HH:mm")
    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(final Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    //Relationships

    @Valid
    @ManyToMany
    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }
}