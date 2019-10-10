<template>
  <div>
    <v-card-text class="py-0">
      <vue-avatar
        ref="vueavatar"
        :width="width"
        :height="height"
        :rotation="rotation"
        :border-radius="borderRadius"
        :border="border"
        :color="color"
        :scale="scale"
        @vue-avatar-editor:image-ready="onImageReady"
        @select-file="onSelectFile($event)"
      >
      </vue-avatar>
    </v-card-text>
    <v-card-text v-if="hasScale" class="py-0">
      <v-slider
        v-model="scale"
        append-icon="fa-search-plus"
        prepend-icon="fa-search-minus"
        :min="1"
        :max="3"
        :step="0.02"
      ></v-slider>
    </v-card-text>
    <v-card-actions class="pt-0">
      <v-spacer></v-spacer>
      <v-btn color="primary" @click="finished">{{ finishText }}</v-btn>
    </v-card-actions>
  </div>
</template>

<script>
import VueAvatar from './VueAvatar.vue'

export default {
  components: {
    VueAvatar
  },
  props: {
    finishText: {
      type: String,
      default: 'Save'
    },
    hasRotation: {
      type: Boolean,
      default: true
    },
    hasScale: {
      type: Boolean,
      default: true
    },
    image: {
      type: String,
      default: ''
    },
    border: {
      type: Number,
      default: 25
    },
    borderRadius: {
      type: Number,
      default: 0
    },
    width: {
      type: Number,
      default: 200
    },
    height: {
      type: Number,
      default: 200
    },
    color: {
      type: Array,
      default: () => [0, 0, 0, 0.5]
    }
  },
  data() {
    return {
      rotation: 0,
      scale: 1
    }
  },
  methods: {
    onSelectFile(files) {
      this.$emit('select-file', files)
    },
    onImageReady() {
      this.scale = 1
      this.rotation = 0
    },
    finished() {
      return this.$emit('finished', this.$refs.vueavatar.getImageScaled())
    }
  }
}
</script>
