<template>
  <v-layout wrap px-3>
    <template v-if="user !== null && enrolled">
      <v-flex xs12>
        <span class="comment-title">Đánh giá của bạn</span>
      </v-flex>
      <v-flex xs12 mt-2>
        <v-rating
          v-model="newReview.rating"
          :empty-icon="emptyIcon"
          :full-icon="fullIcon"
          background-color="grey lighten-1"
          color="yellow darken-3"
          hover
          :size="18"
        ></v-rating>
      </v-flex>
      <v-flex xs9>
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-textarea
            v-model="newReview.content"
            class="mt-2"
            filled
            label="Bình luận"
            no-resize
            :counter="100"
            rows="3"
            :rules="[rules.length(6, 100)]"
            :error-messages="
          postReview && newReview.rating == 0 ? 'Vui lòng đánh giá điểm' : null
        "
          ></v-textarea>
        </v-form>
      </v-flex>
      <v-flex xs3 mb class="pt-2 pl-2">
        <v-btn color="info" @click="createReview()" :min-width="88">Đăng</v-btn>
      </v-flex>
    </template>
    <template v-if="user === null">
      <v-flex xs12 mb-3>
        <v-layout justify-center>
          <span class="text-grey required-login">
            Vui lòng
            <a @click="loginWithGoogle()">đăng nhập</a>
            để đánh giá khóa học.
          </span>
        </v-layout>
      </v-flex>
    </template>

    <template v-if="user !== null && !enrolled ">
      <v-flex xs12 mb-3>
        <v-layout justify-center>
          <span class="text-grey required-login">Bạn phải đăng ký khóa học trước khi đánh giá.</span>
        </v-layout>
      </v-flex>
    </template>
    <v-flex xs12 my-3>
      <span class="comment-title">Đánh giá về khóa học</span>
    </v-flex>
    <template v-if="courseReview.content.length > 0">
      <v-flex v-for="(item, index) in courseReview.content" :key="index" xs12 mb-1>
        <v-layout wrap class="comment-item">
          <v-flex xs1>
            <v-avatar :size="50">
              <img :src="item.reviewer.avatar" alt="avatar" />
            </v-avatar>
          </v-flex>
          <v-flex xs8 pl-3>
            <v-layout wrap align-center fill-height>
              <v-flex xs12>
                <span class="ml-1 reviewer-name">{{ item.reviewer.fullName }}</span>
              </v-flex>
              <v-flex xs12>
                <v-layout>
                  <v-flex :id="'rating-score' + item.reviewId" xs6 class="rating-score">
                    <v-rating
                      v-model="item.rating"
                      :empty-icon="emptyIcon"
                      :full-icon="fullIcon"
                      background-color="grey lighten-1"
                      color="yellow darken-3"
                      :size="14"
                      readonly
                    ></v-rating>
                  </v-flex>
                  <v-flex :id="'rating-' + item.reviewId" xs6 class="edit-rating">
                    <v-rating
                      v-model="item.rating"
                      :empty-icon="emptyIcon"
                      :full-icon="fullIcon"
                      background-color="grey lighten-1"
                      color="yellow darken-3"
                      :size="14"
                      hover
                    ></v-rating>
                  </v-flex>
                </v-layout>
              </v-flex>
            </v-layout>
          </v-flex>
          <v-flex xs3>
            <span class="caption">{{ item.relativeTime }}</span>
          </v-flex>
          <v-flex xs11 mt-2 offset-xs1 pl-3>
            <span :id="'content-' + item.reviewId" class="review-content">{{ item.content }}</span>
          </v-flex>
          <template v-if="user !== null">
            <v-flex
              :id="'edit-container-' + item.reviewId"
              xs11
              offset-xs1
              pl-3
              class="review-content-edit"
            >
              <v-textarea
                v-if="user.userId == item.reviewer.userId"
                :id="'edit-' + item.reviewId"
                v-model="item.content"
                filled
                label="Chỉnh sửa bình luận"
                no-resize
                :counter="100"
                rows="3"
                :rules="[rules.length(6, 100)]"
              ></v-textarea>
            </v-flex>
            <v-flex v-if="user.userId == item.reviewer.userId" xs12>
              <v-layout :id="'action-' + item.reviewId" justify-end class="action-review">
                <span class="mr-3" @click="editComment(item.reviewId)">Sửa</span>
                <span @click="showConfirmDeleteComment(item.reviewId)">Xóa</span>
              </v-layout>
            </v-flex>

            <v-flex v-else xs12>
              <v-layout justify-end class="action-review">
                <span style="z-index: -1;">a</span>
              </v-layout>
            </v-flex>
            <v-flex v-if="user.userId == item.reviewer.userId" xs12>
              <v-layout :id="'edit-action-' + item.reviewId" justify-end class="edit-action">
                <v-btn
                  color="info"
                  text
                  small
                  class="ma-0 mr-1"
                  @click="saveEditComment(item.reviewId)"
                >Lưu</v-btn>
                <v-btn
                  color="error"
                  text
                  class="ma-0"
                  small
                  @click="cancelEditComment(item.reviewId)"
                >Hủy</v-btn>
              </v-layout>
            </v-flex>
          </template>
        </v-layout>
      </v-flex>
      <v-flex v-if="courseReview.content.length < courseReview.totalElements" xs12>
        <v-layout justify-center>
          <span class="load-more" @click="loadMore()">Xem thêm</span>
        </v-layout>
      </v-flex>
    </template>
    <template v-else>
      <v-flex xs12>
        <v-layout justify-center>
          <span class="text-grey required-login">(Hiện chưa có đánh giá nào về khóa học này)</span>
        </v-layout>
      </v-flex>
    </template>
  </v-layout>
