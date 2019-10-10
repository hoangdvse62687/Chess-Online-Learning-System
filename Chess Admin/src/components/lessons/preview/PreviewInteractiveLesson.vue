<template>
  <v-container>
    <v-layout wrap>
    <v-flex xs5 pr-7 offset-xs1>
      <Chessboard :fen="currentFen" :move="move" @onMove="showInfo" />
    </v-flex>
    <v-flex xs5 class="left-chess-info mx-auto">
      <v-card-text class="pr-5 pt-0 pb-0 pl-0" style="position:relative">
        <div class="move-history-content">
          <div v-for="(moved1, index) in moveHistory" :key="index">
            <template v-if="moved1.depth === 1">
              <div class="index">{{ moved1.index }}</div>
              <div
                v-if="moved1.whiteMove"
                :id="`pv-${moved1.whiteMove.id}`"
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
                :id="`pv-${moved1.blackMove.id}`"
                :preId="moved1.blackMove.preId"
                :nextId="moved1.blackMove.nextId"
                :class="moved1.blackMove.class"
                :preFen="moved1.blackMove.preFen"
                :move="moved1.blackMove.moveDirection"
                :depth="moved1.depth"
                @click="loadFen(null, $event, moved1.blackMove.content)"
              >{{ moved1.blackMove.move }}</div>
            </template>
            <div v-if="moved1.depth === 2" class="depth-2">
              <template v-for="(moved2, index2) in moved1.moveHistory">
                <div v-if="moved2.depth === 2" :key="index2">
                  <div class="index">{{ moved2.index }}</div>
                  <div
                    v-if="moved2.whiteMove"
                    :id="`pv-${moved2.whiteMove.id}`"
                    :preId="moved2.whiteMove.preId"
                    :nextId="moved2.whiteMove.nextId"
                    :class="moved2.whiteMove.class"
                    :preFen="moved2.whiteMove.preFen"
                    :move="moved2.whiteMove.moveDirection"
                    :depth="moved2.depth"
                    @click="loadFen(null, $event, moved2.whiteMove.content)"
                  >{{ moved2.whiteMove.move }}</div>
                  <div
                    v-if="moved2.blackMove"
                    :id="`pv-${moved2.blackMove.id}`"
                    :preId="moved2.blackMove.preId"
                    :nextId="moved2.blackMove.nextId"
                    :class="moved2.blackMove.class"
                    :preFen="moved2.blackMove.preFen"
                    :move="moved2.blackMove.moveDirection"
                    :depth="moved2.depth"
                    @click="loadFen(null, $event, moved2.blackMove.content)"
                  >{{ moved2.blackMove.move }}</div>
                </div>
                <div v-if="moved2.depth === 3" :key="index2" class="depth-3">
                  <template v-for="(moved3, index3) in moved2.moveHistory">
                    <div :key="index3" class="index">{{ moved3.index }}</div>
                    <div
                      v-if="moved3.whiteMove"
                      :id="`pv-${moved3.whiteMove.id}`"
                      :key="index3"
                      :preId="moved3.whiteMove.preId"
                      :nextId="moved3.whiteMove.nextId"
                      :class="moved3.whiteMove.class"
                      :preFen="moved3.whiteMove.preFen"
                      :move="moved3.whiteMove.moveDirection"
                      :depth="moved3.depth"
                      @click="loadFen(null, $event, moved3.whiteMove.content)"
                    >{{ moved3.whiteMove.move }}</div>
                    <div
                      v-if="moved3.blackMove"
                      :id="`pv-${moved3.blackMove.id}`"
                      :key="index3"
                      :preId="moved3.blackMove.preId"
                      :nextId="moved3.blackMove.nextId"
                      :class="moved3.blackMove.class"
                      :preFen="moved3.blackMove.preFen"
                      :move="moved3.blackMove.moveDirection"
                      :depth="moved3.depth"
                      @click="loadFen(null, $event, moved3.blackMove.content)"
                    >{{ moved3.blackMove.move }}</div>
                  </template>
                </div>
              </template>
            </div>
          </div>
        </div>
        <div class="review-move-btn-group content-area">
          <v-btn @click="turnToFirstMove" text>
            <v-icon text>fast_rewind</v-icon>
          </v-btn>
          <v-btn @click="turnToPreviousMove" text>
            <v-icon>skip_previous</v-icon>
          </v-btn>
          <v-btn @click="turnToNextMove" text>
            <v-icon>skip_next</v-icon>
          </v-btn>
          <v-btn @click="turnToLastMove" text>
            <v-icon>fast_forward</v-icon>
          </v-btn>
        </div>
        <div class="move-information mt-3 content-area">{{moveContent}}</div>
      </v-card-text>
    </v-flex>
  </v-layout>
  </v-container>
