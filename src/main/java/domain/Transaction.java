package domain;

import datatype.CreditCard;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
        @Index(columnList = "isSale, price, isFinished")
})
public class Transaction extends DomainEntity{

    //Attributes---------------------------------------------------------------------
    private String ticker;
    private Date moment;
    private Double price;
    private CreditCard creditCard;
    private Boolean isSale;
    private Boolean isFinished;


    public Boolean getIsFinished(){
        return this.isFinished;
    }

    public void setIsFinished(Boolean isFinished){
        this.isFinished = isFinished;
    }

    public Boolean getIsSale() {
        return isSale;
    }

    public void setIsSale(Boolean sale) {
        isSale = sale;
    }

    @NotBlank
    @Column(unique = true)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    @Range(min = 0, max = 999)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Valid
    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }


    //Relationships-----------------------------------------------------------------------------------------------------

    private Reader seller;
    private Reader buyer;
    private Collection<Offer> offers;
    private Book book;
    private Collection<Complaint> complaints;

    @OneToMany
    public Collection<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(Collection<Complaint> complaints) {
        this.complaints = complaints;
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
    public Reader getSeller() {
        return seller;
    }

    public void setSeller(Reader seller) {
        this.seller = seller;
    }

    @ManyToOne(optional = true)
    public Reader getBuyer() {
        return buyer;
    }

    public void setBuyer(Reader buyer) {
        this.buyer = buyer;
    }

    @OneToMany(mappedBy = "transaction")
    public Collection<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Collection<Offer> offers) {
        this.offers = offers;
    }
}
