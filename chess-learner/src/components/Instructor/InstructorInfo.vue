<template>
  <v-layout v-if="instructorDetail !== null">
    <div class="round-border">
      <v-avatar :size="120">
        <img :src="instructorDetail.avatar" />
      </v-avatar>
    </div>
    <v-flex xs6 ml-3>
      <v-layout align-center wrap fill-height>
        <v-layout wrap>
          <v-flex xs12 class="title-achievement">Giảng viên</v-flex>
          <v-flex xs12 py-1 class="author-fullname text-truncate">{{ instructorDetail.fullName }}</v-flex>
        </v-layout>
      </v-layout>
    </v-flex>
    <v-divider vertical></v-divider>
    <v-flex xs3 pl-3 fill-height>
      <v-layout wrap fill-height>
        <v-flex xs12 class="title-achievement">Thành tích</v-flex>
        <v-flex xs12>{{instructorDetail.achievement}}</v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  props: {
    instructorDetail: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>
.author-fullname {
  font-size: 28px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.3px;
}
.round-border {
  border-radius: 50%;
  width: 136px;
  height: 136px;
  border: 1px solid #e3e4e5;
  padding: 7px;
}
.title-achievement {
  font-size: 18px;
  color: #4d4d4d;
  font-weight: 600;
}
</style>