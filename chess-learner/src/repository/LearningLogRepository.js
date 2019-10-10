import Repository from '@/repository/Repository'

const resource = '/learning-logs'

export default {
  getLearningLog(courseId) {
    return Repository.get(`${resource}/current-user/${courseId}`)
  },
  createLearningLog(courseId, lessonId, passed) {
    const data = {
      courseId: courseId,
      lessonId: lessonId,
      passed: passed
    }
    return Repository.post(`${resource}`, data)
  }
}
