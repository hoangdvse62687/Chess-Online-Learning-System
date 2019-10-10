package com.chess.chessapi.services;

import com.chess.chessapi.entities.Category;
import com.chess.chessapi.models.CategoryRedis;
import com.chess.chessapi.repositories.CategoryRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.viewmodels.CategoryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RedisCategoryService redisCategoryService;
    //public method
    public List<Category> getAllCategory(){
        Map<Long, CategoryRedis> data = this.redisCategoryService.findAll();

        if(data.size() == 0){
            List<Category> categoryList = this.categoryRepository.findAll();
            categoryList.forEach(item -> {
                CategoryRedis categoryRedis = new CategoryRedis();
                categoryRedis.setCategoryId(item.getCategoryId());
                categoryRedis.setName(item.getName());
                this.redisCategoryService.save(categoryRedis);
                data.put(categoryRedis.getCategoryId(),categoryRedis);
            });
        }

        List<Category> categories = new ArrayList<>();
        data.forEach((key,value) -> {
            Category category = new Category();
            category.setCategoryId(value.getCategoryId());
            category.setName(value.getName());
            categories.add(category);
        });
        return categories;
    }

    public Category getCategoryById(long categoryId){
        CategoryRedis categoryRedis = this.redisCategoryService.find(categoryId);
        if(categoryRedis == null){
            return this.categoryRepository.findById(categoryId).get();
        }

        Category category = new Category();
        category.setCategoryId(categoryRedis.getCategoryId());
        category.setName(categoryRedis.getName());
        return category;
    }

    public List<CategoryViewModel> getListCategoryIdsByCourseId(long courseId){
        //getting category by courseid
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCategoryByCourseid");
        query.setParameter("courseId",courseId);

        query.execute();
        //end getting category by courseid
        return ManualCastUtils.castListObjectToCategoryIdFromGetCategoryByCourseId(query.getResultList());
    }

    public long create(CategoryViewModel categoryViewModel){
        Category category = new Category();
        category.setName(categoryViewModel.getName());
        Category savedCategory = this.categoryRepository.save(category);

        CategoryRedis categoryRedis = new CategoryRedis();
        categoryRedis.setCategoryId(savedCategory.getCategoryId());
        categoryRedis.setName(savedCategory.getName());
        this.redisCategoryService.save(categoryRedis);

        return savedCategory.getCategoryId();
    }

    public void removeCategory(Category category){
        this.redisCategoryService.deleteById(category.getCategoryId());
        this.categoryRepository.delete(category);
    }

    public void update(CategoryViewModel category){
        CategoryRedis categoryRedis = new CategoryRedis();
        categoryRedis.setCategoryId(category.getCategoryId());
        categoryRedis.setName(category.getName());
        this.redisCategoryService.update(categoryRedis);

        this.categoryRepository.update(category.getCategoryId(),category.getName());
    }

    public boolean isExist(long categoryId){
        return this.categoryRepository.existsById(categoryId);
    }
    //end public method
}
