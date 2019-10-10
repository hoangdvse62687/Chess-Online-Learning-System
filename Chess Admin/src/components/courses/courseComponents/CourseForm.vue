<template>
    <div>
      <v-form ref="form" lazy-validation>
        <v-container class="pa-5" grid-list-xs>
          <v-layout wrap>
            <v-flex xs9>
              <v-text-field
                color="amber darken-1"
                v-model="course.name"
                label="Tên khóa học"
                :rules="nameRules"
                required
              ></v-text-field>
            </v-flex>
            <v-flex xs3>
              <v-select
                v-model="course.requiredElo"
                :items="listLevel"
                item-text="levelName"
                item-value="levelId"
                label="Yêu cầu"
                color="amber darken-1"
                item-color="amber darken-1"
              ></v-select>
            </v-flex>
            <v-flex xs12>
              <v-textarea
                v-model="course.description"
                label="Mô tả: "
                value
                required
                color="amber darken-1"
                :rules="descriptionRules"
              ></v-textarea>
            </v-flex>
            <v-flex xs12 v-if="editedCourse === null">
              <v-select
                v-model="course.listCategoryIds"
                :items="listCategories"
                item-text="name"
                item-value="categoryId"
                multiple
                chips
                label="Danh mục"
                color="amber darken-1"
                item-color="amber darken-1"
              ></v-select>
            </v-flex>
          </v-layout>
        </v-container>
      </v-form>
      <v-divider class="my-2" v-if="editedCourse === null"></v-divider>
      <v-card-actions class="px-5" >
        <v-spacer></v-spacer>
        <v-btn depressed dark @click="editedCourse === null ? resetForm() : close()"  color="amber darken-2">{{editedCourse === null ? 'Xóa toàn bộ' : 'Đóng'}}</v-btn>
        <v-btn @click="createCourse" depressed dark color="amber darken-4">Hoàn tất</v-btn>
      </v-card-actions>
    </div>    
</template>
<script>
import { mapState } from 'vuex'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const categoryRepository = RepositoryFactory.get('category')
const courseRepository = RepositoryFactory.get('course')
export default {
  components: {
  },
  props: {
    // courseImage: {
    //   type: File,
    //   default: null
    // },
    editedCourse: {
      type: Object,
      default: null
    }
  },
  watch: {
    courseImage: function() {
      this.backgroundImage = window.URL.createObjectURL(this.courseImage)
    },
    editedCourse: function() {
      console.log(this.editedCourse)
      if (this.editedCourse !== null) {
        this.course = this.editedCourse
        this.backgroundImage = this.course.image
        this.courseStatusName = this.getCourseRoleName(this.course.statusId)
      }
    }
  },
  data() {
    return {
      image: [],
      valueName: '',
      email: this.$store.state.user.email,
      loader: false,
      course: {
        name: '',
        requiredElo: 1,
        rewardPoint: 0,
        description: '',
        listCategoryIds: [],
        image: ''
      },
      listLevel: [
        {levelId: 1,levelName: 'Mới bắt đầu'},
        {levelId: 2,levelName: 'Sơ cấp'},
        {levelId: 3,levelName: 'Nghiệp dư'},
        {levelId: 4,levelName: 'Chuyên nghiệp'},
        {levelId: 5,levelName: 'Cao thủ'},
      ],
      listCategories: [],
      nameRules: [v => !!v || 'Tên khóa học không được để trống', v => v.length > 6 || 'Tên khóa học không được ít hơn 6 kí tự'],
      pointRules: [v => /^\d+$/.test(v) || 'Điểm phải là giá trị số'],
      descriptionRules: [v => !!v || 'Mô tả khóa học không được để trống'],
      backgroundImage: '',
      courseStatusName: '',
      isEditingCourse: false
    }
  },
  mounted() {
    this.loader = true
    this.getCategories()
    if (this.editedCourse !== null) {
      this.course = this.editedCourse
      this.backgroundImage = this.course.image
      this.listCategories = this.course.listCategorys
    }
    this.loader = false
  },
  computed: {},
  methods: {
    resetForm() {
      this.$refs.form.reset()
      this.$refs.form.resetValidation()
    },
    async getCategories() {
      const { data } = await categoryRepository.getCategories()
      this.listCategories = data.data
    },
    createCourse() {
      if (this.$refs.form.validate()) {
        this.$emit('createdCourse', this.course)
        // let match = this.$store.state.user.email.match(/^([^@]*)/)
        // let courseSlug = this.course.name.toLowerCase().split(" ").join('-')
        // this.course.image = await this.uploadImageByFile(this.courseImage, courseSlug, `courses/${match[0]}`)
        // console.log(this.course)
        // const {data} = await courseRepository.createCourse(this.course)
      }
    },
    close() {
        this.$emit('closeForm', true)
      }
  }
}
</script>
<style scoped>

</style>