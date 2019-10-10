package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

public class CourseCreateViewModel {

    @NotNull(message = "Name must not be null")
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    @Length(max = 1000,message = "Description is required not larger than 1000 characters")
    private String description;

    @Min(value =0,message = "Required Elo should equal or larger than 0")
    @Max(value = 5,message = "Required Elo should less than 5")
    private int requiredElo;

    @NotNull(message = "Image must not be null")
    @Length(max = 255,message = "Image must not be larger than 255 characters")
    private String image;

    @NotNull(message = "List category must not be null")
    private List<Long> listCategoryIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Long> getListCategoryIds() {
        return listCategoryIds;
    }

    public void setListCategoryIds(List<Long> listCategoryIds) {
        this.listCategoryIds = listCategoryIds;
    }

    public int getRequiredElo() {
        return requiredElo;
    }

    public void setRequiredElo(int requiredElo) {
        this.requiredElo = requiredElo;
    }
}
