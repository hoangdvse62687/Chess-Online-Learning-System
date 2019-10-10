<template>
  <v-navigation-drawer
    v-model="drawer"
    fixed
    dark
    floating
    src="@/assets/images/drawer-bg.jpg"
    permanent
  >
    <v-list>
      <v-list-item :to="'/'">
        <v-list-item-avatar>
          <v-img :src="chessLogo"></v-img>
        </v-list-item-avatar>

        <v-list-item-title class="logo--text">COLS</v-list-item-title>
      </v-list-item>
    </v-list>
    <v-divider></v-divider>
    <v-list dense rounded v-if="user.roleId == 3">
      <v-list-item v-for="item in items" :key="item.title" link :to="item.path">
        <v-list-item-icon>
          <v-icon>{{ item.icon }}</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <v-list dense rounded v-if="user.roleId == 1">
      <v-list-item v-for="item in items2" :key="item.title" link :to="item.path">
        <v-list-item-icon>
          <v-icon>{{ item.icon }}</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import { mapState } from "vuex";
import Loader from "@/components/Loader";
export default {
  components: {
    Loader
  },
  data() {
    return {
      chessLogo: require("@/assets/images/chess.png"),
      loader: false,
      drawer: true,
      items: [
        {
          title: "Thống kê",
          icon: "fa-chart-line",
          path: "/dashboard/statistics"
        },
        {
          title: "Quản lý khóa học",
          icon: "fa-book",
          path: "/dashboard/courses"
        },
        {
          title: "Quản lý tài khoản",
          icon: "fa-user",
          path: "/dashboard/accounts"
        },
        {
          title: "Quản lý danh mục",
          icon: "fa-th-list",
          path: "/dashboard/category"
        },
        {
          title: "Xét duyệt giảng viên",
          icon: "fa-users-cog",
          path: "/dashboard/instructor-request"
        },
        {
          title: "Thông báo",
          icon: "fa-bell",
          path: "/dashboard/notifications"
        }
      ],
      items2: [
        {
          title: "Thống kê",
          icon: "fa-chart-line",
          path: "/dashboard/statistics"
        },
        {
          title: "Danh sách khóa học",
          icon: "fa-address-book",
          path: "/dashboard/courses"
        },
        {
          title: "Thêm khóa học",
          icon: "fa-plus-circle",
          path: "/dashboard/create-course"
        },
        {
          title: "Thông báo",
          icon: "fa-bell",
          path: "/dashboard/notifications"
        }
      ],
      dialog: false
    };
  },
  computed: {
    ...mapState({ user: state => state.user })
  },
  mounted() {
    // this.loader = true
    // this.loader = false
  },
  methods: {}
};
</script>

<style scoped>
.title-header {
  text-align: center !important;
}
.title-header-parent {
  opacity: 1 !important;
}
>>> .primary--text {
  max-width: 300px;
}
</style>
<style>
.v-navigation-drawer__image {
  opacity: 0.2;
}
.v-image__image--cover {
  background-position-x: left;
}
.logo--text {
  font-family: "Lobster", cursive;
  font-size: 1.7rem;
}
.v-list-item--active {
  color: #fbc02d !important;
}
.v-list-item__icon {
  width: 25px;
}
</style>