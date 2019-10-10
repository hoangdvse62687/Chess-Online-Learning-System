<template>
  <v-app>
    <v-container px-0 v-if="lessonDetails.lessonType === 2">
      <v-layout wrap>
        <v-flex xs12 mb-4>
          <v-breadcrumbs :items="breadcrumbs" class="py-0">
            <template v-slot:divider>
              <v-icon>mdi-chevron-right</v-icon>
            </template>
          </v-breadcrumbs>
        </v-flex>
        <v-layout>
          <v-flex xs6 xl8 offset-xs1 offset-xl0 mr-5>
            <chessboard :status="'viewOnly'" :fen="currentFen" :move="move" @onMove="showInfo" />
          </v-flex>
          <v-flex xs4 class="direction-side">
            <v-layout column>
              <v-flex>
                <v-layout>
                  <v-flex xs10>
                    <v-layout align-center>
                      <v-icon class="mr-2" :size="20" color="black">fa-bars</v-icon>
                      <div class="lesson-name text-truncate">{{lessonDetails.name}}</div>
                    </v-layout>
                  </v-flex>
                  <v-flex xs2>
                    <v-layout justify-end align-center fill-height>
                      <v-tooltip bottom>
                        <template v-slot:activator="{ on }">
                          <v-btn
                            color="#e0e1e2 elevation-0"
                            class="pa-0"
                            min-width="36"
                            text
                            v-on="on"
                            @click="backToCourseDetail()"
                          >
                            <v-icon :size="20" color="#8b8b8c">fa-sign-out-alt</v-icon>
                          </v-btn>
                        </template>
                        <span>Quay trở lại</span>
                      </v-tooltip>
                    </v-layout>
                  </v-flex>
                </v-layout>
              </v-flex>
              <v-flex class="move-history mb-2">
                <v-card-title class="pa-0">
                  <span class="title-table">Nước đi</span>
                </v-card-title>
                <div class="move-history-content">
                  <div v-for="(moved1, index) in moveHistory" :key="index">
                    <template v-if="moved1.depth === 1">
                      <div class="index">{{ moved1.index }}</div>
                      <div
                        v-if="moved1.whiteMove"
                        :id="moved1.whiteMove.id"
                        :preId="moved1.whiteMove.preId"
                        :nextId="moved1.whiteMove.nextId"
                        :preFen="moved1.whiteMove.preFen"
                        :move="moved1.whiteMove.moveDirection"
                        :class="moved1.whiteMove.class"
                        :depth="moved1.depth"
                        @click="loadFen(null, $event, moved1.whiteMove.content)"
                      >{{ moved1.whiteMove.move }}</div>

                      <div
                        v-if="moved1.blackMove"
                        :id="moved1.blackMove.id"
                        :preId="moved1.blackMove.preId"
                        :nextId="moved1.blackMove.nextId"
                        :class="moved1.blackMove.class"
                        :preFen="moved1.blackMove.preFen"
                        :move="moved1.blackMove.moveDirection"
                        :depth="moved1.depth"
                        @click="loadFen(null, $event, moved1.blackMove.content)"
                      >{{ moved1.blackMove.move }}</div>
                    </template>
                    <div v-if="moved1.depth === 2" class="depth_2">
                      <template v-for="(moved2, index2) in moved1.moveHistory">
                        <div v-if="moved2.depth === 2" :key="index2*2">
                          <div class="index">{{ moved2.index }}</div>
                          <div
                            v-if="moved2.whiteMove"
                            :id="moved2.whiteMove.id"
                            :preId="moved2.whiteMove.preId"
                            :nextId="moved2.whiteMove.nextId"
                            :class="moved2.whiteMove.class"
                            :preFen="moved2.whiteMove.preFen"
                            :move="moved2.whiteMove.moveDirection"
                            :depth="moved2.depth"
                            @click="
                              loadFen(null, $event, moved2.whiteMove.content)
                            "
                          >{{ moved2.whiteMove.move }}</div>
                          <div
                            v-if="moved2.blackMove"
                            :id="moved2.blackMove.id"
                            :preId="moved2.blackMove.preId"
                            :nextId="moved2.blackMove.nextId"
                            :class="moved2.blackMove.class"
                            :preFen="moved2.blackMove.preFen"
                            :move="moved2.blackMove.moveDirection"
                            :depth="moved2.depth"
                            @click="
                              loadFen(null, $event, moved2.blackMove.content)
                            "
                          >{{ moved2.blackMove.move }}</div>
                        </div>
                        <div v-if="moved2.depth === 3" :key="index2*2+1" class="depth_3">
                          <template v-for="(moved3, index3) in moved2.moveHistory">
                            <div :key="index3*3" class="index">{{ moved3.index }}</div>
                            <div
                              v-if="moved3.whiteMove"
                              :id="moved3.whiteMove.id"
                              :key="index3*3+1"
                              :preId="moved3.whiteMove.preId"
                              :nextId="moved3.whiteMove.nextId"
                              :class="moved3.whiteMove.class"
                              :preFen="moved3.whiteMove.preFen"
                              :move="moved3.whiteMove.moveDirection"
                              :depth="moved3.depth"
                              @click="
                                loadFen(null, $event, moved3.whiteMove.content)
                              "
                            >{{ moved3.whiteMove.move }}</div>
                            <div
                              v-if="moved3.blackMove"
                              :id="moved3.blackMove.id"
                              :key="index3 *3+2"
                              :preId="moved3.blackMove.preId"
                              :nextId="moved3.blackMove.nextId"
                              :class="moved3.blackMove.class"
                              :preFen="moved3.blackMove.preFen"
                              :move="moved3.blackMove.moveDirection"
                              :depth="moved3.depth"
                              @click="
                                loadFen(null, $event, moved3.blackMove.content)
                              "
                            >{{ moved3.blackMove.move }}</div>
                          </template>
                        </div>
                      </template>
                    </div>
                  </div>
                </div>
              </v-flex>
              <v-flex mb-2 class="move-history-direction">
                <v-layout>
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn text class="extra-button" @click="turnToFirstMove()" v-on="on">
                        <v-icon>fa-fast-backward</v-icon>
                      </v-btn>
                    </template>
                    <span>Về bước đầu tiên</span>
                  </v-tooltip>

                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn text class="main-button" @click="turnToPreviousMove()" v-on="on">
                        <v-icon>fa-backward</v-icon>
                      </v-btn>
                    </template>
                    <span>Bước trước</span>
                  </v-tooltip>
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn text class="main-button" @click="turnToNextMove()" v-on="on">
                        <v-icon>fa-forward</v-icon>
                      </v-btn>
                    </template>
                    <span>Bước tiếp theo</span>
                  </v-tooltip>
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn text class="extra-button" @click="turnToLastMove()" v-on="on">
                        <v-icon>fa-fast-forward</v-icon>
                      </v-btn>
                    </template>
                    <span>Bước cuối cùng</span>
                  </v-tooltip>
                </v-layout>
              </v-flex>
              <v-flex class="lesson-area" mb-2>
                <v-card-title class="pa-0">
                  <span class="title-table">Nội dung</span>
                </v-card-title>
                <div class="lesson-content">
                  <v-card-text v-if="lessonDetails.lessonType == 2">{{ stepContent }}</v-card-text>
                </div>
              </v-flex>
              <v-flex class="lesson-direction">
                <v-layout justify-space-between>
                  <v-tooltip top>
                    <template v-slot:activator="{ on }">
                      <v-btn
                        class="font-weight-bold"
                        :disabled="statusPreviousLesson"
                        @click="changeLesson(-1, false)"
                        v-on="on"
                      >
                        <v-icon class="mr-2">fa-angle-left</v-icon>Bài trước
                      </v-btn>
                    </template>
                    <span>Về lại bài trước</span>
                  </v-tooltip>
                  <v-tooltip top v-if="!statusNextLesson">
                    <template v-slot:activator="{ on }">
                      <v-btn
                        class="font-weight-bold"
                        :disabled="statusNextLesson"
                        @click="changeLesson(1, true)"
                        v-on="on"
                      >
                        Tiếp tục
                        <v-icon class="ml-2">fa-angle-right</v-icon>
                      </v-btn>
                    </template>
                    <span>Tới bài tiếp theo</span>
                  </v-tooltip>
                  <v-tooltip top v-else>
                    <template v-slot:activator="{ on }">
                      <v-btn @click="finishCourse(true)" class="font-weight-bold">Hoàn thành</v-btn>
                    </template>
                    <span>Hoàn tất khóa học</span>
                  </v-tooltip>
                </v-layout>
              </v-flex>
            </v-layout>
          </v-flex>
        </v-layout>
      </v-layout>
    </v-container>
    <v-container fill-height v-if="lessonDetails.lessonType === 3">
      <v-layout fill-height wrap class="content-theory-container">
        <v-flex xs12 mb-4>
          <v-breadcrumbs :items="breadcrumbs" class="py-0">
            <template v-slot:divider>
              <v-icon>mdi-chevron-right</v-icon>
            </template>
          </v-breadcrumbs>
        </v-flex>
        <v-flex xs12 mb-3>
          <v-card class="pa-3">
            <span class="content-theory" v-html="theoryContent"></span>
          </v-card>
        </v-flex>
        <v-flex xs12>
          <v-layout justify-center>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  class="font-weight-bold mr-3"
                  :disabled="statusPreviousLesson"
                  @click="changeLesson(-1, false)"
                  v-on="on"
                >
                  <v-icon class="mr-2">fa-angle-left</v-icon>Bài trước
                </v-btn>
              </template>
              <span>Quay trở lại</span>
            </v-tooltip>
            <v-tooltip top v-if="!statusNextLesson">
              <template v-slot:activator="{ on }">
                <v-btn
                  class="font-weight-bold"
                  :disabled="statusNextLesson"
                  @click="changeLesson(1, true)"
                  v-on="on"
                >
                  Tiếp tục
                  <v-icon class="ml-2">fa-angle-right</v-icon>
                </v-btn>
              </template>
              <span>Tới bài tiếp theo</span>
            </v-tooltip>
            <v-tooltip top v-else>
              <template v-slot:activator="{ on }">
                <v-btn class="font-weight-bold" v-on="on" @click="finishCourse(true)">Hoàn thành</v-btn>
              </template>
              <span>Hoàn tất khóa học</span>
            </v-tooltip>
          </v-layout>
        </v-flex>
      </v-layout>
    </v-container>
    <v-container v-if="lessonDetails.lessonType === 5" px-0>
      <v-layout wrap>
        <v-flex xs12 mb-4>
          <v-breadcrumbs :items="breadcrumbs" class="py-0">
            <template v-slot:divider>
              <v-icon>mdi-chevron-right</v-icon>
            </template>
          </v-breadcrumbs>
        </v-flex>
        <Exercise
          @changeLesson="changeLesson"
          @finishCourse="finishCourse"
          @createLearningLog="createLearningLog"
          :statusPreviousLesson="statusPreviousLesson"
          :statusNextLesson="statusNextLesson"
          :lessonId="parseInt(lessonId)"
          :lessonType="lessonDetails.lessonType"
        />
      </v-layout>
    </v-container>
  </v-app>
