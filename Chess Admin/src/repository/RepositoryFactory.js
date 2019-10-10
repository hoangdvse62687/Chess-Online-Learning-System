import UserRepository from '@/repository/UserRepository'
import CourseRepository from '@/repository/CourseRepository'
import CategoryRepository from '@/repository/CategoryRepository'
import LessonRepository from '@/repository/LessonRepository'
import ReportRepository from '@/repository/ReportRepository'
import NotificationRepository from '@/repository/NotificationRepository'
const repositories = {
  user: UserRepository,
  course: CourseRepository,
  category: CategoryRepository,
  lesson: LessonRepository,
  report: ReportRepository,
  notification: NotificationRepository
}

export const RepositoryFactory = {
  get: name => repositories[name]
}
