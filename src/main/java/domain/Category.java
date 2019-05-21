package domain;

import org.hibernate.validator.constraints.NotBlank;

public class Category extends DomainEntity {
    private String nameEn;
    private String nameEs;


    @NotBlank
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @NotBlank
    public String getNameEs() {
        return nameEs;
    }

    public void setNameEs(String nameEs) {
        this.nameEs = nameEs;
    }
}
