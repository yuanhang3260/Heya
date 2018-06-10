function profileImageURL() {
  if (!this.debug) {
    return "profileimage/profile/" + this.uid;
  } else {
    return "dist/img/profile.jpg";
  }
}

function postImageURL(uid, pid, imageFile) {
  return "post/" + uid + "/" + pid + "/image/" + imageFile;
}

function generatePost(post, uid, username) {
  post["avatar"] = "profileimage/profile/" + uid;
  post["username"] = username;
  post["time"] = new Date(post.time);

  let images = [];
  for (let pic of post.pictures) {
    images.push({url: postImageURL(uid, post.pid, pic)});
  }
  post["images"] = images;
  delete post["pictures"];

  return post;
}

export default {
  generatePost: generatePost,
  profileImageURL: profileImageURL,
}
