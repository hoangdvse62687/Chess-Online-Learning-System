import Repository from '@/repository/Repository'

const resource = '/categories'

export default {
  getCategories() {
    return Repository.get(`${resource}`)
  }
}
