import Repository from '@/repository/Repository'
const resource = '/reports'

export default{
    getEnrollment(year){
        const data = {
            params: {
                year : year 
            }
        }
        return Repository.get(`${resource}/enrollment-report`,data)
    },
    getLeanerStatusCourse(page,pageSize,courseName){
        const data = {
            params: {
                page : page ,
                pageSize : pageSize,
                courseName: courseName
            }
        }
        return Repository.get(`${resource}/learner-status-course`,data)
    },
    getUsersRegister(year){
        const data = {
            params: {
                year : year 
            }
        }
        return Repository.get(`${resource}/users-register-report`,data)
    },
    getRateWinnable(year){
        const data = {
            params: {
                year : year 
            }
        }
        return Repository.get(`${resource}/rate-winnable-report`,data)
    },
    getRateLevelWinnable(year){
        const data = {
            params: {
                year : year 
            }
        }
        return Repository.get(`${resource}/rate-winnable-level-report`,data)
    },
    getPublishCourseReport(){
        return Repository.get(`${resource}/publish-course-report`)
    },
    getCourseStatusReport(){
        return Repository.get(`${resource}/course-status-report`)
    }
}