<template>
  <v-layout wrap px-3>
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
import Moment from '@/assets/js/Moment'
const MomentSetting = new Moment()
//const moment = require('moment')
export default {
  props: {
    courseId: {
      type: String,
      required:true
    }
  },
  data() {
    return {
      user: this.$store.state.user,
      courseReview: {
        content: []
      },
      emptyIcon: 'fa-star',
      fullIcon: 'fa-star',
      halfIcon: 'fa-star-half-alt',
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
      reviewPagination: {
        page: 1,
        pageSize: 4
      },
      moment:MomentSetting.getInstance(),
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      await this.getReviewPagination()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
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
      return this.moment(dateCreated, 'MM/DD/YYYY, hh:mm:ss A').fromNow()
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
    loadMore() {
      this.reviewPagination.page++
      this.$store.commit('incrementLoader', 1)
      this.getReviewPagination()
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
.v-rating .v-icon {
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
textarea {
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

