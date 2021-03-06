package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;


@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
        @Index(columnList = "nameEs, nameEn")
})
public class Category extends DomainEntity {
    private String nameEn;
    private String nameEs;


    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getNameEs() {
        return nameEs;
    }

    public void setNameEs(String nameEs) {
        this.nameEs = nameEs;
    }

    public boolean equalsES(final Category obj) {
        boolean res = false;
        if (this.getNameEs().equals(obj.getNameEs()))
            res = true;
        return res;
    }

    public boolean equalsEN(final Category obj) {
        boolean res = false;
        if (this.getNameEn().equals(obj.getNameEn()))
            res = true;
        return res;
    }
}
