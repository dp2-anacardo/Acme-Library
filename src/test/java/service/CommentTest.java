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
import domain.Comment;
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

    @Autowired
    private CommentService commentService;


    //In this test we are testing the requirement 31 and 34(creating comments).
    //In the negative cases we are testing that the actors involved in the report can not create a comment if they do not
    //submit a body, and it can only be created by the readers involved in the complaint and the referee that has
    //self-assigned it
    //Sequence coverage: 100%
    //Data coverage: 100%

    @Test
    public void createCommentDriver() {
        final Object testingData[][] = {
                {
                        "reader1", "test", "report1", null
                },{
                "reader2", "test", "report1", null
        },{
                "referee1", "test", "report1", null
        }, {
                "reader1", "", "report1", ValidationException.class
        }, {
                "referee2", "test", "report1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.createCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void createCommentTemplate(final String username, final String body, final String report, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(username);

            final int reportId = super.getEntityId(report);
            Comment comment = this.commentService.create();
            comment.setBody(body);
            final DataBinder binding = new DataBinder(new Comment());
            comment = this.commentService.reconstruct(comment, binding.getBindingResult());
            this.commentService.save(comment, reportId);
            super.unauthenticate();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

}
