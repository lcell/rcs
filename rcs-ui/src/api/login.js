import request from '@/utils/request';

export function loginByUsername (username, password) {
  const data = {
    username,
    password
  }
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getUserInfo (token) {
  return request({
    url: '/auth/info',
    method: 'get',
    params: { token }
  })
}
