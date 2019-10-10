import Repository from '@/repository/Repository'

const resource = '/users'

export default {
  getCurrentUserDetail() {
    return Repository.get(`${resource}/current-user-detail`)
  },
  signUpNewAccount(newUser) {
    return Repository.put(`${resource}/register`, newUser)
  },
  updateProfile(newUser) {
    return Repository.put(`${resource}/profile`, newUser)
  },
  getById(userId) {
    return Repository.get(`${resource}/`+userId)
  }
}
