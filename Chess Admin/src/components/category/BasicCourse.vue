<template>
  <div>
    <v-card-title>
      <v-text-field
        v-model="search"
        append-icon="fa-search"
        label="Tìm kiếm"
        single-line
        hide-details
      ></v-text-field>
      <v-spacer></v-spacer>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="listCourses"
      :search="search"
      :pagination.sync="pagination"
      class="elevation-1"
    >
      <template v-slot:items="props">
        <td style="width: 30%">{{ props.item.courseName }}</td>
        <td class="text-xs-center" style="width: 25%">{{ props.item.authorName }}</td>
        <td class="text-xs-center" style="width: 25%">{{ props.item.courseCreatedDate }}</td>
        <td class="text-xs-center" style="width: 20%">{{ props.item.statusName }}</td>
      </template>
    </v-data-table>
  </div>
</template>

<script>
export default {}
</script>

<script>
export default {
  props: {
    listCourses: {
      type: Array,
      default: []
    }
  },
  mounted() {
    console.log(this.listCourses)
  },
  data() {
    return {
      dialog: false,
      loader: false,
      search: '',
      pagination: {
        rowsPerPage: 10
      },
      headers: [
        { text: 'Tên khóa học', value: 'name', align: 'left' },
        { text: 'Tên tác giả', value: 'authorName', align: 'center' },
        { text: 'Ngày tạo', value: 'createdDate', align: 'center' },
        { text: 'Trạng thái', value: 'status', align: 'center' }
      ]
    }
  },
  computed: {
    pages() {
      const rowsPerPage = this.pagination.rowsPerPage
      const totalItems = this.listCourses.length
      if (rowsPerPage == null || totalItems == null) return 0

      return Math.ceil(totalItems / rowsPerPage)
    }
  }
}
</script>
<style scoped>
.btn-active-deactive {
  width: 35px !important;
  height: 35px !important;
}
.btn-active-deactive i {
  font-size: 17px !important;
}
>>> .v-menu__activator {
  justify-content: center;
}
.basic {
  text-align: center;
  font-style: inherit;
}
</style>