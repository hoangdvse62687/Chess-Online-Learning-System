<template>
    <v-container class="py-0">
        <v-layout justify-center>
            <v-flex xs12>
            <v-card-text id="preview-uninteractive-content">{{content}}</v-card-text>
        </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
export default {
    props: {
        content: {
            type: String,
            default: ''
        }
    },
    mounted() {
        let contenlEl = document.getElementById('preview-uninteractive-content').innerHTML = this.content
        contenlEl.innerHTML = this.content
    }
}
</script>

<style>

</style>
