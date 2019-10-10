<template>
  <div>
    <v-card-title class="pa-0">
      <h5 class="text-captital grey--text">Danh sách danh mục</h5>
      <!-- <v-spacer></v-spacer> -->
      <v-btn depressed class="ml-3 align-end" @click="dialog = !dialog" color="amber lighten-1">Thêm danh mục</v-btn>
    </v-card-title>
      <v-card-actions>
      <v-spacer></v-spacer>
    </v-card-actions>
      <v-data-table
      :headers="headers"
      :items="listCategoryItems"
      hide-default-footer
      :items-per-page="rowsPerPage"
    >
      <template v-slot:item.action="{item}">
          <v-icon
            class="mr-2"
            @click="confirmEditCategory(item)"
          >
            edit
          </v-icon>
          <v-icon
            @click="confirmRemoveCategory(item)"
          >
          delete
          </v-icon>
          <!-- <v-btn
            @click="confirmDetailCategory(props.item), getCourseByCategoryId(categoryId = props.item.categoryId)"
            color="" icon small
          ><v-icon>fa-list</v-icon></v-btn>
          <v-btn icon text class="btn-active-deactive" @click="confirmEditCategory(props.item)">
            <v-icon>fa-edit</v-icon>
          </v-btn>
          <v-btn icon text class="btn-active-deactive" @click="confirmRemoveCategory(props.item)">
            <v-icon>fa-trash</v-icon>
          </v-btn> -->
      </template>
    </v-data-table>
    <div class="text-xs-center pt-2">
      <v-layout justify-center>
      <v-pagination v-model="page" :length="pages" color="amber accent-3" circle></v-pagination>
      </v-layout>
    </div>

    <!-- Create Category -->
    <v-dialog persistent v-model="dialog" max-width="500px">
      <v-card>
        <v-form ref="createdForm" lazy-validation>
          <v-card-text>
            <v-text-field :rules="categoryNameRules" :counter="50" type="text" v-model="createdCategory.name" label="Danh mục"></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="red darken-1" @click="dialog = false" text>Hủy bỏ</v-btn>
            <v-btn
              color="green darken-1"
              @click="createCategory()"
              text
            >Đồng Ý</v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-dialog>
    <!-- Show Detail Course of Catagory -->
    <v-dialog v-model="detailContainer" max-width="1000px">
      <v-card>
        <v-card-text>
          <h2 class="basic">Các khóa học {{detailCategory.name}}</h2>
        </v-card-text>
        <hr>
        <v-card>
          <BasicCourse :listCourses="listCourses"></BasicCourse>
        </v-card>
      </v-card>
    </v-dialog>

    <!-- Dialog Update Category -->
    <v-dialog persistent v-model="editDialog" max-width="500px">
      <v-card>
        <v-card-title>
          <span class="headline">CHỈNH SỬA DANH MỤC</span>
        </v-card-title>
        <v-card-text>
          <v-container grid-list-md>
            <v-layout wrap column>
              <v-flex>
                <v-text-field v-model="editedCategory.name" label="Danh mục"></v-text-field>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="editDialog = false" color="red darken-1" text>Hủy</v-btn>
          <v-btn
            @click="updateCategory(), editDialog = false"
            color="green darken-1"
            text
          >Đồng Ý</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Dialog Remove Category-->
    <v-dialog persistent v-model="removeDialog" max-width="500px">
      <v-card>
        <v-card-title>
          <span class="headline">XÓA DANH MỤC</span>
        </v-card-title>
        <v-card-text>
          <v-container grid-list-md>
            <v-flex>
              <v-card-text>Bạn có chắc chắn muốn xóa danh mục {{removedCategory.name}}</v-card-text>
            </v-flex>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="removeDialog = false" color="red darken-1" text>Hủy</v-btn>
          <v-btn
            @click="removeCategory(removedCategory.categoryId), removeDialog = false"
            color="green darken-1"
            text
          >Đồng Ý</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <Loader v-if="loader"/>
  </div>
</template>


