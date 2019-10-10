<template>
  <v-layout v-if="courseDetail.courseId" wrap>
    <v-flex xs12>
      <v-parallax :src="courseDetail.image" height="300">
        <v-layout>
          <v-container px-0 py-4>
            <v-layout fill-height wrap>
              <v-flex xs12>
                <v-breadcrumbs :items="breadcrumbs" class="pa-0">
                  <template v-slot:divider>
                    <v-icon class="white--text">mdi-chevron-right</v-icon>
                  </template>
                </v-breadcrumbs>
              </v-flex>
              <v-flex xs12>
                <span class="course-title">{{ courseDetail.name }}</span>
              </v-flex>
              <v-flex xs12>
                <v-layout align-end fill-height wrap>
                  <v-flex xs4>
                    <v-layout fill-height align-center>
                      <v-avatar :size="36">
                        <img :src="courseDetail.author.avatar" />
                      </v-avatar>
                      <span class="white--text ml-2 subheading">{{ courseDetail.author.fullName }}</span>
                    </v-layout>
                  </v-flex>
                  <v-flex xs4>
                    <v-layout fill-height align-end>
                      <v-flex xs6>
                        <v-layout align-center fill-height justify-end>
                          <img :src="imageIcon.classMates" height="36" />
                          <span
                            class="ml-2 course-member"
                          >{{ courseDetail.userEnrolleds.length }} Học viên</span>
                        </v-layout>
                      </v-flex>
                      <v-flex xs6>
                        <v-layout align-center fill-height justify-end>
                          <img :src="imageIcon.lessons" height="36" />
                          <span
                            class="ml-2 course-total-lesson"
                          >{{ courseDetail.totalLesson }} Bài học</span>
                        </v-layout>
                      </v-flex>
                    </v-layout>
                  </v-flex>
                </v-layout>
              </v-flex>
            </v-layout>
          </v-container>
        </v-layout>
      </v-parallax>
    </v-flex>
    <v-flex xs12>
      <v-container px-0>
        <v-layout wrap>
          <v-flex id="content-container" xs8>
            <v-layout wrap>
              <v-flex xs12 class="title-direction">
                <v-card>
                  <v-layout justify-space-around pt-1>
                    <div
                      v-for="(item, index) in subMenu"
                      :key="index"
                      class="sub-menu text-black py-2"
                      @click="$vuetify.goTo(item.target, options), highLightMenuTab($event)"
                    >{{ item.title }}</div>
                  </v-layout>
                </v-card>
              </v-flex>
              <v-flex id="about-course" xs12 mt-3>
                <About :description="courseDetail.description" />
              </v-flex>
              <v-flex id="curriculum-course" xs12 mt-3>
                <Curriculum :courseDetail="courseDetail" />
              </v-flex>
              <v-flex id="author-course" xs12 mt-3>
                <InstructorInfo :author="courseDetail.author" :courseDetail="courseDetail" />
              </v-flex>
              <v-flex id="review-course" xs12 mt-3>
                <Review :enrolled="courseDetail.enrolled" />
              </v-flex>
            </v-layout>
          </v-flex>
          <v-flex xs12 mt-3>
            <CourseSuggestion />
          </v-flex>
          <div id="enrol-course">
            <v-card class="pa-1">
              <v-layout wrap>
                <v-flex xs12>
                  <v-img :src="courseDetail.image" height="190"></v-img>
                </v-flex>
                <v-flex xs12 pa-3>
                  <v-layout wrap>
                    <v-flex xs12 mb-1>
                      <v-layout>
                        <span class="course-point">Cấp độ</span>
                        <v-spacer></v-spacer>
                        <span class="course-point">{{ courseDetail.requiredEloName }}</span>
                      </v-layout>
                    </v-flex>
                    <v-flex xs12 mb-1>
                      <v-layout>
                        <span class="course-point">Điểm yêu cầu</span>
                        <v-spacer></v-spacer>
                        <span class="course-point">{{ courseDetail.requiredEloNumber }}</span>
                      </v-layout>
                    </v-flex>
                    <v-flex xs12 mb-2>
                      <v-layout>
                        <span class="course-point">Giảng viên</span>
                        <v-spacer></v-spacer>
                        <span class="course-point">{{ courseDetail.author.fullName }}</span>
                      </v-layout>
                    </v-flex>
                    <v-flex v-if="user != null" x12>
                      <v-layout justify-end>
                        <v-btn
                          v-if="courseDetail.enrolled"
                          color="info"
                          class="ma-0 white--text"
                          @click="goToLearningPage()"
                        >Bắt đầu học</v-btn>
                        <v-btn
                          v-else
                          color="error"
                          class="ma-0"
                          @click="showConfirmEnrolCourse()"
                        >Đăng ký khóa học</v-btn>
                      </v-layout>
                    </v-flex>
                  </v-layout>
                </v-flex>
              </v-layout>
            </v-card>
          </div>
        </v-layout>
      </v-container>
    </v-flex>
  </v-layout>
