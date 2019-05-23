package services;

import domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.CategoryRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    //Manager repository
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ActorService actorService;

    public Category create(){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        Category res = new Category();
        return res;
    }

    public Collection<Category> findAll(){
        Collection<Category> res;
        res = this.categoryRepository.findAll();
        Assert.notNull(res);
        return res;
    }

    public Category findOne(int categoryId){
        Category res;
        res = this.categoryRepository.findOne(categoryId);
        Assert.notNull(res);
        return res;
    }

    public Category save(Category c){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));

        Assert.notNull(c);
        Category res;
        res = this.categoryRepository.save(c);
        return res;
    }

    public void delete(Category c){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));

        Assert.notNull(c);
        Assert.isTrue(c.getId() != 0);

        this.categoryRepository.delete(c);
    }

    public Collection<String> getNamesEs(){
        return this.categoryRepository.getNamesEs();
    }

    public Collection<String> getNamesEn(){
        return this.categoryRepository.getNamesEn();
    }

}
