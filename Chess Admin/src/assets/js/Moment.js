const moment = require('moment')
export default class Moment{
    constructor(){

    }

    getInstance(){
        moment.updateLocale('en', {
            relativeTime: {
                past: '%s trước',
                s: '1 giây',
                ss: '%d seconds',
                m: '1 phút',
                mm: '%d phút',
                h: '1 giờ',
                hh: '%d giờ',
                d: '1 ngày',
                dd: '%d ngày',
                M: '1 tháng',
                MM: '%d tháng',
                y: '1 năm',
                yy: '%d năm'
            }
        })
        return moment
    }
}