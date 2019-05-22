/*
 * RegisterAndEditAdministratorTest.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package service;

import domain.Reader;
import domain.Sponsor;
import forms.ReaderForm;
import forms.SponsorForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.ActorService;
import services.ReaderService;
import services.SponsorService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterAndEditSponsorTest extends AbstractTest {

    @Autowired
    private SponsorService sponsorService;
    @Autowired
    private ActorService actorService;


    /*
     * Testing functional requirement : requirement 29.1 (sponsor register)
     * Positive:An actor who is not authenticated can register as sponsor
     * Negative: You cant register in the system if you dont fill in the username field
     * Sentence coverage: 100%
     * Data coverage: 26%
     */
    @Test
    public void registerSponsorDriver() {
        final Object testingData[][] = {
                {
                        "prueba1", "123456", "123456", "prueba1", "pruebaSurname", "pruebaMiddleName", "http://photo", "prueba@prueba.com", "600102030", "address", null
                }
                ,
                {
                         "", "123456", "123456", "prueba1", "pruebaSurname", "pruebaMiddleName", "http://photo", "prueba@prueba.com", "600102030", "address", ConstraintViolationException.class
                 }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateRegisterSponsor((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
                    (String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9],(Class<?>) testingData[i][10]);
    }


    /*
     * Testing functional requirement : requirement 30.2 (sponsor edit personal data)
     * Positive:An actor who is authenticated as Sponsor can edit your personal data
     * Negative:An actor who is authenticated as Reader can not edit edit personal data of any sponsor
     * Sentence coverage: 75%
     * Data coverage: 22%
     */
    @Test
    public void editSponsorDriver() {
        final Object testingData[][] = {
                {
                        "sponsor1", "newName", null
                }, {
                        "reader1", "newName", NullPointerException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateEditSponsor((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
    }

    public void templateRegisterSponsor(final String username, final String password, final String comfirmPass, final String name, final String surname, final String middleName, final String photo, final String email, final String phoneNumber, final String address, final Class<?> expected) {

        Class<?> caught;
        caught = null;

        try {
            final SponsorForm aForm = new SponsorForm();

            final DataBinder binding = new DataBinder(new Sponsor());

            aForm.setName(name);
            aForm.setSurname(surname);
            aForm.setUsername(username);
            aForm.setPassword(password);
            aForm.setMiddleName(middleName);
            aForm.setConfirmPass(comfirmPass);
            aForm.setEmail(email);
            aForm.setAddress(address);
            aForm.setPhoneNumber(phoneNumber);
            aForm.setPhoto(photo);

            final Sponsor a = this.sponsorService.reconstruct(aForm, binding.getBindingResult());

            this.sponsorService.save(a);
            this.sponsorService.flush();

        } catch (final Throwable e) {
            caught = e.getClass();
        }
        super.checkExceptions(expected, caught);

    }

    private void templateEditSponsor(final String sponsor, final String newName, final Class<?> class1) {
        Class<?> caught;
        caught = null;
        try {

            super.authenticate(sponsor);
            final DataBinder binding = new DataBinder(new Sponsor());
            final Sponsor a = this.sponsorService.findOne(this.actorService.getActorLogged().getId());
            a.setName(newName);
            final Sponsor result = this.sponsorService.reconstruct(a, binding.getBindingResult());

            this.sponsorService.save(result);

        } catch (final Exception e) {
            caught = e.getClass();
        }
        super.checkExceptions(class1, caught);
    }

}
