
package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ActorService {

    //Managed Repositories
    @Autowired
    private ActorRepository actorRepository;

    //Supporting services
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private SocialProfileService socialProfileService;
    @Autowired
    private AdministratorService administratorService;

    public Collection<Actor> findAll() {
        Collection<Actor> result;

        result = this.actorRepository.findAll();

        return result;
    }

    public Actor findOne(final int actorId) {
        Assert.isTrue(actorId != 0);

        Actor result;

        result = this.actorRepository.findOne(actorId);

        return result;
    }

    public void delete(final Actor actor) {
        Assert.notNull(actor);
        Assert.isTrue(actor.getId() != 0);
        Assert.isTrue(this.actorRepository.exists(actor.getId()));

        this.actorRepository.delete(actor);
    }

    public Actor findByUserAccount(final UserAccount userAccount) {
        Assert.notNull(userAccount);

        Actor result;

        result = this.actorRepository.findByUserAccountId(userAccount.getId());

        return result;
    }

    public Actor getActorLogged() {
        UserAccount userAccount;
        Actor actor;

        userAccount = LoginService.getPrincipal();
        Assert.notNull(userAccount);

        actor = this.findByUserAccount(userAccount);
        Assert.notNull(actor);

        return actor;
    }

    public Actor save(final Actor actor) {
        Assert.notNull(actor);

        Actor result;

        result = this.actorRepository.save(actor);

        return result;
    }

    public Actor findByUsername(final String username) {
        Assert.notNull(username);

        Actor result;
        result = this.actorRepository.findByUsername(username);
        return result;
    }

    public Boolean existUsername(final String username) {
        Boolean res = false;
        final List<String> lista = new ArrayList<String>();
        for (final Actor a : this.actorRepository.findAll())
            lista.add(a.getUserAccount().getUsername());
        if (!(lista.contains(username)))
            res = true;
        return res;
    }

    public Boolean existIdSocialProfile(final Integer id) {
        Boolean res = false;
        final List<Integer> lista = new ArrayList<Integer>();
        for (final SocialProfile s : this.socialProfileService.findAll())
            lista.add(s.getId());
        if (lista.contains(id))
            res = true;
        return res;
    }

    public void deleteInformation() {

        UserAccount userAccount;
        userAccount = LoginService.getPrincipal();
        final Actor user = this.findByUserAccount(userAccount);

        //Borrado de los socialProfiles de los actores
        if (!(user.getSocialProfiles().isEmpty())) {
            final List<SocialProfile> a = new ArrayList<>();
            final Collection<SocialProfile> ad = user.getSocialProfiles();
            a.addAll(ad);
            for (final SocialProfile i : a)
                this.socialProfileService.delete(i);
        }

    }
}
