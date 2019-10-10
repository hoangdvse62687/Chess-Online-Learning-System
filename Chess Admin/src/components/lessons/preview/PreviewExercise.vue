<template>
  <v-container>
    <v-layout>
    <v-flex xs5 pr-7 offset-xs1 offset-xl0>
      <chessboard
        :move="move"
        :orientation="userColor"
        :fen="currentFen"
        :status="currentGameStatus"
        @onMove="showInfo"
        @onWrong="performWrongMove"
      />
    </v-flex>
    <v-flex xs5>
      <v-layout column>
        <v-flex class="move-history">
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
          <v-layout class="review-move-btn-group">
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
            <div class="game-information content-area" id="game-information-area">
              <div
                :id="`game-information-item-${index}`"
                class="game-information-item"
                v-for="(item, index) in gameHistory"
                :key="index"
              >{{item}}</div>
            </div>
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
  </v-container>
</template>

<script>
import Chessboard from '@/components/plugins/cols-chessboard/index.vue'
export default {
  components: {
    Chessboard
  },
  props: {
    specificAns: {
      type: Number,
      default: -1
    },
    exercise: {
      type: Object,
      default: {}
    },
    question: {
      type: String,
      default: ''
    }
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
      startDialog: false,
      tickLabels: [1, 2, 3, 4, 5],
      player: JSON.parse(localStorage.getItem('user')),
      timePicker: false,
      level: 3,
      botImgLink: '',
      colorPicker: 0,
      userColor: 'white',
      botTime: '',
      playerTime: '',
      turn: '',
      move: '',
      moves: '',
      isStart: false,
      gameNumber: 0,
      pgn: '',
      currentGame: {
        bonusPoint: 0
      },
      currentGameStatus: '',
      sampleData: {},
      answerType: 0,
      preMove: [],
      answerArr: [],
      lastFen: '',
      currentMoveInArr: 0,
      moveData: {}
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
  watch: {},
  updated() {
    this.setCurrentMove()
  },
  created() {
    this.gameHistory.push(this.question)
    this.engine = new Worker('../stockfish.js')
    this.sendUCI('uci')
    this.currentFen = this.exercise.fen
    this.answerType = this.exercise.answerType
    this.userColor =
      this.exercise.fen.split(' ')[1] === 'w' ? 'white' : 'black'
    console.log(this.userColor)
    if (this.answerType === 2) {
      this.answerArr = this.exercise.answerArr
    }
    this.gameStatus = 'new'
    console.log(this.currentMoveInArr)
    // this.lastFen = this.currentFen
  },
  methods: {
    performWrongMove() {
      this.gameHistory.push('Câu trả lời sai, hãy thực hiện lại')
    },
    showInfo(data) {
      console.log(data)
      const black = 'black'
      this.moveData = data
      let fen = data.fen
      this.turn = data.turn
      this.newMove = data.move
      if (this.answerType === 2) {
        if (data.turn !== this.userColor) {
          let ableMoveArr = this.answerArr.filter(moveArr => {
            console.log(moveArr[0])
            return moveArr[this.currentMoveInArr].move === this.newMove
          })
          if (ableMoveArr.length === 0) {
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
            if (ableMoveArr.length > 1) {
              let randomArr = this.getRandomInt(ableMoveArr.length)
              ableMoveArr = this.answerArr.filter((moveArr, index) => {
                return index === randomArr
              })
            }
            this.createNewMoveInMoveHistory()
            if (ableMoveArr[0][this.currentMoveInArr].rightResponse.length !== 0) {
              this.gameHistory.push(
                ableMoveArr[0][this.currentMoveInArr].rightResponse
              )
            }
            if (this.currentMoveInArr === ableMoveArr[0].length - 1) {
              this.gameHistory.push("Hoàn thành")
            } else {
              this.currentMoveInArr++
              this.move = ableMoveArr[0][this.currentMoveInArr].moveDirection
            }
          }
        } else {
          if (data.history.length > 0) {
            this.createNewMoveInMoveHistory()
            this.currentMoveInArr++
          }
        }
      } else {
        this.moves = data.hisMoves
        if (this.newMove === undefined || !this.currentFen) return
        this.createNewMoveInMoveHistory()
        if (data.turn === undefined) {
          //end game
          this.turn = this.turn === 'white' ? 'black' : 'white'
          this.currentGameStatus = 'end_game'
        } else {
          this.turn = data.turn
        }
        if (data.end_game === undefined) {
          if (this.turn !== this.userColor) {
            this.calculateMove()
          }
        } else if (data.end_game === this.userColor) {
          this.$swal('Kết quả', `Xin vui lòng thực hiện lại`, 'error')
        } else {
          this.$swal('Kết quả', `Hoàn thành`, 'success')
        }
      }
    },
    createNewMoveInMoveHistory() {
      const black = 'black'
      let moveHistory = this.moveHistory
      let lastMove = moveHistory[moveHistory.length - 1]
      if (this.moveData.turn === black) {
        //tạo thêm turn mới
        const newTurn = {
          index: moveHistory.length + 1,
          whiteMove: {
            move: this.moveData.move,
            fen: this.moveData.fen,
            moveCount: 'move-' + this.totalMove
          },
          blackMove: null
        }
        moveHistory.push(newTurn)
      } else {
        //nước đi tiếp theo của turn cũ
        lastMove.blackMove = {
          move: this.moveData.move,
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
      this.sendUCI('setoption name Skill Level value ' + this.level)
      this.sendUCI(
        'position fen ' + this.sampleData.fen + ' moves' + this.moves
      )
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
  background-color: #ffdac2;
}
.game-information-item:nth-child(even) {
  background-color: #ffe0cc;
}
</style>

<style scoped src="@/assets/style/chessboard.css"></style>
