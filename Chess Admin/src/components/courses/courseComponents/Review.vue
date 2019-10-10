<template>
  <v-card>
    <v-card-title primary-title>
      <div class="title-review">Đánh giá</div>
    </v-card-title>
    <v-layout py-3>
      <v-flex v-if="courseOverview.medRating != null" xs4>
        <v-layout wrap>
          <template v-if="courseOverview.medRating !== 0">
            <v-flex xs12 class="text-center">
              <span class="review-score text-black">{{ courseOverview.medRating }}</span>
              <v-rating
                v-model="courseOverview.medRating"
                :empty-icon="emptyIcon"
                :full-icon="fullIcon"
                :half-icon="halfIcon"
                :half-increments="true"
                background-color="grey lighten-1"
                color="yellow darken-3"
                readonly
              ></v-rating>
              <span class="total-quantity-ratings">
                ({{ courseOverview.totalQuantityRatings }} người đã đánh
                giá)
              </span>
            </v-flex>
            <v-flex x12 class="text-center">
              <v-layout mt-3 wrap>
                <v-flex v-for="(item, index) in courseOverview.listRatings" :key="index" xs12>
                  <v-layout class="pt-1 py-2">
                    <v-flex xs3 mr-2>
                      <v-layout fill-height align-center justify-end>
                        <span class="mr-1 rating-score-title">{{ index + 1 }}</span>
                        <v-icon color="yellow darken-3" size="12">fa-star</v-icon>
                      </v-layout>
                    </v-flex>
                    <v-flex xs6 mr-2>
                      <v-layout fill-height align-center>
                        <v-progress-linear height="7" v-model="item.ratio" color="yellow darken-3"></v-progress-linear>
                      </v-layout>
                    </v-flex>
                    <v-flex xs3>
                      <v-layout fill-height align-center>
                        <span class="rating-ratio">{{ item.ratio }}%</span>
                      </v-layout>
                    </v-flex>
                  </v-layout>
                </v-flex>
              </v-layout>
            </v-flex>
          </template>
          <template v-else>
            <v-flex xs12>
              <v-layout justify-center>
                <span class="text-grey no-review">(Hiện chưa có đánh giá nào)</span>
              </v-layout>
            </v-flex>
          </template>
        </v-layout>
      </v-flex>
      <v-flex xs8 class="comment-container">
        <Comment :courseId="courseId"/>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<script>
import Comment from './Comment'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')
export default {
  props: {
    courseId: {
      type: String,
      required:true
    }
  },
  components: {
    Comment
  },
  data() {
    return {
      rating: 4.0,
      emptyIcon: 'fa-star',
      fullIcon: 'fa-star',
      halfIcon: 'fa-star-half-alt',
      valueDeterminate: 40,
      listRatings: {},
      courseOverview: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      await this.getCourseOverview()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    async getCourseOverview() {
      const { data } = await courseRepository.getCourseOverview(this.courseId)
      this.courseOverview = data.data
      this.calMedRating()
      this.calListRatings()
      this.listRatings = this.courseOverview.listRatings
    },
    calMedRating() {
      if (
        this.courseOverview.totalRatings === 0 &&
        this.courseOverview.totalQuantityRatings === 0
      ) {
        this.courseOverview.medRating = 0
      } else {
        this.courseOverview.medRating =
          this.courseOverview.totalRatings /
          this.courseOverview.totalQuantityRatings
        this.courseOverview.medRating =
          Math.round(this.courseOverview.medRating * 10) / 10
      }
    },
    calListRatings() {
      this.courseOverview.listRatings.forEach(element => {
        element.ratio = Math.round(element.ratio * 1000) / 10
      })
    }
  }
}
</script>

<style scoped>
.comment-container {
  border-left: 2px solid #e7eaec;
}
.title-review {
  font-size: 18px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.3px;
}
.review-score {
  font-size: 56px !important;
  font-weight: 600;
  line-height: 1.35;
  letter-spacing: 0.3px;
}
.total-lesson-title,
.rating-score-title,
.rating-ratio,
.total-quantity-ratings {
  font-size: 14px;
}
.no-review {
  font-size: 13px;
}
</style>