<script>
import BasicCourse from './BasicCourse'
import Loader from '@/components/Loader'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const categoryRepository = RepositoryFactory.get('category')
const courseRepository = RepositoryFactory.get('course')
export default {
  props:{
    search:{
      required:false,
      type:String,
      default:''
    },
  },
  components: {
    BasicCourse,
    Loader
  },
  data() {
    return {
      dialog: false,
      loader: false,
      editDialog: false,
      removeDialog: false,
      detailContainer: false,
      categoryId: '',
      page:1,
      rowsPerPage:10,
      headers: [
        {
          text: 'Tên Danh Mục Khóa Học ',
          value: 'name',
          align: 'left',
          sortable: false
        },
        { text: 'Hành động', value: 'action',align: 'right', sortable: false },
      ],
      listCourses: [],
      listCategory: [],
      filterListCategory:[],
      listCategoryItems:[],
      editedCategory: [],
      createdCategory: [],
      removedCategory: [],
      detailCategory: [],
      categoryNameRules: [v => !!v || 'Tên Category không được bỏ trống',
      v => (v && v.length >= 6 || 'Tên Category phải có ít nhất 6 kí tự')],
    }
  },
  computed: {
    pages() {
      const totalItems = this.filterListCategory.length
      if (this.rowsPerPage == null || totalItems == null) return 0

      return Math.ceil(totalItems / this.rowsPerPage)
    }
  },
  mounted() {
    this.loader = true
    this.getCategories()
    this.loader = false
  },
  methods: {
    async getCategories() {
      const { data } = await categoryRepository.getCategories()
      this.listCategory = this.formatListCourse(data.data)
      this.filterListCategory = this.listCategory
      this.renderPaging()
    },
    async getCourseByCategoryId() {
      const { data } = await courseRepository.getCourseByCategoryId(
        this.categoryId,
        1,
        500
      )
      this.listCourses = this.formatListCourse(data.data.content)
    },
    async createCategory() {
      var self = this
      if (this.$refs.createdForm.validate()) {
        const { data } = await categoryRepository.createCategory(
          this.createdCategory.name
        ).then(res => {
          if (res.status === 200) {
            self.listCategory.push({categoryId:res.data.data.savedId,name:self.createdCategory.name})
            self.handleFilter()
            self.dialog = false  
          }
        })
      }
    },
    async updateCategory() {
      var self = this
      const { data } = await categoryRepository.updateCategory(
        this.editedCategory.categoryId,
        this.editedCategory.name
      )
      if (data.data) {
        let category = this.filterListCategory.find(
          category => this.editedCategory.categoryId === category.categoryId
        )
        let categoryAll = this.listCategory.find(
          category => this.editedCategory.categoryId === category.categoryId
        )
        category.name = this.editedCategory.name
        categoryAll.name = this.editedCategory.name
        self.handleFilter()
      }
    },
    async removeCategory() {
      const { data } = await categoryRepository.removeCategory(
        this.removedCategory.categoryId,
        this.removedCategory.name
      )
      if(data.data){
        for(var i = 0; i < this.listCategory.length;i++){
          if(this.listCategory[i].categoryId == this.removedCategory.categoryId){
            this.listCategory.splice(i,1)
          }
        }
        this.handleFilter()
      }
    },
    confirmEditCategory(item) {
      this.editedCategory = this.filterListCategory.find(
        category => category.categoryId === item.categoryId
      )
      this.editDialog = true
    },
    confirmRemoveCategory(item) {
      this.removedCategory = this.filterListCategory.find(
        category => category.categoryId === item.categoryId
      )
      this.removeDialog = true
    },
    confirmDetailCategory(item) {
      this.detailCategory = this.filterListCategory.find(
        category => category.categoryId === item.categoryId
      )
      this.detailContainer = true
    },
    renderPaging(){
      var itemIndex = (this.page - 1) * this.rowsPerPage
      this.listCategoryItems = this.filterListCategory
      .slice(itemIndex,itemIndex + this.rowsPerPage)
    },
    handleFilter(){
      this.filterListCategory = this.listCategory.filter(
        category => category.name.indexOf(this.search) > -1
      )
      this.renderPaging()
    }
  },
  watch:{
    page:{
      handler:function(){
        this.renderPaging()
      }
    },
    search:{
      handler:function(){
        this.handleFilter()
      }
    }
  }
}
</script>
<style scoped>
.v-card {
  border-radius: 10px;
}
.btn-active-deactive {
  width: 35px !important;
  height: 35px !important;
}
.btn-active-deactive i {
  font-size: 17px !important;
}
.v-menu__activator {
  justify-content: center;
}
.div1 {
  padding-top: 50px;
  border: 5;
}
.basic {
  text-align: center;
  font-style: inherit;
}
</style>