</template>

<script>
import Chessboard from '@/components/plugins/vue-chessboard'
import MoveHistory from '@/library/ChessHistory.js'
import  Vue from 'vue'
export default {
  components: {
    Chessboard
  },
  props: {
    steps: {
      type: Array,
      default: []
    },
    initFen: {
      type: String,
      default: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
    }
  },
  watch: {
    steps: function(newHis) {
      this.steps = newHis
      this.fakeSteps = this.steps.map(e => ({...e}))
      this.loadMoveHistory()
    }
  },
  data() {
    return {
      defaultFen: '',
      currentFen: '',
      move: '',
      firstId: 0,
      moveHistory: [],
      currentId: 0,
      fakeSteps: [],
      status: '',
      moveContent: ''
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
  mounted() {
    this.currentFen = this.initFen
    this.moveHistory = []
    this.fakeSteps = this.steps.map(e => ({...e}))
    this.loadMoveHistory()
  },
  methods: {
    loadMoveHistory() {
      this.loadFen(this.initFen)
      console.log(this.initFen)
      let lessonDetails = {
        interactiveLesson: {
          initCode: this.initFen,
          steps: this.fakeSteps
        }
      }
      const moveHistory = new MoveHistory(lessonDetails)
      moveHistory.formatMoveHistory()
      this.moveHistory = moveHistory.getMoveHistory
    },
    showInfo(data) {
      console.log(data.history[data.history.length - 1])
      console.log(data.fen)
    },
    loadFen(fen, event, content) {
      if (event != undefined) {
        const divTarget = event.srcElement
        if (divTarget.id) {
          this.moveContent = content
          this.currentId = divTarget.id.replace('pv-', '')
          console.log(this.currentId)
          this.currentFen = divTarget.getAttribute('prefen')
          this.move = divTarget.getAttribute('move')
          this.setCurrentMove()
        }
      } else {
        this.currentFen = fen
        console.log(this.currentFen)
      }
      console.log(this.currentFen)
    },
    setCurrentMove() {
      //set highlight div dựa trên this.current id hiện tại
      let arr = document.getElementsByClassName('move')
      if (arr != undefined && arr != null && arr.length !== 0) {
        Array.prototype.forEach.call(arr, function(move) {
          move.classList.remove('current-move')
        })
        let currentMove = document.getElementById(`pv-${this.currentId}`)
        if (!this.isEmpty(currentMove)) {
          currentMove.classList.add('current-move')
          // document.getElementsByClassName('move-history-content')[0].scrollTop =
          //   currentMove.offsetTop - 251
        }
      }
    },
    getPreMoveById(id) {
      const preId = document.getElementById(`pv-${id}`).getAttribute('preId')
      return document.getElementById(`pv-${preId}`)
    },
    getNextMoveById(id) {
      let nextId = null
      if (id === 0) {
        nextId = this.firstId
      } else {
        nextId = document.getElementById(`pv-${id}`).getAttribute('nextId')
      }
      return document.getElementById(`pv-${nextId}`)
    },
    getLastMoveById(id) {
      const nextStep = this.steps.find(el => el.id == id)
      if (this.isEmpty(nextStep.nextId)) {
        return document.getElementById(`pv-${nextStep.id}`)
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
    getTurnOfFen(fen) {
      if (this.isEmpty(fen)) {
        return 'w'
      }
      const arr = fen.split(' ')
      return arr[1]
    },
    addMoveHistory(newMove, depth) {
      if (depth === 1) {
        let turn = this.getTurnOfFen(newMove.preFen)
        this.totalMove++
        //tạo thêm turn mới
        const newTurn = {
          index: this.moveHistory.length + 1,
          depth: 1,
          whiteMove: newMove,
          blackMove: null
        }
        if (turn === 'w') {
          this.moveHistory.push(newTurn)
        } else {
          let lastMove = this.moveHistory[this.moveHistory.length - 1]
          //nước đi tiếp theo của turn cũ
          lastMove.blackMove = newMove
        }
      }
    }
  }
}
</script>

<style src="@/assets/style/chessboard.css">
</style>

