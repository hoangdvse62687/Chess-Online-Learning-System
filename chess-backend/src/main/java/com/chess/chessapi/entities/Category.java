package com.chess.chessapi.entities;

import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "category")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="categoryId",scope = Category.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getCategoryByCourseid",
                procedureName = "get_category_by_courseid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseId",type = Long.class)
                }
        )
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long categoryId;

    @NotNull(message = "Name must not be null")
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "category")
    @JsonIgnore
    private List<CategoryHasCourse> categoryHasCourses;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryHasCourse> getCategoryHasCourses() {
        return categoryHasCourses;
    }

    public void setCategoryHasCourses(List<CategoryHasCourse> categoryHasCourses) {
        this.categoryHasCourses = categoryHasCourses;
    }
}
