<template>
  <v-layout fill-height align-center class="login-container xs12" wrap>
    <v-flex xs12>
      <span class="text-xs-center login-title mb-4">ĐĂNG NHẬP</span>
      <v-layout justify-center>
        <v-card height="300" width="400">
          <v-layout row wrap justify-center py-4>
            <v-flex xs8 class="text-xs-center subheading" mb-4>
              <span class="text-note">VUI LÒNG ĐĂNG NHẬP ĐỂ SỬ DỤNG HỆ THỐNG</span>
            </v-flex>
            <v-flex xs12 mb-4 class="login-box-header">
              <v-img contain height="100" :src="userImg"/>
            </v-flex>
            <v-flex xs7>
              <v-btn
                v-if="!isLogin"
                black--text
                color="white"
                :style="btnLoginGoogle"
                class="mr-0"
                @click="loginWithGoogle()"
              >Đăng nhập với Gmail</v-btn>
            </v-flex>
          </v-layout>
        </v-card>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  created() {},
  data() {
    return {
      loginBackgroundImage: require('@/assets/images/google-logo.png'),
      userImg: require('@/assets/images/man.png'),
      isLogin: false
    }
  },
  computed: {
    btnLoginGoogle() {
      return {
        backgroundImage: `url(${this.loginBackgroundImage})`,
        backgroundPosition: `10px center`,
        backgroundSize: `30px`,
        paddingLeft: `50px`,
        width: `100%`,
        textTransform: `none`,
        fontSize: `18px`,
        height: `40px`
      }
    }
  },
  methods: {
    loginWithGoogle() {
      var api = this.$store.state.api.login
      api += '?redirect_uri=' + this.getCurrentPage()
      window.location.href = api
    }
  }
}
</script>

<style scoped>
.login-title {
  font-size: 28px;
  color: #fff;
  line-height: 1.2;
  text-align: center;
  text-transform: uppercase;
  display: block;
}
.v-card {
  border-radius: 7px;
}
.login-container {
  position: absolute;
  left: 0px;
  top: 0px;
  width: 100%;
}
.text-note {
  color: #6c757d !important;
}
</style>
