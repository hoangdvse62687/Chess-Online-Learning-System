<template>
  <v-container>
    <v-layout align-center justify-center>
      <v-flex xs8>
        <register-form></register-form>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import RegisterForm from '@/components/RegisterForm'
export default {
  name: 'Register',
  components: {
    RegisterForm
  }
}
</script>

<style></style>
