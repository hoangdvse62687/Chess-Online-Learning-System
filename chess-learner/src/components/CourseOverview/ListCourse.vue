<template>
  <v-layout>
    <v-flex xs3 mr-2>
      <v-layout>
        <v-flex xs12>
          <v-card class="mx-auto category-container pa-3 pb-0">
            <v-list class="category-container" dense>
              <v-list-item-group v-model="selectedCategory">
                <v-subheader>Danh mục</v-subheader>
                <template v-for="(item, index) in listCategories">
                  <v-list-item :key="index" @click="changeFilter(1, item.categoryId, item.name)">
                    <v-list-item-content>
                      <v-list-item-title class="category-name" v-text="item.name"></v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </template>
              </v-list-item-group>
            </v-list>
          </v-card>
        </v-flex>
      </v-layout>
    </v-flex>
    <v-flex xs9>
      <v-layout wrap>
        <v-flex xs12>
          <v-layout justify-end>
            <v-flex xs10>
              <v-text-field
                class="search-field"
                v-model="searchCourseName"
                solo
                label="Tìm kiếm khóa học..."
                @keydown="$event.keyCode === 13 ? searchCourse() : false"
              ></v-text-field>
            </v-flex>
            <v-flex xs2>
              <v-btn
                class="btn-search"
                block
                min-height="40"
                tile
                color="success"
                @click="searchCourse()"
              >Tìm kiếm</v-btn>
            </v-flex>
          </v-layout>
        </v-flex>
        <v-flex xs12>
          <v-layout justify-end>
            <div class="filter-container">
              <div
                v-for="(sort, index) in sortFilters"
                :key="index"
                :class="[
                  'filter-item',
                  sort.sortBy === filter.sortBy ? 'filter-active' : ''
                ]"
                @click="changeFilter(2, sort.sortDirection, sort.sortBy)"
              >
                {{ sort.name }}
                <template
                  v-if="(filter.sortBy === 'required_elo' && sort.sortBy === 'required_elo') "
                >
                  <v-icon v-if="filter.sortDirection === 'ASC'" class="ml-1">fa-caret-up</v-icon>
                  <v-icon v-if="filter.sortDirection === 'DESC'" class="ml-1">fa-caret-down</v-icon>
                </template>
              </div>
            </div>
          </v-layout>
        </v-flex>
        <v-flex mb-1 class="selected-filter" xs12>
          <v-layout fill-height align-center>
            <span class="total-course mr-2">
              <b>{{ getTotalCourse }}</b>
              khóa học
            </span>
            <v-chip
              v-if="filter.chips.courseShow"
              close
              class="mr-2"
              color="#e6e6e6"
              label
              outlined
              text-color="#333333"
              @click:close="filter.chips.courseShow = false"
            >{{ filter.chips.courseName }}</v-chip>
            <v-chip
              v-if="filter.chips.categoryShow"
              close
              class="mr-2"
              color="#e6e6e6"
              label
              outlined
              text-color="#333333"
              @click:close="filter.chips.categoryShow = false"
            >{{ filter.chips.categoryName }}</v-chip>
            <v-chip
              v-if="filter.chips.sortShow"
              close
              color="#e6e6e6"
              label
              outlined
              text-color="#333333"
              @click:close="filter.chips.sortShow = false"
            >{{ filter.chips.sortBy }}</v-chip>
          </v-layout>
        </v-flex>
        <v-flex v-for="(item, index) in listCourses" :key="index" mb-3 class="course-item" xs12>
          <CourseItem :course-detail="item" />
        </v-flex>
        <v-flex>
          <v-layout justify-center>
            <v-pagination
              v-model="filter.page"
              :length="filter.totalPages"
              :total-visible="5"
              color="#fafafa"
            ></v-pagination>
          </v-layout>
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<script>
import CourseItem from '@/components/CourseOverview/CourseItem'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')
const categoryRepository = RepositoryFactory.get('category')
export default {
  components: {
    CourseItem
  },
  data() {
    return {
      selectedCategory: 0,
      selectedElo: 0,
      listCourses: [],
      listCategories: [],
      listEloes: [
        { name: 'Tất cả', eloId: 0 },
        { name: 'Người mới', eloId: 1 },
        { name: 'Mới biết chơi', eloId: 2 },
        { name: 'Nghiệp dư', eloId: 3 },
        { name: 'Chuyên nghiệp', eloId: 4 },
        { name: 'Cao thủ', eloId: 5 }
      ],
      currentCategoryId: 0,
      filter: {
        totalPages: 0,
        page: 1,
        pageSize: 5,
        sortBy: '',
        sortDirection: '',
        statusId: 2,
        categoryId: 0,
        chips: {
          categoryName: '',
          categoryShow: false,
          sortBy: '',
          sortShow: false,
          courseName: '',
          courseShow: false
        },
        nameCourse: ''
      },
      sortFilters: [
        {
          name: 'Mới nhất',
          sortBy: 'created_date',
          sortDirection: ''
        },
        {
          name: 'Đánh giá cao nhất',
          sortBy: 'rating',
          sortDirection: 'DESC'
        },
        {
          name: 'Cấp độ',
          sortBy: 'required_elo',
          sortDirection: null
        }
      ],
      totalCourse: 0,
      searchCourseName: ''
    }
  },
  computed: {
    getTotalCourse() {
      return this.totalCourse
    }
  },
  watch: {
    'filter.page': function() {
      this.factoryGetCourse()
    },
    'filter.chips.categoryShow': function() {
      if (!this.filter.chips.categoryShow) {
        //xóa chip category
        this.changeFilter(1, 0, '')
      }
    },
    'filter.chips.sortShow': function() {
      if (!this.filter.chips.sortShow) {
        //xóa chip sort
        this.changeFilter(2, '', '')
      }
    },
    'filter.chips.courseShow': function() {
      if (!this.filter.chips.courseShow) {
        //xóa chip course name
        this.filter.nameCourse = ''
        this.factoryGetCourse()
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      await this.getCoursesPagination()
      await this.getCategories()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    searchCourse() {
      if (this.filter.nameCourse !== this.searchCourseName) {
        this.filter.nameCourse = this.searchCourseName
        this.factoryGetCourse()
      }
    },
    async factoryGetCourse() {
      if (this.filter.nameCourse.length > 0) {
        this.filter.chips.courseShow = true
        this.filter.chips.courseName = 'Từ khóa: ' + this.filter.nameCourse
      } else {
        this.filter.chips.courseShow = false
      }
      this.$store.commit('incrementLoader', 1)
      if (this.filter.categoryId === -1) {
        //course suggestion
        await this.getCoursesSuggestion()
      } else if (this.filter.categoryId === 0) {
        //search all
        await this.getCoursesPagination()
      } else {
        await this.getCoursesPaginationByCategoryId()
      }
      this.searchCourseName = this.filter.nameCourse
      this.mergeAllCategories()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    mergeAllCategories() {
      this.listCourses.forEach(course => {
        course.tag = ''
        course.listCategorys.forEach(el => {
          if (course.tag.length > 0) {
            course.tag += ', '
          }
          course.tag += el.name
        })
      })
    },
    async getCoursesPagination() {
      const { data } = await courseRepository.getCoursesPagination(this.filter)
      this.listCourses = data.data.content
      this.formatListCourse()
      this.mergeAllCategories()
      this.filter.totalPages = data.data.totalPages
      this.totalCourse = data.data.totalElements
    },
    async getCoursesPaginationByCategoryId() {
      const { data } = await courseRepository.getCoursesPaginationByCategoryId(
        this.filter
      )
      this.listCourses = data.data.content
      this.formatListCourse()
      this.mergeAllCategories()
      this.filter.totalPages = data.data.totalPages
      this.totalCourse = data.data.totalElements
    },
    async getCoursesSuggestion() {
      const { data } = await courseRepository.getCoursesSuggestion(this.filter)
      this.listCourses = data.data.content
      this.formatListCourse()
      this.mergeAllCategories()
      this.filter.totalPages = data.data.totalPages
      this.totalCourse = data.data.totalElements
    },
    formatListCourse() {
      this.listCourses.forEach(course => {
        const date = new Date(Date.parse(course.courseCreatedDate))
        course.courseCreatedDate =
          date.getMonth() + 1 + '/' + date.getDate() + '/' + date.getFullYear()
        course.requiredEloName = this.getEloName(course.requiredElo)
        course.requiredEloClass = `elo-${course.requiredElo}`
        course.requiredEloNumber = 600 + course.requiredElo * 200
      })
    },
    async getCategories() {
      const { data } = await categoryRepository.getCategories()
      this.listCategories = data.data
      this.listCategories.unshift({
        categoryId: 0,
        name: 'Tất cả'
      })
      this.listCategories.push({
        categoryId: -1,
        name: 'Khóa học phù hợp'
      })
    },
    changeFilter(filterType, categoryId, name) {
      //filterType = 1 is category, 2 is sortBy
      if (filterType === 1) {
        this.filter.chips.categoryName = name
        if (categoryId === 0) {
          //category tất cả - xóa category bằng chip
          this.filter.chips.categoryShow = false
          this.selectedCategory = 0
        } else {
          this.filter.chips.categoryShow = true
        }
        //khác mới load lại course trong category
        if (this.filter.categoryId !== categoryId) {
          this.filter.page = 1
          this.filter.categoryId = categoryId
        }
      } else if (filterType === 2) {
        //categoryId có nghĩa là sortDirection trong trường hợp này
        const sortDirection = categoryId
        if (sortDirection === null) {
          if (name === 'required_elo') {
            this.filter.sortDirection =
              this.filter.sortDirection === 'ASC' && this.filter.sortBy === name
                ? 'DESC'
                : 'ASC'
          }
        } else {
          this.filter.sortDirection = sortDirection
        }
        this.filter.chips.sortShow = true
        if (sortDirection === '' && name === '') {
          this.filter.chips.sortShow = false
        }
        this.filter.sortBy = name
        this.filter.chips.sortBy = this.mappingSortNameOfChips(
          name,
          this.filter.sortDirection
        )
      }
      this.factoryGetCourse()
    },
    mappingSortNameOfChips(name, sortDirection) {
      if (name === 'created_date') {
        return 'Mới nhất'
      } else if (name === 'rating') {
        return 'Đánh giá cao nhất'
      } else if (name === 'required_elo' && sortDirection === 'ASC') {
        return 'Cấp độ: thấp đến cao'
      } else if (name === 'required_elo' && sortDirection === 'DESC') {
        return 'Cấp độ: cao đến thấp'
      }
    }
  }
}
</script>

<style scoped>
.category-name {
  color: #333333 !important;
  font-size: 14px;
}
.category-container {
  background-color: transparent !important;
  box-shadow: none;
  color: #333333 !important;
  font-size: 14px;
  line-height: 32px;
}
.v-item--active div.category-name {
  font-weight: 700 !important;
}
>>> .v-pagination__navigation,
>>> .v-pagination__item {
  background: transparent !important;
  box-shadow: none !important;
  font-size: 14px;
}
>>> .v-pagination__item--active {
  color: #c3c3c3 !important;
  background: #fafafa !important;
  border: 1px solid #cccccc !important;
}
>>> .v-pagination__item--active:hover {
  cursor: not-allowed;
}
>>> .v-pagination__item:hover {
  background-color: #f2f2f2 !important;
}
>>> .v-pagination__item:focus {
  outline: none;
}
>>> .v-pagination__navigation--disabled {
  opacity: 0;
}
>>> .v-chip:focus,
.v-chip:focus:after {
  box-shadow: none !important;
  background: transparent !important;
  background-color: transparent !important;
}
>>> .v-chip.v-chip.v-chip--outline {
  height: 28px;
}
>>> .v-chip__content {
  font-size: 12px;
}
>>> .v-chip__close > .v-icon {
  font-size: 18px;
}
.selected-filter {
  height: 36px;
  width: 40px;
}
.filter-item:not(.filter-active):hover {
  cursor: pointer;
  background-color: #f2f2f2;
}
.filter-item {
  border-right: 2px solid #e6e6e6;
  font-size: 12px;
  color: #333333;
  padding: 4px 16px;
  font-weight: 600;
}
.filter-container {
  border: 2px solid #e6e6e6;
  display: flex;
  border-right: none;
}
.total-course {
  font-size: 14px;
  color: #4d4d4d;
}
.v-icon {
  font-size: 17px;
}
.filter-active,
.filter-active > i {
  background: #333333;
  color: #ffffff;
}
>>> .search-field .v-input__slot {
  /* box-shadow: none !important; */
  border-radius: 0px !important;
  /* border: 1px solid #999999; */
}
>>> .btn-search span {
  font-size: 12px;
}
>>> .v-text-field.v-text-field--solo .v-input__control {
  min-height: 40px !important;
}
</style>
