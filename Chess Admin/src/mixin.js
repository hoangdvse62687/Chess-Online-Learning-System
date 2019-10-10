import Vue from 'vue'
import firebase from 'firebase'
const axios = require('axios')
const roles = {
  INSTRUCTOR: "Người hướng dẫn",
  LEARNER: "Người học",
  ADMIN: "Quản trị viên",
  REGISTRATION: "Thành viên mới"
}
const userStatus = {
  ACTIVE: "Kích hoạt",
  INACTIVE: "Vô hiệu hóa"
}
export default Vue.mixin({
  methods: {
    async callAxios(method, url, params) {
      //get token from localStorage
      var token = this.getTokenFromLocalStorage()
      let config = {
        method: method,
        url: url,
        params: params,
        headers: {
          Authorization: 'Chess ' + token
        }
      }
      return await axios(config)
    },
    removeClassNameOfElement(sourceClass, targetClass) {
      let arr = document.getElementsByClassName(sourceClass)
      if (arr != undefined && arr != null && arr.length !== 0) {
        Array.prototype.forEach.call(arr, function (e) {
          e.classList.remove(targetClass)
        })
      }
    },
    removeHighlightForBoard(boardName) {
      let board = document.getElementById(boardName)
      let currentHighLight = board.getElementsByClassName('square-55d63')
      Array.prototype.forEach.call(currentHighLight, function (square) {
        square.classList.remove('highlight-white')
      })
    },
    addHighlightForBoard(boardName, fromSquare, toSquare) {
      let currentBoard = document.getElementById(boardName)
      let fromSquareArr = currentBoard.getElementsByClassName(
        `square-${fromSquare}`
      )
      let toSquareArr = currentBoard.getElementsByClassName(`square-${toSquare}`)
      Array.prototype.forEach.call(fromSquareArr, function (square) {
        square.classList.add('highlight-white')
      })
      Array.prototype.forEach.call(toSquareArr, function (square) {
        square.classList.add('highlight-white')
      })
    },
    getCurrentPage() {
      return window.location.href
    },
    getTokenFromLocalStorage() {
      return localStorage.getItem('access-token')
    },
    getRoleName(roleId) {
      switch (roleId) {
        case 1:
          return roles.INSTRUCTOR
        case 2:
          return roles.LEARNER
        case 3:
          return roles.ADMIN
        case 4:
          return roles.REGISTRATION
      }
    },
    getCourseRoleName(roleId) {
      switch (roleId) {
        case 1:
          return 'Đang soạn thảo'
        case 2:
          return 'Công khai'
        case 3:
          return 'Đã xóa'
        case 4:
          return 'Chờ xét duyệt'
        case 5:
          return 'Đã từ chối'
      }
    },
    getStatusUser(active) {
      if (active) {
        return userStatus.ACTIVE
      } else {
        return userStatus.INACTIVE
      }
    },

    formatListUser(users) {
      users.forEach(user => {
        user.roleName = this.getRoleName(user.roleId)
        user.status = this.getStatusUser(user.isActive)
      })
      return users
    },

    getStatusCourse(statusId) {
      switch (statusId) {
        case 1:
          return 'Đang soạn thảo'
        case 2:
          return 'Công khai'
        case 3:
          return 'Đã xóa'
        case 4:
          return 'Chờ duyệt'
        case 5:
          return 'Từ chối'
      }
    },
    getLevelById(eloId) {
      const eloEnum = [
        'Mới bắt đầu',
        'Sơ cấp',
        'Nghiệp dư',
        'Chuyên nghiệp',
        'Cao thủ'
      ]
      return eloEnum[eloId - 1]
    },
    getDateTimeFormat(datetime) {
      const date = new Date(Date.parse(datetime))
      return date.toLocaleString()
    },
    formatListCourse(courses) {
      courses.forEach(course => {
        course.statusName = this.getStatusCourse(course.statusId)
        course.courseCreatedDate = this.getDateTimeFormat(
          course.courseCreatedDate
        )
      })
      return courses
    },
    async uploadImageByDataURL(image, imageName, directory) {
      const uploadTask = firebase
        .storage()
        .ref(`images/${directory}/${imageName}`)
      const link = await uploadTask
        .putString(image, 'data_url')
        .then(async () => {
          const imageLink = await firebase
            .storage()
            .ref(`images/${directory}`)
            .child(`${imageName}`)
            .getDownloadURL()
            .then(url => {
              console.log(url)
              return url
            })
          return imageLink
        })
      return link
    },
    async uploadImageByFile(image, imageName, directory) {
      const uploadTask = firebase
        .storage()
        .ref(`images/${directory}/${imageName}`)
      const link = await uploadTask
        .put(image)
        .then(async () => {
          const imageLink = await firebase
            .storage()
            .ref(`images/${directory}`)
            .child(`${imageName}`)
            .getDownloadURL()
            .then(url => {
              return url
            })
          return imageLink
        })
      return link
    },
    isEmpty(obj) {
      if (obj !== null && obj !== undefined) {
        return false
      }
      return true
    },
  }
})