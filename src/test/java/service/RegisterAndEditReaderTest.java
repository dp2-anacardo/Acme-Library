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
import domain.Reader;
import forms.OrganizerForm;
import forms.ReaderForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.ActorService;
import services.OrganizerService;
import services.ReaderService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterAndEditReaderTest extends AbstractTest {

    @Autowired
    private ReaderService readerService;
    @Autowired
    private ActorService actorService;


    /*
     * Testing functional requirement : requirement 29.1 (reader register)
     * Positive:An actor who is not authenticated can register as reader
     * Negative: You cant register in the system if you dont fill in the username field
     * Sentence coverage: 100%
     * Data coverage: 26%
     */
    @Test
    public void registerReaderDriver() {
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
            this.templateRegisterReader((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
                    (String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9],(Class<?>) testingData[i][10]);
    }

    /*
     * Testing functional requirement : requirement 30.2 (reader edit personal data)
     * Positive:An actor who is authenticated as Reader can edit your personal data
     * Negative:An actor who is authenticated as Oganizer can not edit edit personal data of any reader
     * Sentence coverage: 75%
     * Data coverage: 22%
     */
    @Test
    public void editReaderDriver() {
        final Object testingData[][] = {
                {
                        "reader1", "newName", null
                }, {
                        "organizer1", "newName", NullPointerException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateEditReader((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
    }

    public void templateRegisterReader(final String username, final String password, final String comfirmPass, final String name, final String surname, final String middleName, final String photo, final String email, final String phoneNumber, final String address, final Class<?> expected) {

        Class<?> caught;
        caught = null;

        try {
            final ReaderForm aForm = new ReaderForm();

            final DataBinder binding = new DataBinder(new Reader());

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

            final Reader a = this.readerService.reconstruct(aForm, binding.getBindingResult());

            this.readerService.save(a);
            this.readerService.flush();

        } catch (final Throwable e) {
            caught = e.getClass();
        }
        super.checkExceptions(expected, caught);

    }

    private void templateEditReader(final String reader, final String newName, final Class<?> class1) {
        Class<?> caught;
        caught = null;
        try {

            super.authenticate(reader);
            final DataBinder binding = new DataBinder(new Reader());
            final Reader a = this.readerService.findOne(this.actorService.getActorLogged().getId());
            a.setName(newName);
            final Reader result = this.readerService.reconstruct(a, binding.getBindingResult());

            this.readerService.save(result);

        } catch (final Exception e) {
            caught = e.getClass();
        }
        super.checkExceptions(class1, caught);
    }

}
