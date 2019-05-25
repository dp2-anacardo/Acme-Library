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
import domain.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.CommentService;
import services.ReportService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommentTest extends AbstractTest {

//    @Autowired
//    private CommentService commentService;
//
//
//    //In this test we are testing the requirement 34(creating reports).
//    //In the negative cases we are testing that referees can not create a report if they do not
//    //submit a description, a report can only be created in final mode or draft mode, and a referee can not
//    //create a report if he/she has not self-assigned the complaint or another referee has self-assigned it.
//    //Sequence coverage: 100%
//    //Data coverage: 66.7%
//
//    @Test
//    public void createCommentDriver() {
//        final Object testingData[][] = {
//                {
//                        "reader1", "tet", "report1", null
//                },{
//                "referee1", "test", "report1", null
//        },{
//                "referee1", "", null, true, "complaint2", ValidationException.class
//        }, {
//                "referee1", "description", null, true, "complaint1", NullPointerException.class
//        }, {
//                "referee2", "description", null, true, "complaint2", IllegalArgumentException.class
//        }
//        };
//        for (int i = 0; i < testingData.length; i++)
//            this.createCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (Collection<Url>) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
//    }
//
//    private void createCommentTemplate(final String username, final String description, final Collection<Url> attachments, final Boolean isFinal, final String complaint, final Class<?> expected) {
//        Class<?> caught;
//        caught = null;
//
//        try {
//            this.authenticate(username);
//
//            final int complaintId = super.getEntityId(complaint);
//            Report report = this.reportService.create();
//            report.setDescription(description);
//            report.setAttachments(attachments);
//            report.setIsFinal(isFinal);
//            final DataBinder binding = new DataBinder(new Report());
//            report = this.reportService.reconstruct(report, complaintId, binding.getBindingResult());
//            this.reportService.save(report, complaintId);
//            super.unauthenticate();
//
//        } catch (final Throwable oops) {
//            caught = oops.getClass();
//        }
//
//        super.checkExceptions(expected, caught);
//
//    }

}
