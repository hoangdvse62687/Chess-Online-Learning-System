<template>
  <v-container class="pa-6">
    <v-layout align-start justify-space-between v-if="user != null">
      <v-flex xs4>
        <v-layout>
          <v-flex>
            <HighlightProfile/>
          </v-flex>
        </v-layout>
      </v-flex>
      <v-flex xs8>
        <ProfileInfo :isInstructor="this.user.roleId === 1"/>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import HighlightProfile from './profile-pieces/HighlightProfile'
import ProfileInfo from './profile-pieces/ProfileInfo'
import { mapState } from 'vuex'
export default {
  components: {
    HighlightProfile,
    ProfileInfo
  },
  data() {
    return {}
  },
  computed: {
    ...mapState({ user: state => state.user })
  }
}
</script>

<style scoped>
.xs4 {
  max-width: 31.5%;
}
</style>
