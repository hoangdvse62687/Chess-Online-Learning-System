import firebase from 'firebase'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const userRepository = RepositoryFactory.get('user')

export default class UploadImagePlugin {
    constructor( loader) {
        this.loader = loader;
    }

    upload() {
      const loader = this.loader;
        return loader.file
        .then(file => new Promise( ( resolve, reject ) => {
            this.handleUpdate(file,resolve, reject)
        } )
        )
    }

    async handleUpdate(file,resolve,reject){
      if(!this.user){
        await userRepository.getCurrentUserDetail().then(res => {
          this.user = res.data.data
        })
      }
      
      let fileRef = firebase.storage()
                  .ref("images/lessons/" + this.user.userId + "/" + file.name);

      let uploadTask = fileRef.put(file);

      uploadTask.on(
        "state_changed",
          _snapshot => {
            console.log(
              "snapshot progess " +
                (_snapshot.bytesTransferred / _snapshot.totalBytes) * 100
            );
          },
          _error => {
            console.log(_error);
            reject(_error);
          },
          () => {
            uploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
              resolve(
                {default:downloadURL}
              )
            });
          }
        );
    }
}