<template>
  <v-card>
    <v-card-title>Đăng ký</v-card-title>
    <v-form ref="form" v-model="valid" lazy-validation class="pa-4">
      <v-hover>
        <v-avatar slot-scope="{ hover }" :tile="false" :size="100" color="grey lighten-4">
          <img :src="user.avatar" alt="avatar" />
          <v-fade-transition>
            <div v-if="hover" class="upload-file">
              <span class="white--text lighten-3 body-1">Tải ảnh</span>
              <br />
              <v-btn class="ma-0" text icon @click="avaDialog = true">
                <v-icon small color="grey lighten-3">fa-camera</v-icon>
              </v-btn>
            </div>
          </v-fade-transition>
        </v-avatar>
      </v-hover>
      <v-text-field v-model="user.email" label="E-mail" disabled></v-text-field>
      <v-text-field
        v-model="user.fullName"
        :counter="50"
        :rules="fullNameRules"
        label="Họ và tên"
        required
      ></v-text-field>
      <p class="subheading grey--text">Chọn loại tài khoản</p>
      <v-radio-group v-model="role" row>
        <v-radio color="primary" label="Học viên" value="learner"></v-radio>
        <v-radio color="primary" label="Người hướng dẫn" value="instructor"></v-radio>
      </v-radio-group>
      <p class="subheading grey--text mb-0" v-if="!isInstructor">Trình độ</p>
      <v-slide-y-transition>
        <v-layout>
          <v-flex xs11>
            <v-select
              class="pt-1"
              v-show="!isInstructor"
              v-model="selectElo"
              :items="items"
              item-text="eloName"
              item-value="eloId"
              label="Select"
              return-object
              single-line
            ></v-select>
          </v-flex>
          <v-flex xs1 pt-3>
            <v-layout justify-center>
              <span class="btn-faq" @click="levelDialog = true">
                <img :src="faqLogo" height="30" width="30" />
              </span>
            </v-layout>
          </v-flex>
        </v-layout>
      </v-slide-y-transition>
      <v-slide-y-transition>
        <v-textarea
          v-show="isInstructor"
          v-model="user.achievement"
          solo
          name="input-7-4"
          label="Thành tích"
          value
        ></v-textarea>
      </v-slide-y-transition>
      <v-slide-y-transition>
        <div v-show="isInstructor">
          <p class="subheading grey--text">Các chứng nhận</p>
          <vue-upload-multiple-image
            @upload-success="uploadImageSuccess"
            @before-remove="beforeRemove"
          ></vue-upload-multiple-image>
        </div>
      </v-slide-y-transition>
      <template v-show="!isInstructor">
        <v-slide-y-transition></v-slide-y-transition>
      </template>
      <div class="terms">
        <v-card-text v-for="(item,index) in terms" :key="index" class="pb-0">{{item}}</v-card-text>
      </div>
      <v-checkbox
        v-model="checkbox"
        :rules="[v => !!v || 'Bạn phải đồng ý để tiếp tục']"
        label="Bạn đã đọc các điều khoản sử dụng của hệ thống?"
        color="primary"
        required
      ></v-checkbox>
      <v-dialog v-model="avaDialog" max-width="382">
        <v-card>
          <v-card-title class="headline">Ảnh đại diện</v-card-title>
          <vue-avatar
            :width="300"
            :height="300"
            :has-rotation="false"
            :border-radius="150"
            @finished="handlerUploadImage"
          ></vue-avatar>
        </v-card>
      </v-dialog>
      <v-dialog v-model="levelDialog" width="500px">
        <v-card>
          <v-card-title>
            <span class="headline">Các cấp độ</span>
          </v-card-title>
          <v-layout wrap px-5>
            <template v-for="(item,index) in levelNote">
              <v-flex xs3 :key="index*2" mt-2>
                <span class="level-title">{{item.levelTitle}}</span>
              </v-flex>
              <v-flex xs9 :key="index*2+1" mt-2>
                <span class="level-description">{{item.levelDescription}}</span>
              </v-flex>
            </template>
          </v-layout>
          <v-card-actions>
            <div class="flex-grow-1"></div>
            <v-btn color="green darken-1" text @click="levelDialog = false">Đã hiểu</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn :disabled="!checkbox" color="primary" @click="submit">Xác nhận</v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script>
