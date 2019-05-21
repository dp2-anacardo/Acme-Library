package services;

import domain.Configuration;
import forms.ConfigurationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.ConfigurationRepository;
import security.UserAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ConfigurationService {

    //Managed repository
    @Autowired
    private ConfigurationRepository	configurationRepository;
    //Services
    @Autowired
    private ActorService			actorService;
    //Validator
    @Autowired
    private Validator				validator;


    //CRUD
    public List<Configuration> findAll() {
        return this.configurationRepository.findAll();
    }

    public Configuration findOne(final Integer arg0) {
        return this.configurationRepository.findOne(arg0);
    }

    public Configuration getConfiguration() {
        final Configuration result = this.configurationRepository.getConfiguration().iterator().next();
        return result;
    }

    public Configuration save(final Configuration configuration) {
        Assert.notNull(configuration);

        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();

        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));

        Configuration result;
        result = this.configurationRepository.save(configuration);

        return result;
    }

    //Reconstructs
    public Configuration reconstructEdit(final ConfigurationForm configF, final BindingResult binding) {
        Configuration config;
        final Configuration result = new Configuration();

        config = this.configurationRepository.findOne(configF.getId());

        result.setId(config.getId());
        result.setVersion(config.getVersion());
        result.setSpamWords(config.getSpamWords());
        result.setPosWords(config.getPosWords());
        result.setNegWords(config.getNegWords());
        result.setBrandName(config.getBrandName());

        resultSetsReconstruct(result, configF);

        this.validator.validate(result, binding);

        return result;
    }

    public Configuration reconstructAddWord(final ConfigurationForm configF, final BindingResult binding) {
        Configuration config;
        final Configuration result = new Configuration();

        config = this.configurationRepository.findOne(configF.getId());

        resultSetsReconstruct(result, new ConfigurationForm(config));

        final Collection<String> sW = new ArrayList<>();
        final Collection<String> pW = new ArrayList<>();
        final Collection<String> nW = new ArrayList<>();
        final Collection<String> bN = new ArrayList<>();

        sW.addAll(config.getSpamWords());
        pW.addAll(config.getPosWords());
        nW.addAll(config.getNegWords());
        bN.addAll(config.getBrandName());

        result.setSpamWords(sW);
        result.setPosWords(pW);
        result.setNegWords(nW);
        result.setBrandName(bN);

        if (!configF.getAddSW().equals(""))
            result.getSpamWords().add(configF.getAddSW());
        if (!configF.getAddPW().equals(""))
            result.getPosWords().add(configF.getAddPW());
        if (!configF.getAddNW().equals(""))
            result.getNegWords().add(configF.getAddNW());
        if (!configF.getAddBN().equals(""))
            result.getBrandName().add(configF.getAddBN());

        result.setId(config.getId());
        result.setVersion(config.getVersion());

        return result;
    }

    private void resultSetsReconstruct(Configuration result, ConfigurationForm configF) {
        result.setMaxResults(configF.getMaxResults());
        result.setMaxTime(configF.getMaxTime());
        result.setSystemName(configF.getSystemName());
        result.setBanner(configF.getBanner());
        result.setWelcomeMessageEn(configF.getWelcomeMessageEn());
        result.setWelcomeMessageEs(configF.getWelcomeMessageEs());
        result.setCountryCode(configF.getCountryCode());
        result.setDefaultVAT(configF.getDefaultVAT());
        result.setFlatFee(configF.getFlatFee());
    }
}
