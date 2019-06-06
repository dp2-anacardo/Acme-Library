package controllers.category;

import controllers.AbstractController;
import domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.CategoryService;

import javax.validation.ValidationException;
import java.util.Collection;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/administrator/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView result;
        try{
            Collection<Category> categories = this.categoryService.findAll();
            final String language = LocaleContextHolder.getLocale().getLanguage();
            result = new ModelAndView("category/administrator/list");
            result.addObject("categories", categories);
            result.addObject("requestURI", "category/administrator/list.do");
            result.addObject("lang", language);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/administrator/create", method = RequestMethod.GET)
    public ModelAndView create(){
        ModelAndView result;
        try{
            Category category = this.categoryService.create();
            result = new ModelAndView("category/administrator/create");
            result.addObject("category", category);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/administrator/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int categoryId){
        ModelAndView result;
        try{
            Category category;
            category = this.categoryService.findOne(categoryId);
            Assert.notNull(category);
            Assert.isTrue(!category.getNameEs().equals("Por defecto") && !category.getNameEn().equals("Default"));
            result = new ModelAndView("category/administrator/edit");
            result.addObject("category", category);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/administrator/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(Category category, BindingResult binding){
        ModelAndView result;
        try{
            Assert.notNull(category);
            category = this.categoryService.reconstruct(category, binding);
            category = this.categoryService.save(category);
            result = new ModelAndView("redirect:list.do");
        }catch (ValidationException e){
          result = this.createEditModelAndView(category);
        } catch (Throwable oops){
            result = this.createEditModelAndView(category, "category.commit.error");
        }
        return result;
    }

    @RequestMapping(value = "/administrator/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int categoryId){
        ModelAndView result;
        try{
            Category category = this.categoryService.findOne(categoryId);
            Assert.notNull(category);
            Assert.isTrue(!category.getNameEs().equals("Por defecto") && !category.getNameEn().equals("Default"));
            this.categoryService.delete(category);
            result = new ModelAndView("redirect:list.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/administrator/show", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam int categoryId){
        ModelAndView result;
        try{
            Category category = this.categoryService.findOne(categoryId);
            final String language = LocaleContextHolder.getLocale().getLanguage();
            Assert.notNull(category);
            result = new ModelAndView("category/administrator/show");
            result.addObject("category", category);
            result.addObject("requestURI", "category/administrator/show.do");
            result.addObject("lang", language);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }


    private ModelAndView createEditModelAndView(final Category category) {
        ModelAndView result;
        result = this.createEditModelAndView(category, null);
        return result;
    }

    private ModelAndView createEditModelAndView(final Category category, final String messageCode) {
        ModelAndView result;

        result = new ModelAndView("category/administrator/edit");
        result.addObject("category", category);
        result.addObject("message", messageCode);
        return result;
    }
}
