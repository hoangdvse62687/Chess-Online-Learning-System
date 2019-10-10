import Repository from '@/repository/Repository'

const resource = '/notifications'

export default {
    getNotificationPagination(page, pageSize,sortBy,sortDirection) {
        const data = { 
            params: { 
                page: page, 
                pageSize: pageSize,
                sortBy: sortBy,
                sortDirection: sortDirection
            } 
        }
        return Repository.get(`${resource}/current-user`, data)
    },
    updateIsViewed(notificationIds){
        const data = {
            notificationIds: notificationIds
        }
        return Repository.put(`${resource}/is-viewed`, data)
    },
    updateNotificationToken(token){
        const data = {
            token: token
        }
        return Repository.post(`${resource}/token`, data)
    }
}