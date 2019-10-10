<template>
  <div>
    <v-container class="px-6 py-3" id="interactive-lesson">
      <v-window v-model="interactiveLessonStep">
        <v-window-item :value="1">
          <v-layout wrap>
            <v-flex xs6 class="left-chess-info">
              <v-card-text class="pr-5 pt-0 pb-0 pl-0" style="position:relative">
                <v-form ref="form" v-model="interactiveLessonForm" lazy-validation>
                  <v-text-field
                    color="blue-grey darken-1"
                    :rules="nameRules"
                    v-model="lessonName"
                    label="Tên bài học"
                  ></v-text-field>
                  <v-textarea
                  v-model="lessonDes"
                  :rules="descriptionRules"
                  color="grey darken-2"
                  label="Mô tả:  "
                ></v-textarea>
                </v-form>
                <div class="move-history-content">
                  <div v-for="(moved1, index) in moveHistory" :key="index">
                    <template v-if="moved1.depth === 1">
                      <div class="index">{{ moved1.index }}</div>
                      <div
                        v-if="moved1.whiteMove"
                        :id="`il-${moved1.whiteMove.id}`"
                        :preId="moved1.whiteMove.preId"
                        :nextId="moved1.whiteMove.nextId"
                        :preFen="moved1.whiteMove.preFen"
                        :fen="moved1.whiteMove.fen"
                        :move="moved1.whiteMove.moveDirection"
                        :class="`il-${moved1.whiteMove.class}`"
                        :depth="moved1.depth"
                        @click="loadFen(null, $event, moved1.whiteMove.content)"
                      >{{ moved1.whiteMove.move }}</div>

                      <div
                        v-if="moved1.blackMove"
                        :id="`il-${moved1.blackMove.id}`"
                        :preId="moved1.blackMove.preId"
                        :nextId="moved1.blackMove.nextId"
                        :class="`il-${moved1.blackMove.class}`"
                        :preFen="moved1.blackMove.preFen"
                        :fen="moved1.blackMove.fen"
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
                            :id="`il-${moved2.whiteMove.id}`"
                            :preId="moved2.whiteMove.preId"
                            :nextId="moved2.whiteMove.nextId"
                            :class="`il-${moved2.whiteMove.class}`"
                            :preFen="moved2.whiteMove.preFen"
                            :fen="moved2.whiteMove.fen"
                            :move="moved2.whiteMove.moveDirection"
                            :depth="moved2.depth"
                            @click="
                              loadFen(null, $event, moved2.whiteMove.content)
                            "
                          >{{ moved2.whiteMove.move }}</div>
                          <div
                            v-if="moved2.blackMove"
                            :id="`il-${moved2.blackMove.id}`"
                            :preId="moved2.blackMove.preId"
                            :nextId="moved2.blackMove.nextId"
                            :class="`il-${moved2.blackMove.class}`"
                            :preFen="moved2.blackMove.preFen"
                            :fen="moved2.blackMove.fen"
                            :move="moved2.blackMove.moveDirection"
                            :depth="moved2.depth"
                            @click="
                              loadFen(null, $event, moved2.blackMove.content)
                            "
                          >{{ moved2.blackMove.move }}</div>
                        </div>
                        <div v-if="moved2.depth === 3" :key="index2" class="depth-3">
                          <template v-for="(moved3, index3) in moved2.moveHistory">
                            <div :key="index3" class="index">{{ moved3.index }}</div>
                            <div
                              v-if="moved3.whiteMove"
                              :id="`il-${moved3.whiteMove.id}`"
                              :key="index3"
                              :preId="moved3.whiteMove.preId"
                              :nextId="moved3.whiteMove.nextId"
                              :class="`il-${moved3.whiteMove.class}`"
                              :preFen="moved3.whiteMove.preFen"
                              :fen="moved3.whiteMove.fen"
                              :move="moved3.whiteMove.moveDirection"
                              :depth="moved3.depth"
                              @click="
                                loadFen(null, $event, moved3.whiteMove.content)
                              "
                            >{{ moved3.whiteMove.move }}</div>
                            <div
                              v-if="moved3.blackMove"
                              :id="`il-${moved3.blackMove.id}`"
                              :key="index3"
                              :preId="moved3.blackMove.preId"
                              :nextId="moved3.blackMove.nextId"
                              :class="`il-${moved3.blackMove.class}`"
                              :preFen="moved3.blackMove.preFen"
                              :fen="moved3.blackMove.fen"
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
                <v-textarea
                  color="grey darken-2"
                  v-model="moveContent"
                  class="mt-0"
                  label="Nội dung:  "
                  @keyup="saveContent"
                  @keydown="isSavedContent = false"
                  :disabled="currentMove === 0 || moveHistory.length === 0"
                ></v-textarea>
                <v-card-actions class="py-0" style="align-items:unset">
                  <v-alert v-if="isSavedContent" class="xs6 py-1" dense text type="success">Đã lưu</v-alert>
                  <v-spacer></v-spacer>
                  <!-- <v-btn color="blue-grey" @click="removeMove" class="white--text">Xóa nước đi</v-btn> -->
                  <v-btn
                    color="amber darken-2"
                    depressed
                    @click="resetBoard"
                    class="white--text"
                  >Xóa toàn bộ</v-btn>
                </v-card-actions>
              </v-card-text>
            </v-flex>
            <v-flex xs5 pr-7 style="margin: auto; position:relative">
              <Chessboard
                @onMove="showInfo"
                :orientation="orientation"
                :reset="isResetBoard"
                :fen="fen"
                :boardName="'board'"
              />
              <v-btn
                style="transform: translate(35px, -50%); top: 50%"
                absolute
                right
                @click="editBoard = true"
                icon
                fab
                text
                small
                color="grey"
              >
                <v-icon>fa-pen</v-icon>
              </v-btn>
            </v-flex>
          </v-layout>
        </v-window-item>
        <v-window-item :value="2">
          <PreviewInteractiveLesson :initFen="initFen" :steps="lessonContent" />
        </v-window-item>
      </v-window>
      <v-flex xs11>
        <v-card-actions class="mt-1">
          <v-spacer></v-spacer>
          <v-btn
            depressed
            class="white--text"
            color="amber darken-2"
            :disabled="interactiveLessonStep === 1"
            @click="interactiveLessonStep--"
          >Trở về</v-btn>
          <v-btn
            depressed
            class="white--text"
            color="amber darken-2"
            :disabled="!isValiated"
            @click="interactiveLessonStep === 1 ? preview() : addLesson()"
          >{{interactiveLessonStep === 1 ? 'Xem trước' : 'Lưu'}}</v-btn>
        </v-card-actions>
      </v-flex>
    </v-container>
    <v-dialog v-model="editBoard" persistent max-width="800px">
      <v-card :elevation="8">
        <v-toolbar :elevation="0" color="grey lighten-3">
          <v-toolbar-title class="grey--text text--darken-3">Tạo thế cờ</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn
            @click="editBoard = false"
            class="btn-create-puzzle text-xs-center"
            text
            icon
            color="grey darken-3"
            dark
            depressed
          >
            <v-icon>close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-container>
          <create-chess-puzzle @onChangeFen="getFen" :boardName="'createBoard'"></create-chess-puzzle>
        </v-container>
      </v-card>
    </v-dialog>
    <v-dialog v-model="editMoveDialog" persistent max-width="500">
      <v-card>
        <v-card-actions>
          <v-card-title class="headline">Nước cờ mới</v-card-title>
          <v-spacer></v-spacer>
          <v-btn color="grey" small @click="closeEditMoveDialog" fab icon>
            <v-icon>close</v-icon>
          </v-btn>
        </v-card-actions>
        <v-card-text>"Thêm nước khác" để thêm nước đi khác vào ván cờ hiện tại. Nếu "Thay đổi" nước đi, các nước sau đó sẽ bị xóa</v-card-text>
        <v-card-actions class="pa-3">
          <v-spacer></v-spacer>
          <v-btn
            color="amber darken-2"
            :disabled="depth === 3"
            text
            @click="currentMoveWithSamePreId.length > 1 ? replaceOtherMove() : addOtherMove()"
          >{{ currentMoveWithSamePreId.length > 1 ? 'Thay nước khác' : 'Thêm nước khác'}}</v-btn>
          <v-btn color="amber darken-1" class="white--text" depressed @click="editMove">Thay đổi</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import Chessboard from '@/components/plugins/cols-chessboard'
