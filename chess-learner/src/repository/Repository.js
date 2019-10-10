import axios from 'axios'

const baseDomain = 'http://cols-be.ml'
// const baseDomain = 'http://localhost:5000'
const baseURL = `${baseDomain}`

const instance = axios.create({
  baseURL,
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': 'GET, PUT, POST, DELETE, OPTIONS',
    'Content-Type': 'application/json',
    Authorization: localStorage.getItem('access-token')
  }
})
axios.interceptors.response.use(
  response => {
    return response
  },
  function(error) {
    // Do something with response error
    if (error.response.status === 401) {
      console.log('unauthorized, logging out ...')
    }
    return Promise.reject(error.response)
  }
)
export default instance

export const setAuthorizationHeader = (axiosInstance, token) => {
  axiosInstance.defaults.headers['Authorization'] = token
  axios.defaults.headers['Authorization'] = token
}
