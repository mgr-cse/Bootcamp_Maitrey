import http from "../http-common";

class UploadService {
  upload(data) {
    return http.post("/upload", data);
  }
}

export default new UploadService();