import CreateChessPuzzle from './CreateChessPuzzle'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
import PreviewInteractiveLesson from './preview/PreviewInteractiveLesson'
const lessonRepository = RepositoryFactory.get('lesson')
import MoveHistory from '@/library/ChessHistory.js'

export default {
  components: {
    Chessboard,
    CreateChessPuzzle,
    PreviewInteractiveLesson
  },
  props: {
    editingLessonId: {
      type: Number,
      default: -1
    }
  },
  data() {
    return {
      lessonContent: [],
      moveHistory: [],
      editBoard: false, //editBoard dialog
      initFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
      fen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
      orientation: '',
      chessboardData: {},
      totalMove: 0, //total of move
      currentMove: 0, // move index is clicked,
      lastMove: 0,
      currentMoveWithSamePreId: [],
      totalMoveIndex: 0,
      preId: null,
      moveContent: '',
      isResetBoard: false,
      depth: 1, //depth of current Move
      editMoveDialog: false,
      moveData: {},
      newHalfMove: {},
      interactiveLessonStep: 1,
      isSavedContent: false,
      lessonName: '',
      lessonDes: '',
      nameRules: [
        v => !!v || 'Tên bài học không được để trống',
        v => (v && v.length > 6) || 'Tên bài học phải nhiều hơn 6 kí tự'
      ],
      descriptionRules: [
        v => !!v || 'Mô tả bài học không được để trống'
      ],
      interactiveLessonForm: true,
      isEditing: false,
      isValiated: false,
      lastMoves: [0]
    }
  },
  watch: {
    editingLessonId: function(newId) {
      this.editingLessonId = newId
      if (this.editingLessonId > 0) {
        console.log("is editing")
        this.getById(this.editingLessonId)
        this.isEditing = true
      }
    },
    lessonName: function() {
      if (!this.isEditing) {
        this.isValiated = this.moveHistory.length > 0 && this.$refs.form.validate()
      }
    },
    lessonDes: function() {
      if (!this.isEditing) {
        this.isValiated = this.moveHistory.length > 0 && this.$refs.form.validate()
      }
    },
    moveHistory: function() {
      if (!this.isEditing) {
        this.isValiated = this.moveHistory.length > 0 && this.$refs.form.validate()
      }
    }
  },
  created() {
    if (this.editingLessonId > 0) {
      this.getById(this.editingLessonId)
      this.isEditing = true
    }
  },
  updated() {
    if (this.currentMove > 0) {
      this.setCurrentMove()
    }
  },
  methods: {
    saveContent() {
      let timeOut = window.setTimeout(() => {
        let currentHalfMove = this.lessonContent.find(halfMove => {
          return halfMove.id == this.currentMove
        })
        currentHalfMove.content = this.moveContent
        this.isSavedContent = true
      }, 500)
    },
    getFen(data) {
      this.chessboardData = data
      this.initFen = data.fen
      this.fen = data.fen
      this.editBoard = false
      this.resetValue()
    },
    resetValue() {
      this.moveHistory = []
      this.currentMove = 0
      this.moveData = {}
      this.lessonContent = []
      this.preId = null
    },
    resetBoard() {
      this.isResetBoard = true
      this.resetValue()
    },
    saveFen() {
      this.fen = this.chessboardData.fen
      this.initFen = this.chessboardData.fen
      this.orientation = this.chessboardData.orientation
      let posKey = Object.keys(this.chessboardData.object) //position of chess pieces
      this.editBoard = false
    },
    showInfo(data) {
      this.moveData = data
      this.fen = data.fen
      let nextMoveArr = this.lessonContent.filter(move => {
        return move.preId == this.currentMove
      })
      if (parseInt(this.currentMove) === this.lastMove || nextMoveArr.length == 0) {
        //check lastmove in current moveHistory
        console.log("add new for edit")
        this.totalMove++
        let newHalfMove = {
          id: this.totalMove,
          fen: data.fen,
          move: data.move,
          moveDirection: data.moveDirection,
          preId: this.preId,
          content: ''
        }
        this.preId = this.totalMove
        this.moveContent = '' //set moveContent to '' for newMove
        this.lessonContent.push(newHalfMove)
        this.currentMove = this.totalMove
        this.lastMove = this.totalMove
      } else {
        let nextMove = this.lessonContent.find(e => {
          return e.preId == this.currentMove
        })
        if (nextMove.moveDirection === data.moveDirection) {
          let nextMoveEl = document.getElementById(`il-${nextMove.id}`)
          nextMoveEl.click()
          this.currentMove = nextMove.id
        } else {
          this.currentMoveWithSamePreId = this.lessonContent.filter(e => {
            return e.preId == this.currentMove
          })
          this.editMoveDialog = true
        }
      }
      this.loadMoveHistory(this.lessonContent)
    },
    loadMoveHistory(lessonContent) {
      console.log(lessonContent)
      let lessonDetails = {
        interactiveLesson: {
          initCode: this.initFen,
          steps: lessonContent.map(e => ({ ...e }))
        }
      }
      this.moveHistoryObj = new MoveHistory(lessonDetails)
      this.moveHistoryObj.formatMoveHistory()
      this.moveHistory = this.moveHistoryObj.getMoveHistory
    },
    editMove() {
      this.totalMove++
      this.preId = parseInt(this.currentMove)
      let newHalfMove = {
        id: this.totalMove,
        move: this.moveData.move,
        moveDirection: this.moveData.moveDirection,
        content: '',
        preId: this.preId,
        fen: this.moveData.fen
      }
      this.lessonContent = this.lessonContent.filter(halfMove => {
        return halfMove.preId < this.preId
      })
      this.currentMove = this.totalMove
      this.lastMove = this.totalMove
      this.preId = this.currentMove
      this.lessonContent.push(newHalfMove)
      this.loadMoveHistory(this.lessonContent)
      this.editMoveDialog = false
    },
    replaceOtherMove() {
      let movesWithSamePreId = this.lessonContent.filter(e => {
        return e.preId == this.currentMove
      })
      let otherMove = movesWithSamePreId.sort((a, b) => {
        return a.id - b.id
      })[1]
      this.lessonContent = this.lessonContent.filter(e => {
        return e.preId < otherMove.id
      })
      this.preId = parseInt(this.currentMove)
      this.totalMove++
      let newHalfMove = {
        id: this.totalMove,
        move: this.moveData.move,
        moveDirection: this.moveData.moveDirection,
        content: '',
        preId: this.preId,
        fen: this.moveData.fen
      }
      this.lessonContent[this.lessonContent.indexOf(otherMove)] = newHalfMove
      this.currentMove = this.totalMove
      this.lastMove = this.totalMove
      this.preId = this.currentMove
      this.loadMoveHistory(this.lessonContent)
      this.editMoveDialog = false
    },
    loadFen(fen, event, content) {
      if (event != undefined) {
        const divTarget = event.srcElement
        if (divTarget.id) {
          this.currentId = divTarget.id.replace('il-', '')
          this.currentMove = this.currentId
          let currentHalfMove = this.lessonContent.find(move => {
            return move.id == this.currentMove
          })
          this.moveContent = currentHalfMove.content
          // let afterMove = this.lessonContent.find(move => {
          //   move.preId == this.currentMove
          // })
          // if (afterMove === undefined) {
          //   this.lastMove = parseInt(this.currentMove)
          // }
          this.preId = parseInt(this.currentMove)
          if (this.currentMove == 1) {
            this.fen = document
              .getElementById(`il-${divTarget.getAttribute('nextId')}`)
              .getAttribute('preFen')
          } else {
            this.fen = divTarget.getAttribute('fen')
          }
          this.move = divTarget.getAttribute('move')
          this.setCurrentMove()
        }
      } else {
        this.fen = fen
      }
    },
    closeEditMoveDialog() {
      this.editMoveDialog = false
      let currentMove = document.getElementById(`il-${this.currentMove}`)
      currentMove.click()
    },
    setCurrentMove() {
      let arr = document.getElementsByClassName('il-move')
      if (arr != undefined && arr != null && arr.length !== 0) {
        Array.prototype.forEach.call(arr, function(move) {
          move.classList.remove('il-current-move')
        })
        let currentMove = document.getElementById(`il-${this.currentMove}`)
        currentMove.classList.add('il-current-move')
        // currentMove.parentNode.parentNode.scrollTop = currentMove.offsetTop
      }
    },
    addOtherMove() {
      this.totalMove++
      this.preId = parseInt(this.currentMove)
      let newHalfOtherMove = {
        id: this.totalMove,
        move: this.moveData.move,
        moveDirection: this.moveData.moveDirection,
        fen: this.moveData.fen,
        content: '',
        preId: this.preId
      }
      this.lessonContent.push(newHalfOtherMove)
      this.currentMove = this.totalMove
      this.lastMove = this.currentMove
      this.preId = this.currentMove
      this.editMoveDialog = false
      this.loadMoveHistory(this.lessonContent)
    },
    addLesson() {
      if (this.$refs.form.validate()) {
        // this.lessonContent = this.lessonContent.map(e => {
        //   e.id = parseInt(e.id)
        //   e.preId = parseInt(e.preId)
        // })
        this.lessonContent = this.lessonContent.sort((a, b) => {
          return a.id - b.id
        })
        const lesson = {
          name: this.lessonName,
          description: this.lessonDes,
          interactiveLesson: {
            steps: this.lessonContent,
            initCode: this.initFen
          }
        }
        console.log(lesson)
        if (this.editingLessonId > 0) {
          console.log(this.lessonContent)
          this.$emit('onUpdateInteractiveLesson', lesson)
        } else {
          this.$emit('onAddInteractiveLesson', lesson)
        }
      }
    },
    preview() {
      this.interactiveLessonStep++
    },
    async getById(lessonId) {
      const data = await lessonRepository.getById(lessonId).then(res => {
        console.log(res)
        this.isValiated = true
        console.log(this.isValiated)
        this.lessonName = res.data.data.name
        this.lessonDes = res.data.data.description
        this.lessonContent = res.data.data.lessonContent.steps
        this.lessonContent = this.lessonContent.map(e => {
          let obj = e
          obj['id'] = parseInt(e.id)
          obj['preId'] = parseInt(e.preId)
          return obj
        })
        this.initFen = res.data.data.lessonContent.initCode
        this.totalMove = this.lessonContent.length > 0 ? parseInt(this.lessonContent[this.lessonContent.length - 1].id) : 0
        console.log(this.totalMove)
        this.lastMove = this.totalMove
        console.log(this.lessonContent)
        this.loadMoveHistory(this.lessonContent)
      })
    },
  }
}
</script>

<style scoped src="@/assets/style/chessboard.css" >
</style>
<style scoped>
.move-history-content {
  height: 120px;
}
</style>