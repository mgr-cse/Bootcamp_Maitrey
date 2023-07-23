import http from "../http-common";



class UserService {
  create(data) {
    return http.post('/create', data);
  }

  login(data) {
    return http.post( "/login", data);
  }
}

export default new UserService()