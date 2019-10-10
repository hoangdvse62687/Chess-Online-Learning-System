import Repository from '@/repository/Repository'

const resource = '/lessons'

export default {
  getById(lessonId) {
    return Repository.get(`${resource}/${lessonId}`)
  }
}
