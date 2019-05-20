
package domain;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Reader extends Actor {

//    @Expose
//    private Collection<Finder> finder;
//    @Expose
//    private Collection<Book> book;
//
//    @Valid
//    @ElementCollection
//    @OneToMany
//    public Collection<Finder> getFinder() {
//        return finder;
//    }
//
//    public void setFinder(Collection<Finder> finder) {
//        this.finder = finder;
//    }
//
//    @Valid
//    @ElementCollection
//    @OneToMany
//    public Collection<Book> getBook() {
//        return book;
//    }
//
//    public void setBook(Collection<Book> book) {
//        this.book = book;
//    }
    /*
    Esto para el populate de reader:
    <bean id="reader1" class="domain.Reader">
		<property name="userAccount" ref="userAccount3"/>
		<property name="name" value="reader1" />
		<property name="surname" value="reader1"/>
		<property name="photo" value="https://github.com/dp2-anacardo/DP2-D01"/>
		<property name="email" value="emailDePruebareader@gmail.com"/>
		<property name="phoneNumber" value="+34 645887763"/>
		<property name="address" value="Avenida readers"/>
		<property name="isSuspicious" value="false"/>
		<property name="isBanned" value="false"/>
		<property name="score" value="0"/>
		<property name="socialProfiles">
			<list>
			</list>
		</property>
		<property name="finder">
			<list>
			</list>
		</property>

		<property name="book">
			<list>
			</list>
		</property>
	</bean>
     */
}
