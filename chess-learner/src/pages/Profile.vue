<template>
  <v-container>
    <v-layout>
      <v-flex xs12>
        <v-breadcrumbs :items="breadcrumbs">
          <template v-slot:divider>
            <v-icon>mdi-chevron-right</v-icon>
          </template>
        </v-breadcrumbs>
      </v-flex>
    </v-layout>
    <v-layout>
      <v-flex xs3>
        <v-card class="py-3">
          <v-layout justify-center fill-height align-center class="my-4">
            <v-flex xs4>
              <v-layout justify-end>
                <v-avatar :size="48" class="mr-3">
                  <v-img :src="user.avatar" />
                </v-avatar>
              </v-layout>
            </v-flex>
            <v-flex xs8>
              <v-layout column>
                <v-flex>
                  <v-layout
                    align-end
                    fill-height
                    class="profile-fullname text-black"
                  >{{user.fullName}}</v-layout>
                </v-flex>
                <v-flex>
                  <v-layout align-start class="profile-role-name text-grey">Học viên</v-layout>
                </v-flex>
              </v-layout>
            </v-flex>
          </v-layout>
          <v-divider class="mb-3"></v-divider>
          <v-tabs vertical v-model="tabModel" slider-color="#343a40" color="#343a40">
            <v-tab
              v-for="(item,index) in listMenu"
              :key="index"
              :href="`#${item.tabModel}`"
            >{{item.title}}</v-tab>
          </v-tabs>
        </v-card>
      </v-flex>
      <v-flex xs9 pl-4>
        <v-card>
          <v-tabs-items v-model="tabModel">
            <v-tab-item value="tab-edit">
              <AccountInformation />
            </v-tab-item>
            <v-tab-item value="tab-course">
              <v-card flat>
                <RegistedCourse />
              </v-card>
            </v-tab-item>
          </v-tabs-items>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import AccountInformation from '@/components/Profile/AccountInformation'
import RegistedCourse from '@/components/Profile/RegistedCourse'
import { mapState } from 'vuex'
export default {
  components: {
    AccountInformation,
    RegistedCourse
  },
  data() {
    return {
      listMenu: [
        {
          id: 'edit',
          title: 'Thông tin tài khoản',
          href: '/profile/edit',
          tabModel: 'tab-edit'
        },
        {
          id: 'course',
          title: 'Khóa học của tôi',
          href: '/profile/course',
          tabModel: 'tab-course'
        }
      ],
      tabModel: 'tab-course',
      selectedItem: 0,
      tabItem: this.$route.params.tabItem,
      breadcrumbs: [
        {
          text: 'Trang chủ',
          disabled: false,
          href: '/'
        },
        {
          text: 'Thông tin tài khoản',
          disabled: true
        }
      ]
    }
  },
  computed: {
    ...mapState({ user: state => state.user })
  },
  mounted() {
    this.renderMenuTab()
  },
  watch: {
    $route(to, from) {
      if (to.path !== from.path) {
        this.tabItem = this.$route.params.tabItem
        this.renderMenuTab()
      }
    },
    tabModel: function() {
      const selectedTab = this.listMenu.filter(el => {
        return el.tabModel === this.tabModel
      })
      this.$router.push(selectedTab[0].href)
    }
  },
  methods: {
    renderMenuTab() {
      const selectedTab = this.listMenu.filter(el => {
        return el.id === this.tabItem
      })
      this.tabModel = selectedTab[0].tabModel
      this.breadcrumbs[1].text = selectedTab[0].title
    }
  }
}
</script>

<style scoped>
>>> .v-item-group {
  width: 100% !important;
}
.v-tab {
  justify-content: left;
  text-transform: none;
}
>>> .v-tabs-slider-wrapper {
  width: 4px !important;
}
.profile-role-name {
  font-size: 12px;
}
.profile-fullname {
  font-size: 18px;
  font-weight: 600;
}
</style>
