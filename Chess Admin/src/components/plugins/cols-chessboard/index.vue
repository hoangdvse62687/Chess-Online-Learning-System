<template>
  <div :id="boardName"></div>
</template>

<script>
import Chess from 'chess.js'
require('./chessboard-1.0.0.js')
export default {
  name: 'ColsChessboard',
  props: {
    fen: {
      type: String,
      default: 'start'
    },
    orientation: {
      type: String,
      default: 'white'
    },
    sparePieces: {
      type: Boolean,
      default: false
    },
    boardName: {
      type: String,
      default: 'board'
    },
    reset: {
      type: Boolean,
      default: false
    },
    move: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      currentFen: '',
      squareClass: 'square-55d63',
      fromSquare: '',
      toSquare: ''
    }
  },
  watch: {
    fen: function(newFen) {
      if (newFen !== this.game.fen()) {
        this.fen = newFen
        this.board.position(this.fen)
        this.game.load(this.fen)
        console.log("reloadFen")
        console.log(this.fen)
      }
    },
    reset: function(reset) {
      this.reset = reset
      if (this.reset) {
        this.board.position('start')
        this.fromSquare = ''
        this.toSquare = ''
        let currentHighLight = document.getElementsByClassName(this.squareClass)
        Array.prototype.forEach.call(currentHighLight, function(square) {
          square.classList.remove('highlight-white')
        })
        this.game = new Chess()
      }
    },
    orientation: function(orientation) {
      console.log('change orientation')
      this.orientation = orientation
      this.board.orientation(this.orientation)
    },
    move: function() {
      this.loadMove()
    }
  },
  methods: {
    getPosition(oldPos, newPos) {
      let data = {}
      if (ChessBoard.objToFen(newPos) !== undefined) {
        data['object'] = newPos
        data['fen'] = ChessBoard.objToFen(newPos)
        this.$emit('onChangePiece', data)
      }
    },
    getMove(source, target, piece, newPos, oldPos, orientation) {
      let data = {}
      let move = this.game.move({
        from: source,
        to: target,
        promotion: 'q'
      })
      console.log(move)
      console.log(move === null)
      if (move === null) {
        return 'snapback'
      } else {
        data['moveDirection'] = `${source}${target}`
        data['move'] = this.game.pgn().split(' ')[
          this.game.pgn().split(' ').length - 1
        ]
        data['turn'] = this.game.turn() === 'w' ? 'white' : 'black'
        data['fen'] = this.game.fen()
        this.$emit('onMove', data)
      }
      let currentHighLight = document.getElementsByClassName(this.squareClass)
      Array.prototype.forEach.call(currentHighLight, function(square) {
        square.classList.remove('highlight-white')
      })
      this.fromSquare = source
      this.toSquare = target
    },
    performMove() {
      this.board.position(this.game.fen())
      let currentBoard = document.getElementById(this.boardName)
      let fromSquare = currentBoard.getElementsByClassName(
        `square-${this.fromSquare}`
      )
      let toSquare = currentBoard.getElementsByClassName(`square-${this.toSquare}`)
      Array.prototype.forEach.call(fromSquare, function(square) {
        square.classList.add('highlight-white')
      })
      Array.prototype.forEach.call(toSquare, function(square) {
        square.classList.add('highlight-white')
      })
    },
    loadPosition() {
      console.log(this.currentFen)
      this.game.load(this.currentFen)
      let cfg = {
        position: this.game.fen(),
        draggable: true,
        sparePieces: this.sparePieces,
        dropOffBoard: this.sparePieces ? 'trash' : 'snackback',
        orientation: this.orientation,
        onChange: this.getPosition,
        onDrop: !this.sparePieces ? this.getMove : '',
        pieceTheme: '/assets/chesspieces/wikipedia/{piece}.png',
        onSnapEnd: this.performMove
      }
      this.board = ChessBoard(this.boardName, cfg)
    },
    loadMove() {
      this.game.move({
        from: this.move.substring(0, 2),
        to: this.move.substring(2, 4),
        promotion: this.move.charAt(4)
      })
      this.board.position(this.game.fen())
    }
  },
  mounted() {
    this.loadPosition()
  },
  created() {
    this.game = new Chess()
    this.board = null
    if (this.sparePieces) {
      this.currentFen = `8/8/8/8/8/8/8/8 ${
        this.orientation === 'white' ? 'w' : 'b'
      } KQkq - 0 1`
    } else {
      this.currentFen = this.fen
    }
  }
}
</script>

<style>
.highlight-white {
  box-shadow: inset 0 0 3px 3px yellow;
}
</style>
