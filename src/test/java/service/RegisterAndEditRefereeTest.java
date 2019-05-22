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
import domain.Referee;
import forms.AdministratorForm;
import forms.RefereeForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.ActorService;
import services.AdministratorService;
import services.RefereeService;
import utilities.AbstractTest;

import javax.transaction.Transactional;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterAndEditRefereeTest extends AbstractTest {

    @Autowired
    private RefereeService  refereeService;
    @Autowired
    private ActorService actorService;


    /*
     * Testing functional requirement : requirement 35.11 (referee register)
     * Positive:An actor who is authenticated as Administrator can register referee
     * Negative:An actor who is authenticated as Referee can not register other referee
     * Sentence coverage: 100%
     * Data coverage: 30%
     */
    @Test
    public void registerRefereeDriver() {
        final Object testingData[][] = {
                {
                        "prueba1", "123456", "123456", "prueba1", "pruebaSurname", "pruebaMiddleName", "http://photo", "prueba@prueba.com", "600102030", "address","admin1", null
                }
                ,
                {
                         "prueba1", "123456", "123456", "prueba1", "pruebaSurname", "pruebaMiddleName", "http://photo", "prueba@prueba.com", "600102030", "address","referee1", IllegalArgumentException.class
                 }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateRegisterReferee((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
                    (String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10],(Class<?>) testingData[i][11]);
    }


    /*
     * Testing functional requirement : requirement 30.2 (referee edit personal data)
     * Positive:An actor who is authenticated as Referee can edit your personal data
     * Negative:An actor who is authenticated as Reader can not edit edit personal data of any referee
     * Sentence coverage: 80%
     * Data coverage: 23%
     */
    @Test
    public void editRefereeDriver() {
        final Object testingData[][] = {
                {
                        "referee1", "newName", null
                }, {
                        "reader1", "newName", NullPointerException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateEditReferee((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
    }

    public void templateRegisterReferee(final String username, final String password, final String comfirmPass, final String name, final String surname, final String middleName, final String photo, final String email, final String phoneNumber, final String address,final String admin, final Class<?> expected) {

        Class<?> caught;
        caught = null;

        try {
            this.authenticate(admin);
            final RefereeForm aForm = new RefereeForm();

            final DataBinder binding = new DataBinder(new Referee());

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

            final Referee a = this.refereeService.reconstruct(aForm, binding.getBindingResult());

            this.refereeService.save(a);

        } catch (final Throwable e) {
            caught = e.getClass();
        }
        super.checkExceptions(expected, caught);

    }

    private void templateEditReferee(final String referee, final String newName, final Class<?> class1) {
        Class<?> caught;
        caught = null;
        try {

            super.authenticate(referee);
            final DataBinder binding = new DataBinder(new Referee());
            final Referee a = this.refereeService.findOne(this.actorService.getActorLogged().getId());
            a.setName(newName);
            final Referee result = this.refereeService.reconstruct(a, binding.getBindingResult());

            this.refereeService.save(result);

        } catch (final Exception e) {
            caught = e.getClass();
        }
        super.checkExceptions(class1, caught);
    }

}
