package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.RegisterService;
import utilities.AbstractTest;

import javax.transaction.Transactional;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterTest extends AbstractTest {

    @Autowired
    private RegisterService registerService;

    /*
     * Testing functional requirement : An actor who is authenticated as reader must be able to register in an event
     * Positive: A reader register in an event
     * Negative: An organizer try register in an event
     * Sentence coverage:
     * Data coverage:
     */
    @Test
    public void checkInDriver(){
        Object testingData[][] = {
                {"event3", "reader1", null},
                {"event3", "organizer1", IllegalArgumentException.class}
        };

    for (int i = 0; i<testingData.length; i++){
        this.checkInTemplate(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (Class<?>) testingData[i][2]);
        }
    }

    private void checkInTemplate(int eventId, String user, Class<?> expected){
        Class<?> caught;
        caught = null;

        try{
            this.authenticate(user);
           this.registerService.save(eventId);
        } catch (Throwable oops){
            caught = oops.getClass();
        }
        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : An actor who is authenticated as reader must be able to cancel a register
     * Positive: A reader cancel a register
     * Negative: An organizer try cancel a register
     * Sentence coverage:
     * Data coverage:
     */
    @Test
    public void cancelRegisterDriver(){
        Object testingData[][] = {
                {"event1", "register1", "reader1", null},
                {"event1", "register1","organizer1", IllegalArgumentException.class}
        };

        for (int i = 0; i<testingData.length; i++){
            this.cancelRegisterTemplate(super.getEntityId((String) testingData[i][0]),super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);
        }
    }

    private void cancelRegisterTemplate(int eventId, int registerId, String user, Class<?> expected){
        Class<?> caught;
        caught = null;

        try{
            this.authenticate(user);
            this.registerService.cancel(registerId, eventId);
        } catch (Throwable oops){
            caught = oops.getClass();
        }
        super.checkExceptions(expected, caught);
    }
}
