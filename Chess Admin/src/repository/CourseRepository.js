import Repository from '@/repository/Repository'
import router from '../router'

const resource = '/courses'

export default {
  getCoursesPagination(page, pageSize) {
    const data = {
      params: {
        page: page,
        pageSize: pageSize
      }
    }

    return Repository.get(`${resource}`, data)
  },
  getById(courseId) {
    return Repository.get(`${resource}/`+courseId)
  },
  getCourseByCategoryId(categoryId, page, pageSize) {
    const data = {
      params: {
        categoryId: categoryId,
        page: page,
        pageSize: pageSize
      }
    }
    return Repository.get(
      `${resource}/category-id`,
      data
    )
  },
  createCourse(data) {
    return Repository.post(`${resource}`, data)
  },
  updateCourse(data) {
    return Repository.put(`${resource}`, data)
  },
  updateCourseStatus(courseId, statusId) {
    const data = {
      courseId: courseId,
      statusId: statusId
    }
    return Repository.put(`${resource}/status`, data)
  },
  getCoursesPagination(page, pageSize,nameCourse,statusId) {
    const data = {
      params: {
        page: page,
        pageSize: pageSize,
        nameCourse: nameCourse ? nameCourse : '',
        statusId: statusId ? statusId : ''
      }
    }
    return Repository.get(
      `${resource}`,
      data
    )
  },
  getCoursesPaginationByUserId(page, pageSize,nameCourse,statusId,userId) {
    const data = {
      params: {
        page: page,
        pageSize: pageSize,
        nameCourse: nameCourse ? nameCourse : '',
        statusId: statusId ? statusId : '',
        userId: userId
      }
    }
    return Repository.get(
      `${resource}/userid`,
      data
    )
  },
  publishCourse(courseId) {
    const data = {
      courseId: courseId
    }
    return Repository.put(`${resource}/publish`, data)
  },
  updateCourseStatus(courseId,reasonReject,statusId){
    const data = {
      courseId: courseId,
      reasonReject: reasonReject,
      statusId: statusId
    }
    return Repository.put(`${resource}/status`, data)
  },
  changeStatusCourseToDrafting(courseId){
    const data = {
      courseId: courseId
    }
    return Repository.put(`${resource}/change-status-to-drafting`, data)
  },
  getReviewPagination(courseId, page, pageSize) {
    const data = {
      params: {
        courseId: courseId,
        page: page,
        pageSize: pageSize
      }
    }
    return Repository.get(`${resource}/reviews`, data)
  },
  getCourseOverview(courseId) {
    const data = {
      params: {
        courseId: courseId
      }
    }
    return Repository.get(`${resource}/overview`, data)
  },
}
