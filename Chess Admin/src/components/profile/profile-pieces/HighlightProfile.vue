<template>
  <v-card :elevation="8">
    <v-img :src="backgroundImage"></v-img>
    <div class="avatar-container justify-center align-center">
      <v-avatar :size="100">
      <img :src="user.avatar" alt="avatar">
    </v-avatar>
    </div>
    <v-card-text class="profile-highlight-container">
      <p class="mb-0 headline font-weight-medium grey--text text--darken-2">{{user.fullName}}</p>
      <p class="body-1 font-weight-medium grey--text">{{user.roleName}}</p>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  props:{
    userInput:{
      required:false,
      type:Object,
      default:null
    }
  },
  data() {
    return {
      randomBg: 1,
      backgroundImage: require(`@/assets/images/chess-user-bg1.jpg`),
      user: this.userInput ? this.formatListUser([this.userInput])[0] : this.$store.state.user
    }
  },
  created() {
    this.randomBg = Math.floor(Math.random() * Math.floor(5)) + 1
    this.backgroundImage = require(`@/assets/images/chess-user-bg${this.randomBg}.jpg`)
    console.log(this.$store.state.user)
  }
}
</script>

<style scoped>
.v-responsive.v-image {
    height: 145px;
  }
.avatar-container {
  width: 110px;
  height: 110px;
  position: absolute;
  top: 145px;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 50% !important;
  background-color: #fff;
  display: flex;
}
.v-avatar {
  align-items: center;
}
.profile-highlight-container {
  padding-top: 55px;
}
.profile-highlight-container p {
  text-align: center;
}
@media(min-width: 1904px) {
  .v-responsive.v-image {
    height: 180px;
  }
  .avatar-container {
    top: 180px;
  }
  .profile-highlight-container {
  padding-top: 65px;
}
}
>>> input {
  color: #757575 !important;
}
</style>
