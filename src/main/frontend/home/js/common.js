function postImageURL(uid, pid, imageFile) {
  return "post/" + uid + "/" + pid + "/image/" + imageFile;
}

function generatePost(post, uid, username) {
  post["avatar"] = "profileimage/profile/" + uid;
  post["username"] = username;

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
}
