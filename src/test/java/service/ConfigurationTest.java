package service;

import domain.Configuration;
import domain.Finder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.ConfigurationService;
import services.FinderService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ConfigurationTest extends AbstractTest {

    @Autowired
    public ConfigurationService configurationService;

    //In this test we are testing the requirement 35.7(creating new positive word).
    //In the negative cases we are testing that other user that he isn't admin try to edit it
    //Sequence coverage: 100%
    //Data coverage: 100%

    @Test
    public void creatingPositiveWordConfigurationDriver() {
        final Object testingData[][] = {
                {
                        "admin1", "configuration1", "word", null
                }, {
                "reader1", "configuration1", "word", IllegalArgumentException.class
        },};
        for (int i = 0; i < testingData.length; i++)
            this.creatingPositiveWordConfigurationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void creatingPositiveWordConfigurationTemplate(final String username, final String finder, final String word, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(username);

            Configuration configuration1 = this.configurationService.findOne(super.getEntityId(finder));
            Collection<String> posWords = configuration1.getPosWords();
            posWords.add(word);
            configuration1.setPosWords(posWords);
            this.configurationService.save(configuration1);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

    //In this test we are testing the requirement 35.7(updating/deleting a positive word).
    //In the negative cases we are testing that other user that he isn't admin try to edit it
    //Sequence coverage: 100%
    //Data coverage: 100%

    @Test
    public void deletingPositiveWordConfigurationDriver() {
        final Object testingData[][] = {
                {
                        "admin1", "configuration1", "good", null
                }, {
                "reader1", "configuration1", "good", IllegalArgumentException.class
        },};
        for (int i = 0; i < testingData.length; i++)
            this.deletingPositiveWordConfigurationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void deletingPositiveWordConfigurationTemplate(final String username, final String finder, final String word, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(username);

            Configuration configuration1 = this.configurationService.findOne(super.getEntityId(finder));
            Collection<String> posWords = configuration1.getPosWords();
            posWords.remove(word);
            configuration1.setPosWords(posWords);
            this.configurationService.save(configuration1);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

    //In this test we are testing the requirement 35.7(creating new negative word).
    //In the negative cases we are testing that other user that he isn't admin try to edit it
    //Sequence coverage: 100%
    //Data coverage: 100%

    @Test
    public void creatingNegativeWordConfigurationDriver() {
        final Object testingData[][] = {
                {
                        "admin1", "configuration1", "word", null
                }, {
                "reader1", "configuration1", "word", IllegalArgumentException.class
        },};
        for (int i = 0; i < testingData.length; i++)
            this.creatingNegativeWordConfigurationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void creatingNegativeWordConfigurationTemplate(final String username, final String finder, final String word, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(username);

            Configuration configuration1 = this.configurationService.findOne(super.getEntityId(finder));
            Collection<String> negWords = configuration1.getNegWords();
            negWords.add(word);
            configuration1.setNegWords(negWords);
            this.configurationService.save(configuration1);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

    //In this test we are testing the requirement 35.7(updating/deleting a negative word).
    //In the negative cases we are testing that other user that he isn't admin try to edit it
    //Sequence coverage: 100%
    //Data coverage: 100%

    @Test
    public void deletingNegativeWordConfigurationDriver() {
        final Object testingData[][] = {
                {
                        "admin1", "configuration1", "bad", null
                }, {
                "reader1", "configuration1", "bad", IllegalArgumentException.class
        },};
        for (int i = 0; i < testingData.length; i++)
            this.deletingNegativeWordConfigurationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void deletingNegativeWordConfigurationTemplate(final String username, final String finder, final String word, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(username);

            Configuration configuration1 = this.configurationService.findOne(super.getEntityId(finder));
            Collection<String> negWords = configuration1.getNegWords();
            negWords.remove(word);
            configuration1.setNegWords(negWords);
            this.configurationService.save(configuration1);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }
}
