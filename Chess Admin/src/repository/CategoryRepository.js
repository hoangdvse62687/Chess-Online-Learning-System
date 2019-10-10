import Repository from '@/repository/Repository'

const resource = '/categories'

export default {
  getCategories() {
    return Repository.get(`${resource}`)
  },
  getCategoryDetail(categoryId) {
    return Repository.get(`${resource}/`+ categoryId)
  },
  createCategory(name) {
    const data = {
      name: name
    }
    return Repository.post(`${resource}`, data)
  },
  updateCategory(categoryId, name) {
    const data = {
      categoryId: categoryId,
      name: name
    }
    return Repository.put(`${resource}`, data)
  },
  removeCategory(categoryId) {
    return Repository.delete(`${resource}/`+categoryId)
  }

  // bid(id, price) {
  //   const data = { bidId: id, price: price }
  //   return Repository.post(`${resource}`, data)
  // }

  // updateCourseStatus(courseId, statusId) {
  //   const data = {
  //     courseId: courseId,
  //     statusId: statusId
  //   }
  //   return Repository.put(
  //     `${resource}/update-course-status`,
  //     {},
  //     { params: data }
  //   )
  // }
}
