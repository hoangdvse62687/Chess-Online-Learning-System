<template>
  <div>
    <v-layout align-center justify-center row wrap>
      <v-flex xs10>
        <v-form ref="form" lazy-validation>
          <v-layout wrap>
            <v-flex xs6 pr-1>
              <v-text-field
                :rules="lessonNameRules"
                color="blue-grey darken-1"
                label="Tên Bài Học:"
                v-model="lessonName"
              ></v-text-field>
            </v-flex>
            <v-flex xs6 pl-1>
              <v-text-field
                :rules="lessonDesRules"
                color="blue-grey darken-1"
                label="Mô tả:"
                v-model="lessonDes"
              ></v-text-field>
            </v-flex>
            <v-flex xs12>
              <v-alert dense outlined type="error" v-if="errors.length > 0">{{errors}}</v-alert>
            </v-flex>
            <v-flex xs12>
              <ckeditor5 :content="content" />
            </v-flex>
          </v-layout>
        </v-form>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="orange darken-3"
            dark
            depressed
            @click="isEditing ? updateUninteractiveLesson() : createUninteractiveLesson()"
            v-text="isEditing ? 'Lưu' : 'Thêm'"
          ></v-btn>
        </v-card-actions>
      </v-flex>
      <Loader v-if="loader" />
    </v-layout>
  </div>
</template>
<script>
import ckeditor5 from './htmlEditor/Ckeditor5'
import Loader from '@/components/Loader'
import Swal from 'sweetalert2'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const lessonRepository = RepositoryFactory.get('lesson')

export default {
  components: {
    ckeditor5,
    Loader
  },
  props: {
    editingLessonId: {
      type: Number,
      default: -1
    },
    lessonIdInput: {
      required: false,
      type: Number,
      default: 0
    },
    courseIdInput: {
      required: false,
      type: Number,
      default: 0
    },
    courseId: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      content: '',
      lessonName: '',
      lessonDes: '',
      editor: ckeditor5,
      errors: '',
      loader: false,
      uninteractiveLessonId: 0,
      isEditing: false,
      lessonId: this.lessonIdInput,
      isUpdatedName: false,
      lessonNameRules: [
        v => !!v || 'Tên bài học không được bỏ trống',
        v => (v && v.length >= 6) || 'Tên bài học phải có ít nhất 6 kí tự'
      ],
      lessonDesRules: [v => !!v || 'Mô tả bài học không được bỏ trống']
    }
  },
  watch: {
    //help passing data from child to parent after created
    // id and name will help you display in list lesson
    name: {
      handler: function() {
        this.isUpdatedName = false
      }
    },
    isUpdatedName: {
      handler: function() {
        if (this.isUpdatedName) {
          this.$emit('newdata', [this.lessonId, this.name])
        }
      },
      deep: true
    },
    editingLessonId: function(newId) {
      this.editingLessonId = newId
      if (this.editingLessonId > 0) {
        console.log('is editing')
        this.getById(this.editingLessonId)
        this.isEditing = true
      }
    }
  },
  created() {
    if (this.editingLessonId > 0) {
      this.getById(this.editingLessonId)
      this.isEditing = true
    } else {
      this.content = ''
    }
  },
  methods: {
    createUninteractiveLesson() {
      if (this.editor.methods.getText().length === 0) {
        this.errors = 'Nội dung bài học không được bỏ trống'
      } else {
        if (this.$refs.form.validate()) {
          const lesson = {
            uninteractiveLesson: {
              content: this.editor.methods.getVal()
            },
            name: this.lessonName,
            description: this.lessonDes
          }
          this.$emit('onAddUninteractiveLesson', lesson)
        }
      }
    },
    async getById(lessonId) {
      const data = await lessonRepository.getById(lessonId)
      this.lessonName = data.data.data.name
      this.lessonDes = data.data.data.description
      console.log(data)
      this.content = data.data.data.lessonContent.content
    },
    updateUninteractiveLesson() {
      if (this.editor.methods.getText().length === 0) {
        this.errors = 'Nội dung bài học không được bỏ trống'
      }
      if (this.$refs.form.validate()) {
        const lesson = {
          name: this.lessonName,
          lessonId: this.editingLessonId,
          description: this.lessonDes,
          uninteractiveLesson: {
            content: this.editor.methods.getVal()
          }
        }
        this.$emit('onEditUninteractiveLesson', lesson)
      }
    },
    scrollTop: function() {
      document.documentElement.scrollTop = 0
    }
  }
}
</script>
<style scoped>
input {
  color: #757575 !important;
}
.text-danger {
  color: #dc3545 !important;
}
</style>
