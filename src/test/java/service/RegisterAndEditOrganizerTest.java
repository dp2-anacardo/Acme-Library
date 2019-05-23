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

import domain.Organizer;
import domain.Referee;
import forms.OrganizerForm;
import forms.RefereeForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.ActorService;
import services.OrganizerService;
import services.RefereeService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterAndEditOrganizerTest extends AbstractTest {

    @Autowired
    private OrganizerService organizerService;
    @Autowired
    private ActorService actorService;


    /*
     * Testing functional requirement : requirement 29.1 (organizer register)
     * Positive:An actor who is not authenticated can register as organizer
     * Negative: You cant register in the system if you dont fill in the username field
     * Sentence coverage: 100%
     * Data coverage: 26%
     */
    @Test
    public void registerOrganizerDriver() {
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
            this.templateRegisterOrganizer((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
                    (String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9],(Class<?>) testingData[i][10]);
    }


    /*
     * Testing functional requirement : requirement 30.2 (organizer edit personal data)
     * Positive:An actor who is authenticated as Organizer can edit your personal data
     * Negative:An actor who is authenticated as Reader can not edit edit personal data of any organizer
     * Sentence coverage: 75%
     * Data coverage: 22%
     */
    @Test
    public void editOrganizerDriver() {
        final Object testingData[][] = {
                {
                        "organizer1", "newName", null
                }, {
                        "reader1", "newName", NullPointerException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateEditOrganizer((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
    }

    public void templateRegisterOrganizer(final String username, final String password, final String comfirmPass, final String name, final String surname, final String middleName, final String photo, final String email, final String phoneNumber, final String address, final Class<?> expected) {

        Class<?> caught;
        caught = null;

        try {
            final OrganizerForm aForm = new OrganizerForm();

            final DataBinder binding = new DataBinder(new Organizer());

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

            final Organizer a = this.organizerService.reconstruct(aForm, binding.getBindingResult());

            this.organizerService.save(a);
            this.organizerService.flush();

        } catch (final Throwable e) {
            caught = e.getClass();
        }
        super.checkExceptions(expected, caught);

    }

    private void templateEditOrganizer(final String organizer, final String newName, final Class<?> class1) {
        Class<?> caught;
        caught = null;
        try {

            super.authenticate(organizer);
            final DataBinder binding = new DataBinder(new Organizer());
            final Organizer a = this.organizerService.findOne(this.actorService.getActorLogged().getId());
            a.setName(newName);
            final Organizer result = this.organizerService.reconstruct(a, binding.getBindingResult());

            this.organizerService.save(result);

        } catch (final Exception e) {
            caught = e.getClass();
        }
        super.checkExceptions(class1, caught);
    }

}
