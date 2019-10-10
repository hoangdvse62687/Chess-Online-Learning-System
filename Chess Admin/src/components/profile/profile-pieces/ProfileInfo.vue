<template>
  <v-card :elevation="8">
    <v-card-title>
      <h3 class="headline">Thông tin</h3>
      <v-spacer></v-spacer>
        <v-btn v-if="isUpdate" color="darken-3" fab small @click="isEditing = !isEditing">
          <v-icon v-if="isEditing">mdi-close</v-icon>
          <v-icon v-else>mdi-pencil</v-icon>
        </v-btn>
    </v-card-title>
    <v-form>
      <v-container pa-5 grid-list-xl>
        <v-layout wrap>
          <v-flex xs12>
            <v-text-field color="amber darken-1" v-model="user.fullName" label="Họ và tên" :disabled="!isEditing"></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field :value="user.email" label="Email" disabled></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field :value="user.status" label="Trạng thái" disabled></v-text-field>
          </v-flex>
          <v-flex xs12 v-if="isInstructor">
            <v-textarea
          color="amber darken-1"
          name="input-7-1"
          label="Thành tích"
          v-model="user.achievement"
          hint="Hint text"
          :disabled="!isEditing"
        ></v-textarea>
        <div v-show="isInstructor">
          <p class="subheading grey--text">Các chứng nhận</p>
          <v-layout v-if="!isEditing" wrap>
            <v-flex
              v-for="(item,index) in user.certificates"
              :key="index"
              xs4
            >
              <v-card text tile>
                <v-img
                  :src="item.certificateLink"
                  height="180px"
                ></v-img>
              </v-card>
            </v-flex>
          </v-layout>
          <v-layout v-if="isEditing" wrap>
            <v-flex xs12>
              <vue-upload-multiple-image
                @upload-success="uploadImageSuccess"
                @before-remove="beforeRemove"
                @edit-image="editImage"
                :dataImages="certificates"
              ></vue-upload-multiple-image>
            </v-flex>
          </v-layout>
        </div>
          </v-flex>
        </v-layout>
      </v-container>
    </v-form>
    <v-divider class="my-2" v-if="isUpdate"></v-divider>
    <v-card-actions class="px-5">
      <v-btn :disabled="!isEditing" v-if="isUpdate" color="success" @click="save">Lưu</v-btn>
    </v-card-actions>
    <v-snackbar v-model="hasSaved" top :timeout="5000">
      Thông tin đã được cập nhật
      <v-btn @click="snackbar = false" fab icon color="grey">
        <v-icon>close</v-icon>
      </v-btn>
    </v-snackbar>
  </v-card>
</template>

<script>
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const userRepository = RepositoryFactory.get('user')
import firebase from 'firebase'
import VueUploadMultipleImage from '@/components/plugins/vue-upload-multiple-image/VueUploadMultipleImage'
import VueAvatar from '@/components/plugins/vue-avatar/VueAvatarEditor'

export default {
  props: {
    isInstructor: {
      type: Boolean,
      default: false
    },
    userInput:{
      required:false,
      type:Object,
      default:null
    },
    isUpdate:{
      required:false,
      type:Boolean,
      default:true
    }
  },
  components: {
    VueUploadMultipleImage,
    VueAvatar
  },
  data() {
    return {
      hasSaved: false,
      isEditing: null,
      user:this.handleUserData(this.userInput),
      certificates:[],
      listUpload: [],
    }
  },
  methods: {
    save() {
      this.$store.commit('incrementLoader', 1)
      this.isEditing = !this.isEditing
      let match = this.user.email.match(/^([^@]*)/)
      this.certificates.forEach(el => {
        if(el.path.indexOf('http') > -1){
            let newImage = {
              file: null,
              done: true,
              url: el.path,
              directory: 'certificates'
            }

            this.listUpload.push(newImage)
          }else{
            this.uploadImageByDataURL(el.path, match[0] + el.name, 'certificates')
          }
      })

      var checkUploadImgProgress = setInterval(() => {
        let done = true
        this.listUpload.forEach(el => {
          if (!el.done) {
            done = false
          }
        })

        if (done) {
          this.user.certificates = []
          this.listUpload.forEach(el => {
            this.user.certificates.push({
              certificateLink: el.url
            })
          })
          var self = this
          userRepository.updateProfile(this.user).then(res => {
            self.hasSaved = true
            self.listUpload = []
            this.$store.commit('incrementLoader', -1)
          })
          clearInterval(checkUploadImgProgress)
        }
      }, 1000)
      
    },
    handleUserData(user){
      if(user){
        var obj = user
        obj.roleName = this.getRoleName(user.roleId)
        obj.status = this.getStatusUser(user.active)
        return obj
      }else{
        return this.$store.state.user
      }
    },
    uploadImageSuccess(formData, index, fileList) {
      this.certificates = fileList
    },
    beforeRemove(index, done,fileList) {
      done()
      this.certificates = fileList
    },
    editImage(formData, index, fileList){
      this.certificates = fileList
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
  },
  watch:{
    userInput:{
      handler:function(){
        this.user = this.handleUserData(this.userInput)
        //forced updating status
        this.$forceUpdate()
      },
      deep:true
    },
    isEditing:{
      handler:function(){
        if(this.isEditing && this.isEditing){
          if(this.user.certificates){
            var self = this
            self.certificates = []
            this.user.certificates.forEach(function(item,index){
              self.certificates.push({
                default:0,
                highlight:index == 0 ? 1 : 0,
                name:item.certificateLink.substring(item.certificateLink.lastIndexOf('/') + 1,item.certificateLink.length),
                path:item.certificateLink
              })
            })
          }
        }
      }
    }
  }
}
</script>

<style scoped>
</style>