</template>
<script>
import { mapState } from 'vuex'
import Review from '@/components/CourseDetail/Review'
import InstructorInfo from '@/components/CourseDetail/InstructorInfo'
import Curriculum from '@/components/CourseDetail/Curriculum'
import About from '@/components/CourseDetail/About'
import CourseSuggestion from '@/components/CourseDetail/CourseSuggestion'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')
const userRepository = RepositoryFactory.get('user')
export default {
  components: {
    Review,
    InstructorInfo,
    Curriculum,
    About,
    CourseSuggestion
  },
  data() {
    return {
      courseDetail: {},
      breadcrumbs: [
        {
          text: 'Trang chủ',
          disabled: false,
          href: '/'
        },
        {
          text: 'Khóa học',
          disabled: false,
          href: '/course'
        },
        {
          text: '',
          disabled: true
        }
      ],
      courseId: this.$route.params.courseId,
      subMenu: [
        {
          title: 'Thông tin chung',
          target: '#about-course'
        },
        {
          title: 'Giáo trình',
          target: '#curriculum-course'
        },
        {
          title: 'Giảng viên',
          target: '#author-course'
        },
        {
          title: 'Đánh giá',
          target: '#review-course'
        }
      ],
      imageIcon: {
        classMates: require('@/assets/images/classmates.png'),
        lessons: require('@/assets/images/lessons.png')
      }
    }
  },
  computed: {
    ...mapState({ user: state => state.user }),
    options() {
      return {
        duration: 900,
        offset: 0
      }
    },
    target() {
      const value = document.getElementById('course')
      return value
    }
  },
  watch: {
    $route(to, from) {
      if (to.path !== from.path) {
        location.reload()
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  updated() {
    this.setLayoutForEnrolDialog()
    this.setEventScroll()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      await this.getCourseById()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    setEventScroll() {
      window.addEventListener('scroll', function() {
        const scroll = this.scrollY
        const divEnrolCourse = document.getElementById('enrol-course')
        if (divEnrolCourse !== null) {
          if (scroll >= 368) {
            divEnrolCourse.style.top = '0px'
          } else if (scroll >= 205) {
            divEnrolCourse.style.top = 368 - scroll + 'px'
          } else {
            divEnrolCourse.style.top = '162px'
          }
        }
      })
    },
    highLightMenuTab(event) {
      let divTarget = event.srcElement
      let arr = document.getElementsByClassName('sub-menu')
      if (!this.isEmpty(arr)) {
        Array.prototype.forEach.call(arr, function(sub) {
          sub.classList.remove('active')
        })
      }
      divTarget.classList.add('active')
    },

    async getCourseById() {
      const { data } = await courseRepository.getById(this.courseId)
      this.courseDetail = data.data
      console.log(this.courseDetail.courseId)
      this.formatListCourse()
      this.breadcrumbs[
        this.breadcrumbs.length - 1
      ].text = this.courseDetail.name
      if (this.courseDetail.lessonViewModels != null) {
        this.getLessonType()
      }
    },
    formatListCourse() {
      this.courseDetail.requiredEloName = this.getEloName(
        this.courseDetail.requiredElo
      )
      this.courseDetail.requiredEloNumber =
        600 + this.courseDetail.requiredElo * 200
    },
    showConfirmEnrolCourse() {
      this.$swal({
        title: 'Xác nhận',
        html: `Bạn có chắc chắn muốn đăng ký khóa học này?`,
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
      }).then(result => {
        if (result.value) {
          this.enrollCourse()
        }
      })
    },
    async enrollCourse() {
      if (this.courseDetail.requiredEloNumber <= this.$store.state.user.point) {
        const { data } = await courseRepository.enrollCourse(
          this.courseDetail.courseId
        )
        if (data.data) {
          this.courseDetail.enrolled = true
          this.$swal({
            type: 'success',
            title: 'Đăng ký thành công.',
            text:
              'Chúc mừng bạn đã đăng ký thành công. Chào mừng bạn đến với khóa học này.',
            confirmButtonText: 'Xác nhận'
          })
        }
      } else {
        this.$swal({
          type: 'error',
          title: 'Không thể đăng ký.',
          text:
            'Bạn chưa điều kiện để đăng ký khóa học này. Xin thử lại các khóa học khác!',
          confirmButtonText: 'Xác nhận'
        })
      }
    },
    goToLearningPage() {
      if (this.courseDetail.lessonViewModels.length === 0) {
        this.$swal({
          type: 'info',
          text:
            'Khóa học này hiện chưa có bài học nào. Xin vui lòng quay lại sau.',
          confirmButtonText: 'Xác nhận'
        })
      } else {
        const firstLessonId = this.courseDetail.lessonViewModels[0].lessonId
        this.$router.push(
          `/learning/course/${this.courseDetail.courseId}/lesson/${firstLessonId}`
        )
      }
    },
    getLessonType() {
      this.courseDetail.lessonViewModels.forEach(element => {
        element.lessonTypeName = this.getLessonTypeName(element.lessonType)
      })
    },
    setLayoutForEnrolDialog() {
      const width = document.getElementsByClassName('container')[0].offsetWidth
      document.getElementById('enrol-course').style.width =
        width * 0.333333333333 + 'px'

      const widthContent = document.getElementById('content-container')
        .offsetWidth

      document.getElementById('enrol-course').style.marginLeft =
        widthContent + 16 + 'px'
    }
  }
}
</script>

<style scoped>
>>> .v-parallax__image {
  filter: blur(8px);
  opacity: 0.1;
}
>>> .v-parallax__content {
  background: rgba(0, 0, 0, 0.35);
}
.v-icon {
  font-size: 32px;
}
>>> .v-breadcrumbs__item {
  color: white !important;
}
#enrol-course > div {
  border-radius: 7px;
}
#enrol-course {
  position: fixed;
  margin-left: 50%;
  width: 33.33333333333333%;
  border-radius: 7px;
  top: 162px;
}
.course-point {
  color: #4d4d4d;
  font-size: 14px;
  font-weight: 600;
}
.course-title {
  font-size: 32px;
  font-weight: 600;
  line-height: 1.25;
  letter-spacing: 0.7px;
}
.v-card {
  border-radius: 4px;
}
.sub-menu {
  font-size: 14px;
  font-weight: 600;
  border-bottom: 3px solid transparent;
}
.sub-menu:not(.active):hover {
  border-bottom-color: #393b41;
  cursor: pointer;
}
.title-direction {
  position: sticky;
  top: 0px;
  z-index: 4;
}
.btn-learn {
  font-weight: 600;
}
.active {
  border-bottom-color: #393b41;
}
.course-total-lesson,
.course-member {
  font-size: 14px;
}
</style>
