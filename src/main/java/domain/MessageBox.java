package domain;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
        @Index(columnList = "name")
})
public class MessageBox extends DomainEntity {

    // Attributes -------------------------------------------------------------
    @Expose
    private String	name;
    @Expose
    private Boolean	isSystem;


    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @NotNull
    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(final Boolean isSystem) {
        this.isSystem = isSystem;
    }


    // Relationships -------------------------------------------------------------
    @Expose
    private Collection<Message>		messages;

    @Valid
    @ManyToMany
    public Collection<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(final Collection<Message> messages) {
        this.messages = messages;
    }

    //Methods
    public void addMessage(final Message message) {
        this.messages.add(message);
    }

    public void deleteMessage(final Message message) {
        this.messages.remove(message);

    }

    public boolean equals(final MessageBox obj) {
        boolean res = false;
        if (this.getName().equals(obj.getName()))
            res = true;
        return res;
    }

}
