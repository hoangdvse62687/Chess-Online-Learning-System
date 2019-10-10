<template>
  <v-layout wrap justify-center>
    <v-flex xs12>
      <v-card-title primary-title>
        <div class="title-instructor">Chứng nhận</div>
      </v-card-title>
    </v-flex>
    <v-flex xs6 mt-3>
      <agile class="agile-main mb-3" ref="main" :as-nav-for="asNavFor1" :options="options">
        <div class="slide main-item" v-for="(item,index) in certificates" :key="index">
          <v-img :src="item.certificateLink" contain height="350" aspect-ratio="1.7778" />
        </div>
      </agile>
      <agile class="agile-thumbnails" ref="thumbnails" :options="options2" :as-nav-for="asNavFor2">
        <div class="slide thumbnails-item" v-for="(item,index) in certificates" :key="index">
          <v-img
            :src="item.certificateLink"
            contain
            height="60"
            aspect-ratio="1.7778"
            @click="$refs.thumbnails.goTo(index), $refs.main.goTo(index)"
          />
        </div>
      </agile>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  props: {
    certificates: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      asNavFor1: [],
      asNavFor2: [],
      options: {
        dots: false,
        fade: true,
        navButtons: false,
        autoplay: true
      },
      options2: {
        autoplay: true,
        centerMode: true,
        dots: false,
        navButtons: false,
        slidesToShow: 5
      }
    }
  }
}
</script>

<style scoped>
.title-instructor {
  font-size: 24px;
  font-weight: 500;
  line-height: 32px;
  letter-spacing: 0.3px;
}
.thumbnails-item:hover {
  opacity: 0.75;
  cursor: pointer;
}
</style>