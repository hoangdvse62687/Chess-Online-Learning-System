<template>
  <v-app-bar app class="bg-dark" height="56" absolute>
    <v-toolbar class="bg-dark none-box-shadow" height="56">
      <v-flex xs1>
        <router-link to="/">
          <v-layout justify-center align-center fill-height>
            <img :src="chessLogo" />
            <span class="white--text text-logo ml-2">COLS</span>
          </v-layout>
        </router-link>
      </v-flex>
      <v-spacer></v-spacer>
      <v-flex v-if="user === null" xs3 offset-xs6>
        <v-layout justify-end>
          <v-btn
            black--text
            color="white"
            :style="btnLoginGoogle"
            class="mr-0"
            @click="loginWithGoogle()"
          >Đăng nhập</v-btn>
        </v-layout>
      </v-flex>
      <template v-else>
        <v-flex xs10>
          <v-layout justify-end align-center>
            <v-flex xs3 mr-2>
              <v-layout fill-height align-center justify-end>
                <v-img :src="elo" height="30" max-width="30" />
                <span class="ml-3 user-point" v-if="user.point != 0">{{user.point}}</span>
                <span class="ml-3 user-point" v-else>Chưa có xếp hạng</span>
              </v-layout>
            </v-flex>
            <v-flex xs3>
              <v-layout justify-end>
                <v-menu offset-y transition="slide-y-transition" bottom left :max-width="250">
                  <template v-slot:activator="{ on }">
                    <v-btn color="#333940" class="btn-profile px-1" v-on="on" :max-width="350">
                      <v-layout align-center fill-height justify-start>
                        <v-avatar :size="28">
                          <img :src="user.avatar" />
                        </v-avatar>
                        <span class="white--text mx-2 text-truncate">{{ user.fullName }}</span>
                        <v-spacer></v-spacer>
                        <v-icon color="white">fa-caret-down</v-icon>
                      </v-layout>
                    </v-btn>
                  </template>
                  <v-list>
                    <v-list-item v-for="(item, index) in userMenu" :key="index" :to="item.href">
                      <v-list-item-title>{{ item.title }}</v-list-item-title>
                    </v-list-item>
                    <v-divider></v-divider>
                    <v-list-item @click="logout()">
                      <v-list-item-title>Đăng xuất</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-layout>
            </v-flex>
          </v-layout>
        </v-flex>
      </template>
    </v-toolbar>
  </v-app-bar>
</template>

<script>
import { mapState } from 'vuex'
import Repository, { setAuthorizationHeader } from '@/repository/Repository.js'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
let userRepository = RepositoryFactory.get('user')
export default {
  data() {
    return {
      chessLogo: require('@/assets/images/chess.png'),
      elo: require('@/assets/images/award.png'),
      loginBackgroundImage: require('@/assets/images/google-logo.png'),
      userMenu: [
        { title: 'Thông tin cá nhân', href: '/profile/edit' },
        { title: 'Khóa học của tôi', href: '/profile/course' }
      ]
    }
  },
  computed: {
    ...mapState({ user: state => state.user }),
    btnLoginGoogle() {
      return {
        backgroundImage: `url(${this.loginBackgroundImage})`,
        backgroundPosition: `10px center`,
        backgroundSize: `30px`,
        paddingLeft: `50px`
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      if (
        localStorage.getItem('access-token') != null &&
        this.$store.state.user === null
      ) {
        setAuthorizationHeader(Repository, localStorage.getItem('access-token'))
        await this.getCurrentUserDetail()
      }
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    loginWithGoogle() {
      var api = this.$store.state.api.login
      api += '?redirect_uri=' + this.getCurrentPage()
      window.location.href = api
    },
    async getCurrentUserDetail() {
      const { data } = await userRepository.getCurrentUserDetail()
      let user = data.data
      user.roleName = this.getRoleName(user.roleId)
      user.status = this.getStatusUser(user.active)
      localStorage.setItem('user', JSON.stringify(user))
      this.setUserState()
    },
    setUserState() {
      const user = localStorage.getItem('user')
      const userToken = localStorage.getItem('access-token')
      this.$store.commit('setUser', JSON.parse(user))
      this.$store.commit('setUserToken', userToken)
    },
    logout() {
      localStorage.removeItem('access-token')
      localStorage.removeItem('user')
      localStorage.removeItem('role')
      this.$store.commit('setUser', null)
      this.$store.commit('setUserToken', null)
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
>>> .v-toolbar__content {
  max-width: 1125.9375px;
  -webkit-box-flex: 1;
  -ms-flex: 1 1 100%;
  flex: 1 1 100%;
  margin: auto;
  padding: 24px;
  width: 100%;
  padding-left: 0 !important;
  padding-right: 0 !important;
}
.text-logo {
  font-weight: bold;
  font-size: 16px;
}
img {
  width: 40px;
  height: 40px;
}
.btn-profile {
  box-shadow: none !important;
}
.btn-profile span {
  max-width: 170px;
  overflow: hidden;
  font-size: 12px;
  text-transform: none;
}
.user-point {
  color: white;
  font-size: 13px;
}
</style>
