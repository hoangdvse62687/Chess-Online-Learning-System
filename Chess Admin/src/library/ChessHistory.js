export default class MoveHistory {
    constructor(lessonDetails) {
      this.lessonDetails = lessonDetails
      this.moveHistory = []
      this.steps = lessonDetails.interactiveLesson.steps
      this.totalMove = 0
    }
    formatMoveHistory() {
      this.refactorPreviousFen()
      const firstStep = this.steps[0]
      this.dfs(null, firstStep, 1)
      this.refactorDepth2()
      this.refactorDepth3()
    }
  
    refactorPreviousFen() {
      this.steps.forEach(el => {
        el.class = 'move'
        const parent = this.steps.find(pr => pr.id === el.preId)
        if (this.isEmpty(parent)) {
          el.preFen = this.lessonDetails.interactiveLesson.initCode
        } else {
          el.preFen = parent.fen
        }
      })
      this.steps[0].fen = this.lessonDetails.interactiveLesson.initCode
    }
  
    isEmpty(obj) {
      if (obj !== null && obj !== undefined) {
        return false
      }
      return true
    }
  
    dfs(parent, move, depth) {
      if (depth === 2) {
        if (this.isEmpty(parent.depth2)) {
          parent.depth2 = []
        }
        parent.depth2.push(move)
      } else if (depth === 3) {
        if (this.isEmpty(parent.depth3)) {
          parent.depth3 = []
        }
        parent.depth3.push(move)
      }
      this.addMoveHistory(move, depth)
      let index = depth - 1
      let firstBranch = null
      this.steps.forEach(el => {
        if (el.preId === move.id) {
          index++
          if (this.isEmpty(move.nextId)) {
            move.nextId = el.id
          }
          if (index === depth) {
            this.dfs(parent, el, index)
            firstBranch = el
          } else {
            this.dfs(firstBranch, el, index)
          }
        }
      })
    }
  
    refactorDepth2() {
      let queue = []
      for (let index = 0; index < this.moveHistory.length; index++) {
        const el = this.moveHistory[index]
        if (el.depth === 1) {
          if (!this.isEmpty(el.whiteMove) && !this.isEmpty(el.whiteMove.depth2)) {
            const newMove = {
              index: el.index,
              whiteMove: {
                move: '...',
                nextId: null,
                preId: null,
                class: 'empty-move'
              },
              blackMove: el.blackMove,
              depth: 1
            }
            el.whiteMove.class = 'move single-move-white'
            queue.push({ index: el.index, move: el.whiteMove, pos: index + 1 })
            queue.push({ oldMove: el, newMove: newMove, pos: index + 1 })
          }
          if (!this.isEmpty(el.blackMove) && !this.isEmpty(el.blackMove.depth2)) {
            const oddMove = {
              move: '...',
              nextId: null,
              preId: null,
              class: 'empty-move'
            }
            el.blackMove.depth2.unshift(oddMove)
            queue.push({ index: el.index, move: el.blackMove, pos: index + 1 })
          }
        }
      }
      while (queue.length > 0) {
        const el = queue.pop()
        if (this.isEmpty(el.index)) {
          el.oldMove.blackMove = null
          this.moveHistory.splice(el.pos, 0, el.newMove)
        } else {
          const depth2 = {
            depth: 2,
            moveHistory: []
          }
          el.move.depth2.forEach(childStep => {
            const newTurn = {
              index: el.index + depth2.moveHistory.length,
              whiteMove: childStep,
              blackMove: null,
              depth: 2
            }
            const turn = this.getTurnOfFen(childStep.preFen)
            if (turn === 'w') {
              newTurn.whiteMove.class = 'move single-move-white'
              depth2.moveHistory.push(newTurn)
            } else {
              let lastMove = depth2.moveHistory[depth2.moveHistory.length - 1]
              lastMove.whiteMove.class = 'move'
              if (lastMove.whiteMove.move === '...') {
                lastMove.whiteMove.class = 'empty-move'
              }
              lastMove.blackMove = childStep
            }
          })
          this.moveHistory.splice(el.pos, 0, depth2)
        }
      }
    }
  
    getTurnOfFen(fen) {
      if (this.isEmpty(fen)) {
        return 'w'
      }
      const arr = fen.split(' ')
      return arr[1]
    }
  
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
  
    refactorDepth3() {
      let queue = []
      this.moveHistory.forEach(el => {
        if (el.depth === 2) {
          const listDepth2 = el.moveHistory
          listDepth2.forEach((moved2, pos) => {
            if (
              !this.isEmpty(moved2.whiteMove) &&
              !this.isEmpty(moved2.whiteMove.depth3)
            ) {
              const newMove = {
                index: moved2.index,
                whiteMove: {
                  move: '...',
                  nextId: null,
                  preId: null,
                  class: 'empty-move'
                },
                blackMove: moved2.blackMove,
                depth: 2
              }
              moved2.whiteMove.class = 'move single-move-white'
              queue.push({
                index: moved2.index,
                move: moved2.whiteMove,
                pos: pos + 1,
                list: listDepth2
              })
              queue.push({
                oldMove: moved2,
                newMove: newMove,
                pos: pos + 1,
                list: listDepth2
              })
            }
            if (
              !this.isEmpty(moved2.blackMove) &&
              !this.isEmpty(moved2.blackMove.depth3)
            ) {
              const oddMove = {
                move: '...',
                nextId: null,
                preId: null,
                class: 'empty-move'
              }
              moved2.blackMove.depth3.unshift(oddMove)
              queue.push({
                index: moved2.index,
                move: moved2.blackMove,
                pos: pos + 1,
                list: listDepth2
              })
            }
          })
        }
      })
      while (queue.length > 0) {
        const el = queue.pop()
        if (this.isEmpty(el.index)) {
          el.oldMove.blackMove = null
          el.list.splice(el.pos, 0, el.newMove)
        } else {
          const depth3 = {
            depth: 3,
            moveHistory: []
          }
          el.move.depth3.forEach(childStep => {
            const newTurn = {
              index: el.index + depth3.moveHistory.length,
              whiteMove: childStep,
              blackMove: null,
              depth: 2
            }
            const turn = this.getTurnOfFen(childStep.preFen)
            if (turn === 'w') {
              newTurn.whiteMove.class = 'move single-move-white'
              depth3.moveHistory.push(newTurn)
            } else {
              let lastMove = depth3.moveHistory[depth3.moveHistory.length - 1]
              lastMove.whiteMove.class = 'move'
              if (lastMove.whiteMove.move === '...') {
                lastMove.whiteMove.class = 'empty-move'
              }
              lastMove.blackMove = childStep
            }
          })
          el.list.splice(el.pos, 0, depth3)
        }
      }
    }
  
    get getMoveHistory() {
      return this.moveHistory
    }
  
    get getSteps() {
      return this.steps
    }
  }
  