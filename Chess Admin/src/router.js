import Vue from 'vue'
import Router from 'vue-router'
//import page
import Login from '@/pages/Login'
import Dashboard from '@/pages/Dashboard'
//import components
import Profile from '@/components/profile/Profile'
import Statistics from '@/components/statistics/Statistics'
import CourseManagement from '@/components/courses/CourseManagement'
import UserManagement from '@/components/users/UserManagement'
import CategoryManagement from '@/components/category/CategoryManagement'
import NewInstructorManagement from '@/components/instructor/NewInstructorManagement'
import NewInstructor from '@/components/instructor/NewInstructor'
import CreateCourse from '@/components/courses/CreateCourse'
import DetailCourseById from '@/components/courses/DetailCourseById'
import Notification from '@/components/notifications/Notification'

Vue.use(Router)
export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {path: '/', component: Login},
        {
            path: '/dashboard',
            component: Dashboard,
            children: [
                { path: 'profile', component: Profile,
                    meta:{
                        breadcrumb:[
                            {text:'Trang chủ',href:'',disabled:true},
                            {text:'Trang cá nhân',href:'/dashboard/profile',disabled:true}
                        ]
                    }
                },
                { path: 'statistics', component: Statistics,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Thống kê',href:'/dashboard/profile',disabled:true}
                        ]
                    }
                },
                { path: 'courses', component: CourseManagement,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách khóa học',href:'/dashboard/courses',disabled:true}
                        ]
                    }
                },
                { path: 'courses?:courseName=&:status', component: CourseManagement,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách khóa học',href:'/dashboard/courses',disabled:true}
                        ]
                    }
                },
                { path: 'courses?:courseName=', component: CourseManagement,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách khóa học',href:'/dashboard/courses',disabled:true}
                        ]
                    }
                },
                { path: 'courses/:courseId', component: DetailCourseById,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách khóa học',href:'/dashboard/courses',disabled:false},
                        {text:'Khóa học',href:'/dashboard/courses/:courseId',disabled:true}
                        ]
                    }
                },
                { path: 'accounts', component: UserManagement,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách người dùng',href:'/dashboard/accounts',disabled:true}
                        ]
                    }
                },
                { path: 'category', component: CategoryManagement,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách danh mục',href:'/dashboard/category',disabled:true}
                        ]
                    }
                },
                { path: 'instructor-request', component: NewInstructorManagement,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách giảng viên đăng ký',href:'/dashboard/instructor-request',disabled:true}
                        ]
                    }
                },
                { path: 'instructor-request?:email', component: NewInstructorManagement,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách giảng viên đăng ký',href:'/dashboard/instructor-request',disabled:true}
                        ]
                    }
                },
                { path: 'new-ins-profile/:userId', component: NewInstructor,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách giảng viên đăng ký',href:'/dashboard/instructor-request',disabled:false},
                        {text:'Thông tin giảng viên',href:'/dashboard/new-ins-profile/:userId',disabled:true}
                        ]
                    } 
                },
                { path: 'notifications', component: Notification,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Danh sách thông báo',href:'/dashboard/notifications',disabled:true}
                        ]
                    }
                },
                { path: 'create-course', component: CreateCourse,
                    meta:{
                        breadcrumb:[
                        {text:'Trang chủ',href:'',disabled:true},
                        {text:'Tạo khóa học',href:'/dashboard/create-course',disabled:true}
                        ]
                    }
                }
            ]
        },
        { path: '*', redirect: '/'}
    ]
})