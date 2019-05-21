
package forms;

import domain.Referee;
import domain.Sponsor;
import org.hibernate.validator.constraints.*;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class SponsorForm {

	private String	confirmPass;
	private String	password;
	private String	username;
	private String	name;
	private String	photo;
	private String	email;
	private String	phoneNumber;
	private String	address;
	private int		id;
	private int		version;
	private String	surname;

	private String	holder;
	private String	brandName;
	private String	number;
	private Date expirationYear;
	private Integer	cvv;

	public SponsorForm(final Sponsor s) {
		final SponsorForm result = new SponsorForm();
		result.setAddress(s.getAddress());
		result.setEmail(s.getEmail());
		result.setId(s.getId());
		result.setName(s.getName());
		result.setPhoneNumber(s.getPhoneNumber());
		result.setPhoto(s.getPhoto());
		result.setSurname(s.getSurname());
		result.setVersion(s.getVersion());

	}

	public SponsorForm() {

	}

	@Size(min = 5, max = 32)
	@NotNull
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getConfirmPass() {
		return this.confirmPass;
	}

	public void setConfirmPass(final String confirmPass) {
		this.confirmPass = confirmPass;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	@Email
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Size(min = 5, max = 32)
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPassword() {
		return this.password;
	}
	public void setPassword(final String password) {
		this.password = password;
	}

	@Size(min = 5, max = 32)
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getUsername() {
		return this.username;
	}
	public void setUsername(final String username) {
		this.username = username;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Valid
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBrandName() {
		return brandName;
	}


	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@NotBlank
	@CreditCardNumber
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@NotNull
	@Future
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "MM/YY")
	public Date getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Date expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	@Range(min = 100, max = 999)
	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}
}
