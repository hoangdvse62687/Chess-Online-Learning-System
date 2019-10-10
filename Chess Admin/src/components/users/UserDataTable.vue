<template>
  <v-flex>
    <v-data-table
      :headers="headers"
      :items="listUsers"
      :search="search"
      :items-per-page="pagination.rowsPerPage"
      hide-default-footer
      :page="currentPage"
      :loading="loader"
      loading-text="Đang tải"
    >
      <template v-slot:items="props">
        <td style="width: 25%">{{ props.item.fullName }}</td>
        <td class="text-xs-center" style="width: 25%">{{ props.item.email }}</td>
        <td class="text-xs-center" style="width: 25%">{{ props.item.roleName }}</td>
        <td class="text-xs-center" style="width: 20%">{{ props.item.status }}</td>
        <!-- <td class="justify-center layout px-0">
          <v-btn
            icon
            text
            color="success"
            v-if="!props.item.isActive"
            class="btn-active-deactive"
            @click="confirmChangeStatus(true, props.item)"
          >
            <v-icon>fa-lock-open</v-icon>
          </v-btn>
          <v-btn
            icon
            text
            color="error"
            v-if="props.item.isActive"
            class="btn-active-deactive"
            @click="confirmChangeStatus(false, props.item)"
          >
            <v-icon>fa-lock</v-icon>
          </v-btn>
        </td> -->
      </template>
      <template v-slot:item.action="{ item }">
        <v-btn
            icon
            text
            color="success"
            v-if="!item.isActive"
            class="btn-active-deactive"
            @click="confirmChangeStatus(true, item)"
          >
            <v-icon>fa-lock-open</v-icon>
          </v-btn>
          <v-btn
            icon
            text
            color="error"
            v-if="item.isActive"
            class="btn-active-deactive"
            @click="confirmChangeStatus(false, item)"
          >
            <v-icon>fa-lock</v-icon>
          </v-btn>
      </template>
    </v-data-table>
    <Pagination 
    :currentPage="currentPage"
    :pages="pages" 
    :rowDataLength="listUsers.length" 
    :isShowEmptyMessage="isShowEmptyMessage"
    @triggerpaging="handlPaging($event)"
    />
    <v-dialog v-model="dialog" width="400">
      <v-card>
        <v-card-title class="headline">Xác nhận</v-card-title>
        <v-card-text>
          Bạn có chắc chắn muốn
          <b>{{editedUser.action}}</b>
          tài khoản
          <b>{{editedUser.email}}</b>?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="red darken-1"
            @click="dialog = false"
            text
          >Hủy bỏ</v-btn>
          <v-btn
            color="green darken-1"
            @click="changeStatus(), dialog = false"
            text
          >Đồng ý</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-flex>
</template>

<script>
export default {}
</script>

<script>
import Pagination from '@/components/kit/Pagination'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const userRepository = RepositoryFactory.get('user')
export default {
  components: {
    Pagination
  },
  props: {
    search: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dialog: false,
      editDialog: false,
      editedUser: [],
      loader: false,
      // search: '',
      pagination: {
        rowsPerPage: 10
      },
      headers: [
        { text: 'Họ và tên', value: 'fullName', align: 'left' },
        { text: 'Email', value: 'email', align: 'center' },
        { text: 'Vai trò', value: 'roleName', align: 'center' },
        { text: 'Trạng thái', value: 'status', align: 'center' },
        { text: 'Hành động', value: 'action', sortable: false }
      ],
      listUsers: [],
      isShowEmptyMessage:false,
      currentPage:1,
      pages:1
    }
  },
  mounted() {
    this.getUsersPagination()
  },
  methods: {
    async getUsersPagination() {
      this.loader = true
      const { data } = await userRepository.getUsersPagination(this.currentPage,this.pagination.rowsPerPage,this.search)
      if(data.data){
        this.listUsers = this.formatListUser(data.data.content)
        this.pages = data.data.totalPages
      }
      this.loader = false
    },
    async changeStatus() {
      this.loader = true
      const data = {
        userId: this.editedUser.userId,
        active: !this.editedUser.isActive
      }
      const {result} = await userRepository.updateStatus(data)
      this.editedUser.isActive = !this.editedUser.isActive
      this.editedUser.isActive ? this.editedUser.status = 'Kích hoạt' : this.editedUser.status = 'Vô hiệu hóa'
      this.loader = false
    },
    confirmChangeStatus(status, item) {
      this.editedUser = this.listUsers.find(user => user.userId === item.userId)
      this.editedUser.action = this.editedUser.isActive
        ? 'vô hiệu hóa'
        : 'kích hoạt'
      this.dialog = true
    },
    handlPaging(e){
      this.currentPage = e
    }
  },
  watch:{
    currentPage:{
      handler:function(){
        this.getUsersPagination()
      }
    },
    search:{
      handler:function(){
        this.getUsersPagination()
      }
    },
    loader:{
      handler:function(){
        if(this.loader){
          this.$store.commit('incrementLoader', 1)
        }else{
          this.$store.commit('incrementLoader', -1)
        }
      }
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
</style>