import VueUploadMultipleImage from '@/components/plugins/vue-upload-multiple-image/VueUploadMultipleImage'
import VueAvatar from '@/components/plugins/vue-avatar/VueAvatarEditor'
import Repository, { setAuthorizationHeader } from '@/repository/Repository.js'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
import terms from '@/data/terms.json'
const userRepository = RepositoryFactory.get('user')
import firebase from 'firebase'
import { setInterval, clearInterval } from 'timers'
export default {
  name: 'RegisterForm',
  components: {
    VueUploadMultipleImage,
    VueAvatar
  },
  data() {
    return {
      terms,
      faqLogo: require('@/assets/images/faq.png'),
      valid: true,
      role: 'learner',
      isInstructor: false,
      avaDialog: false,
      levelDialog: false,
      checkbox: false,
      user: '',
      fullNameRules: [
        v => !!v || 'Tên không được bỏ trống',
        v => (v && v.length <= 50) || 'Tên không được quá 50 kí tự'
      ],
      imageUrl: null,
      listUpload: [],
      isChangedAvatar: false,
      selectElo: { eloName: 'Mới bắt đầu', eloId: 1 },
      items: [
        { eloName: 'Mới bắt đầu', eloId: 1 },
        { eloName: 'Sơ cấp', eloId: 2 },
        { eloName: 'Nghiệp dư', eloId: 3 },
        { eloName: 'Chuyên nghiệp', eloId: 4 },
        { eloName: 'Cao thủ', eloId: 5 }
      ],
      levelNote: [
        {
          levelTitle: 'Mới bắt đầu',
          levelDescription: '800 điểm - dành cho người chưa biết gì về cờ vua'
        },
        {
          levelTitle: 'Sơ cấp',
          levelDescription:
            '1000 điểm - dành cho người mới biết các nước đi cơ bản và các luật chơi về cơ vua'
        },
        {
          levelTitle: 'Nghiệp dư',
          levelDescription:
            '1200 điểm - dành cho người đã từng tham gia các giải cờ vua nhỏ lẻ'
        },
        {
          levelTitle: 'Chuyên nghiệp',
          levelDescription:
            '1400 điểm - dành cho người đã từng đi thi các giải đấu lớn, cấp khu vực trở lên'
        },
        {
          levelTitle: 'Cao thủ',
          levelDescription:
            '1600 điểm - dành cho người muốn đi thi đấu quốc gia, quốc tế'
        }
      ]
    }
  },
  watch: {
    role: function(role) {
      this.role = role
      this.isInstructor = role == 'instructor'
    }
  },
  created() {
    this.$store.commit('incrementLoader', 1)
    if (
      localStorage.getItem('access-token') != null &&
      this.$store.state.user === null
    ) {
      setAuthorizationHeader(Repository, localStorage.getItem('access-token'))
      this.getCurrentUserDetail()
    } else {
      this.user = JSON.parse(localStorage.getItem('user'))
    }
    setTimeout(() => {
      this.$store.commit('incrementLoader', -1)
    }, 500)
  },
  methods: {
    async getCurrentUserDetail() {
      const { data } = await userRepository.getCurrentUserDetail()
      this.user = data.data
    },
    uploadImageSuccess(formData, index, fileList) {
      this.certificates = fileList
    },
    handlerUploadImage(data) {
      this.user.avatar = data.toDataURL()
      this.isChangedAvatar = true
      this.avaDialog = false
    },
    beforeRemove(index, done) {
      done()
    },
    async submit() {
      if (this.isInstructor && this.isEmpty(this.certificates)) {
        this.$swal({
          type: 'error',
          title: 'Không thể đăng kí',
          text: 'Bạn phải cập nhật các chứng nhận.',
          confirmButtonText: 'Xác nhận'
        })
        return
      }
      this.$store.commit('incrementLoader', 1)
      let match = this.user.email.match(/^([^@]*)/)
      if (this.isChangedAvatar) {
        this.uploadImageByDataURL(this.user.avatar, match[0], 'ava')
      }
      this.user.roleId = this.isInstructor ? 1 : 2
      this.user.eloId = this.selectElo.eloId
      if (this.isInstructor) {
        this.certificates.forEach(el => {
          this.uploadImageByDataURL(el.path, match[0] + el.name, 'certificates')
        })
      }
      var checkUploadImgProgress = setInterval(async () => {
        let done = true
        this.listUpload.forEach(el => {
          if (!el.done) {
            done = false
          }
        })

        if (done) {
          this.user.certificates = []
          this.listUpload.forEach(el => {
            if (el.directory === 'certificates') {
              this.user.certificates.push({
                certificateLink: el.url
              })
            } else if (el.directory === 'ava') {
              this.user.avatar = el.url
            }
          })
          this.signUpNewAccount()
          clearInterval(checkUploadImgProgress)
        }
      }, 1000)
    },
    async uploadImageByDataURL(image, imageName, directory) {
      const uploadTask = firebase
        .storage()
        .ref(`images/${directory}/${imageName}`)
        .putString(image, 'data_url')
      uploadTask.on(
        firebase.storage.TaskEvent.STATE_CHANGED, // or 'state_changed'
        null,
        null,
        () => {
          // Upload completed successfully, now we can get the download URL
          uploadTask.snapshot.ref.getDownloadURL().then(downloadURL => {
            newImage.done = true
            newImage.url = downloadURL
          })
        }
      )
      let newImage = {
        file: uploadTask,
        done: false,
        url: null,
        directory: directory
      }

      this.listUpload.push(newImage)
    },
    async signUpNewAccount() {
      this.user.active = true
      this.user.fullName = this.user.fullName.trim()
      if (this.user.achievement !== null) {
        this.user.achievement = this.user.achievement.trim()
      }
      const { data } = await userRepository.signUpNewAccount(this.user)
      if (data.data) {
        this.user = data.data
        localStorage.removeItem('user')
        localStorage.setItem('user', JSON.stringify(this.user))
        localStorage.removeItem('role')
        localStorage.setItem('role', this.user.roleId)
        this.$store.commit('setUser', this.user)
        if (this.isInstructor) {
          let r = confirm(
            'Đăng kí tài khoản thành công, chuyển đến trang quản trị?'
          )
          if (r == true) {
            window.location.href = 'http://admin-cols.ml/'
          } else {
            this.$router.push('/')
          }
        } else {
          this.$router.push({ name: 'courseOverView', params: { isNew: true } })
        }
      }
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    }
  }
}
</script>

<style scoped>
.v-avatar {
  display: block;
  margin: auto;
  position: relative;
}
.upload-file {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.5);
  left: 0;
  bottom: 0;
  width: 100px;
  height: 50px;
  overflow: hidden;
  border-bottom-left-radius: 50px;
  border-bottom-right-radius: 50px;
  z-index: 1;
  text-align: center;
}
.upload-file .v-btn {
  width: 26px;
  height: 26px;
}
.upload-file .v-icon {
  width: 23px;
  height: 23px;
}
.btn-faq:hover {
  cursor: pointer;
}
.level-title {
  font-size: 14px;
  font-weight: 600;
}
.level-description {
  font-size: 13px;
}
.terms {
  box-shadow: 0px 3px 1px -2px rgba(0, 0, 0, 0.2),
    0px 2px 2px 0px rgba(0, 0, 0, 0.14), 0px 1px 5px 0px rgba(0, 0, 0, 0.12);
  height: 320px;
  overflow-y: scroll;
}
</style>
