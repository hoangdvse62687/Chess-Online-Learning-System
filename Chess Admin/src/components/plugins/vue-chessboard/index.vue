<template>
  <div class="blue merida">
    <div ref="board" class="cg-board-wrap"></div>
    <br />
  </div>
</template>

<script>
import Chess from 'chess.js'
import { Chessground } from 'chessground'
import { uniques } from './Utils.js'

export default {
  name: 'Chessboard',
  props: {
    fen: {
      type: String,
      default: ''
    },
    free: {
      type: Boolean,
      default: false
    },
    showThreats: {
      type: Boolean,
      default: false
    },
    onPromotion: {
      type: Function,
      default: () => 'q'
    },
    orientation: {
      type: String,
      default: 'white'
    },
    move: {
      type: String,
      default: ''
    },
    isBotTurn: {
      type: Boolean,
      default: false
    },
    status: {
      type: String,
      default: 'playing' //playing, new and pausing
    }
  },
  data() {
    return {
      hisMoves: '',
      defaultFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
    }
  },
  watch: {
    fen: function(newFen) {
      // if (this.status !== 'new') {
      //   this.fen = newFen
      // this.board.set({
      //   fen: this.fen
      // })
      if (this.fen !== this.game.fen()) {
        this.game.load(this.fen)
      }
      // }
    },
    orientation: function(orientation) {
      console.log('watch orientation________', orientation)
      this.orientation = orientation
      this.game.reset()
      this.loadPosition()
    },
    status: function(start) {
      this.start = start
      if (this.start === 'new') {
        this.game.reset()
        this.loadPosition()
      }
    },
    showThreats: function(st) {
      this.showThreats = st
      if (this.showThreats) {
        this.paintThreats()
      }
    },
    move: function(move) {
      console.log('watch occur')
      this.move = move
      this.loadMove()
    }
  },
  mounted() {
    this.loadPosition()
  },
  created() {
    this.game = new Chess()
    this.board = null
    this.promotions = []
    this.promoteTo = 'q'
    this.hisMoves = ''
    console.log('craetd')
  },
  methods: {
    possibleMoves() {
      const dests = {}
      this.game.SQUARES.forEach(s => {
        const ms = this.game.moves({ square: s, verbose: true })
        if (ms.length) dests[s] = ms.map(m => m.to)
      })
      return dests
    },
    opponentMoves() {
      let originalPGN = this.game.pgn()
      let tokens = this.game.fen().split(' ')
      tokens[1] = tokens[1] === 'w' ? 'b' : 'w'
      tokens = tokens.join(' ')
      let valid = this.game.load(tokens)
      if (valid) {
        let moves = this.game.moves({ verbose: true })
        this.game.load_pgn(originalPGN)
        return moves
      } else {
        return []
      }
    },
    toColor() {
      return this.game.turn() === 'w' ? 'white' : 'black'
    },
    paintThreats() {
      let moves = this.game.moves({ verbose: true })
      let threats = []
      moves.forEach(function(move) {
        threats.push({ orig: move.to, brush: 'yellow' })

        if (move['captured']) {
          threats.push({ orig: move.from, dest: move.to, brush: 'red' })
        }
        if (move['san'].includes('+')) {
          threats.push({ orig: move.from, dest: move.to, brush: 'blue' })
        }
      })
      this.board.setShapes(threats)
    },
    calculatePromotions() {
      let moves = this.game.moves({ verbose: true })
      this.promotions = []
      for (let move of moves) {
        if (move.promotion) {
          this.promotions.push(move)
        }
      }
    },
    isPromotion(orig, dest) {
      let filteredPromotions = this.promotions.filter(
        move => move.from === orig && move.to === dest
      )
      return filteredPromotions.length > 0 // The current movement is a promotion
    },
    changeTurn() {
      return (orig, dest) => {
        this.hisMoves += ' ' + orig + dest
        if (this.isPromotion(orig, dest)) {
          this.promoteTo = this.onPromotion()
          this.hisMoves += this.promoteTo
        }
        this.game.move({ from: orig, to: dest, promotion: this.promoteTo }) // promote to queen for simplicity
        this.board.set({
          // fen: this.game.fen(),
          turnColor: this.toColor(),
          movable: {
            color: this.toColor(),
            dests: this.possibleMoves()
          }
        })
        this.calculatePromotions()
        this.afterMove()
      }
    },
    afterMove() {
      if (this.showThreats) {
        this.paintThreats()
      }
      let threats = this.countThreats(this.toColor()) || {}
      threats['history'] = this.game.history()
      threats['fen'] = this.game.fen()
      threats['hisMoves'] = this.hisMoves
      this.game.game_over()
        ? (threats['end_game'] = this.game.game_over())
        : (threats['end_game'] = false)
      this.$emit('onMove', threats)
    },
    countThreats(color) {
      let threats = {}
      let captures = 0
      let checks = 0
      let moves = this.game.moves({ verbose: true })
      if (color !== this.toColor()) {
        moves = this.opponentMoves()
      }

      if (moves.length === 0) {
        return null // It´s an invalid position
      }

      moves.forEach(function(move) {
        if (move['captured']) {
          captures++
        }
        if (move['san'].includes('+')) {
          checks++
        }
      })

      threats[`legal_${color}`] = uniques(moves.map(x => x.from + x.to)).length // promotions count as 4 moves. This remove those duplicates moves.
      threats[`checks_${color}`] = checks
      threats[`threat_${color}`] = captures
      threats[`turn`] = color
      return threats
    },
    loadPosition() {
      console.log("init load")
      // set a default value for the configuration object itself to allow call to loadPosition()
      this.game.load(this.fen)
      this.hisMoves = ''
      this.board = Chessground(this.$refs.board, {
        fen: this.game.fen(),
        turnColor: this.toColor(),
        movable: {
          color: this.toColor(),
          free: this.free,
          dests: this.possibleMoves()
        },
        orientation: this.orientation
      })
      this.board.set({
        movable: { events: { after: this.changeTurn() } }
      })
      this.afterMove()
    },
    loadOnlyFen() {
      console.log('load Only fen')
      console.log('fen')
      console.log(this.fen)
      console.log('game')
      console.log(this.game.fen())
      this.board = Chessground(this.$refs.board, {
        fen: this.fen,
        movable: {
          color: this.fen.split(' ')[1] === 'w' ? 'white' : 'black',
          free: this.free,
          dests: this.possibleMoves()
        }
      })

      this.afterMove()
    },
    loadMove() {
      console.log(this.move)
      this.hisMoves += ' ' + this.move
      // console.log("ahihi load ne")
      // console.log(this.move)
      this.game.move({
        from: this.move.substring(0, 2),
        to: this.move.substring(2, 4),
        promotion: this.move.charAt(4)
      })
      this.board.set({
        fen: this.game.fen(),
        turnColor: this.toColor(),
        movable: {
          color: this.toColor(),
          dests: this.possibleMoves()
          // events: { after: this.changeTurn() }
        }
      })
      this.fen = this.game.fen()
      this.calculatePromotions()
      this.afterMove()
    }
  }
}
</script>
<style>
.black, .white {
  background-color: transparent !important;
}
</style>
