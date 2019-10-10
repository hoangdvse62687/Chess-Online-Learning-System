import Repository from '@/repository/Repository'

const resource = '/users'

export default {
  getUsersPagination(page, pageSize,email,isActive,isReview,role) {
    const data = { 
      params: { 
        page: page, 
        pageSize: pageSize,
        email: email ? email : '',
        isActive: isActive ? isActive : '',
        isReview: isReview ? isReview : '',
        role: role ? role : ''
      } }
    return Repository.get(`${resource}`, data)
  },
  updateStatus(data) {
    return Repository.put(`${resource}/status`, data)
  },
  getCurrentUserDetail() {
    return Repository.get(`${resource}/current-user-detail`)
  },
  getUserById(userId){
    return Repository.get(`${resource}/`+userId)
  },
  updateUserStatus(userId,isActive){
    const data = {
      active: isActive,
      userId: userId
    }
    return Repository.put(`${resource}/status`, data)
  },
  updateProfile(data) {
    return Repository.put(`${resource}/profile`, data)
  },
  // http://cols-be.ml/user/get-current-user-detail

  // bid(id, price) {
  //   const data = { bidId: id, price: price }
  //   return Repository.post(`${resource}`, data)
  // }
}
