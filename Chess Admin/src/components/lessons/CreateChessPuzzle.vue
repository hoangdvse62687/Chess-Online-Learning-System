<template>
  <v-layout wrap>
    <v-flex xs1 style="align-self:center;">
      <v-btn small @click="flipBoard" text icon color="grey">
          <v-icon>fa-redo-alt</v-icon>
        </v-btn>
    </v-flex>
    <v-flex xs5>
      <v-layout column>
        <chessboard :sparePieces="true" @onChangePiece="changePos" :fen="fen" :orientation="chessColor" :boardName="boardName"></chessboard>
        <v-card-actions class="justify-center py-0">
      <v-btn small style="margin-top:auto;margin-bottom:auto" @click="startBoard" text icon color="grey">
          <v-icon>fa-play-circle</v-icon>
        </v-btn>
          <v-btn small style="margin-top:auto;margin-bottom:auto" @click="clearBoard" text icon color="grey">
          <v-icon>fa-eraser</v-icon>
        </v-btn>
        </v-card-actions>
      </v-layout>
    </v-flex>
    <v-flex xs6 pl-5 pr-3>
      <v-card-title class="px-0">
        <span class="subheading font-weight-bold">Chọn cánh nhập thành</span>
      </v-card-title>
        <v-flex xs12>
          <v-btn-toggle class="btn-castling-group" v-model="castling" multiple>
            <v-btn :value="'K'" class="btn-castling white king"></v-btn>
            <v-btn :value="'Q'" class="btn-castling queen white"></v-btn>
            <v-btn :value="'k'" class="btn-castling king black"></v-btn>
            <v-btn :value="'q'" class="btn-castling queen black"></v-btn>
          </v-btn-toggle>
        </v-flex>
        <v-flex xs12>
          <v-text-field class="mt-3" v-model="previousMove" @keyup="bindFen" regular hide-details label="Nước đi trước đó"></v-text-field>
          <v-text-field class="mt-3" v-model="fen" regular readonly hide-details label="Chuỗi FEN"></v-text-field>
          <v-card-actions class="pt-5 pr-0">
            <v-spacer></v-spacer>
            <v-btn @click="saveFen" class="btn-create-puzzle" dark depressed color="amber darken-3">Lưu thế cờ</v-btn>
          </v-card-actions>
        </v-flex>
    </v-flex>
  </v-layout>
</template>

<script>
import Chessboard from '@/components/plugins/cols-chessboard/index.vue'
export default {
  components: {
    Chessboard
  },
  props: {
    boardName: {
      type: String,
      default: ''
    },
    resetBoard: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      userColors: ['black', 'white'],
      chessColor: 'white',
      chessColors: '',
      indexColor: 0,
      castling: [],
      fen: '8/8/8/8/8/8/8/8 w - - 0 1',
      previousMove: '-',
      position: {},
      defaultFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
      fenData: {}
    }
  },
  watch: {  
    castling: function(newVal) {
      let castlingVal = ''
      this.castling = newVal
      console.log(this.castling)
      if (this.castling.includes('K')) {
        castlingVal += 'K'
      }
      if (this.castling.includes('Q')) {
        castlingVal += 'Q'
      }
      if (this.castling.includes('k')) {
        castlingVal += 'k'
      }
      if (this.castling.includes('q')) {
        castlingVal += 'q'
      }
      if (this.castling.length === 0) {
        castlingVal = '-'
      }
      let fenArr = this.fen.split(' ')
      this.fen = `${fenArr[0]} ${fenArr[1]} ${castlingVal} ${fenArr[3]} ${fenArr[4]} ${fenArr[5]}`
    },
    fen: function(newFen) {
      this.fen = newFen
      this.fenData['fen'] = this.fen
      this.fenData['object'] = this.position
      this.fenData['orientation'] = this.chessColor
    },
    resetBoard: function(reset) {
      this.resetBoard = reset
      if (this.resetBoard) {
        this.clearBoard()
      }
    }
  },
  created() {},
  methods: {
    saveFen() {
      this.$emit('onChangeFen', this.fenData)
    },
    changePos(data) {
      this.position = data.object
      let fenArr = this.fen.split(' ')
      this.fen = `${data.fen} ${fenArr[1]} ${fenArr[2]} ${fenArr[3]} ${fenArr[4]} ${fenArr[5]}`
    },
    bindFen() {
      let fenArr = this.fen.split(' ')
      let match
      if (fenArr[1] === 'w') {
        match = this.previousMove.match(/([a-h][5])?/)
      } else {
        match = this.previousMove.match(/([a-h][4])?/)
      }
      if (match[0].length !== 0) {
        this.fen = `${fenArr[0]} ${fenArr[1]} ${fenArr[2]} ${match[0]} ${fenArr[4]} ${fenArr[5]}`
      }
    },
    startBoard() {
      this.fen = `rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1`
      this.castling = ['K', 'Q', 'k', 'q']
    },
    clearBoard() {  
      console.log(this.chessColor)
      console.log(this.chessColor === 'white')
      this.fen = `8/8/8/8/8/8/8/8 ${this.chessColor === 'white' ? 'w' : 'b'} - - 0 1`
      this.castling = []
    },
    flipBoard() {
      const black = 'black'
      this.chessColor = this.chessColor === 'white' ? 'black' : 'white'
      if (this.fen !== this.defaultFen) {
        this.fen = this.fen.replace(`${this.chessColor === black ? 'w' : 'b'}`, `${this.chessColor === black ? 'b' : 'w'}`)
      }
    },
    exampleChess(exampleFen) {
      this.currentFen = exampleFen[this.currentStep]
      this.lessonViewModel.interactiveLesson.initCode =
        exampleFen[this.currentStep++]
      if (this.currentStep >= exampleFen.length) {
        return (this.currentStep = 0)
      }
      console.log(this.currentStep)
    },
    getIdNumberOfMove(divTarget) {
      return parseInt(divTarget.id.replace('move-', ''))
    },
    getMoveByIdNumber() {
      return document.getElementById('move-' + this.currentMove)
    }
  }
}
</script>

<style scoped src="@/assets/style/chessboard.css" >
>>> piece,
.blue {
  background-color: transparent !important;
}
.option_1 {
  text-align: justify;
}
.option_2 {
  color: dimgray;
  font-size: 17px;
}
.btn {
  font-size: 20px;
}
</style>
<style scoped>
.btn-castling {
  width: 25%;
  height: 50px;
  background-size: 100% 100%;
}
.v-btn::before {
  background-color: transparent;
}
.v-btn.v-item--active::before {
  background-color: grey;
}
.btn-castling-group .black, .btn-castling-group .white {
  background-color: transparent !important;
}
</style>
