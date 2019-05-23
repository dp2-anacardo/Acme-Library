package services;

import domain.Book;
import domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.CategoryRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;

@Service
@Transactional
public class CategoryService {
    //Manager repository
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private Validator validator;

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

        Collection<Book> bookWithThisCategory = this.getBooksWithCategoryParam(c);

        for (Book b: bookWithThisCategory){
            Collection<Category> categories = b.getCategories();
            if(b.getCategories().size() == 1) {
                categories.add(this.getDefaultCategory());
            }
            categories.remove(c);
            b.setCategories(categories);
        }
        this.categoryRepository.delete(c);
    }

    public Collection<String> getNamesEs(){
        return this.categoryRepository.getNamesEs();
    }

    public Collection<String> getNamesEn() {
        return this.categoryRepository.getNamesEn();
    }

    public Collection<Book> getBooksWithCategoryParam(Category category){
        Collection<Book> res;
        res = this.categoryRepository.getBooksWithCategoryParam(category);
        return res;
    }

    public Category getDefaultCategory(){
        Category res;
        res = this.categoryRepository.getDefaultCategory();
        Assert.notNull(res);
        return res;
    }

    public Category reconstruct(Category category, BindingResult binding){
        Category result;
        if (category.getId() == 0){
            result = this.create();
        }else {
            result = this.categoryRepository.findOne(category.getId());
        }
            result.setNameEn(category.getNameEn());
            result.setNameEs(category.getNameEs());
            validator.validate(result, binding);
        if (binding.hasErrors()){
            throw new ValidationException();
        }
        return result;
    }
}
