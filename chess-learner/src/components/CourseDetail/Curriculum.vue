<template>
  <v-card v-if="curriculum !== null">
    <v-card-title primary-title>
      <v-layout wrap>
        <v-layout pr-3 align-center>
          <span class="title-curriculum text-black">Giáo trình</span>
        </v-layout>
      </v-layout>
    </v-card-title>
    <v-layout pa-3 wrap justify-center>
      <template v-if="curriculum.length !== 0">
        <v-flex xs10 mb-1>
          <v-progress-linear :value="value" rounded :height="13">
            <template v-slot="{ value }">
              <span class="curriculum-percentage">{{ Math.ceil(value) }}%</span>
            </template>
          </v-progress-linear>
        </v-flex>
        <v-flex xs10 mb-3>
          <v-layout justify-center>
            <span
              class="learning-log-caption"
            >Đã hoàn thành {{learningLog.size}} trên {{curriculum.length}} bài</span>
          </v-layout>
        </v-flex>
      </template>
      <v-flex xs10 v-else>
        <v-layout justify-center>
          <span class="text-grey empty-curriculum">(Khóa học này hiện chưa có bài học nào)</span>
        </v-layout>
      </v-flex>
      <v-flex xs12>
        <v-expansion-panels popout inset focusable>
          <v-expansion-panel v-for="(item,index) in curriculum" :key="index">
            <v-expansion-panel-header disable-icon-rotate>
              <v-layout align-center>
                <v-icon
                  v-if="item.learned === true"
                  color="#21ba45"
                  size="18"
                  class="mr-2"
                >fa-check-circle</v-icon>
                <v-icon v-else size="18" class="mr-2" color="#999999">far fa-circle</v-icon>
                Bài {{index + 1}}: {{item.name}}
                <v-spacer></v-spacer>

                <div class="course-type pr-3">{{ item.lessonTypeName }}</div>
              </v-layout>
            </v-expansion-panel-header>
            <v-expansion-panel-content class="mt-2">
              <v-layout>
                <v-flex xs11 mr-2>{{item.description}}</v-flex>
                <v-flex xs1 v-if="courseDetail.enrolled">
                  <v-layout justify-end>
                    <v-btn
                      icon
                      text
                      min-width="20"
                      min-height="20"
                      width="40"
                      height="40"
                      color="success"
                      :to="`/learning/course/${courseDetail.courseId}/lesson/${item.lessonId}`"
                    >
                      <v-icon :size="16">fa-play</v-icon>
                    </v-btn>
                  </v-layout>
                </v-flex>
              </v-layout>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-flex>
    </v-layout>
  </v-card>
</template>
<script>
export default {
  props: {
    courseDetail: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      learningLog: null,
      curriculum: null,
      value: 0
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      this.sortLessonViewModel()
      this.curriculum = this.courseDetail.lessonViewModels
      this.learningLog = new Map()
      if (this.courseDetail.enrolled) {
        this.getLearningLog()
      }
      this.value = (this.learningLog.size / this.curriculum.length) * 100
      this.value = Math.ceil(this.value)
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    getLearningLog() {
      this.courseDetail.listLearningLogLessonIds.forEach(el => {
        if (el.passed) {
          this.learningLog.set(el.learningLogId, true)
        }
      })
      this.curriculum.forEach(el => {
        el.learned = false
        if (this.learningLog.get(el.lessonId) === true) {
          el.learned = true
        }
      })
    },
    sortLessonViewModel() {
      this.courseDetail.lessonViewModels.sort((a, b) =>
        a.lessonOrdered > b.lessonOrdered ? 1 : -1
      )
    }
  }
}
</script>

<style scoped>
.course-title {
  font-size: 16px;
  color: #464646;
  font-weight: 600;
}
.course-type {
  flex: none !important;
  font-weight: 400;
  color: #9b9b9b;
  font-size: 13px;
}
.total-lesson {
  color: #276fbd;
}
>>> .v-expansion-panel__header {
  border-radius: 7px;
  border: 1px solid #dedfe0;
}
>>> .v-expansion-panel__container--active .v-expansion-panel__header {
  border-radius: 0px;
  border: none;
}
>>> .theme--light.v-expansion-panel .v-expansion-panel__container {
  border-top: none;
}
.title-curriculum {
  font-size: 18px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.3px;
}
.total-lesson-title,
.total-lesson,
>>> .v-expansion-panel-content__wrap {
  font-size: 14px;
}
.curriculum-percentage {
  font-size: 11px;
  color: white;
  font-weight: 600;
}
.learning-log-caption {
  font-weight: 600;
  font-size: 13px;
}
.empty-curriculum {
  font-size: 13px;
}
</style>