</template>

<script>
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')
const moment = require('moment')
export default {
  props: {
    enrolled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      valid: true,
      courseId: this.$route.params.courseId,
      user: this.$store.state.user,
      courseReview: {
        content: []
      },
      emptyIcon: 'fa-star',
      fullIcon: 'fa-star',
      halfIcon: 'fa-star-half-alt',
      newReview: {
        rating: 0,
        content: '',
        courseId: this.$route.params.courseId
      },
      postReview: false,
      rules: {
        length: (min, max) => v =>
          ((v || '').length <= max && (v || '').length >= min) ||
          `Nội dung phải từ ${min} đến ${max} kí tự.`,
        commentRules: [
          v => !!v || 'Nội dung không được để trống.',
          v => (v < 6 && v.length > 100) || 'Nội dung phải từ 6 đến 100 kí tự.'
        ]
      },

      origin: {
        content: '',
        rating: ''
      },
      updatedReview: {},
      reviewPagination: {
        page: 1,
        pageSize: 4
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  created() {
    moment.updateLocale('en', {
      relativeTime: {
        past: '%s trước',
        s: '1 giây',
        ss: '%d seconds',
        m: '1 phút',
        mm: '%d phút',
        h: '1 giờ',
        hh: '%d giờ',
        d: '1 ngày',
        dd: '%d ngày',
        M: '1 tháng',
        MM: '%d tháng',
        y: '1 năm',
        yy: '%d năm'
      }
    })
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      await this.getReviewPagination()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    loginWithGoogle() {
      var api = this.$store.state.api.login
      api += '?redirect_uri=' + this.getCurrentPage()
      window.location.href = api
    },
    async getReviewPagination() {
      const { data } = await courseRepository.getReviewPagination(
        this.courseId,
        this.reviewPagination.page,
        this.reviewPagination.pageSize
      )
      if (this.courseReview.content.length != 0) {
        this.courseReview.content = [
          ...this.courseReview.content,
          ...data.data.content
        ]
      } else {
        this.courseReview = data.data
      }
      this.formatCreatedDate()
    },
    formatCreatedDate() {
      this.courseReview.content.forEach(element => {
        element.relativeTime = this.getDateTimeFormat(element.createdDate)
        element.relativeTime = this.getDurationBetween(element.relativeTime)
      })
    },
    getDurationBetween(dateCreated) {
      return moment(dateCreated, 'MM/DD/YYYY, hh:mm:ss A').fromNow()
    },
    async createReview() {
      this.postReview = true
      if (
        this.newReview.rating > 0 &&
        this.newReview.content.length <= 100 &&
        this.newReview.content.length >= 6
      ) {
        this.$store.commit('incrementLoader', 1)
        let data = null
        const self = this
        await courseRepository
          .createReview(this.newReview)
          .then(result => (data = result.data))
          .catch(error => {
            this.$store.commit('incrementLoader', -1)
            if (error.response.status === 406) {
              self.$swal({
                type: 'error',
                title: 'Lỗi',
                text:
                  'Đánh giá không thành công. Bạn đã đánh giá khóa học này rồi.',
                confirmButtonText: 'Xác nhận'
              })
            }
          })
        if (data && data.data.success) {
          this.showNewReview(data.data.savedId)
          this.$emit('getCourseOverview')
          this.$store.commit('incrementLoader', -1)
          this.$swal({
            type: 'success',
            title: 'Thành công',
            text:
              'Đánh giá thành công. Cảm ơn bạn đã đánh giá về khóa học này.',
            confirmButtonText: 'Xác nhận'
          })
        }
      }
    },
    showNewReview(newReviewId) {
      this.newReview.relativeTime = 'vài giây trước'
      this.newReview.reviewId = newReviewId
      this.newReview.reviewer = this.$store.state.user
      this.courseReview.content = [this.newReview, ...this.courseReview.content]
      this.newReview = {
        rating: 0,
        content: '',
        courseId: this.$route.params.courseId
      }
      this.postReview = false
      this.$refs.form.resetValidation()
    },
    async updateReview(updatedReview) {
      if (updatedReview.rating > 0 && updatedReview.content.length <= 100) {
        updatedReview.courseId = this.courseId
        const { data } = await courseRepository.updateReview(updatedReview)
        if (data.data) {
          this.$emit('getCourseOverview')
        }
      }
    },
    async removeReview(reviewId) {
      this.$store.commit('incrementLoader', 1)
      const { data } = await courseRepository.removeReview(reviewId)
      this.courseReview.content = this.courseReview.content.filter(
        element => element.reviewId != reviewId
      )
      if (data.data) {
        this.$emit('getCourseOverview')
        this.emptyListReview()
        await this.getReviewPagination()
        setTimeout(() => {
          this.$store.commit('incrementLoader', -1)
        }, 500)
      }
    },
    editComment(reviewId) {
      this.origin.content = document.getElementById('edit-' + reviewId).value
      const reviewElement = this.getReviewInList(reviewId)
      this.origin.rating = reviewElement.rating
      this.resetCommentView(reviewId)
      document.getElementById('action-' + reviewId).style.display = 'none'
      document.getElementById('content-' + reviewId).style.display = 'none'
      document.getElementById('edit-action-' + reviewId).style.display = 'flex'
      document.getElementById('edit-container-' + reviewId).style.display =
        'block'
      document.getElementById('rating-score' + reviewId).style.display = 'none'
      document.getElementById('rating-' + reviewId).style.display = 'flex'
    },
    resetCommentView() {
      Array.from(
        document.getElementsByClassName('review-content-edit')
      ).forEach(element => {
        element.style.display = 'none'
      })
      Array.from(document.getElementsByClassName('review-content')).forEach(
        element => {
          element.style.display = 'block'
        }
      )
      Array.from(document.getElementsByClassName('edit-action')).forEach(
        element => {
          element.style.display = 'none'
        }
      )
      Array.from(document.getElementsByClassName('action-review')).forEach(
        element => {
          element.style.display = 'flex'
        }
      )
      Array.from(document.getElementsByClassName('edit-rating')).forEach(
        element => {
          element.style.display = 'none'
        }
      )
      Array.from(document.getElementsByClassName('rating-score')).forEach(
        element => {
          element.style.display = 'flex'
        }
      )
    },
    saveEditComment(reviewId) {
      const reviewElement = this.getReviewInList(reviewId)
      this.updateReview(reviewElement)
      this.resetCommentView(reviewId)
    },
    cancelEditComment(reviewId) {
      this.resetCommentView(reviewId)
      let reviewElement = this.getReviewInList(reviewId)
      reviewElement.content = this.origin.content
      reviewElement.rating = this.origin.rating
    },
    getReviewInList(reviewId) {
      let result = null
      this.courseReview.content.forEach(element => {
        if (element.reviewId == reviewId) {
          result = element
        }
      })
      return result
    },
    showConfirmDeleteComment(reviewId) {
      this.$swal({
        title: 'Bạn có muốn xóa bình luận này?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
      }).then(result => {
        if (result.value) {
          this.removeReview(reviewId)
        }
      })
    },
    async loadMore() {
      this.reviewPagination.page++
      this.$store.commit('incrementLoader', 1)
      await this.getReviewPagination()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    emptyListReview() {
      this.courseReview = {
        content: []
      }
    }
  }
}
</script>

<style scoped>
.reviewer-name {
  font-size: 14px;
  font-weight: 600;
}
.review-content {
  font-size: 14px;
  color: #9b9b9b;
  word-break: break-word;
}
>>> .v-rating .v-icon {
  padding: 3px;
}
.action-review span {
  color: #385898;
  cursor: pointer;
  font-size: 12px;
  opacity: 0;
}
.action-review > span:hover {
  text-decoration: underline !important;
}
.comment-item:hover .action-review > span {
  opacity: 1;
}
textarea {
  font-size: 16px;
}
.review-content-edit,
.edit-action {
  display: none;
}
>>> textarea {
  font-size: 14px;
}
.edit-rating {
  display: none;
}
.load-more {
  color: #df322f;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
}
.comment-title {
  font-size: 18px;
  font-weight: 600;
  line-height: 1;
  letter-spacing: 0.3px;
}
.required-login {
  font-size: 13px;
}
</style>

