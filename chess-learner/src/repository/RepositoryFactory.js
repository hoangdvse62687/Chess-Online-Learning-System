import CourseRepository from '@/repository/CourseRepository'
import UserRepository from '@/repository/UserRepository'
import LessonRepository from '@/repository/LessonRepository'
import CategoryRepository from '@/repository/CategoryRepository'
import LearningLogRepository from '@/repository/LearningLogRepository'
import GameHistoryRepository from '@/repository/GameHistoryRepository'

const repositories = {
  course: CourseRepository,
  user: UserRepository,
  lesson: LessonRepository,
  category: CategoryRepository,
  learning: LearningLogRepository,
  gameHistory: GameHistoryRepository
}

export const RepositoryFactory = {
  get: name => repositories[name]
}
