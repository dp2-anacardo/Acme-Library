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

    public Category create() {
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        Category res = new Category();
        return res;
    }

    public Collection<Category> findAll() {
        Collection<Category> res;
        res = this.categoryRepository.findAll();
        Assert.notNull(res);
        return res;
    }

    public Category findOne(int categoryId) {
        Category res;
        res = this.categoryRepository.findOne(categoryId);
        Assert.notNull(res);
        return res;
    }

    public Category save(Category c) {
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));

        Assert.notNull(c);
        Category res;
        res = this.categoryRepository.save(c);
        return res;
    }

    public void delete(Category c) {
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));

        Assert.notNull(c);
        Assert.isTrue(c.getId() != 0);

        Collection<Book> bookWithThisCategory = this.getBooksWithCategoryParam(c);

        for (Book b : bookWithThisCategory) {
            Collection<Category> categories = b.getCategories();
            if (b.getCategories().size() == 1) {
                categories.add(this.getDefaultCategory());
            }
            categories.remove(c);
            b.setCategories(categories);
        }
        this.categoryRepository.delete(c);
    }

    public Collection<String> getNamesEs() {
        return this.categoryRepository.getNamesEs();
    }

    public Collection<String> getNamesEn() {
        return this.categoryRepository.getNamesEn();
    }

    public Collection<Book> getBooksWithCategoryParam(Category category) {
        Collection<Book> res;
        res = this.categoryRepository.getBooksWithCategoryParam(category);
        return res;
    }

    public Category getDefaultCategory() {
        Category res;
        res = this.categoryRepository.getDefaultCategory();
        Assert.notNull(res);
        return res;
    }

    public Category reconstruct(Category category, BindingResult binding) {
        Category result;

        Boolean existsES = false;
        Boolean existsEN = false;

        if (category.getId() == 0) {
            result = this.create();
        } else {
            result = this.categoryRepository.findOne(category.getId());

        }

        if (result.getId() == 0) {
            existsES = existsES(category);
            existsEN = existsEN(category);
        } else {
            if (!result.equalsES(category))
                existsES = existsES(category);
            if (!result.equalsEN(category))
                existsEN = existsEN(category);
        }

        result.setNameEn(category.getNameEn());
        result.setNameEs(category.getNameEs());


        validator.validate(result, binding);

        if (existsES)
            binding.rejectValue("nameEs", "category.es.duplicated");

        if (existsEN)
            binding.rejectValue("nameEn", "category.en.duplicated");

        if (binding.hasErrors()) {
            throw new ValidationException();
        }
        return result;
    }

    public Boolean existsES(final Category a) {
        Boolean exist = false;

        final Collection<Category> categories = this.findAll();
        for (final Category b : categories)
            if (a.equalsES(b)) {
                exist = true;
                break;
            }
        return exist;
    }

    public Boolean existsEN(final Category a) {
        Boolean exist = false;

        final Collection<Category> categories = this.findAll();
        for (final Category b : categories)
            if (a.equalsEN(b)) {
                exist = true;
                break;
            }
        return exist;
    }
}
