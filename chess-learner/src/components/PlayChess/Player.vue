<template>
  <v-layout mb-2>
    <v-avatar size="45" color="grey lighten-4" class="mr-3 ml-3">
      <img :src="imageLink" alt="avatar" />
    </v-avatar>
    <v-layout column>
      <h6 class="subheading mt-3">
        <v-icon>fa-trophy</v-icon>
        {{ point }}
      </h6>
    </v-layout>
    <v-card height="50">
      <div class="time display-1">
        <p class="text-xs-center pb-1 pt-1 pl-3 pr-3">
          <span class="hour">{{ timeArr[0] }}</span>:
          <span class="minnute">{{ timeArr[1] }}</span>:
          <span class="second">{{ timeArr[2] }}</span>
        </p>
      </div>
    </v-card>
  </v-layout>
</template>

<script>
export default {
  name: 'Player',
  props: {
    imageLink: {
      type: String,
      default: ''
    },
    point: {
      type: Number,
      default: 1000
    },
    time: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      timeArr: []
    }
  },
  watch: {
    time: function(time) {
      this.time = time
      this.timeArr = this.time.split(':')
    }
  },
  created() {
    this.timeArr = this.time.split(':')
  }
}
</script>

<style scoped>
.layout {
  margin-bottom: 8px;
}
</style>
