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

import domain.Administrator;
import forms.AdministratorForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;
import services.ActorService;
import services.AdministratorService;
import utilities.AbstractTest;

import javax.transaction.Transactional;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterAndEditAdministratorTest extends AbstractTest {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private ActorService actorService;


    /*
     * Testing functional requirement : requirement 35.1 (administrator register)
     * Positive:An actor who is authenticated as Administrator can register other administrator
     * Negative:An actor who is authenticated as Reader can not register other administrator
     * Sentence coverage: 100%
     * Data coverage: 30%
     */
    @Test
    public void registerAdministratorDriver() {
        final Object testingData[][] = {
                {
                        "prueba1", "123456", "123456", "prueba1", "pruebaSurname", "pruebaMiddleName", "http://photo", "prueba@prueba.com", "600102030", "address", "admin1", null
                }
                ,
                {
                         "prueba1", "123456", "123456", "prueba1", "pruebaSurname", "pruebaMiddleName", "http://photo", "prueba@prueba.com", "600102030", "address", "reader1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateRegisterAdministrator((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
                    (String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);
    }

    /*
     * Testing functional requirement : requirement 30.2 (administrator edit personal data)
     * Positive:An actor who is authenticated as Administrator can edit your personal data
     * Negative:An actor who is authenticated as Rookie can not edit edit personal data of any administrator
     * Sentence coverage: 80%
     * Data coverage: 18%
     */
    @Test
    public void editAdministratorDriver() {
        final Object testingData[][] = {
                {
                        "admin1", "newName", null
                }, {
                        "reader1", "newName", NullPointerException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateEditAdministrator((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
    }

    public void templateRegisterAdministrator(final String username, final String password, final String comfirmPass, final String name, final String surname, final String middleName, final String photo, final String email, final String phoneNumber, final String address,final String admin, final Class<?> expected) {

        Class<?> caught;
        caught = null;

        try {
            this.authenticate(admin);

            final AdministratorForm aForm = new AdministratorForm();

            final DataBinder binding = new DataBinder(new Administrator());

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

            final Administrator a = this.administratorService.reconstruct(aForm, binding.getBindingResult());

            this.administratorService.save(a);

        } catch (final Throwable e) {
            caught = e.getClass();
        }
        super.checkExceptions(expected, caught);

    }

    private void templateEditAdministrator(final String admin, final String newName, final Class<?> class1) {
        Class<?> caught;
        caught = null;
        try {

            super.authenticate(admin);
            final DataBinder binding = new DataBinder(new Administrator());
            final Administrator a = this.administratorService.findOne(this.actorService.getActorLogged().getId());
            a.setName(newName);
            final Administrator result = this.administratorService.reconstruct(a, binding.getBindingResult());

            this.administratorService.save(result);

        } catch (final Exception e) {
            caught = e.getClass();
        }
        super.checkExceptions(class1, caught);
    }

}
