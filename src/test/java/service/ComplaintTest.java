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

import datatype.Url;
import domain.Complaint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.ComplaintService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ComplaintTest extends AbstractTest {

    @Autowired
    private ComplaintService complaintService;


    //In this test we are testing the requirement 31(creating complaints).
    //In the negative cases we are testing that readers can not create a complaint if they do not
    //submit a body, a complaint can only be created if the transaction is finished, and they can only create
    //a complaint if they are the seller or the buyer.
    //Sequence coverage: 100%
    //Data coverage: 50%

    @Test
    public void createComplaintDriver() {
        final Object testingData[][] = {
                {
                        "reader1", "test", null, "transaction1", null
                },{
                "reader2", "test", null, "transaction1", null
        }, {
                "reader1", "", null, "transaction1", ValidationException.class
        }, {
                "reader1", "test", null, "transaction2", IllegalArgumentException.class
        }, {
                "reader2", "test", null, "transaction2", NullPointerException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.createComplaintTemplate((String) testingData[i][0], (String) testingData[i][1], (Collection<Url>) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
    }

    private void createComplaintTemplate(final String username, final String body, final Collection<Url> attachments, final String transaction, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(username);

            final int transactionId = super.getEntityId(transaction);
            Complaint complaint = this.complaintService.create();
            complaint.setBody(body);
            complaint.setAttachments(attachments);
            final DataBinder binding = new DataBinder(new Complaint());
            complaint = this.complaintService.reconstruct(complaint, binding.getBindingResult());
            this.complaintService.save(complaint, transactionId);
            super.unauthenticate();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

    //In this test we are testing the requirement 34(self-assign complaints).
    //In the negative cases we are testing that a referee can not self-assign a complaint that already has a referee
    //Sequence coverage: 100%
    //Data coverage: 100%

    @Test
    public void autoAssignateComplaintDriver() {
        final Object testingData[][] = {
                {
                        "referee1", "complaint1", null
                },{
                "referee1", "complaint2", IllegalArgumentException.class
        }, {
                "referee2", "complaint2", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.autoAssignateComplaintTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
    }

    private void autoAssignateComplaintTemplate(final String username, final String complaint, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(username);

            final int complaintId = super.getEntityId(complaint);
            Complaint c = this.complaintService.findOne(complaintId);
            c = this.complaintService.autoAssign(c);
            this.complaintService.saveAutoassign(c);
            super.unauthenticate();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

}