</template>

<script>
import Chessboard from '@/components/plugins/vue-chessboard/index.vue'
import Exercise from '@/components/Exercise.vue'
import sampleText from '@/data/sampletext.json'
import sampleLesson from '@/data/lesson.json'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')
const lessonRepository = RepositoryFactory.get('lesson')
const learningRepository = RepositoryFactory.get('learning')
import MoveHistory from '@/library/ChessHistory.js'
export default {
  components: {
    Chessboard,
    Exercise
  },
  data() {
    return {
      sampleText,
      sampleLesson,
      lessonContent: null,
      stepContent: null,
      moveHistory: [],
      defaultFen: '',
      currentFen: '',
      currentMove: 0,
      currentId: 0,
      firstId: 0,
      totalMove: 0,
      activeLesson: 0,
      courseDetails: null,
      lessonDetails: {},
      statusNextLesson: false,
      statusPreviousLesson: false,
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
          disabled: false,
          href: ''
        },
        {
          text: 'Học',
          disabled: true
        }
      ],
      height: {
        moveHistory: 0,
        lessonContent: 0
      },
      move: '',
      steps: [],
      theoryContent: '',
      learningLog: [],
      courseId: this.$route.params.courseId,
      lessonId: this.$route.params.lessonId,
      isCompleted: false
    }
  },
  watch: {
    $route() {
      this.lessonId = this.$route.params.lessonId
    }
  },
  computed: {
    statusNextMove() {
      if (this.currentMove === this.totalMove) {
        return true
      }
      return false
    },
    statusPreviousMove() {
      if (this.currentMove <= 1) {
        return true
      }
      return false
    }
  },
  updated() {
    if (this.lessonDetails.lessonType == 3) {
      this.lessonContent = this.sampleText
    } else {
      this.lessonContent = null
      if (this.lessonDetails.lessonType !== 5) {
        this.initHeightForDirecitonSide()
      }
    }
  },
  created() {
    this.currentFen = this.defaultFen
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      this.isCompleted = false
      await this.getCourseById()
      await this.getLessonById()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    async finishCourse(isPassed) {
      const courseId = this.$route.params.courseId
      if (isPassed) {
        await this.createLearningLog(courseId, this.lessonDetails.lessonId)
      }
      let timerInterval
      this.$swal
        .fire({
          title: 'Hoàn thành',
          type: 'success',
          html:
            'Bạn đã hoàn thành khóa học. Quay về trang chi tiết trong <strong></strong>...',
          timer: 5000,
          onBeforeOpen: () => {
            this.$swal.showLoading()
            timerInterval = setInterval(() => {
              this.$swal
                .getContent()
                .querySelector('strong').textContent = Math.ceil(
                this.$swal.getTimerLeft() / 1000
              )
            }, 100)
          },
          onClose: () => {
            clearInterval(timerInterval)
          }
        })
        .then(() => {
          this.$router.push(`/course/${courseId}`)
        })
    },
    initHeightForDirecitonSide() {
      //reset height
      const totalHeight = document.getElementsByClassName('cg-board-wrap')[0]
        .offsetHeight
      const moveHeight = totalHeight * 0.6 - 120
      document.getElementsByClassName('move-history-content')[0].style.height =
        moveHeight + 'px'
      const lessonHeight = totalHeight * 0.4 - 76
      document.getElementsByClassName('lesson-content')[0].style.height =
        lessonHeight + 'px'
    },
    showInfo() {
      // console.log(data.history[data.history.length - 1])
      // console.log(data.fen)
      // console.log(data)
    },
    loadFen(fen, event, content) {
      if (event != undefined) {
        const divTarget = event.srcElement
        if (divTarget.id) {
          this.stepContent = content
          this.currentId = divTarget.id
          this.currentFen = divTarget.getAttribute('prefen')
          this.move = divTarget.getAttribute('move')
          this.setCurrentMove()
        }
      } else {
        this.currentFen = fen
      }
    },
    async getCourseById() {
      const { data } = await courseRepository.getById(this.courseId)
      this.courseDetails = data.data
      this.lessons = this.courseDetails.lessonViewModels
      this.sortLessonViewModel()
      this.breadcrumbs[2].text = this.courseDetails.name
      this.breadcrumbs[2].href = `/course/${this.courseId}`
      this.activeLesson = this.lessons
        .map(el => {
          return el.lessonId
        })
        .indexOf(parseInt(this.lessonId))
    },
    sortLessonViewModel() {
      this.lessons.sort((a, b) => (a.lessonOrdered > b.lessonOrdered ? 1 : -1))
    },
    async getLessonById() {
      this.resetBoardAndResetHistory()
      this.theoryContent = ''
      this.$store.commit('incrementLoader', 1)
      const { data } = await lessonRepository.getById(this.lessonId)
      this.lessonDetails = data.data
      this.checkStatusDirectLesson()
      if (this.lessonDetails.lessonType == 2) {
        this.defaultFen = this.lessonDetails.lessonContent.initCode
        this.currentFen = this.defaultFen
        this.loadMoveHistory()
      } else if (this.lessonDetails.lessonType == 3) {
        this.theoryContent = this.lessonDetails.lessonContent.content
      }
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    async getCurrentUserLearningLogByCourseId() {
      const courseId = this.$route.params.courseId
      const { data } = await learningRepository.getLearningLog(courseId)
      this.learningLog = data.data
    },
    async createLearningLog(courseId, lessonId) {
      await this.getCurrentUserLearningLogByCourseId()
      const indexOfLearningLog =
        this.learningLog
          .map(function(el) {
            return el.learningLogId
          })
          .indexOf(lessonId) === -1
      if (indexOfLearningLog) {
        const { data } = await learningRepository.createLearningLog(
          courseId,
          lessonId,
          true
        )
        this.isCompleted = data.data.complete
      }
    },
    loadMoveHistory() {
      this.loadFen(this.lessonDetails.lessonContent.initCode)
      const moveHistory = new MoveHistory(this.lessonDetails)
      moveHistory.formatMoveHistory()
      this.moveHistory = moveHistory.getMoveHistory
      this.steps = moveHistory.getSteps
      this.firstId = this.steps[0].id
      // console.log(this.moveHistory)
    },

    setCurrentMove() {
      //set highlight div dựa trên this.current id hiện tại
      let arr = document.getElementsByClassName('move')
      if (arr != undefined && arr != null && arr.length !== 0) {
        Array.prototype.forEach.call(arr, function(move) {
          move.classList.remove('current-move')
        })
        let currentMove = document.getElementById(this.currentId)
        if (!this.isEmpty(currentMove)) {
          currentMove.classList.add('current-move')
          // document.getElementsByClassName('move-history-content')[0].scrollTop =
          //   currentMove.offsetTop - 251
        }
      }
    },
    getPreMoveById(id) {
      if (id === 0) {
        return null
      } else {
        const preId = document.getElementById(id).getAttribute('preId')
        return document.getElementById(preId)
      }
    },
    getNextMoveById(id) {
      let nextId = null
      if (id === 0) {
        nextId = this.firstId
      } else {
        nextId = document.getElementById(id).getAttribute('nextId')
      }
      return document.getElementById(nextId)
    },
    getLastMoveById(id) {
      const nextStep = this.steps.find(el => el.id == id)
      if (this.isEmpty(nextStep.nextId)) {
        return document.getElementById(nextStep.id)
      }
      return this.getLastMoveById(nextStep.nextId)
    },
    turnToNextMove() {
      const nextMove = this.getNextMoveById(this.currentId)
      if (!this.isEmpty(nextMove)) {
        this.currentId = nextMove.id
        nextMove.click()
        this.setCurrentMove()
      }
    },
    turnToLastMove() {
      if (this.currentId === 0) {
        this.currentId = this.firstId
      }
      const lastMove = this.getLastMoveById(this.currentId)
      if (!this.isEmpty(lastMove)) {
        this.currentId = lastMove.id
        lastMove.click()
        this.setCurrentMove()
      }
    },
    turnToPreviousMove() {
      const preMove = this.getPreMoveById(this.currentId)
      if (!this.isEmpty(preMove)) {
        this.currentId--
        preMove.click()
        this.setCurrentMove()
      }
    },
    turnToFirstMove() {
      this.currentId = 0
      this.currentFen = this.defaultFen
      this.setCurrentMove()
    },
    async changeLesson(val, isPassed) {
      this.lessonDetails.lessonType = 1
      if (0 <= this.activeLesson + val <= this.lessons.length) {
        this.activeLesson += val
        if (isPassed) {
          this.$store.commit('incrementLoader', 1)
          await this.createLearningLog(
            this.$route.params.courseId,
            this.lessonDetails.lessonId
          )
          setTimeout(() => {
            this.$store.commit('incrementLoader', -1)
          }, 500)
        }
        if (this.isCompleted) {
          this.$swal({
            title: 'Hoàn thành',
            html: `Bạn đã hoàn thành khóa học này. Bạn có muốn học tiếp?`,
            type: 'success',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Học tiếp',
            cancelButtonText: 'Về trang chi tiết'
          }).then(result => {
            if (result.value) {
              this.loadNewLesson()
            } else {
              this.$router.push(`/course/${this.courseId}`)
            }
          })
        } else {
          this.loadNewLesson()
        }
      }
    },
    loadNewLesson() {
      const nextLessonId = this.lessons[this.activeLesson].lessonId
      this.lessonId = nextLessonId
      this.$router.push(
        `/learning/course/${this.courseId}/lesson/${nextLessonId}`
      )
      this.checkStatusDirectLesson()
      this.getLessonById()
    },
    checkStatusDirectLesson() {
      this.statusNextLesson = this.checkStatusNextLesson()
      this.statusPreviousLesson = this.checkStatusPreviousLesson()
    },
    checkStatusNextLesson() {
      if (this.activeLesson === this.lessons.length - 1) {
        return true
      }
      return false
    },
    checkStatusPreviousLesson() {
      if (this.activeLesson <= 0) {
        return true
      }
      return false
    },
    resetBoardAndResetHistory() {
      this.currentFen = ''
      this.moveHistory = []
      this.currentMove = 0
      this.totalMove = 0
      this.currentId = 0
    },
    backToCourseDetail() {
      this.$router.push(`/course/${this.courseDetails.courseId}`)
    }
  }
}
</script>
<style scoped src="@/assets/style/chessboard.css"></style>
<style scoped>
.depth_2 {
  background-color: hsla(0, 59%, 85%, 0.85);
  margin: 5pt 0 5pt 5pt;
  flex: 0 0 100%;
}
.depth_3 {
  background-color: rgba(196, 240, 196, 0.85) !important;
  color: #080;
  margin: 5pt 0 5pt 5pt;
  flex: 0 0 100%;
}
.depth_2 > div {
  flex: 0 0 100%;
}
.depth_2 > div > div {
  color: #c00;
}
.single-move-white {
  -ms-flex: 0 0 86% !important;
  flex: 0 0 86% !important;
}
.content-theory-container > div:nth-child(2) {
  min-height: calc(100% - 85px);
}
.content-theory-container > div:nth-child(2) > div:first-child {
  height: 100%;
}
>>> .v-application--wrap {
  margin-top: -56px;
  padding-top: 56px !important;
}
>>> .content-theory img {
  position: relative;
  overflow: hidden;
  -webkit-box-flex: 1;
  -ms-flex: 1 0 auto;
  flex: 1 0 auto;
  max-width: 100%;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}
.title-table {
  font-size: 16px;
  font-weight: 600;
}
.lesson-name {
  font-size: 19px;
  font-weight: 700;
}
</style>
