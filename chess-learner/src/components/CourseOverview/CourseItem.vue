<template>
  <v-hover>
    <v-card slot-scope="{ hover }" class="black--text" :class="`elevation-${hover ? 12 : 2}`">
      <v-layout wrap pa-3>
        <v-flex xs4>
          <v-img
            :src="courseDetail.courseImage"
            height="170px"
            class="course-image"
            @click="goToCourseDetail()"
          ></v-img>
          <span
            class="text-grey course-tag"
            v-if="courseDetail.tag.length > 0"
          >Danh mục: {{courseDetail.tag}}</span>
        </v-flex>
        <v-flex xs5>
          <v-layout wrap>
            <v-flex xs12>
              <v-card-title
                primary-title
                class="pt-2 pb-0 pl-3 text-truncate course-title-container"
                @click="goToCourseDetail()"
              >
                <span class="course-title text-truncate text-black">
                  {{
                  courseDetail.courseName
                  }}
                </span>
              </v-card-title>
            </v-flex>
            <v-flex xs12 mb-2 pl-3>
              <span class="course-author">T/g {{ courseDetail.author.fullName }}</span>
            </v-flex>
            <v-flex pl-3>
              <span
                :class="['course-required-elo px-3 py-1 elevation-2', courseDetail.requiredEloClass ]"
              >{{courseDetail.requiredEloName}}</span>
            </v-flex>
          </v-layout>
        </v-flex>
        <v-flex xs3 class="course-detail-container" pl-3>
          <v-layout column justify-space-between fill-height wrap>
            <v-flex style="height: 28px">
              <v-layout justify-end class="course-ribbon">
                <span
                  class="course-inprogress px-3 elevation-4"
                  v-if="courseDetail.enrolled"
                >Đã đăng ký</span>
              </v-layout>
            </v-flex>
            <v-flex>
              <v-layout justify-center column align-center pt-3>
                <span class="text-detail text-grey">{{ courseDetail.courseCreatedDate }}</span>
                <v-layout align-center v-if="courseDetail.rating > 0">
                  <v-rating
                    v-model="courseDetail.rating"
                    background-color="yellow darken-3"
                    color="yellow darken-3"
                    small
                    readonly
                  ></v-rating>
                  <span class="pt-1 ml-1 course-total-rate text-grey">({{courseDetail.totalRating}})</span>
                </v-layout>
                <span class="no-rating text-grey" v-else>(Hiện chưa có đánh giá nào)</span>
              </v-layout>
            </v-flex>
            <v-flex>
              <v-layout fill-height align-end>
                <v-flex xs12 mr-3>
                  <v-btn
                    class="ma-0 btn-detail"
                    outlined
                    color="primary"
                    block
                    @click="goToCourseDetail()"
                  >Xem chi tiết</v-btn>
                </v-flex>
              </v-layout>
            </v-flex>
          </v-layout>
        </v-flex>
      </v-layout>
    </v-card>
  </v-hover>
</template>

<script>
export default {
  props: {
    courseDetail: {
      type: Object,
      default: null
    }
  },
  methods: {
    goToCourseDetail() {
      this.$router.push(`/course/${this.courseDetail.courseId}`)
    }
  }
}
</script>

<style scoped>
.course-author {
  font-size: 13px;
  color: #686f7a;
}
.course-title {
  font-size: 16px;
  font-weight: 700;
  line-height: 24px !important;
}
>>> .v-rating .v-icon {
  padding: 0px;
}

.course-score {
  font-weight: 600;
  color: #505763;
  font-size: 14px;
}

.course-total-rate {
  font-size: 12px;
  font-weight: 400;
}
.course-point {
  font-size: 18px;
  font-weight: 700;
}
.course-detail-container {
  border-left: 1px solid hsl(0, 0%, 90%);
}
.btn-detail {
  font-weight: 600;
}
.text-detail,
.course-tag {
  font-size: 12px;
}
.course-image:hover,
.course-title-container:hover {
  cursor: pointer;
}
.course-title-container:hover {
  text-decoration: underline;
}
.no-rating {
  font-size: 13px;
}
.course-ribbon {
  border-radius: 3px;
  color: white;
  font-size: 14px;
}
.course-inprogress {
  border-radius: 3px;
  background-image: linear-gradient(90deg, #00b359, #00cc66);
}
.point-reward {
  font-size: 14px;
  color: #57d9ad;
}
.point-required {
  font-size: 14px;
  font-weight: 600;
  text-align: center;
  color: #ff5252;
}
.course-required-elo {
  border-radius: 3px;
  color: white;
  font-size: 14px;
}
.elo-1 {
  background-image: linear-gradient(90deg, #666666, #999999);
}
.elo-2 {
  background-image: linear-gradient(90deg, #075f2a, #0ebe54);
}
.elo-3 {
  background-image: linear-gradient(90deg, #24284d, #225982);
}
.elo-4 {
  background-image: linear-gradient(90deg, #42104b, #990a7e);
}
.elo-5 {
  background-image: linear-gradient(90deg, #6f4604, #cf8f30);
}
</style>
