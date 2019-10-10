<template>
  <div class="container2">
    <v-app>
      <div class="split left">
        <router-link to="/course" class="button">
          <v-layout fill-height align-center justify-center>Bắt đầu Học</v-layout>
        </router-link>
      </div>
      <div class="split right">
        <router-link to="/play-chess" class="button">
          <v-layout fill-height align-center justify-center>Chơi với máy</v-layout>
        </router-link>
      </div>
      <v-snackbar
        v-model="snackbar"
        :color="color"
        :multi-line="mode === 'multi-line'"
        :timeout="timeout"
        :vertical="mode === 'vertical'"
      >
        {{ snackbarText }}
        <v-btn dark text @click="snackbar = false">Đóng</v-btn>
      </v-snackbar>
    </v-app>
  </div>
</template>

<script>
import Category from '@/components/Introduce/Category'
import { setTimeout } from 'timers'
export default {
  components: {
    Category
  },
  data() {
    return {
      snackbar: false,
      color: '',
      mode: '',
      timeout: 6000,
      snackbarText:
        'Chúc mừng bạn đã đăng kí tài khoản thành công! Bạn vừa nhận được 1000 điểm vào tài khoản.'
    }
  },
  created() {
    if (this.$route.params.isNew) {
      setTimeout(() => {
        this.snackbar = true
      }, 1000)
    }
  },
  mounted() {
    const left = document.querySelector('.left')
    const right = document.querySelector('.right')
    const container = document.querySelector('.container2')

    left.addEventListener('mouseenter', () => {
      container.classList.add('hover-left')
    })

    left.addEventListener('mouseleave', () => {
      container.classList.remove('hover-left')
    })

    right.addEventListener('mouseenter', () => {
      container.classList.add('hover-right')
    })

    right.addEventListener('mouseleave', () => {
      container.classList.remove('hover-right')
    })
  }
}
</script>

<style scoped>
h1 {
  font-size: 4rem;
  color: #fff;
  position: absolute;
  left: 50%;
  top: 20%;
  transform: translateX(-50%);
  white-space: nowrap;
}

.button {
  display: block;
  position: absolute;
  left: 50%;
  top: 40%;
  height: 55px;
  width: 15rem;
  text-align: center;
  color: #fff;
  border: #fff solid 0.2rem;
  font-size: 1rem;
  font-weight: bold;
  text-transform: uppercase;
  text-decoration: none;
  transform: translateX(-50%);
}

.split.left .button:hover {
  background-color: rgba(226, 109, 41, 0.733);
  border-color: rgba(226, 109, 41, 0.575);
}

.split.right .button:hover {
  background-color: rgba(92, 92, 92, 0.3);
  border-color: rgba(92, 92, 92, 0.3);
}

.container2 {
  position: relative;
  width: 100%;
  height: 100%;
  background: #333;
  margin-top: -56px;
  z-index: 3;
}

.split {
  position: absolute;
  width: 50%;
  height: 100%;
  overflow: hidden;
}

.split.left {
  left: 0;
  background: url(https://firebasestorage.googleapis.com/v0/b/cols-fpt.appspot.com/o/images%2Fbackground%2Flearning.jpg?alt=media&token=cd7a7a57-7dcc-4797-957b-e74d76bc7d49)
    center center no-repeat;
  background-size: cover;
}

.split.left:before {
  position: absolute;
  content: '';
  width: 100%;
  height: 100%;
  background: rgba(226, 109, 41, 0.397);
}

.split.right {
  right: 0;
  background: url(https://firebasestorage.googleapis.com/v0/b/cols-fpt.appspot.com/o/images%2Fbackground%2Fplaying.jpg?alt=media&token=30cb7e0a-d40a-4007-860a-5ce0bb6a5fd9)
    center center no-repeat;
  background-size: cover;
}

.split.right:before {
  position: absolute;
  content: '';
  width: 100%;
  height: 100%;
  background: rgba(43, 43, 43, 0.8);
}

.split.left,
.split.right,
.split.right:before,
.split.left:before {
  transition: 1000ms all ease-in-out;
}

.hover-left .left {
  width: 75%;
}

.hover-left .right {
  width: 25%;
}

.hover-left .right:before {
  z-index: 2;
}

.hover-right .right {
  width: 75%;
}

.hover-right .left {
  width: 25%;
}

.hover-right .left:before {
  z-index: 2;
}
::-webkit-scrollbar {
  width: 0px;
}
</style>
