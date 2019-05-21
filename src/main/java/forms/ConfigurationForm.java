package forms;

import domain.Configuration;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import java.util.Collection;

public class ConfigurationForm {

    private int					id;
    private int					version;
    private int					maxResults;
    private int					maxTime;
    private String				systemName;
    private String				banner;
    private String				welcomeMessageEn;
    private String				welcomeMessageEs;
    private Collection<String>	spamWords;
    private Collection<String>	posWords;
    private Collection<String>	negWords;
    private String				countryCode;
    private String				addSW;
    private String				addPW;
    private String				addNW;
    private Double				defaultVAT;
    private Double				flatFee;
    private Collection<String>	brandName;
    private String				addBN;


    public ConfigurationForm(final Configuration config) {
        this.id = config.getId();
        this.version = config.getVersion();
        this.maxResults = config.getMaxResults();
        this.maxTime = config.getMaxTime();
        this.systemName = config.getSystemName();
        this.banner = config.getBanner();
        this.welcomeMessageEn = config.getWelcomeMessageEn();
        this.welcomeMessageEs = config.getWelcomeMessageEs();
        this.spamWords = config.getSpamWords();
        this.posWords = config.getPosWords();
        this.negWords = config.getNegWords();
        this.countryCode = config.getCountryCode();
        this.defaultVAT = config.getDefaultVAT();
        this.flatFee = config.getFlatFee();
        this.brandName = config.getBrandName();
    }

    public ConfigurationForm() {

    }

    public int getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public int getMaxResults() { return this.maxResults; }

    public int getMaxTime() { return this.maxTime; }

    public String getSystemName() {
        return this.systemName;
    }

    public String getBanner() {
        return this.banner;
    }

    public String getWelcomeMessageEn() {
        return this.welcomeMessageEn;
    }

    public String getWelcomeMessageEs() {
        return this.welcomeMessageEs;
    }

    public Collection<String> getSpamWords() {
        return this.spamWords;
    }

    public Collection<String> getPosWords() { return posWords; }

    public Collection<String> getNegWords() { return negWords; }

    public Collection<String> getBrandName() { return brandName; }

    public String getCountryCode() {
        return this.countryCode;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getAddSW() {
        return this.addSW;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getAddPW() {
        return addPW;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getAddNW() {
        return addNW;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getAddBN() {
        return addBN;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public void setMaxResults(final int maxResults) { this.maxResults = maxResults; }

    public void setMaxTime(final int maxTime) { this.maxTime = maxTime; }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    public void setBanner(final String banner) {
        this.banner = banner;
    }

    public void setWelcomeMessageEn(final String welcomeMessageEn) {
        this.welcomeMessageEn = welcomeMessageEn;
    }

    public void setWelcomeMessageEs(final String welcomeMessageEs) {
        this.welcomeMessageEs = welcomeMessageEs;
    }

    public void setSpamWords(final Collection<String> spamWords) {
        this.spamWords = spamWords;
    }

    public void setPosWords(Collection<String> posWords) { this.posWords = posWords; }

    public void setNegWords(Collection<String> negWords) { this.negWords = negWords; }

    public void setBrandName(Collection<String> brandName) { this.brandName = brandName; }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public void setAddSW(final String addSW) {
        this.addSW = addSW;
    }

    public void setAddPW(String addPW) { this.addPW = addPW; }

    public void setAddNW(String addNW) { this.addNW = addNW; }

    public void setAddBN(String addBN) { this.addBN = addBN; }

    public Double getDefaultVAT() {
        return defaultVAT;
    }

    public void setDefaultVAT(Double defaultVAT) {
        this.defaultVAT = defaultVAT;
    }

    public Double getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(Double flatFee) {
        this.flatFee = flatFee;
    }
}
