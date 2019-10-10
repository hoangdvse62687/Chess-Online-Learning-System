import Vue from 'vue'
import firebase from 'firebase'
export default Vue.mixin({
  methods: {
    getCurrentPage() {
      return window.location.href
    },
    getTokenFromLocalStorage() {
      return localStorage.getItem('access-token')
    },
    // getStatusCourse(statusId) {
    //   switch (statusId) {
    //     case 1:
    //       return 'Đang soạn thảo'
    //     case 2:
    //       return 'Công khai'
    //     case 3:
    //       return 'Đã xóa'
    //     case 4:
    //       return 'Chờ duyệt'
    //     case 5:
    //       return 'Từ chối'
    //   }
    // },
    // getDateTimeFormat(datetime) {
    //   const date = new Date(Date.parse(datetime))
    //   return date.toLocaleString()
    // },
    getRoleName(roleId) {
      switch (roleId) {
        case 1:
          return 'Người hướng dẫn'
        case 2:
          return 'Người học'
        case 3:
          return 'Quản trị viên'
        case 4:
          return 'Thành viên mới'
      }
    },
    getStatusUser(active) {
      if (active) {
        return 'Kích hoạt'
      } else {
        return 'Vô hiệu hóa'
      }
    },
    getEloName(eloId) {
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
    getLessonTypeName(lessonTypeId) {
      switch (lessonTypeId) {
        case 2:
          return 'Trận đấu'
        case 3:
          return 'Lý thuyết'
        case 5:
          return 'Thực hành'
      }
    },
    async uploadImageByDataURL2(image, imageName, directory) {
      const uploadTask = firebase
        .storage()
        .ref(`images/${directory}/${imageName}`)
        .putString(image, 'data_url')

      let imageLink = null
      uploadTask.on(
        firebase.storage.TaskEvent.STATE_CHANGED, // or 'state_changed'
        null,
        null,
        function() {
          // Upload completed successfully, now we can get the download URL
          uploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
            imageLink = downloadURL
            console.log('File available at', downloadURL)
          })
        }
      )
      console.log(imageLink)
      return imageLink
    },
    isEmpty(obj) {
      if (obj !== null && obj !== undefined) {
        return false
      }
      return true
    }
  }
})
