package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import java.util.Date;


@Entity
@Access(AccessType.PROPERTY)
public class Register extends DomainEntity {

    private Date moment;

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

}
