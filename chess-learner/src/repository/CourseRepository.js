import Repository from '@/repository/Repository'

const resource = '/courses'
const review = '/review'

export default {
  getCoursesPagination(searchParams) {
    const data = {
      params: searchParams
    }
    return Repository.get(`${resource}`, data)
  },
  getCoursesPaginationsByUserId(searchParams) {
    const data = {
      params: searchParams
    }
    return Repository.get(`${resource}/userid`, data)
  },
  getCoursesPaginationByCategoryId(searchParams) {
    const data = {
      params: searchParams
    }
    return Repository.get(`${resource}/category-id`, data)
  },
  getCoursesSuggestion(searchParams) {
    const data = {
      params: searchParams
    }
    return Repository.get(`${resource}/suggestion`, data)
  },
  getCommonSuggestion(searchParams) {
    const data = {
      params: searchParams
    }
    return Repository.get(`${resource}/common-suggestion`, data)
  },
  getById(courseId) {
    return Repository.get(`${resource}/${courseId}`)
  },
  enrollCourse(courseId) {
    const data = {
      courseId: courseId
    }
    return Repository.post(`${resource}/enroll`, data)
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
  createReview(newReview) {
    const data = newReview
    return Repository.post(`${resource}${review}`, data)
  },
  updateReview(updatedReview) {
    const data = updatedReview
    return Repository.put(`${resource}${review}`, data)
  },
  removeReview(reviewId) {
    return Repository.delete(`${resource}${review}/${reviewId}`)
  }
}
