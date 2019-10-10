<template>
  <v-layout>
    <v-flex xs6 xl8 offset-xs1 offset-xl0 mr-5>
      <chessboard
        :move="move"
        :orientation="userColor"
        :fen="currentFen"
        :status="currentGameStatus"
        @onMove="showInfo"
        @onWrong="performWrongMove"
      />
    </v-flex>
    <v-flex xs4>
      <v-layout column>
        <v-flex class="move-history">
          <v-card-title class="pl-0 mb-2 py-0 mt-1" style="height:60px">
            <span class="title font-weight-bold">Nước đi</span>
          </v-card-title>
          <div class="move-history-content">
            <div v-for="(item, index) in moveHistory" :key="index">
              <div class="index">{{ item.index }}</div>
              <div
                :id="item.whiteMove.moveCount"
                class="move"
                @click="loadFen(item.whiteMove.fen, $event)"
              >{{ item.whiteMove.move }}</div>
              <div
                v-if="item.blackMove"
                :id="item.blackMove.moveCount"
                class="move"
                @click="loadFen(item.blackMove.fen, $event)"
              >{{ item.blackMove.move }}</div>
            </div>
          </div>
        </v-flex>
        <v-flex mb-2>
          <v-layout>
            <v-btn text :disabled="statusPreviousMove || this.isStart" @click="turnToFirstMove()">
              <v-icon>fa-fast-backward</v-icon>
            </v-btn>
            <v-btn
              text
              class="main-button"
              :disabled="statusPreviousMove || this.isStart"
              @click="turnToPreviousMove()"
            >
              <v-icon>fa-backward</v-icon>
            </v-btn>

            <v-btn
              text
              class="main-button"
              :disabled="statusNextMove || this.isStart"
              @click="turnToNextMove()"
            >
              <v-icon>fa-forward</v-icon>
            </v-btn>
            <v-btn text :disabled="statusNextMove || this.isStart" @click="turnToLastMove()">
              <v-icon>fa-fast-forward</v-icon>
            </v-btn>
          </v-layout>
        </v-flex>
        <v-flex class="move-analyse" mb-2>
          <v-card height="200">
            <div class="game-information" id="game-information-area">
              <div
                :id="`game-information-item-${index}`"
                class="game-information-item"
                v-for="(item, index) in gameHistory"
                :key="index"
              >{{item}}</div>
            </div>
          </v-card>
        </v-flex>
        <v-flex xs12>
          <v-layout justify-space-between>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  class="font-weight-bold"
                  :disabled="statusPreviousLesson"
                  @click="changeLesson(-1)"
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
                  @click="changeLesson(1)"
                  v-on="on"
                >
                  Tiếp theo
                  <v-icon class="ml-2">fa-angle-right</v-icon>
                </v-btn>
              </template>
              <span>Tới bài tiếp theo</span>
            </v-tooltip>
            <v-tooltip top v-else>
              <template v-slot:activator="{ on }">
                <v-btn class="font-weight-bold" @click="finishCourse()" v-on="on">
                  Hoàn thành
                  <v-icon class="ml-2">fa-angle-right</v-icon>
                </v-btn>
              </template>
              <span>Hoàn tất khóa học</span>
            </v-tooltip>
          </v-layout>
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<script>
import Chessboard from '@/components/plugins/vue-chessboard/index.vue'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const lessonRepository = RepositoryFactory.get('lesson')
export default {
  props: {
    statusNextLesson: {
      type: Boolean,
      default: false
    },
    statusPreviousLesson: {
      type: Boolean,
      default: false
    },
    lessonId: {
      type: Number,
      default: -1
    },
    lessonType: {
      type: Number,
      default: -1
    }
  },
  components: {
    Chessboard
  },
  data() {
    return {
      moveHistory: [],
      gameHistory: [],
      defaultFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
      currentFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
      initFen: '',
      updateMove: true,
      currentMove: 0,
      totalMove: 0,
      player: JSON.parse(localStorage.getItem('user')),
      userColor: 'white',
      turn: '',
      move: '',
      moves: '',
      isStart: false,
      currentGameStatus: '',
      sampleData: {},
      answerType: 0,
      answerArr: [],
      lastFen: '',
      currentMoveInArr: 0,
      moveData: {},
      isPassed: false,
      turn: '',
      ableMoveArr: [],
      isBotMove: false
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
  watch: {
    // '$route.params.lessonId': {
    //   handler: function() {
    //     if (this.lessonType === 5) {
    //       this.gameHistory = []
    //       this.getLessonById()
    //     }
    //   },
    //   deep: true,
    //   immediate: true
    // }
  },
  updated() {
    this.setCurrentMove()
  },
  created() {
    this.engine = new Worker('../../../../../../stockfish.js')
    this.sendUCI('uci')
          this.getLessonById()
  },
  methods: {
    performWrongMove() {
      this.gameHistory.push('Câu trả lời sai, hãy thực hiện lại')
    },
    resetBoard() {
      this.moveHistory = []
      this.moves = ''
      this.currentFen = this.defaultFen
      this.currentMove = 0
      this.totalMove = 0
    },
    showInfo(data) {
      this.currentGameStatus = 'playing'
      const black = 'black'
      this.moveData = data
      let fen = data.fen
      this.newMove = data.history[data.history.length - 1]
      if (data.turn === undefined) {
              //end game
              this.turn = this.turn === 'white' ? 'black' : 'white'
            } else {
              this.turn = data.turn
            }
      //manual answer
      if (this.answerType === 2) {
        //after user turn
        this.ableMoveArr = this.answerArr.filter(moveArr => {
            return moveArr[this.currentMoveInArr].move === this.newMove
          })
        if (data.turn !== this.userColor) {
          if (this.ableMoveArr.length === 0) {
            this.currentGameStatus = 'wrong_ans'
            let wrongResArr = this.answerArr.filter(moveArr => {
              return moveArr[this.currentMoveInArr].wrongResponse.length !== 0
            })
            if (wrongResArr.length === 1) {
              this.gameHistory.push(
                wrongResArr[0][this.currentMoveInArr].wrongResponse
              )
            } else if (wrongResArr.length > 1) {
              let randomArr = this.getRandomInt(wrongResArr.length)
              this.gameHistory.push(
                wrongResArr[randomArr][this.currentMoveInArr].wrongResponse
              )
            }
            return
          } else {
            console.log('right')
            //right answer
            if (this.ableMoveArr.length > 1) {
              //more 1 answer, get random answer
              let randomArr = this.getRandomInt(this.ableMoveArr.length)
              this.ableMoveArr = this.answerArr.filter((moveArr, index) => {
                return index === randomArr
              })
            }
            this.createNewMoveInMoveHistory()
            if (
              this.ableMoveArr[0][this.currentMoveInArr].rightResponse
                .length !== 0
            ) {
              this.gameHistory.push(
                this.ableMoveArr[0][this.currentMoveInArr].rightResponse
              )
            }
            if (this.currentMoveInArr < this.ableMoveArr[0].length - 1) {
              this.currentMoveInArr++
              this.move = this.ableMoveArr[0][
                this.currentMoveInArr
              ].moveDirection
              this.isBotMove = true
            } else if (
              this.ableMoveArr.length > 0 &&
              this.currentMoveInArr === this.ableMoveArr[0].length - 1
            ) {
              this.$swal('Kết quả', `Hoàn thành`, 'success')
              this.isPassed = true
              this.createLearningLog()
            }
          }
        } else {
          if (this.ableMoveArr.length > 0) {
            this.createNewMoveInMoveHistory()
            console.log(this.turn)
            console.log('plus')
            this.currentMoveInArr++
            if (
              this.ableMoveArr.length > 0 &&
              this.currentMoveInArr === this.ableMoveArr[0].length
            ) {
              this.$swal('Kết quả', `Hoàn thành`, 'success')
              this.isPassed = true
              this.createLearningLog()
            }
          }
        }
      } else {
        this.moves = data.hisMoves
        if (this.newMove === undefined || !this.currentFen) return
        this.createNewMoveInMoveHistory()
        if (data.end_game === undefined) {
          if (this.turn !== this.userColor) {
            this.calculateMove()
          }
        } else if (data.end_game === this.userColor) {
          this.$swal('Kết quả', `Xin vui lòng thực hiện lại`, 'error')
        } else {
          this.isPassed = true
          this.$swal({
            title: 'Kết quả',
            text: 'Hoàn thành',
            type: 'success'
          })
          this.createLearningLog()
        }
      }
    },
    createNewMoveInMoveHistory() {
      const black = 'black'
      let moveHistory = this.moveHistory
      let lastMove = moveHistory[moveHistory.length - 1]
      if (this.turn === black) {
        //tạo thêm turn mới
        const newTurn = {
          index: moveHistory.length + 1,
          whiteMove: {
            move: this.moveData.history[this.moveData.history.length - 1],
            fen: this.moveData.fen,
            moveCount: 'move-' + this.totalMove
          },
          blackMove: null
        }
        moveHistory.push(newTurn)
      } else {
        //nước đi tiếp theo của turn cũ
        lastMove.blackMove = {
          move: this.moveData.history[this.moveData.history.length - 1],
          fen: this.moveData.fen,
          moveCount: 'move-' + this.totalMove
        }
      }
      this.currentMove = this.totalMove
      this.totalMove++
    },
    getRandomInt(max) {
      return Math.floor(Math.random() * Math.floor(max))
    },
    getNextMove() {
      console.log(this.answerArr[0])
      this.move = this.answerArr[0][this.currentMove].moveDirection
      this.currentMove++
    },
    loadFen(fen, event) {
      if (this.currentGameStatus === 'end_game') {
        this.currentFen = fen
        const divTarget = event.srcElement
        let id = divTarget.id
        this.currentMove = parseInt(id.replace('move-', ''))
      }
    },
    setCurrentMove() {
      //set highlight div dựa trên this.current move hiện tại
      let arr = document.getElementsByClassName('move')
      if (arr != undefined && arr != null && arr.length !== 0) {
        Array.prototype.forEach.call(arr, function(move) {
          move.classList.remove('current-move')
        })
        let currentMove = document.getElementById('move-' + this.currentMove)
        currentMove.classList.add('current-move')
        currentMove.parentNode.parentNode.scrollTop = currentMove.offsetTop
      }
    },
    getIdNumberOfMove(divTarget) {
      return parseInt(divTarget.id.replace('move-', ''))
    },
    getMoveByIdNumber() {
      return document.getElementById('move-' + this.currentMove)
    },
    turnToNextMove() {
      if (this.currentMove !== this.totalMove) {
        this.currentMove++
        const divTarget = this.getMoveByIdNumber()
        divTarget.click()
        this.updateMove = false
        this.setCurrentMove()
      }
    },
    turnToLastMove() {
      if (this.currentMove !== this.totalMove) {
        this.currentMove = this.totalMove
        const divTarget = this.getMoveByIdNumber()
        divTarget.click()
        this.updateMove = false
        this.setCurrentMove()
      }
    },
    turnToPreviousMove() {
      if (this.currentMove > 1) {
        this.currentMove--
        const divTarget = this.getMoveByIdNumber()
        divTarget.click()
        this.updateMove = false
        this.setCurrentMove()
      }
    },
    turnToFirstMove() {
      if (this.currentMove > 1) {
        this.currentMove = 1
        const divTarget = this.getMoveByIdNumber()
        divTarget.click()
        this.updateMove = false
        this.setCurrentMove()
      }
    },
    sendUCI(str) {
      console.log('Send: ' + str)
      this.engine.postMessage(str)
    },
    calculateMove() {
      let self = this
      this.sendUCI('setoption name Skill Level value 20')
      this.sendUCI('position fen ' + this.currentFen + ' moves' + this.moves)
      this.sendUCI('go depth 15')
      this.engine.onmessage = function(event) {
        console.log(event.data)
        let line = event.data
        if (event.data.indexOf('bestmove') > -1) {
          let match = line.match(/^bestmove ([a-h][1-8])([a-h][1-8])([qrbn])?/)
          match[3] == undefined
            ? (self.move = match[1] + match[2])
            : (self.move = match[1] + match[2] + match[3])
        }
      }
    },
    async changeLesson(val) {
      console.log(this.isPassed)
      await this.$emit('changeLesson', val, false)
      this.isPassed = false
    },
    async finishCourse() {
      await this.$emit('finishCourse', false)
    },
    async createLearningLog() {
      const courseId = this.$route.params.courseId
      const lessonId = this.$route.params.lessonId
      await this.$emit('createLearningLog', courseId, lessonId)
    },
    async getLessonById() {
      this.theoryContent = ''
      this.$store.commit('incrementLoader', 1)
      const { data } = await lessonRepository.getById(this.lessonId)
      console.log(data)
      this.currentFen = data.data.lessonContent.answer.fen
      this.answerType = data.data.lessonContent.answer.answerType
      this.userColor =
        data.data.lessonContent.answer.fen.split(' ')[1] === 'w'
          ? 'white'
          : 'black'
      this.answerArr = data.data.lessonContent.answer.answerArr
      this.currentGameStatus = 'new'
      console.log("get")
      this.gameHistory.push(data.data.lessonContent.question)
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    }
  }
}
</script>

<style scoped>
.move-history-content {
  height: 180px !important;
}
.game-information {
  height: 135px;
  overflow: auto;
}
.game-information-item {
  padding: 5px;
}
.game-information-item:nth-child(odd) {
  background-color: #bcd1d8;
}
.game-information-item:nth-child(even) {
  background-color: #9db9cc;
}
</style>

<style scoped src="@/assets/style/chessboard.css"></style>
