<template>
  <v-app-bar
    absolute
    style="padding-left: 256px"
    :height="72"
    class="v-toolbar--fixed"
    color="white"
    scroll-target="#scrolling-techniques"
  >
    <v-breadcrumbs class="grey--text" :items="items" divider=">"></v-breadcrumbs>
    <v-spacer></v-spacer>
    <v-menu
        :close-on-content-click="true"
        offset-y bottom left
        transition="slide-y-transition"
        :eager="true"
        :close-on-click="true"
        >
        <template v-slot:activator="{ on }">
            <v-btn v-on="on" icon color="grey">
        <v-badge color="red darken-1" right overlap>
            <template v-slot:badge>{{$store.state.unreadNotifications}}</template>
            <v-icon>fa-bell</v-icon>
        </v-badge>
        </v-btn>
        </template>
        <ListNotification 
            :pageSize="defaultLoadNotifications" />
    </v-menu>
    <v-toolbar-items class="align-center py-1 pr-3">
      <v-divider color="#BDBDBD" class="mx-4" vertical inset></v-divider>
        <v-menu bottom left offset-y transition="slide-y-transition">
          <template v-slot:activator="{on}">
            <a v-on="on">
        <span class="grey--text text--darken-3 mr-2 body-2">{{user.fullName}}</span>
              <v-avatar :size="35">
                <img :src="user.avatar" />
              </v-avatar>
            </a>
          </template>
          <v-list>
            <v-list-item :to="'/dashboard/profile'">
              <v-list-item-title>Thông tin cá nhân</v-list-item-title>
            </v-list-item>
            <v-list-item @click="signOut">
              <v-list-item-title>Đăng xuất</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      
    </v-toolbar-items>
  </v-app-bar>
</template>

<script>
import { mapState } from 'vuex'
import ListNotification from '@/components/notifications/ListNotification'
export default {
  name: 'Toolbar',
  components:{
    ListNotification
  },
  computed: {
    ...mapState({ user: state => state.user }),
  },
  data() {
    return {
      chessLogo: require('@/assets/images/chess.png'),
      items: [
        // {
        //   text: 'Dashboard',
        //   disabled: false,
        //   href: 'breadcrumbs_dashboard'
        // },
        // {
        //   text: 'Link 1',
        //   disabled: false,
        //   href: 'breadcrumbs_link_1'
        // },
        // {
        //   text: 'Link 2',
        //   disabled: true,
        //   href: 'breadcrumbs_link_2'
        // }
      ],
      defaultLoadNotifications:5
    }
  },
  methods: {
    signOut() {
      localStorage.removeItem('user')
      localStorage.removeItem('access-token')
      localStorage.removeItem('role')
      this.$store.commit('changeUser', null)
      this.$router.push('/')
    },
  },
  watch:{
    '$route.meta.breadcrumb':{
      handler:function(){
        this.items = this.$route.meta.breadcrumb
      }
    }
  }
}
</script>

<style>
.v-breadcrumbs__item {
  color: #ffab00 !important;
}
.v-breadcrumbs__item--disabled {
  color: #bdbdbd !important;
}
.v-menu__content {
  border-radius: 10px;
}
</style>
