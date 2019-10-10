import axios from 'axios'

const baseDomain = 'http://cols-be.ml'
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

export default instance

export const setAuthorizationHeader = (axiosInstance, token) => {
  axiosInstance.defaults.headers['Authorization'] = token
  axios.defaults.headers['Authorization'] = token
}
