
package service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;

import domain.Category;
import services.CategoryService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryTest extends AbstractTest {

	@Autowired
	private CategoryService categoryService;


	/*
	 * Testing functional requirement : 35.2 An actor who is authenticated as an administrator must be able to manage the catalogue of categories
	 * Positive: A admin create a category
	 * Negative: A admin tries to create a category with invalid data
	 * Sentence coverage: 95%
	 * Data coverage: 66%
	 */
	@Test
	public void createCategoryDriver() {
		final Object testingData[][] = {
			{
				"prueba", "prueba", "admin1", null
			}, {
				"prueba", "", "admin1", ValidationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.createCategoryTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void createCategoryTemplate(final String nameEn, final String nameEs, final String user, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			Category category = this.categoryService.create();
			category.setNameEn(nameEn);
			category.setNameEs(nameEs);
			final DataBinder binding = new DataBinder(new Category());
			category = this.categoryService.reconstruct(category, binding.getBindingResult());
			this.categoryService.save(category);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * Testing functional requirement : 35.2 An actor who is authenticated as an administrator must be able to manage the catalogue of categories
	 * Positive: A admin edit a category
	 * Negative: A admin tries to create a category with invalids data
	 * Sentence coverage: 95%
	 * Data coverage: 66%
	 */
	@Test
	public void editCategoryDriver() {
		final Object testingData[][] = {
			{
				"category1", "prueba", "pruebass", "admin1", null
			}, {
				"category2", "prueba", "", "admin1", ValidationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.editCategoryTemplate(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void editCategoryTemplate(final int entityId, final String nameEs, final String nameEn, final String user, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			final Category category = this.categoryService.findOne(entityId);
			Category n = new Category();
			n.setId(category.getId());
			n.setNameEn(nameEn);
			n.setNameEs(nameEs);
			final DataBinder binding = new DataBinder(new Category());
			n = this.categoryService.reconstruct(n, binding.getBindingResult());
			this.categoryService.save(n);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	/*
	 * Testing functional requirement : 35.2 An actor who is authenticated as an administrator must be able to manage the catalogue of categories
	 * Positive: An admin delete a category
	 * Negative: An reader try delete a category
	 * Sentence coverage: 98%
	 * Data coverage: Not applicable
	 */
	@Test
	public void deleteCategoryDriver() {
		final Object testingData[][] = {
			{
				"category1", "admin1", null
			}, {
				"category1", "reader1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.deleteCategoryTemplate(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void deleteCategoryTemplate(final int entityId, final String user, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			final Category category = this.categoryService.findOne(entityId);
			this.categoryService.delete(category);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
