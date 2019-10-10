package com.chess.chessapi.services;

import com.chess.chessapi.constants.EloRatingLevel;
import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.Course;
import com.chess.chessapi.models.*;
import com.chess.chessapi.viewmodels.CategoryViewModel;
import com.chess.chessapi.viewmodels.CoursePaginationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SuggestionAlgorithmService {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RedisCourseSuggestionService redisCourseSuggestionService;

    @Autowired
    private RedisCommonCourseItemFilterService redisCommonCourseItemFilterService;

    private final int QUANTITY_USED_USER_FILTER = 5;

    public void executeItemFilterSuggestionAlgorithm(){
        List<Long> userIds = this.userService.getAllListLearnerIds();
        if(userIds != null){
            for (Long userId:
                 userIds) {
                Course lastCourse = this.courseService.getLastCourseEnrollByUserId(userId);
                if(lastCourse != null){
                    int userElo = this.userService.getPointByUserId(userId);
                    this.executeItemFilterSuggestionAlgorithm(lastCourse,userElo,userId);
                }
            }
        }
    }

    public void executeCommonItemFilterSuggestionAlgorithm(long courseId){
        List<CoursePaginationViewModel> courses = courseService.getListCourseSuggestionByEloIdAndStatusId(Status.COURSE_STATUS_PUBLISHED,0);
        if(courses != null && !courses.isEmpty()){
            //finding index of course enrolling
            int index = 0;
            for (int i = 0; i < courses.size();i++) {
                if(courses.get(i).getCourseId() == courseId){
                    index = i;
                    break;
                }
            }

            CoursePaginationViewModel currentCourse = courses.get(index);
            List<CourseUserFilterData> results = new ArrayList<>();
            for(int i = 0; i < courses.size();i++){
                if(i != index){
                    ItemFilterSuggestion B = new ItemFilterSuggestion();
                    B.setRating(courses.get(i).getRating());
                    B.setSameAuthor(courses.get(i).getAuthor().getUserId()
                            == currentCourse.getAuthor().getUserId() ? 1 : 0);
                    B.setSameCategories(this.checkCategory(currentCourse.getListCategorys()
                            ,courses.get(i).getListCategorys()));
                    double result = this.itemFiltering(currentCourse.getRating(),B);
                    results.add(new CourseUserFilterData(courses.get(i).getCourseId(),result));
                }
            }
            Collections.sort(results);
            CommonCourseItemSuggestionRedis commonCourseItemSuggestionRedis = new CommonCourseItemSuggestionRedis();
            commonCourseItemSuggestionRedis.setCourseId(courseId);
            commonCourseItemSuggestionRedis.setCourseItemFilterData(results);
            this.redisCommonCourseItemFilterService.save(commonCourseItemSuggestionRedis);
        }
    }

    public void executeCommonItemFilterSuggestionAlgorithm(){
        List<CoursePaginationViewModel> courses = courseService.getListCourseSuggestionByEloIdAndStatusId(Status.COURSE_STATUS_PUBLISHED,0);
        if(courses != null && !courses.isEmpty()) {
            //finding index of course enrolling
            for (int j = 0; j < courses.size(); j++) {
                CoursePaginationViewModel currentCourse = courses.get(j);
                List<CourseUserFilterData> results = new ArrayList<>();
                for (int i = 0; i < courses.size(); i++) {
                    if (i != j) {
                        double result = this.itemFiltering(currentCourse.getRating(),
                                this.calculaterItemFilterSimiler(courses.get(j),courses.get(i)));
                        results.add(new CourseUserFilterData(courses.get(i).getCourseId(), result));
                    }
                }
                //save
                Collections.sort(results);
                CommonCourseItemSuggestionRedis commonCourseItemSuggestionRedis = new CommonCourseItemSuggestionRedis();
                commonCourseItemSuggestionRedis.setCourseId(currentCourse.getCourseId());
                commonCourseItemSuggestionRedis.setCourseItemFilterData(results);
                this.redisCommonCourseItemFilterService.save(commonCourseItemSuggestionRedis);
            }
        }
    }

    public void executeItemFilterSuggestionAlgorithm(Course lastCourse,int userElo,long userId){
        //int userEloId = EloRatingLevel.getIdByEloRange(userElo);
        //get all course in elo id
        List<CoursePaginationViewModel> courses = courseService.getListCourseSuggestionByEloIdAndStatusId(Status.COURSE_STATUS_PUBLISHED,userId);
        if(courses != null && !courses.isEmpty()){
            //finding index of course enrolling
            int index = 0;
            for (int i = 0; i < courses.size();i++) {
                if(courses.get(i).getCourseId() == lastCourse.getCourseId()){
                    index = i;
                    break;
                }
            }

            CoursePaginationViewModel currentEnrolLingCourse = courses.get(index);
            int counterComment = 0;
            List<CourseUserFilterData> results = new ArrayList<>();
            for(int i = 0; i < courses.size();i++){
                if(i != index){
                    if(this.reviewService.checkIsComment(userId,courses.get(i).getCourseId())){
                        counterComment++;
                    }
                    double result = this.itemFiltering(currentEnrolLingCourse.getRating(),
                            this.calculaterItemFilterSimiler(currentEnrolLingCourse,courses.get(i)));
                    results.add(new CourseUserFilterData(courses.get(i).getCourseId(),result));
                }
            }
            this.saveItemFilterSuggestionOnRedis(userId,results,counterComment);
        }
    }

    public void executeUserFilterSuggestionAlgorithm(){
        SuggestionAlgorithmData suggestionAlgorithmData = new SuggestionAlgorithmData();
        //init data for start algorithm
        suggestionAlgorithmData.setAllUser(this.getListUserIdByRangeElo());

        suggestionAlgorithmData.setAllCourse(this.courseService.getAllListCoursePulishedIds());

        this.getReviews(suggestionAlgorithmData.getAllUser(),suggestionAlgorithmData.getAllCourse());

        //find similar of each user in group
        this.executeCosineSimilarity(suggestionAlgorithmData.getAllUser());

        this.executeUserBasedFiltering(suggestionAlgorithmData.getAllUser(),suggestionAlgorithmData.getAllCourse());

        this.saveUserFilterSuggestionOnRedis(suggestionAlgorithmData.getAllUser());
    }

    private ItemFilterSuggestion calculaterItemFilterSimiler(CoursePaginationViewModel currentCourse,CoursePaginationViewModel difCourse){
        ItemFilterSuggestion B = new ItemFilterSuggestion();
        B.setRating(difCourse.getRating());
        B.setSameAuthor(difCourse.getAuthor().getUserId()
                == currentCourse.getAuthor().getUserId() ? 1 : 0);
        B.setSameCategories(this.checkCategory(currentCourse.getListCategorys()
                ,difCourse.getListCategorys()));
        return B;
    }

    private double checkCategory(List<CategoryViewModel> A,List<CategoryViewModel> B){
        if(A == null || B == null || A.isEmpty()){
            return 0;
        }

        double counterSame = 0;
        double counterA = A.size();
        for (CategoryViewModel bCategory:
             B) {
            for (CategoryViewModel aCategory:
                 A) {
                if(aCategory.getCategoryId() == bCategory.getCategoryId()){
                    counterSame++;
                }
            }
        }

        return counterSame / counterA;
    }

    private void saveItemFilterSuggestionOnRedis(Long userId,List<CourseUserFilterData> data, int counterComment){
        CourseSuggestionRedis courseSuggestionRedis = new CourseSuggestionRedis();
        Collections.sort(data);
        courseSuggestionRedis.setCourseItemFilterData(data);
        courseSuggestionRedis.setUserId(userId);

        CourseSuggestionRedis dataOnRedis = this.redisCourseSuggestionService.find(userId);
        if(dataOnRedis == null){
            this.redisCourseSuggestionService.save(courseSuggestionRedis);
        }else{
            dataOnRedis.setCourseItemFilterData(courseSuggestionRedis.getCourseItemFilterData());
            if(counterComment > QUANTITY_USED_USER_FILTER){
                dataOnRedis.setUsedCourseItemFilterData(false);
            }else{
                dataOnRedis.setUsedCourseItemFilterData(true);
            }
            this.redisCourseSuggestionService.update(dataOnRedis);
        }
    }

    private void saveUserFilterSuggestionOnRedis(List<UserSuggestionAlgorithm> suggestionAlgorithmData){
        suggestionAlgorithmData.forEach((user) -> {
            CourseSuggestionRedis courseSuggestionRedis = new CourseSuggestionRedis();
            courseSuggestionRedis.setUserId(user.getUserId());
            Collections.sort(user.getUserFilterScore());
            courseSuggestionRedis.setCourseUserFilterData(user.getUserFilterScore());

            CourseSuggestionRedis dataOnRedis = this.redisCourseSuggestionService.find(user.getUserId());
            if(dataOnRedis == null){
                this.redisCourseSuggestionService.save(courseSuggestionRedis);
            }else{
                dataOnRedis.setCourseUserFilterData(courseSuggestionRedis.getCourseUserFilterData());
                if(user.getReviewSuggestionAlgorithms().size() > QUANTITY_USED_USER_FILTER){
                    dataOnRedis.setUsedCourseItemFilterData(false);
                }else{
                    dataOnRedis.setUsedCourseItemFilterData(true);
                }
                this.redisCourseSuggestionService.update(dataOnRedis);
            }
        });
    }

    private void executeCosineSimilarity(List<UserSuggestionAlgorithm> userSuggestionAlgorithms){
        for(int i = 0;i < userSuggestionAlgorithms.size();i++){
            for(int j = 0; j < userSuggestionAlgorithms.size();j++){
                if(i != j){
                    double result = this.cosineSimilarity(userSuggestionAlgorithms.get(i).getReviewSuggestionAlgorithms()
                            ,userSuggestionAlgorithms.get(j).getReviewSuggestionAlgorithms());
                    userSuggestionAlgorithms.get(i).getUserSimilarSuggestionAlgorithms()
                            .add(new UserSimilarSuggestionAlgorithm(userSuggestionAlgorithms.get(j).getUserId(),result));
                }
            }
        }
    }

    private void executeUserBasedFiltering(List<UserSuggestionAlgorithm> userSuggestionAlgorithms,List<Long> courseIds){
        for(int i = 0; i < userSuggestionAlgorithms.size();i++){
            for(int j = 0; j < courseIds.size();j++){
                List<Double> listRatingOfUser = new ArrayList<>();
                for (int z = 0; z < userSuggestionAlgorithms.size();z++){
                    if(z != i){
                        listRatingOfUser.add(userSuggestionAlgorithms.get(z).getReviewSuggestionAlgorithms().get(j).getScore());
                    }
                }
                double result = this.userBasedFiltering(listRatingOfUser
                        ,userSuggestionAlgorithms.get(i).getUserSimilarSuggestionAlgorithms());
                userSuggestionAlgorithms.get(i).getUserFilterScore().add(new CourseUserFilterData(courseIds.get(j),result));
            }
        }
    }

    private void getReviews(List<UserSuggestionAlgorithm> userSuggestionAlgorithms,List<Long> courseIds){
        userSuggestionAlgorithms.forEach((user) ->{
            courseIds.forEach((courseId) -> {
                user.getReviewSuggestionAlgorithms().add(new CourseUserFilterData(courseId
                        ,Double.parseDouble(this.reviewService.getRatingByUserIdAndCourseId(user.getUserId(),courseId).toString())));
            });
        });
    }

    private List<UserSuggestionAlgorithm> getListUserIdByRangeElo(){
        List<UserSuggestionAlgorithm> users = new ArrayList<>();
        List<Long> listUsers = this.userService.getAllListLearnerIds();
        listUsers.forEach((item) ->{
            users.add(new UserSuggestionAlgorithm(item));
        });
        return users;
    }

    private double cosineSimilarity(List<CourseUserFilterData> A,List<CourseUserFilterData> B) {
        //A is user a and B is user b
        if (A == null || B == null || A.size() == 0 || B.size() == 0 || A.size() != B.size()) {
            return 0;
        }

        double sumProduct = 0;
        double sumASq = 0;
        double sumBSq = 0;
        for (int i = 0; i < A.size(); i++) {
            sumProduct += A.get(i).getScore()*B.get(i).getScore();
            sumASq += A.get(i).getScore() * A.get(i).getScore();
            sumBSq += B.get(i).getScore() * B.get(i).getScore();
        }
        if (sumASq == 0) {
            sumASq = 1;
        }
        if(sumBSq == 0){
            sumBSq = 1;
        }
        return sumProduct / (Math.sqrt(sumASq) * Math.sqrt(sumBSq));
    }

    private double userBasedFiltering(List<Double> A,List<UserSimilarSuggestionAlgorithm> B){
        //A is rating of user and B is similar of user
        if(A == null || B == null || A.size() == 0 || B.size() == 0 || A.size() != B.size()){
            return 0;
        }
        double sumProduct = 0;
        double sumSimilar = 0;
        for(int i = 0; i < A.size(); i++){
            sumProduct += A.get(i)*B.get(i).getSimilarScore();
            sumSimilar += B.get(i).getSimilarScore();
        }
        if(sumSimilar == 0){
            return 0;
        }
        return sumProduct / sumSimilar;
    }

    private double itemFiltering(Double ratingA,ItemFilterSuggestion B){
        if(B == null){
            return 0;
        }
        double Per = ((B.getSameAuthor() + B.getSameCategories()) + (ratingA * B.getRating()));
        double Der = Math.sqrt(ratingA * ratingA + 3) *
                Math.sqrt(B.getSameAuthor() * B.getSameAuthor() + B.getSameCategories() * B.getSameCategories() + B.getRating() * B.getRating());
        if(Der == 0){
            return 0;
        }
        return Per/Der;
    }
}
