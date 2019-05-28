
package domain;

import org.hibernate.validator.constraints.*;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	private Integer maxResults;
	private Integer maxTime;
	private String				systemName;
	private String				banner;
	private String				welcomeMessageEn;
	private String				welcomeMessageEs;
	private Collection<String>	spamWords;
	private Collection<String>	posWords;
	private Collection<String>	negWords;
	private String				countryCode;
	private Double				defaultVAT;
	private Double				flatFee;
	private Collection<String>	brandName;


	@Range(min = 10, max = 100)
	@NotNull
	public Integer getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(final Integer maxResults) {
		this.maxResults = maxResults;
	}

	@Range(min = 1, max = 24)
	@NotNull
	public Integer getMaxTime() {
		return this.maxTime;
	}

	public void setMaxTime(final Integer maxTime) {
		this.maxTime = maxTime;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageEn() {
		return this.welcomeMessageEn;
	}

	public void setWelcomeMessageEn(final String welcomeMessageEn) {
		this.welcomeMessageEn = welcomeMessageEn;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageEs() {
		return this.welcomeMessageEs;
	}

	public void setWelcomeMessageEs(final String welcomeMessageEs) {
		this.welcomeMessageEs = welcomeMessageEs;
	}

	@ElementCollection
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@ElementCollection
	public Collection<String> getPosWords() {
		return posWords;
	}

	public void setPosWords(Collection<String> posWords) {
		this.posWords = posWords;
	}

	@ElementCollection
	public Collection<String> getNegWords() {
		return negWords;
	}

	public void setNegWords(Collection<String> negWords) {
		this.negWords = negWords;
	}

	@NotBlank
	public String getCountryCode() {
		return this.countryCode;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	@NotNull
	public Double getDefaultVAT() {
		return this.defaultVAT;
	}

	public void setDefaultVAT(final Double defaultVAT) {
		this.defaultVAT = defaultVAT;
	}

	@NotNull
	public Double getFlatFee() {
		return this.flatFee;
	}

	public void setFlatFee(final Double flatFee) {
		this.flatFee = flatFee;
	}

	@ElementCollection
	@NotEmpty
	public Collection<String> getBrandName() {
		return brandName;
	}

	public void setBrandName(Collection<String> brandName) {
		this.brandName = brandName;
	}
}
