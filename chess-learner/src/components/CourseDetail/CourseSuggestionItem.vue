<template>
  <v-flex class="course-item">
    <v-hover class="mr-2">
      <v-card
        slot-scope="{ hover }"
        class="black--text"
        :class="`elevation-${hover ? 12 : 2}`"
        @click="goToCourseDetail()"
      >
        <v-layout wrap>
          <v-flex xs12>
            <v-img :src="courseDetail.courseImage" height="115px"></v-img>
          </v-flex>
          <v-flex xs12>
            <v-layout align-center fill-height v-if="courseDetail.rating > 0" px-4>
              <v-rating
                v-model="courseDetail.rating"
                background-color="yellow darken-3"
                color="yellow darken-3"
                small
                readonly
              ></v-rating>
              <span class="ml-1 pt-1 course-total-rate text-grey">({{courseDetail.totalRating}})</span>
            </v-layout>
            <v-layout v-else justify-center fill-height align-center>
              <span class="no-rating text-grey pt-1">(Hiện chưa có đánh giá nào)</span>
            </v-layout>
          </v-flex>
          <v-flex xs12>
            <v-card-title primary-title class="py-0 text-truncate">
              <span class="course-title text-truncate">
                {{
                courseDetail.courseName
                }}
              </span>
            </v-card-title>
          </v-flex>
          <v-flex xs12 mb-2 v-if="courseDetail.author !== null">
            <v-layout px-3 fill-height align-center>
              <v-flex xs2>
                <v-avatar :size="30">
                  <img :src="courseDetail.author.avatar" alt="avatar" />
                </v-avatar>
              </v-flex>
              <v-flex xs9 ml-2>
                <div class="course-author text-truncate">
                  {{
                  courseDetail.author.fullName
                  }}
                </div>
              </v-flex>
            </v-layout>
          </v-flex>
        </v-layout>
        <v-divider light></v-divider>
        <v-card-actions class="px-3 py-1">
          <v-spacer></v-spacer>
          <span
            :class="['course-required-elo px-3 py-1 elevation-2', courseDetail.requiredEloClass ]"
          >{{courseDetail.requiredEloName}}</span>
        </v-card-actions>
      </v-card>
    </v-hover>
  </v-flex>
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
>>> .v-rating i:first-child {
  padding-left: 0px !important;
}
.course-author {
  font-size: 12px;
  color: #686f7a;
  max-width: 100%;
}
.course-title {
  font-size: 13px;
  font-weight: 600;
  line-height: 26px !important;
  letter-spacing: normal !important;
  font-family: 'Open Sans', sans-serif !important;
}
>>> .v-rating .v-icon {
  padding: 3px;
}

.course-score {
  font-weight: 600;
  color: #505763;
  font-size: 14px;
}

.course-total-rate {
  font-size: 12px;
  font-weight: 400;
  color: #686f7a;
}
.no-rating {
  font-size: 13px;
  height: 24.72px;
}
.course-item {
  max-width: 20%;
  width: 20%;
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
